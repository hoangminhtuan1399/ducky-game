package editor;

import components.NonPickable;
import imgui.ImGui;
import jade.GameObject;
import jade.MouseListener;
import physics2d.components.Box2DCollider;
import physics2d.components.CircleCollider;
import physics2d.components.Rigidbody2D;
import renderer.PickingTexture;
import scenes.Scene;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

// Lớp PropertiesWindow quản lý cửa sổ hiển thị thuộc tính và tương tác với đối tượng trong trò chơi.
public class PropertiesWindow {
    private GameObject activeGameObject = null;
    private PickingTexture pickingTexture;

    private float debounce = 0.2f;

    // Constructor nhận vào một PickingTexture để đọc thông tin pixel từ texture.
    public PropertiesWindow(PickingTexture pickingTexture) {
        this.pickingTexture = pickingTexture;
    }

    // Phương thức cập nhật, được gọi trong mỗi frame để kiểm tra sự kiện click chuột và chọn đối tượng.
    public void update(float dt, Scene currentScene) {
        debounce -= dt;

        // Kiểm tra xem nút chuột trái có được nhấn và debounce đã hết hay chưa.
        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0) {
            int x = (int)MouseListener.getScreenX();
            int y = (int)MouseListener.getScreenY();

            // Đọc giá trị pixel từ texture để xác định đối tượng được chọn.
            int gameObjectId = pickingTexture.readPixel(x, y);
            GameObject pickedObj = currentScene.getGameObject(gameObjectId);

            // Kiểm tra xem đối tượng đã chọn có tồn tại và không phải là đối tượng không thể chọn.
            if (pickedObj != null && pickedObj.getComponent(NonPickable.class) == null) {
                activeGameObject = pickedObj;
            } else if (pickedObj == null && !MouseListener.isDragging()) {
                // Nếu không có đối tượng nào được chọn và không có sự kéo thả, đặt đối tượng đang chọn là null.
                activeGameObject = null;
            }
            this.debounce = 0.2f; // Đặt lại debounce để tránh nhận nhiều sự kiện click liên tiếp.
        }
    }

    // Phương thức hiển thị giao diện người dùng cho cửa sổ thuộc tính của đối tượng.
    public void imgui() {
        if (activeGameObject != null) {
            ImGui.begin("Properties");

            // Hiển thị menu ngữ cảnh để thêm các thành phần mới cho đối tượng.
            if (ImGui.beginPopupContextWindow("ComponentAdder")) {
                if (ImGui.menuItem("Add Rigidbody")) {
                    if (activeGameObject.getComponent(Rigidbody2D.class) == null) {
                        activeGameObject.addComponent(new Rigidbody2D());
                    }
                }

                if (ImGui.menuItem("Add Box Collider")) {
                    if (activeGameObject.getComponent(Box2DCollider.class) == null &&
                            activeGameObject.getComponent(CircleCollider.class) == null) {
                        activeGameObject.addComponent(new Box2DCollider());
                    }
                }

                if (ImGui.menuItem("Add Circle Collider")) {
                    if (activeGameObject.getComponent(CircleCollider.class) == null &&
                            activeGameObject.getComponent(Box2DCollider.class) == null) {
                        activeGameObject.addComponent(new CircleCollider());
                    }
                }

                ImGui.endPopup();
            }

            // Hiển thị giao diện cho đối tượng đang chọn.
            activeGameObject.imgui();
            ImGui.end();
        }
    }

    // Phương thức trả về đối tượng đang chọn.
    public GameObject getActiveGameObject() {
        return this.activeGameObject;
    }

    // Phương thức đặt đối tượng đang chọn.
    public void setActiveGameObject(GameObject go) {
        this.activeGameObject = go;
    }
}
