package components;

import imgui.ImGui;
import jade.GameObject;
import org.joml.Vector3f;
import org.joml.Vector4d;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Component {
    private static int ID_COUNTER = 0;
    private int uid = -1;
    public transient GameObject gameObject = null;

    public void start() {

    }

    public void update(float dt) {

    }

    public void imgui() {
        try {
            /** Get các properties của component để khởi tạo field trong ImGUI tương ứng */
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field: fields) {
                /** Skip property nếu là transient */
                boolean isTransient = Modifier.isTransient(field.getModifiers());
                if (isTransient) {
                    continue;
                }

                /** Nếu là private, tạm thời mở access */
                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                if (isPrivate) {
                    field.setAccessible(true);
                }

                /** Lấy thông tin của property: Class, value, name */
                Class type = field.getType();
                Object value = field.get(this);
                String name = field.getName();

                /** Khởi tạo field trong ImGui tương ứng */
                if (type == int.class) {
                    int val = (int) value;
                    int[] imInt = {val};
                    if (ImGui.dragInt(name + ": ", imInt)) {
                        field.set(this, imInt[0]);
                    }
                } else if (type == float.class) {
                    float val = (float) value;
                    float[] imFloat = {val};
                    if (ImGui.dragFloat(name + ": ", imFloat)) {
                        field.set(this, imFloat[0]);
                    }
                } else if (type == boolean.class) {
                    boolean val = (boolean) value;
                    boolean[] imBoolean = {val};
                    if (ImGui.checkbox(name + ": ", val)) {
                        field.set(this, !val);
                    }
                } else if (type == Vector3f.class) {
                    Vector3f val = (Vector3f) value;
                    float[] imVec = {val.x, val.y, val.z};
                    if (ImGui.dragFloat3(name + ": ", imVec)) {
                        val.set(imVec[0], imVec[1], imVec[2]);
                    }
                } else if (type == Vector4f.class) {
                    Vector4f val = (Vector4f) value;
                    float[] imVec = {val.x, val.y, val.z, val.w};
                    if (ImGui.dragFloat4(name + ": ", imVec)) {
                        val.set(imVec[0], imVec[1], imVec[2], imVec[3]);
                    }
                }

                /** Đóng access sau khi thao tác */
                if (isPrivate) {
                    field.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /** Khởi tạo và gán id cho component */
    public void generateId() {
        if (this.uid == -1) {
            this.uid = ID_COUNTER++;
        }
    }

    public int getUid() {
        return this.uid;
    }

    public static void init(int maxId) {
        ID_COUNTER = maxId;
    }
}