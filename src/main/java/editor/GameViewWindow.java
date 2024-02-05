package editor;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import jade.MouseListener;
import jade.Window;
import org.joml.Vector2f;

public class GameViewWindow {

    private static float leftX, rightX, topY, bottomY;

    public static void imgui() {
        ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);

        /** tính toán kích thước và vị trí cửa sổ thông qua các phương thức hỗ trợ **/
        ImVec2 windowSize = getLargestSizeForViewport();
        ImVec2 windowPos = getCenteredPositionForViewport(windowSize);

        /** Đặt vị trí con trỏ của ImGui trong viewport tại vị trí đã tính toán trước đó **/
        ImGui.setCursorPos(windowPos.x, windowPos.y);

        /** Lấy tọa độ màn hình của điểm đầu của cửa sổ ImGui, cập nhật các biến đó để lưu trữ tọa độ của caác cạnh trái phải trên dưới **/
        ImVec2 topLeft = new ImVec2();
        ImGui.getCursorScreenPos(topLeft);
        topLeft.x -= ImGui.getScrollX();
        topLeft.y -= ImGui.getScrollY();
        leftX = topLeft.x;
        bottomY = topLeft.y;
        rightX = topLeft.x + windowSize.x;
        topY = topLeft.y + windowSize.y;

        /** Dòng này vẽ hình ảnh từ framebuffer của cửa sổ trò chơi vào cửa sổ ImGui sử dụng một texture ID **/
        int textureId = Window.getFramebuffer().getTextureID();
        ImGui.image(textureId, windowSize.x, windowSize.y, 0, 1, 1, 0);

        /** Xử lý chuột để cho hình ảnh kh cách xa chuột **/
        MouseListener.setGameViewportPos(new Vector2f(topLeft.x, topLeft.y));
        MouseListener.setGameViewportSize(new Vector2f(windowSize.x, windowSize.y));

        ImGui.end();
    }

    /** Kiểm tra xem chuột có nằm trong viewport trò chơi không **/
    public static boolean getWantCaptureMouse() {
        return MouseListener.getX() >= leftX && MouseListener.getX() <= rightX &&
                MouseListener.getY() >= bottomY && MouseListener.getY() <= topY;
    }

    /** Phương thức tính toán kích thước lớn nhất cho viewport dựa trên tỷ lệ khung hình cửa sổ **/
    private static ImVec2 getLargestSizeForViewport() {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);

        //giảm đi giá trị cuộn theo chiều ngang và dọc của cửa sổ ImGui
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / Window.getTargetAspectRatio();
        if (aspectHeight > windowSize.y) {
            // We must switch to pillarbox mode
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * Window.getTargetAspectRatio();
        }

        return new ImVec2(aspectWidth, aspectHeight);
    }

    /** Xử lý việc viewport luôn được căn giữa dựa trên kích thước của cửa sổ **/
    private static ImVec2 getCenteredPositionForViewport(ImVec2 aspectSize) {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float viewportX = (windowSize.x / 2.0f) - (aspectSize.x / 2.0f);
        float viewportY = (windowSize.y / 2.0f) - (aspectSize.y / 2.0f);

        return new ImVec2(viewportX + ImGui.getCursorPosX(),
                viewportY + ImGui.getCursorPosY());
    }
}

