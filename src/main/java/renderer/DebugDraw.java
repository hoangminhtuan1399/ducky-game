package renderer;

import jade.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;
import util.AssetPool;
import util.JMath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class DebugDraw {
    private static int MAX_LINES = 500; // số lg đường kẻ tối đa
    private static List<Line2D> lines = new ArrayList<>();

    // 6 floats cho 1 vertex (đỉnh) (vd: vertex(x,y,z,r,b,g), xyz là tọa độ), 2 vertices (điểm) cho 1 đường kẻ (line)
    // đây là số float cần cho 1 mảng float (đường kẻ) chứa đủ các số
    private static float[] vertexArray = new float[MAX_LINES * 6 * 2];
    private static Shader shader = AssetPool.getShader("assets/shaders/debugLine2D.glsl");

    private static int vaoID; //vertex array object
    private static int vboID; //vertex buffer obj

    private static boolean started = false; //sẽ cho biết mấy cái trên đã dc khởi tạo trên GPU chưa

    public static void start() {
        //khởi tạo VAO
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //tạo VBO và buffer some memory (@Net dịch hộ e)
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

        //Enable the vertex array attributes
        // mỗi đỉnh có 3 tọa độ mà đường kẻ có 2 đỉnh nên nhân 6
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        // SET LINE WIDTH
        glLineWidth(1.0f);

    }

    public static void beginFrame() {
        if (!started) {
            start();
            started = true;
        }

        //remove dead lines (deadline?)
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).beginFrame() < 0) {
                lines.remove(i);
                i--;
            }
        }
    }

    public static void draw() {
        if (lines.size() <= 0) return;

        int index = 0;
        for (Line2D line : lines) {
            for (int i = 0; i < 2; i++) { // 2 ở đây là 2 điểm của 1 đường kẻ
                Vector2f position = i == 0 ? line.getFrom() : line.getTo();
                Vector3f color = line.getColor();

                // load position
                vertexArray[index] = position.x;
                vertexArray[index + 1] = position.y;
                vertexArray[index + 2] = -10.0f;

                //load the color
                vertexArray[index + 3] = color.x;
                vertexArray[index + 4] = color.y;
                vertexArray[index + 5] = color.z;
                index += 6;
            }
        }

        //tải lại dữ liệu lên GPU (bind VBO)
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, lines.size() * 6 * 2));

        //use our shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());

        // bind the VAO
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        //draw the batch
        glDrawArrays(GL_LINES, 0, lines.size() * 6 * 2);

        // Disable location
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        // unbind shader
        shader.detach();
    }

    //==============================
    // add line2D methods: điểm đầu, điểm cuối của đường kẻ, màu, thời gian tồn tại
    //==============================
    public static void addLine2D (Vector2f from, Vector2f to) {
        // TODO: ADD CONSTANTS FOR COMMON COLORS
        addLine2D(from, to, new Vector3f(0, 1, 0), 1);
    }
    public static void addLine2D (Vector2f from, Vector2f to, Vector3f color) {

        addLine2D(from, to, color, 1);
    }
    public static void addLine2D (Vector2f from, Vector2f to, Vector3f color, int lifetime) {
        if (lines.size() >= MAX_LINES) return;
        DebugDraw.lines.add(new Line2D(from, to, color, lifetime));
    }

    //==============================
    // add box2d methods: tâm của tứ giác, size của tứ giác, độ nghiêng, màu, thời gian tồn tại
    //==============================
    public static void addBox2D (Vector2f center, Vector2f dimensions, float rotation) {
        // TODO: ADD CONSTANTS FOR COMMON COLORS
        addBox2D(center, dimensions, rotation, new Vector3f(0, 1, 0), 1);
    }
    public static void addBox2D (Vector2f center, Vector2f dimensions, float rotation, Vector3f color) {

        addBox2D(center, dimensions, rotation, color, 1);
    }
    public static void addBox2D (Vector2f center, Vector2f dimensions, float rotation, Vector3f color, int lifetime) {
        // lấy các đỉnh của box từ tâm và size của hình tứ giác
            //đỉnh dưới trái
        Vector2f min = new Vector2f(center).sub(new Vector2f(dimensions).mul(0.5f));
            //đỉnh trên phải
        Vector2f max = new Vector2f(center).add(new Vector2f(dimensions).mul(0.5f));

        // vẽ hình tứ giác:
        Vector2f[] vertices = {
            new Vector2f(min.x, min.y), new Vector2f(min.x, max.y),
            new Vector2f(max.x, max.y), new Vector2f(max.x, min.y)
        };

        // cho hình tứ giác nó xoay
        if (rotation != 0.0f) {
            for (Vector2f vert : vertices) {
                JMath.rotate(vert, rotation, center);
            }
        }

        addLine2D(vertices[0], vertices[1], color, lifetime);
        addLine2D(vertices[0], vertices[3], color, lifetime);
        addLine2D(vertices[1], vertices[2], color, lifetime);
        addLine2D(vertices[2], vertices[3], color, lifetime);
    }

    //==============================
    // add Circle methods
    //==============================
    public static void addCircle (Vector2f center, float radius) {
        // TODO: ADD CONSTANTS FOR COMMON COLORS
        addCircle(center, radius, new Vector3f(0, 1, 0), 1);
    }
    public static void addCircle (Vector2f center, float radius, Vector3f color) {

        addCircle(center, radius, color, 1);
    }
    public static void addCircle (Vector2f center, float radius, Vector3f color, int lifetime) {
        //xác định các điểm quanh tâm hình tròn vd số 20 kia càng tăng thì càng tròn nma vòng lặp sẽ nhiều hơn - nặng
        Vector2f[] points = new Vector2f[20];
        // góc giữa các điểm liền kề nhau
        int increment = 360 / points.length;
        // góc hiện tại
        int currentAngle = 0;

        // xác định 1 tỷ điểm quanh tâm r nối vs nhay bằng tỷ đường kẻ là ra hình tròn
        for (int i = 0; i < points.length; i++) {
            Vector2f tmp = new Vector2f(0, radius);
            JMath.rotate(tmp, currentAngle, new Vector2f());
            points[i] = new Vector2f(tmp).add(center);

            //vẽ các đường kẻ
            if (i > 0) {
                addLine2D(points[i - 1], points[i], color, lifetime);
            }
            // góc hiện tại tăng lên dần
            currentAngle += increment;
        }
        // vòng lặp trên vé thiếu đường giữa điểm cuối và điểm đầu nên cần dòng này
        addLine2D(points[points.length-1], points[0], color, lifetime);
    }
}