package renderer;

import org.joml.Vector2f;
import org.joml.Vector3f;
/** tạo đường kẻ lằng nhắng lắm lày: từ hình từ giác tạo thành đường kẻ */
public class Line2D {
    private Vector2f from;
    private Vector2f to;
    private Vector3f color;
    private int lifetime;

    public Line2D(Vector2f from, Vector2f to) {
        this.from = from;
        this.to = to;
    }

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

    public Vector2f getStart() {
        return this.from;
    }

    public Vector2f getEnd() {
        return this.to;
    }

    public Vector3f getColor() {
        return color;
    }

    public float lengthSquared() {
        return new Vector2f(to).sub(from).lengthSquared();
    }
}