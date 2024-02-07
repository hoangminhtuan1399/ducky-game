package jade;

import components.Component;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;

// Lớp GameObject đại diện cho một đối tượng trong thế giới game.
public class GameObject {
    // Biến đếm tĩnh để gán các ID duy nhất cho GameObjects.
    private static int ID_COUNTER = 0;

    // Định danh duy nhất cho mỗi GameObject.
    private int uid = -1;

    // Tên của GameObject.
    private String name;

    // Danh sách các thành phần đính kèm vào GameObject.
    private List<Component> components;

    // Thành phần Transform đại diện cho vị trí, tỉ lệ, xoay và zIndex.
    public transient Transform transform;

    // Cờ để kiểm soát việc nạp chồng (serialization) của GameObject.
    private boolean doSerialization = true;

    // Constructor để khởi tạo một GameObject với tên cụ thể.
    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();

        // Gán một ID duy nhất cho GameObject.
        this.uid = ID_COUNTER++;
    }

    // Lấy một loại cụ thể của thành phần được đính kèm vào GameObject.
    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false : "Lỗi: Ép kiểu thành phần.";
                }
            }
        }
        return null;
    }

    // Gỡ bỏ một loại cụ thể của thành phần khỏi GameObject.
    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i = 0; i < components.size(); i++) {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    // Thêm một thành phần vào GameObject.
    public void addComponent(Component c) {
        c.generateId();
        this.components.add(c);
        c.gameObject = this;
    }

    // Cập nhật tất cả các thành phần của GameObject.
    public void update(float dt) {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).update(dt);
        }
    }

    // Bắt đầu tất cả các thành phần của GameObject.
    public void start() {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).start();
        }
    }

    // Hiển thị các điều khiển ImGui cho mỗi thành phần của GameObject.
    public void imgui() {
        for (Component c : components) {
            if (ImGui.collapsingHeader(c.getClass().getSimpleName()))
                c.imgui();
        }
    }

    // Phương thức tĩnh để khởi tạo ID_COUNTER với một ID tối đa.
    public static void init(int maxId) {
        ID_COUNTER = maxId;
    }

    // Lấy định danh duy nhất của GameObject.
    public int getUid() {
        return this.uid;
    }

    // Lấy danh sách tất cả các thành phần đính kèm vào GameObject.
    public List<Component> getAllComponents() {
        return this.components;
    }

    // Đặt cờ nạp chồng để xác định liệu GameObject có nên được nạp chồng hay không.
    public void setNoSerialize() {
        this.doSerialization = false;
    }

    // Kiểm tra xem GameObject có nên được nạp chồng hay không.
    public boolean doSerialization() {
        return this.doSerialization;
    }
}
