package components;

import editor.PropertiesWindow;
import jade.GameObject;
import jade.Prefabs;
import jade.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class TranslateGizmo extends Component {
    //Màu sắc cho các trục X và Y khi không được hover, Màu sắc cho các trục X và Y khi được hover.
    private Vector4f xAxisColor = new Vector4f(1, 0, 0, 1);
    private Vector4f xAxisColorHover = new Vector4f();
    private Vector4f yAxisColor = new Vector4f(0, 1, 0, 1);
    private Vector4f yAxisColorHover = new Vector4f();

    //Đối tượng đại diện cho các trục X và Y trong không gian 3D.
    private GameObject xAxisObject;
    private GameObject yAxisObject;

    //Đối tượng GameObject đang được chọn hoặc tương tác.
    private SpriteRenderer xAxisSprite;
    private SpriteRenderer yAxisSprite;
    private GameObject activeGameObject = null;

    //Vector2f xác định vị trí offset của các đối tượng trục X và Y.
    private Vector2f xAxisOffset = new Vector2f(64, -5);
    private Vector2f yAxisOffset = new Vector2f(16, 61);

    //Tham chiếu đến cửa sổ thuộc tính, có thể được sử dụng để theo dõi đối tượng đang được chọn.
    private PropertiesWindow propertiesWindow;

    public TranslateGizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow) {
        this.xAxisObject = Prefabs.generateSpriteObject(arrowSprite, 16, 48);
        this.yAxisObject = Prefabs.generateSpriteObject(arrowSprite, 16, 48);
        this.xAxisSprite = this.xAxisObject.getComponent(SpriteRenderer.class);
        this.yAxisSprite = this.yAxisObject.getComponent(SpriteRenderer.class);
        this.propertiesWindow = propertiesWindow;

        Window.getScene().addGameObjectToScene(this.xAxisObject);
        Window.getScene().addGameObjectToScene(this.yAxisObject);
    }

    @Override
    public void start() {
        this.xAxisObject.transform.rotation = 90;
        this.yAxisObject.transform.rotation = 180;
        this.xAxisObject.setNoSerialize();
        this.yAxisObject.setNoSerialize();
    }
    //Cập nhật vị trí của các đối tượng trục X và Y dựa trên vị trí của đối tượng đang được chọn (nếu có).
    //Kiểm tra và cập nhật đối tượng đang được chọn từ cửa sổ thuộc tính.
    //Nếu có đối tượng đang được chọn, gọi setActive(), ngược lại, gọi setInactive().
    @Override
    public void update(float dt) {
        if (this.activeGameObject != null) {
            this.xAxisObject.transform.position.set(this.activeGameObject.transform.position);
            this.yAxisObject.transform.position.set(this.activeGameObject.transform.position);
            this.xAxisObject.transform.position.add(this.xAxisOffset);
            this.yAxisObject.transform.position.add(this.yAxisOffset);
        }

        this.activeGameObject = this.propertiesWindow.getActiveGameObject();
        if (this.activeGameObject != null) {
            this.setActive();
        } else {
            this.setInactive();
        }
    }
    //Thiết lập màu sắc của các đối tượng trục X và Y khi đối tượng đang được chọn.
    private void setActive() {
        this.xAxisSprite.setColor(xAxisColor);
        this.yAxisSprite.setColor(yAxisColor);
    }
    //Vô hiệu hóa các đối tượng trục X và Y khi không có đối tượng nào đang được chọn.
    private void setInactive() {
        this.activeGameObject = null;
        this.xAxisSprite.setColor(new Vector4f(0, 0, 0, 0));
        this.yAxisSprite.setColor(new Vector4f(0, 0, 0, 0));
    }
}
