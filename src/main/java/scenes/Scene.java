package scenes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Component;
import components.ComponentDeserializer;
import imgui.ImGui;
import jade.Camera;
import jade.GameObject;
import jade.GameObjectDeserializer;
import jade.Transform;
import org.joml.Vector2f;
import physics2d.Physics2D;
import renderer.Renderer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Đại diện cho một cảnh trong trò chơi.
 */
public class Scene {

    private Renderer renderer;
    private Camera camera;
    private boolean isRunning;
    private List<GameObject> gameObjects;
    private Physics2D physics2D;

    private SceneInitializer sceneInitializer;

    /**
     * Xây dựng một Scene với một SceneInitializer cụ thể.
     *
     * @param sceneInitializer SceneInitializer để sử dụng cho thiết lập cảnh.
     */
    public Scene(SceneInitializer sceneInitializer) {
        this.sceneInitializer = sceneInitializer;
        this.physics2D = new Physics2D();
        this.renderer = new Renderer();
        this.gameObjects = new ArrayList<>();
        this.isRunning = false;
    }

    /**
     * Khởi tạo cảnh.
     */
    public void init() {
        this.camera = new Camera(new Vector2f(-250, 0));
        this.sceneInitializer.loadResources(this);
        this.sceneInitializer.init(this);
    }

    /**
     * Bắt đầu cảnh bằng cách gọi phương thức start trên tất cả các đối tượng trò chơi.
     */
    public void start() {
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject go = gameObjects.get(i);
            go.start();
            this.renderer.add(go);
            this.physics2D.add(go);
        }
        isRunning = true;
    }

    /**
     * Thêm một đối tượng trò chơi vào cảnh.
     *
     * @param go GameObject để thêm vào.
     */
    public void addGameObjectToScene(GameObject go) {
        if (!isRunning) {
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
            this.physics2D.add(go);
        }
    }

    /**
     * Hủy bỏ cảnh bằng cách gọi phương thức destroy trên tất cả các đối tượng trò chơi.
     */
    public void destroy() {
        for (GameObject go : gameObjects) {
            go.destroy();
        }
    }

    /**
     * Lấy danh sách các đối tượng trò chơi trong cảnh.
     *
     * @return Danh sách các đối tượng trò chơi.
     */
    public List<GameObject> getGameObjects() {
        return this.gameObjects;
    }

    /**
     * Lấy đối tượng trò chơi theo ID.
     *
     * @param gameObjectId ID của đối tượng trò chơi.
     * @return Đối tượng trò chơi hoặc null nếu không tìm thấy.
     */
    public GameObject getGameObject(int gameObjectId) {
        Optional<GameObject> result = this.gameObjects.stream()
                .filter(gameObject -> gameObject.getUid() == gameObjectId)
                .findFirst();
        return result.orElse(null);
    }

    /**
     * Cập nhật trình biên tập cảnh.
     *
     * @param dt Thời gian delta.
     */
    public void editorUpdate(float dt) {
        this.camera.adjustProjection();

        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject go = gameObjects.get(i);
            go.editorUpdate(dt);

            if (go.isDead()) {
                gameObjects.remove(i);
                this.renderer.destroyGameObject(go);
                this.physics2D.destroyGameObject(go);
                i--;
            }
        }
    }

    /**
     * Cập nhật cảnh.
     *
     * @param dt Thời gian delta.
     */
    public void update(float dt) {
        this.camera.adjustProjection();
        this.physics2D.update(dt);

        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject go = gameObjects.get(i);
            go.update(dt);

            if (go.isDead()) {
                gameObjects.remove(i);
                this.renderer.destroyGameObject(go);
                this.physics2D.destroyGameObject(go);
                i--;
            }
        }
    }

    /**
     * Render cảnh.
     */
    public void render() {
        this.renderer.render();
    }

    /**
     * Trả về Camera của cảnh.
     *
     * @return Camera của cảnh.
     */
    public Camera camera() {
        return this.camera;
    }

    /**
     * Hiển thị giao diện ImGui.
     */
    public void imgui() {
        this.sceneInitializer.imgui();
    }

    /**
     * Tạo một đối tượng trò chơi mới với tên cho trước.
     *
     * @param name Tên của đối tượng trò chơi mới.
     * @return Đối tượng trò chơi mới tạo.
     */
    public GameObject createGameObject(String name) {
        GameObject go = new GameObject(name);
        go.addComponent(new Transform());
        go.transform = go.getComponent(Transform.class);
        return go;
    }

    /**
     * Lưu cảnh xuống tệp với định dạng JSON sử dụng Gson.
     */
    public void save() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        try {
            FileWriter writer = new FileWriter("level.txt");
            List<GameObject> objsToSerialize = new ArrayList<>();
            for (GameObject obj : this.gameObjects) {
                if (obj.doSerialization()) {
                    objsToSerialize.add(obj);
                }
            }
            writer.write(gson.toJson(objsToSerialize));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Đọc cảnh từ tệp JSON sử dụng Gson.
     */
    public void load() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get("level.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.equals("")) {
            int maxGoId = -1;
            int maxCompId = -1;
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for (int i = 0; i < objs.length; i++) {
                addGameObjectToScene(objs[i]);

                for (Component c : objs[i].getAllComponents()) {
                    if (c.getUid() > maxCompId) {
                        maxCompId = c.getUid();
                    }
                }
                if (objs[i].getUid() > maxGoId) {
                    maxGoId = objs[i].getUid();
                }
            }

            maxGoId++;
            maxCompId++;
            GameObject.init(maxGoId);
            Component.init(maxCompId);
        }
    }
}
