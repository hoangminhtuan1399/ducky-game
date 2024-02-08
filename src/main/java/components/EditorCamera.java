package components;

import components.Component;
import jade.Camera;
import jade.KeyListener;
import jade.MouseListener;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_DECIMAL;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_MIDDLE;

// Lớp EditorCamera là một Component đặc biệt để xử lý logic camera trong chế độ chỉnh sửa (editor).
public class EditorCamera extends Component {

    private float dragDebounce = 0.032f;

    private Camera levelEditorCamera;
    private Vector2f clickOrigin;
    private boolean reset = false;

    private float lerpTime = 0.0f;
    private float dragSensitivity = 30.0f;
    private float scrollSensitivity = 0.1f;

    // Constructor nhận một đối tượng Camera để quản lý camera trong chế độ chỉnh sửa.
    public EditorCamera(Camera levelEditorCamera) {
        this.levelEditorCamera = levelEditorCamera;
        this.clickOrigin = new Vector2f();
    }

    // Phương thức editorUpdate được gọi trong chế độ chỉnh sửa để cập nhật logic của EditorCamera.
    @Override
    public void editorUpdate(float dt) {
        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE) && dragDebounce > 0) {
            // Khi nút giữa chuột được nhấn, ghi nhớ vị trí chuột để tính toán sự chuyển động.
            this.clickOrigin = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
            dragDebounce -= dt;
            return;
        } else if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)) {
            // Khi nút giữa chuột được giữ, thực hiện chuyển động theo sự chuyển động của chuột.
            Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
            Vector2f delta = new Vector2f(mousePos).sub(this.clickOrigin);
            levelEditorCamera.position.sub(delta.mul(dt).mul(dragSensitivity));
            this.clickOrigin.lerp(mousePos, dt);
        }

        if (dragDebounce <= 0.0f && !MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)) {
            // Thiết lập thời gian debounce khi nút giữa chuột được thả.
            dragDebounce = 0.1f;
        }

        if (MouseListener.getScrollY() != 0.0f) {
            // Điều chỉnh khoảng cách zoom của camera dựa trên sự cuộn chuột.
            float addValue = (float)Math.pow(Math.abs(MouseListener.getScrollY() * scrollSensitivity),
                    1 / levelEditorCamera.getZoom());
            addValue *= -Math.signum(MouseListener.getScrollY());
            levelEditorCamera.addZoom(addValue);
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_KP_DECIMAL)) {
            // Thiết lập cờ để reset camera khi nút "KP_DECIMAL" được nhấn.
            reset = true;
        }

        if (reset) {
            // Thực hiện reset camera về vị trí và zoom mặc định với hiệu ứng lerp.
            levelEditorCamera.position.lerp(new Vector2f(), lerpTime);
            levelEditorCamera.setZoom(this.levelEditorCamera.getZoom() +
                    ((1.0f - levelEditorCamera.getZoom()) * lerpTime));
            this.lerpTime += 0.1f * dt;
            if (Math.abs(levelEditorCamera.position.x) <= 5.0f &&
                    Math.abs(levelEditorCamera.position.y) <= 5.0f) {
                // Khi reset hoàn tất, thiết lập lại các giá trị và tắt cờ reset.
                this.lerpTime = 0.0f;
                levelEditorCamera.position.set(0f, 0f);
                this.levelEditorCamera.setZoom(1.0f);
                reset = false;
            }
        }
    }
}
