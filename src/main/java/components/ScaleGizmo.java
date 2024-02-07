package components;

import editor.PropertiesWindow;
import jade.MouseListener;

public class ScaleGizmo extends  Gizmos {
    // Constructor của ScaleGizmo, nhận vào một sprite và một PropertiesWindow
    public ScaleGizmo(Sprite scaleSprite, PropertiesWindow propertiesWindow) {
        // Gọi constructor của lớp cha (Gizmos) để khởi tạo các thuộc tính chung của Gizmo
        super(scaleSprite, propertiesWindow);
    }

    // Phương thức update được gọi mỗi frame để cập nhật trạng thái của ScaleGizmo
    @Override
    public void update(float dt) {
        // Kiểm tra xem có đối tượng đang được chọn không
        if (activeGameObject != null) {
            // Nếu chỉ có trục x được chọn, cập nhật tỷ lệ theo hướng x
            if (xAxisActive && !yAxisActive) {
                activeGameObject.transform.scale.x -= MouseListener.getWorldDx();
            }
            // Nếu chỉ có trục y được chọn, cập nhật tỷ lệ theo hướng y
            else if (yAxisActive) {
                activeGameObject.transform.scale.y -= MouseListener.getWorldDy();
            }
        }

        // Gọi phương thức update của lớp cha (Gizmos) để thực hiện các công việc chung của Gizmo
        super.update(dt);
    }
}
