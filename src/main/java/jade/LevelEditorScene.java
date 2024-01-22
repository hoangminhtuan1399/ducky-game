package jade;

import jade.Scene;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader; // Import Shader class

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene {

    // Đoạn mã nguồn của vertex shader
    private String vertexShaderSrc = "#version 330 core\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}\n";

    // Đoạn mã nguồn của fragment shader
    private String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "out vec4 color;\n" +
            "void main() {\n" +
            "    color = fColor;\n" +
            "}\n";

    // ID của vertex shader
    private int vertexID;

    // ID của fragment shader
    private int fragmentID;

    // ID của shader program
    private int shaderProgram;

    // Mảng chứa thông tin về đỉnh của hình vuông
    private float[] vertexArray = {
            100.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
            0.5f, 100.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
            100.5f, 100.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
            0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f
    };

    // Mảng chỉ số của các đỉnh để tạo hình vuông
    private int[] elementArray = {
            2, 1, 0,
            0, 1, 3
    };

    // ID của Vertex Array Object (VAO)
    private int vaoID;

    // ID của Vertex Buffer Object (VBO)
    private int vboID;

    // ID của Element Buffer Object (EBO)
    private int eboID;

    // Shader object
    private Shader defaultShader;

    // Constructor mặc định
    public LevelEditorScene() {

    }

    // Phương thức để khởi tạo shaders và shader program
    @Override
    public void init() {
        this.camera=new Camera(new Vector2f());
        // Tạo Shader object và biên dịch shader
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();

        // Tạo Vertex Array Object (VAO)
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Tạo Vertex Buffer Object (VBO) cho dữ liệu đỉnh
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Tạo Element Buffer Object (EBO) cho chỉ số đỉnh
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Thiết lập các thuộc tính của đỉnh (position và color)
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;

        // Thiết lập thuộc tính position
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        // Thiết lập thuộc tính color
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {
        camera.position.x -=dt*50f;
        // Sử dụng shader program và VAO đã tạo
        defaultShader.use();
        defaultShader.uploadMat4f("uProjection",camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView",camera.getViewMatrix());
        glBindVertexArray(vaoID);

        // Bật thuộc tính position và color
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Vẽ hình vuông
        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Tắt thuộc tính position và color
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        // Unbind VAO và shader program
        glBindVertexArray(0);
        defaultShader.detach();
    }
}
