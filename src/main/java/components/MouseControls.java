package components;

import jade.GameObject;
import jade.MouseListener;
import jade.Window;
import org.lwjgl.glfw.GLFW;

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
            this.holdingObject.transform.position.x = MouseListener.getOrthoX() - 16;
            this.holdingObject.transform.position.y = MouseListener.getOrthoY() - 16;

            if (MouseListener.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
                place();
            }
        }
    }
}
