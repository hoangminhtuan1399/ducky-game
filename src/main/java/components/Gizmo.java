package components;

import editor.PropertiesWindow;
import jade.GameObject;
import jade.MouseListener;
import jade.Prefabs;
import jade.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

// Lớp Gizmo là một Component để xử lý và vẽ Gizmo trong chế độ chỉnh sửa.
public class Gizmo extends Component {
    // Các màu sắc của các trục của Gizmo và màu khi hover.
    private Vector4f xAxisColor = new Vector4f(1, 0.3f, 0.3f, 1);
    private Vector4f xAxisColorHover = new Vector4f(1, 0, 0, 1);
    private Vector4f yAxisColor = new Vector4f(0.3f, 1, 0.3f, 1);
    private Vector4f yAxisColorHover = new Vector4f(0, 1, 0, 1);

    // Đối tượng và SpriteRenderer của các mũi tên trục X và Y.
    private GameObject xAxisObject;
    private GameObject yAxisObject;
    private SpriteRenderer xAxisSprite;
    private SpriteRenderer yAxisSprite;
    protected GameObject activeGameObject = null;

    // Vị trí offset của các mũi tên trục X và Y.
    private Vector2f xAxisOffset = new Vector2f(64, -5);
    private Vector2f yAxisOffset = new Vector2f(16, 61);

    // Kích thước của Gizmo.
    private int gizmoWidth = 16;
    private int gizmoHeight = 48;

    // Trạng thái hoạt động của các trục.
    protected boolean xAxisActive = false;
    protected boolean yAxisActive = false;

    // Trạng thái sử dụng Gizmo.
    private boolean using = false;

    // PropertiesWindow để lấy thông tin về GameObject đang được chọn.
    private PropertiesWindow propertiesWindow;

    // Constructor nhận một Sprite để tạo mũi tên và PropertiesWindow để lấy thông tin GameObject.
    public Gizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow) {
        // Tạo đối tượng mũi tên X và Y với Sprite đã cho.
        this.xAxisObject = Prefabs.generateSpriteObject(arrowSprite, 16, 48);
        this.yAxisObject = Prefabs.generateSpriteObject(arrowSprite, 16, 48);

        // Lấy SpriteRenderer của đối tượng mũi tên.
        this.xAxisSprite = this.xAxisObject.getComponent(SpriteRenderer.class);
        this.yAxisSprite = this.yAxisObject.getComponent(SpriteRenderer.class);

        // Gán PropertiesWindow để lấy thông tin về GameObject.
        this.propertiesWindow = propertiesWindow;

        // Thêm NonPickable để tránh việc chọn Gizmo trong chế độ chỉnh sửa.
        this.xAxisObject.addComponent(new NonPickable());
        this.yAxisObject.addComponent(new NonPickable());

        // Thêm các đối tượng vào Scene.
        Window.getScene().addGameObjectToScene(this.xAxisObject);
        Window.getScene().addGameObjectToScene(this.yAxisObject);
    }

    // Phương thức start được gọi khi Gizmo được khởi tạo.
    @Override
    public void start() {
        // Thiết lập góc xoay và zIndex cho mỗi mũi tên.
        this.xAxisObject.transform.rotation = 90;
        this.yAxisObject.transform.rotation = 180;
        this.xAxisObject.transform.zIndex = 100;
        this.yAxisObject.transform.zIndex = 100;

        // Đánh dấu các đối tượng này để không được serialize khi lưu trạng thái Scene.
        this.xAxisObject.setNoSerialize();
        this.yAxisObject.setNoSerialize();
    }

    // Phương thức update được gọi trong trạng thái chạy game.
    @Override
    public void update(float dt) {
        // Nếu đang sử dụng Gizmo, đặt trạng thái không hoạt động.
        if (using) {
            this.setInactive();
        }
    }

    // Phương thức editorUpdate được gọi trong chế độ chỉnh sửa để cập nhật Gizmo.
    @Override
    public void editorUpdate(float dt) {
        // Nếu không sử dụng Gizmo, thoát phương thức.
        if (!using) return;

        // Lấy GameObject đang được chọn từ PropertiesWindow.
        this.activeGameObject = this.propertiesWindow.getActiveGameObject();
        if (this.activeGameObject != null) {
            // Nếu có GameObject đang được chọn, đặt trạng thái hoạt động.
            this.setActive();
        } else {
            // Nếu không có GameObject đang được chọn, đặt trạng thái không hoạt động.
            this.setInactive();
            return;
        }

        // Kiểm tra trạng thái hover của các mũi tên X và Y.
        boolean xAxisHot = checkXHoverState();
        boolean yAxisHot = checkYHoverState();

        // Xác định trạng thái hoạt động của trục dựa trên sự kéo chuột và hover.
        if ((xAxisHot || xAxisActive) && MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            xAxisActive = true;
            yAxisActive = false;
        } else if ((yAxisHot || yAxisActive) && MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            yAxisActive = true;
            xAxisActive = false;
        } else {
            xAxisActive = false;
            yAxisActive = false;
        }

        // Cập nhật vị trí của mũi tên theo vị trí của GameObject.
        if (this.activeGameObject != null) {
            this.xAxisObject.transform.position.set(this.activeGameObject.transform.position);
            this.yAxisObject.transform.position.set(this.activeGameObject.transform.position);
            this.xAxisObject.transform.position.add(this.xAxisOffset);
            this.yAxisObject.transform.position.add(this.yAxisOffset);
        }
    }

    // Đặt trạng thái hoạt động cho Gizmo.
    private void setActive() {
        this.xAxisSprite.setColor(xAxisColor);
        this.yAxisSprite.setColor(yAxisColor);
    }

    // Đặt trạng thái không hoạt động cho Gizmo.
    private void setInactive() {
        this.activeGameObject = null;
        this.xAxisSprite.setColor(new Vector4f(0, 0, 0, 0));
        this.yAxisSprite.setColor(new Vector4f(0, 0, 0, 0));
    }

    // Kiểm tra trạng thái hover của mũi tên X.
    private boolean checkXHoverState() {
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
        if (mousePos.x <= xAxisObject.transform.position.x &&
                mousePos.x >= xAxisObject.transform.position.x - gizmoHeight &&
                mousePos.y >= xAxisObject.transform.position.y &&
                mousePos.y <= xAxisObject.transform.position.y + gizmoWidth) {
            xAxisSprite.setColor(xAxisColorHover);
            return true;
        }

        xAxisSprite.setColor(xAxisColor);
        return false;
    }

    // Kiểm tra trạng thái hover của mũi tên Y.
    private boolean checkYHoverState() {
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
        if (mousePos.x <= yAxisObject.transform.position.x &&
                mousePos.x >= yAxisObject.transform.position.x - gizmoWidth &&
                mousePos.y <= yAxisObject.transform.position.y &&
                mousePos.y >= yAxisObject.transform.position.y - gizmoHeight) {
            yAxisSprite.setColor(yAxisColorHover);
            return true;
        }

        yAxisSprite.setColor(yAxisColor);
        return false;
    }

    // Đặt Gizmo vào trạng thái sử dụng.
    public void setUsing() {
        this.using = true;
    }

    // Đặt Gizmo vào trạng thái không sử dụng.
    public void setNotUsing() {
        this.using = false;
        this.setInactive();
    }
}
