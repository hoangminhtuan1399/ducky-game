package components;

import jade.KeyListener;
import jade.Window;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;

// Lớp GizmoSystem quản lý hệ thống Gizmo trong chế độ chỉnh sửa.
public class GizmoSystem extends Component {
    private Spritesheet gizmos; // Spritesheet chứa các Sprite cho Gizmos.
    private int usingGizmo = 0; // Biến để xác định Gizmo nào đang được sử dụng.

    // Constructor nhận một Spritesheet để tạo Gizmos.
    public GizmoSystem(Spritesheet gizmoSprites) {
        gizmos = gizmoSprites;
    }

    // Phương thức start được gọi khi GizmoSystem được khởi tạo.
    @Override
    public void start() {
        // Thêm Gizmos vào GameObject với các Sprite đã chọn.
        gameObject.addComponent(new TranslateGizmo(gizmos.getSprite(1),
                Window.getImguiLayer().getPropertiesWindow()));
        gameObject.addComponent(new ScaleGizmo(gizmos.getSprite(2),
                Window.getImguiLayer().getPropertiesWindow()));
    }

    // Phương thức editorUpdate được gọi trong chế độ chỉnh sửa để cập nhật GizmoSystem.
    @Override
    public void editorUpdate(float dt) {
        // Xác định Gizmo nào đang được sử dụng dựa trên giá trị của biến usingGizmo.
        if (usingGizmo == 0) {
            gameObject.getComponent(TranslateGizmo.class).setUsing();
            gameObject.getComponent(ScaleGizmo.class).setNotUsing();
        } else if (usingGizmo == 1) {
            gameObject.getComponent(TranslateGizmo.class).setNotUsing();
            gameObject.getComponent(ScaleGizmo.class).setUsing();
        }

        // Kiểm tra phím được nhấn để chuyển đổi giữa các loại Gizmo.
        if (KeyListener.isKeyPressed(GLFW_KEY_E)) {
            usingGizmo = 0; // Chọn TranslateGizmo.
        } else if (KeyListener.isKeyPressed(GLFW_KEY_R)) {
            usingGizmo = 1; // Chọn ScaleGizmo.
        }
    }
}
