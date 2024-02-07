package components;

import jade.KeyListener;
import jade.Window;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;

public class GizmoSystem extends Component {
    private Spritesheet gizmos;  // Đối tượng Spritesheet chứa các hình ảnh của các Gizmo
    private int usingGizmo = 0;  // Biến theo dõi Gizmo nào đang được sử dụng

    // Constructor, nhận vào một Spritesheet để khởi tạo đối tượng GizmoSystem
    public GizmoSystem(Spritesheet gizmoSprites) {
        gizmos = gizmoSprites;
    }

    // Phương thức start() được gọi khi đối tượng được tạo và bắt đầu tồn tại
    @Override
    public void start() {
        // Thêm các Gizmo vào đối tượng GameObject
        // TranslateGizmo sử dụng sprite ở vị trí 1 trong Spritesheet
        gameObject.addComponent(new TranslateGizmo(gizmos.getSprite(1),
                Window.getImguiLayer().getPropertiesWindow()));
        // ScaleGizmo sử dụng sprite ở vị trí 2 trong Spritesheet
        gameObject.addComponent(new ScaleGizmo(gizmos.getSprite(2),
                Window.getImguiLayer().getPropertiesWindow()));
    }

    // Phương thức update() được gọi trong mỗi frame của trò chơi
    @Override
    public void update(float dt) {
        // Kiểm tra xem Gizmo nào đang được sử dụng và cập nhật trạng thái của chúng
        if (usingGizmo == 0) {
            gameObject.getComponent(TranslateGizmo.class).setUsing();
            gameObject.getComponent(ScaleGizmo.class).setNotUsing();
        } else if (usingGizmo == 1) {
            gameObject.getComponent(TranslateGizmo.class).setNotUsing();
            gameObject.getComponent(ScaleGizmo.class).setUsing();
        }

        // Kiểm tra phím được nhấn để chuyển đổi giữa dịch chuyển Gizmo và tỉ lệ Gizmo
        if (KeyListener.isKeyPressed(GLFW_KEY_E)) { //dịch chuyển
            usingGizmo = 0;
        } else if (KeyListener.isKeyPressed(GLFW_KEY_R)) { //tỉ lệ
            usingGizmo = 1;
        }
    }
}
