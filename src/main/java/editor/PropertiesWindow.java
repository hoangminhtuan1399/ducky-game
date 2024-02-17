package editor;

import components.SpriteRenderer;
import imgui.ImGui;
import jade.GameObject;
import org.joml.Vector4f;
import physics2d.components.Box2DCollider;
import physics2d.components.CircleCollider;
import physics2d.components.Rigidbody2D;
import renderer.PickingTexture;

import java.util.ArrayList;
import java.util.List;

// Lớp PropertiesWindow quản lý cửa sổ hiển thị thuộc tính và tương tác với đối tượng trong trò chơi.
public class PropertiesWindow {
    //thêm để chọn nhiều block
    private List<GameObject> activeGameObjects;
    private List<Vector4f> activeGameObjectOgColor;
    private GameObject activeGameObject = null;
    private PickingTexture pickingTexture;

    private float debounce = 0.2f;

    // Constructor nhận vào một PickingTexture để đọc thông tin pixel từ texture.
    public PropertiesWindow(PickingTexture pickingTexture) {
        this.activeGameObjects = new ArrayList<>();
        this.pickingTexture = pickingTexture;
        this.activeGameObjectOgColor = new ArrayList<>();
    }

    // Phương thức hiển thị giao diện người dùng cho cửa sổ thuộc tính của đối tượng.
    public void imgui() {
        // có thể có nhiều đối tượng đc chọn
        if (activeGameObjects.size() == 1 && activeGameObjects.get(0) != null) {

            activeGameObject = activeGameObjects.get(0);
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
        return activeGameObjects.size() == 1 ? this.activeGameObjects.get(0) :
                null;
    }

    // Phương thức trả về nhiều đối tượng đang chọn.
    public List<GameObject> getActiveGameObjects() {
        return this.activeGameObjects;
    }

    public void clearSelected() {
        if (activeGameObjectOgColor.size() > 0) {
            int i = 0;
            for (GameObject go : activeGameObjects) {
                SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
                if (spr != null) {
                    spr.setColor(activeGameObjectOgColor.get(i));
                }
                i++;
            }
        }
        this.activeGameObjects.clear();
        this.activeGameObjectOgColor.clear();
    }

    // Phương thức đặt đối tượng đang chọn.
    public void setActiveGameObject(GameObject go) {
        if (go != null) {
            clearSelected();
            this.activeGameObjects.add(go);
        }
    }

    // Phương thức thêm đối tượng.
    public void addActiveGameObject(GameObject go) {
        SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
        if (spr != null ) {
            this.activeGameObjectOgColor.add(new Vector4f(spr.getColor()));
            spr.setColor(new Vector4f(0.8f, 0.8f, 0.0f, 0.8f));
        } else {
            this.activeGameObjectOgColor.add(new Vector4f());
        }
        this.activeGameObjects.add(go);
    }

    public PickingTexture getPickingTexture() {
        return this.pickingTexture;
    }
}
