package components;

import jade.GameObject;
import jade.MouseListener;
import jade.Window;
import org.lwjgl.glfw.GLFW;
import util.Settings;

public class MouseControls extends Component {
    GameObject holdingObject = null;

    /** Giữ chuột vào game object => Add vào scene */
    public void pickupObject(GameObject go) {
        this.holdingObject = go;
        Window.getScene().addGameObjectToScene(go);
    }

    /** Nhả chuột */
    public void place() {
         this.holdingObject = null;
    }

    @Override
    public void update(float dt) {
        /** Nếu đang giữ 1 game object */
        if (this.holdingObject != null) {
            /** Cập nhật vị trí game object theo chuột */
            this.holdingObject.transform.position.x = MouseListener.getOrthoX();
            this.holdingObject.transform.position.y = MouseListener.getOrthoY();

            // khi giữ các khối vuông sẽ luôn vừa khít lỗ... với gridlines (hay các ô vuông do các đường kẻ tạo ra)
            this.holdingObject.transform.position.x = (int)(holdingObject.transform.position.x / Settings.GRID_WIDTH) * Settings.GRID_WIDTH;
            this.holdingObject.transform.position.y = (int)(holdingObject.transform.position.y / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT;

            if (MouseListener.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
                place();
            }
        }
    }
}
