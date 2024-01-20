package main.jade;

import static org.lwjgl.opengl.GL20.*;

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
            "    fColor =aColor;\n" +
            "    gl_Position =vec4(aPos,1.0);\n" +
            "}\n";

    // Đoạn mã nguồn của fragment shader
    private String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "out vec4 color;\n" +
            "void main(){\n" +
            "    color=fColor;\n" +
            "}\n";
    // ID của vertex shader
    private int vertexID;

    // ID của fragment shader
    private int fragmentID;

    // ID của shader program
    private int shaderProgram;

    // Constructor mặc định
    public LevelEditorScene() {
    }

    // Phương thức để khởi tạo shaders và shader program
    @Override
    public void init() {
        // Tạo và biên dịch vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexShaderSrc);
        glCompileShader(vertexID);

        // Kiểm tra lỗi khi biên dịch vertex shader
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: Vertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : " ";
        }

        // Tạo và biên dịch fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentShaderSrc);
        glCompileShader(fragmentID);

        // Kiểm tra lỗi khi biên dịch fragment shader
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: Fragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : " ";
        }

        // Tạo và liên kết shader program
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        // Kiểm tra lỗi khi liên kết shader program
        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("Error: Linking of shaders failed.");
            System.out.println(glGetShaderInfoLog(shaderProgram, len));
            assert false : " ";
        }
    }


    @Override
    public void update(float dt) {
    }
}
