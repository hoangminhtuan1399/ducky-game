package scene;

import components.*;
import imgui.ImGui;
import imgui.ImVec2;
import jade.Camera;
import jade.GameObject;
import jade.Prefabs;
import jade.Transform;
import org.joml.Vector2f;
import util.AssetPool;

public class LevelEditorScene extends Scene {

    private GameObject obj1;
    private Spritesheet sprites;
    SpriteRenderer obj1Sprite;
//    MouseControls mouseControls = new MouseControls();
    GameObject levelEditorStuff = new GameObject("LevelEditor", new Transform(new Vector2f()), 0);

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new GridLines());

        loadResources();
        this.camera = new Camera(new Vector2f(-250, 0));

        /** Load spritesheet from AssetPool trước khi tạo GameObject */
        sprites = AssetPool.getSpritesheet("assets/images/spritesheets/decorationsAndBlocks.png");


//        obj1 = new GameObject("Object 1", new Transform(new Vector2f(200, 100), new Vector2f(256, 256)), 2);
//        obj1Sprite = new SpriteRenderer();
//        obj1Sprite.setColor(new Vector4f(1, 0, 0, 1));
//        obj1.addComponent(obj1Sprite);
//
//        /** Add 1 component để test hàm ImGUI */
//        obj1.addComponent(new RigidBody());
//        this.addGameObjectToScene(obj1);
//        this.activeGameObject = obj1;
//
//        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 3);
//        SpriteRenderer obj2SpriteRenderer = new SpriteRenderer();
//        Sprite obj2Sprite = new Sprite();
//        obj2Sprite.setTexture(AssetPool.getTexture("assets/images/blendImage2.png"));
//        obj2SpriteRenderer.setSprite(obj2Sprite);
//        obj2.addComponent(obj2SpriteRenderer);
//        this.addGameObjectToScene(obj2);
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpritesheet("assets/images/spritesheets/decorationsAndBlocks.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheets/decorationsAndBlocks.png"),
                        16, 16, 81, 0));
        AssetPool.getTexture("assets/images/blendImage2.png");

        for (GameObject g : gameObjects){
            if (g.getComponent(SpriteRenderer.class) != null){
                SpriteRenderer spr = g.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null){
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));
                }
            }
        }
    }


    @Override
    public void update(float dt) {
        levelEditorStuff.update(dt);
//        //tạo thử cái hình tròn
//        DebugDraw.addCircle(new Vector2f(400, 200), 64, new Vector3f(1,0,0), 1);

//        // tọa thử cái tứ giác xoay xoay
//        DebugDraw.addBox2D(new Vector2f(400, 200), new Vector2f(64, 32), angle, new Vector3f(0,1,0), 1);
//        angle += 40.0f * dt;

//        //cho đường kẻ chạy vòng tròn
//        float x = ((float)Math.sin(t) * 200.0f) + 600;
//        float y = ((float)Math.cos(t) * 200.0f) + 400;
//        t +=  0.05f;
//        //có 4 tham số cho 1 đưởng kẻ nối giữa 2 điểm: cái thứ 1 là điểm đầu tiền, thứ 2 điểm 2, thứ 3 là màu, thứ 4 là tgian nó tồn tại
//        DebugDraw.addLine2D(new Vector2f(600, 400), new Vector2f(x, y), new Vector3f(0,0,1), 10);


        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }

    @Override
    public void render() {
        this.renderer.render();
    }

    @Override
    public void imgui() {
        ImGui.begin("Test window");

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;

        /** Duyệt qua từng phần tử trong mảng sprite */
        for (int i = 0; i < sprites.size(); i++) {
            /** Lấy thông tin sprite để khởi tạo image button */
            Sprite sprite = sprites.getSprite(i);
            float spriteWidth = sprite.getWidth() * 4;
            float spriteHeight = sprite.getHeight() * 4;
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();




            ImGui.pushID(i);
            /**
             * Add image button vào ImGui và kiểm tra event listener tại image button đó:
             * texTureId
             * kích thước của sprite (width, height) phải vừa vs gridlines
             * toạ độ góc trên-phải và dưới-trái
             * */
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                GameObject object = Prefabs.generateSpriteObject(sprite, 32, 32);
                levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
            }
            ImGui.popID();

            /** Kiểm tra xem button tiếp theo có bị tràn ra ngoài window không */
            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);

            /** Lấy viền ngoài của button hiện tại */
            float lastButtonX2 = lastButtonPos.x;

            /** Lấy viền ngoài của button tiếp theo */
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;

            /** Kiểm tra tràn viền */
            if (i + 1 < sprites.size() && nextButtonX2 < windowX2) {
                ImGui.sameLine();
            }
        }

        ImGui.end();
    }
}