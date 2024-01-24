package jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    private static KeyListener instance;
    private boolean keyPresses[] = new boolean[350];

    private KeyListener() {

    }

    public static KeyListener get() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }

        return KeyListener.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().keyPresses[key] = true;
        } else if (action == GLFW_RELEASE) {
            get().keyPresses[key] = false;
        }
    }

    public static boolean isKeyPress(int keyCode) {
        return get().keyPresses[keyCode];
    }
}
