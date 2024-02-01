package renderer;

import org.joml.Vector2f;
import org.joml.Vector3f;

/** tạo đường thẳng lằng nhắng lắm lày: từ hình từ giác tạo thành đường thẳng */
public class Line2D {
    private Vector2f from;
    private Vector2f to;
    private Vector3f color;
    private int lifetime;

    public Line2D(Vector2f from, Vector2f to, Vector3f color, int lifetime) {
        this.from = from;
        this.to = to;
        this.color = color;
        this.lifetime = lifetime;
    }

    // thời gian của 1 dòng dc xác định theo tốc độ khung hình
    public int beginFrame() {
        this.lifetime--;
        return this.lifetime;
    }

    public Vector2f getFrom() {
        return from;
    }

    public Vector2f getTo() {
        return to;
    }

    public Vector3f getColor() {
        return color;
    }
}
