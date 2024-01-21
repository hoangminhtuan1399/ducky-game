package jade;
import util.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String title;
    private long glfwWindow;
    public float r, g, b, a;
    private boolean fadeToBlack = false;
    private static Window window = null;

    private static Scene currentScene;

    private Window() {
        // Thiết lập giá trị mặc định cho chiều rộng, chiều cao, và tiêu đề cửa sổ
        this.width = 1920;
        this.height = 1080;
        this.title = "duck";
        r = 1;
        b = 0;
        g = 0;
        a = 0;
    }


    public  static void changeScene(int newScene){
        switch (newScene){
            case 0:
                currentScene =new LevelEditorScene();
                currentScene.init();
                break;
            case 1:
                currentScene =new LevelScene();
                currentScene.init();
                break;
            default:
                assert false:"unknown scene"+newScene +"'";
                break;
        }
    }
    public static Window get() {
        // Triển khai mẫu Singleton để đảm bảo chỉ có một thể hiện của lớp Window
        if (Window.window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }

    public void run() {
        // In ra màn hình console thông báo chào mừng cùng phiên bản của LWJGL
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        // Gọi phương thức khởi tạo và vòng lặp chính của ứng dụng
        init();
        loop();

        // Giải phóng bộ nhớ và đóng cửa sổ GLFW khi ứng dụng kết thúc
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        // Tạo một callback để in ra thông báo lỗi trực tiếp ra màn hình console
        GLFWErrorCallback.createPrint(System.err).set();

        // Khởi tạo GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Đặt các giá trị mặc định cho cửa sổ GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Tạo cửa sổ GLFW với chiều rộng, chiều cao, tiêu đề và giá trị NULL
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);

        // Kiểm tra nếu việc tạo cửa sổ không thành công
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        // Đặt callback cho sự kiện di chuyển chuột, nhấn nút chuột, và cuộn chuột
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);

        // Đặt callback cho sự kiện nhấn phím
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // Đặt cửa sổ GLFW vừa tạo làm ngữ cảnh hiện tại
        glfwMakeContextCurrent(glfwWindow);

        // V-sync: đồng bộ hóa với tần số làm mới của màn hình
        glfwSwapInterval(1);

        // Làm cho cửa sổ hiển thị
        glfwShowWindow(glfwWindow);

        // Kích hoạt các khả năng của OpenGL trong ngữ cảnh hiện tại
        GL.createCapabilities();
        Window.changeScene(0);
    }

    public void loop() {

        float beginTime = Time.getTime();
        float endTime =Time.getTime();
        float dt =-1.0f;

        // Vòng lặp chính của ứng dụng, kiểm tra điều kiện đóng cửa sổ
        while (!glfwWindowShouldClose(glfwWindow)) {
            // Giao tiếp với cửa sổ GLFW: Đổi frame buffers và xử lý sự kiện
            glfwPollEvents();

            // Xóa màn hình với màu được thiết lập (có thể fade màu nếu điều kiện được đáp ứng)
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if(dt>=0) {
                currentScene.update(dt);
            }

            // Hoán đổi các buffers để hiển thị hình ảnh đã được vẽ
            glfwSwapBuffers(glfwWindow);
            endTime =Time.getTime();
             dt=endTime-beginTime;
            beginTime=endTime;
        }
    }
}
