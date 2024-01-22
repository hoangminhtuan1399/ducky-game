package jade;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f projectionMatrix, viewMatrix;
    public Vector2f position;

    // Constructor với tham số là Vector2f position
    public Camera(Vector2f position) {
        // Khởi tạo biến position bằng giá trị truyền vào
        this.position = position;

        // Khởi tạo ma trận chiếu (projection matrix) và ma trận view (view matrix)
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();

        // Gọi phương thức adjustProjection để cấu hình ma trận chiếu
        adjustProjection();
    }

    // Phương thức cấu hình ma trận chiếu
    public void adjustProjection() {
        // Đặt ma trận chiếu là ma trận đơn vị (identity matrix)
        projectionMatrix.identity();

        // Thiết lập ma trận chiếu theo phương thức orthographic
        // Các tham số là left, right, bottom, top, near, far
        projectionMatrix.ortho(0.0f, 32.0f * 40.0f, 0.0f, 32.0f * 21.0f, 0.0f, 100.0f);
    }

    // Phương thức trả về ma trận view
    public Matrix4f getViewMatrix() {
        // Vector3f cameraFront là hướng mặt của camera
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);

        // Vector3f cameraUp là hướng lên của camera
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);

        // Đặt ma trận view là ma trận đơn vị
        this.viewMatrix.identity();

        // Gọi phương thức lookAt để cập nhật ma trận view
        this.viewMatrix = viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f),
                cameraFront.add(position.x, position.y, 0.0f), cameraUp);

        // Trả về ma trận view đã được cập nhật
        return this.viewMatrix;
    }

    // Phương thức trả về ma trận chiếu
    public Matrix4f getProjectionMatrix() {
        // Trả về ma trận chiếu
        return this.projectionMatrix;
    }
}
