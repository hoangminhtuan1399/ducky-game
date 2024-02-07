package components;

import jade.Camera;
import jade.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;
import renderer.DebugDraw;
import util.Settings;

public class GridLines extends Component{

    @Override
    public void update(float dt) {
        Camera camera = Window.getScene().camera();
        Vector2f cameraPos = camera.position; // vị trí camera
        Vector2f projectionSize = camera.getProjectionSize(); // hình như là độ lớn của map

        // điểm đầu cho gridline (lưới hay các ô vuông ấy)
        int firstX = ((int)(cameraPos.x / Settings.GRID_WIDTH) - 1) * Settings.GRID_WIDTH;
        int firstY = ((int)(cameraPos.y / Settings.GRID_HEIGHT) - 1) * Settings.GRID_HEIGHT;

        int numVtLines = (int)(projectionSize.x * camera.getZoom()/ Settings.GRID_WIDTH) + 2; // số đường kẻ dọc
        int numHzLines = (int)(projectionSize.y * camera.getZoom()/ Settings.GRID_HEIGHT) + 2; //số đường kẻ ngang

        int height = (int)(projectionSize.y * camera.getZoom()) + Settings.GRID_HEIGHT * 2;
        int width = (int)(projectionSize.x * camera.getZoom())+ Settings.GRID_WIDTH * 2;

        //bắt đầu vẽ các đường kẻ
        int maxLines = Math.max(numHzLines, numVtLines);
        Vector3f color = new Vector3f(0.2f, 0.2f, 0.2f);
        for (int i = 0; i < maxLines; i++) {
            int x = firstX + (Settings.GRID_WIDTH * i);
            int y = firstY + (Settings.GRID_HEIGHT * i);

            // vẽ đường kẻ dọc
            if (i < numVtLines) {
                DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, firstY + height), color);
            }
            // vẽ đường kẻ ngang
            if (i < numHzLines) {
                DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width, y), color);
            }
        }
    }
}
