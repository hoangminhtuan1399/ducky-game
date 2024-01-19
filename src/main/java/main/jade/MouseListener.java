package main.jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    // Biến static để giữ một thể hiện duy nhất của lớp MouseListener theo mô hình Singleton
    private static MouseListener instance;

    // Biến lưu trữ giá trị cuộn chuột theo chiều ngang và chiều dọc
    private double scrollX, scrollY;

    // Biến lưu trữ tọa độ x và y hiện tại của chuột, cũng như tọa độ x và y của chuột trong frame trước đó
    private double xPos, yPos, lastY, lastX;

    // Mảng lưu trữ trạng thái nhấn của các nút chuột (trái, phải, giữa)
    private boolean mouseButtonPressed[] = new boolean[3];

    // Biến kiểm tra xem chuột có đang được kéo không
    private boolean isDragging;

    // Constructor riêng tư để đảm bảo không thể tạo ra thể hiện mới từ bên ngoài lớp
    private MouseListener() {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    // Phương thức static để lấy thể hiện duy nhất của lớp MouseListener theo mô hình Singleton
    public static MouseListener get() {
        if (MouseListener.instance == null) {
            MouseListener.instance = new MouseListener();
        }
        return MouseListener.instance;
    }

    // Callback khi di chuyển chuột
    public static void mousePosCallback(long window, double xpos, double ypos) {
        // Lưu trữ tọa độ chuột trước đó
        get().lastX = get().xPos;
        get().lastY = get().yPos;

        // Cập nhật tọa độ chuột hiện tại
        get().xPos = xpos;
        get().yPos = ypos;

        // Kiểm tra xem chuột có đang được kéo không
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }

    // Callback khi nhấn hoặc nhả nút chuột
    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            // Kiểm tra và cập nhật trạng thái nút chuột được nhấn
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            // Kiểm tra và cập nhật trạng thái nút chuột được nhả
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }
        }
    }

    // Callback khi cuộn chuột
    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        // Cập nhật giá trị cuộn chuột theo chiều ngang và chiều dọc
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    // Phương thức kết thúc frame, cập nhật lại giá trị scroll và tọa độ chuột
    public static void endFrame() {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    // Các phương thức getter để lấy thông tin về tọa độ, cuộn, và trạng thái chuột
    public static float getX() {
        return (float) get().xPos;
    }

    public static float getY() {
        return (float) get().yPos;
    }

    public static float getDx() {
        return (float) (get().lastX - get().xPos);
    }

    public static float getDy() {
        return (float) (get().lastY - get().yPos);
    }

    public static float getScrollX() {
        return (float) get().scrollX;
    }

    public static float getScrollY() {
        return (float) get().scrollY;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }

    // Kiểm tra trạng thái nhấn của nút chuột
    public static boolean mouseButtonDown(int button) {
        if (button < get().mouseButtonPressed.length) {
            return get().mouseButtonPressed[button];
        } else {
            return false;
        }
    }
}
