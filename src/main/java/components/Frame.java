package components;

public class Frame {
    //hoạt ảnh giữa nhiều ảnh (sprite)
    public Sprite sprite;
    public float frameTime;

    public Frame() {

    }

    public Frame(Sprite sprite, float frameTime) {
        this.sprite = sprite;
        this.frameTime = frameTime;
    }
}
