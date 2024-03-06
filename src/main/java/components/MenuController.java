package components;

import jade.KeyListener;
import jade.Window;
import observers.EventSystem;
import observers.events.Event;
import observers.events.EventType;
import scenes.Scene;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;

public class MenuController extends Component {
    private boolean isPlaying = false;
    private Scene menuScene;

    @Override
    public void start() {
        this.menuScene = Window.getScene();
    }

    @Override
    public void update(float dt) {
        if (KeyListener.isKeyPressed(GLFW_KEY_ENTER)) {
            EventSystem.notify(null, new Event(EventType.GameEngineStartPlay));
        }
    }

}
