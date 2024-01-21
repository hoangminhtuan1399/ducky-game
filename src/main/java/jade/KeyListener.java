package jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    // Biến static để giữ một thể hiện duy nhất của lớp KeyListener theo mô hình Singleton
    private static KeyListener instance;

    // Mảng lưu trữ trạng thái nhấn của các phím
    private boolean keyPressed[] = new boolean[350];

    // Constructor riêng tư để đảm bảo không thể tạo ra thể hiện mới từ bên ngoài lớp
    private KeyListener() {
    }

    // Phương thức static để lấy thể hiện duy nhất của lớp KeyListener theo mô hình Singleton
    public static KeyListener get() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }
        return KeyListener.instance;
    }

    // Callback khi có sự kiện nhấn hoặc nhả phím
    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            // Khi phím được nhấn, đặt trạng thái của phím đó là true trong mảng keyPressed
            get().keyPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            // Khi phím được nhả, đặt trạng thái của phím đó là false trong mảng keyPressed
            get().keyPressed[key] = false;
        }
    }

    // Phương thức kiểm tra xem một phím cụ thể có đang được nhấn hay không
    public static boolean isKeyPressed(int keyCode) {
        return get().keyPressed[keyCode];
    }
}
