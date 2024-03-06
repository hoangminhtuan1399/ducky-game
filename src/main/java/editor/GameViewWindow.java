package editor;

import components.CoinCounter;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import jade.MouseListener;
import jade.Window;
import observers.EventSystem;
import observers.events.Event;
import observers.events.EventType;
import org.joml.Vector2f;
import scenes.GameMenuSceneInitializer;
import scenes.LevelSceneInitializer;

public class GameViewWindow {
    private enum state {
        GameEditor,
        MenuEditor,
        Play
    }
    private float leftX, rightX, topY, bottomY;
    private String currentState = "GameStop";
    private boolean isEditingLevel = false;
    private boolean isEditingMenu = true;
    private boolean isPlaying = false;
    private void updateState() {
        isEditingLevel = currentState.equalsIgnoreCase(state.GameEditor.name());
        isEditingMenu = currentState.equalsIgnoreCase(state.MenuEditor.name());
        isPlaying = currentState.equalsIgnoreCase(state.Play.name());
    }

    private boolean windowIsHovered;

    public void imgui() {
        ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse
                | ImGuiWindowFlags.MenuBar);

        ImGui.beginMenuBar();
        if (ImGui.menuItem("Edit menu", "", isEditingMenu, !isEditingMenu)) {
            currentState = "MenuEditor";
            updateState();
            EventSystem.notify(null, new Event(EventType.GameMenuEnd));
        }
        if (ImGui.menuItem("Edit level", "", isEditingLevel, !isEditingLevel)) {
            currentState = "GameEditor";
            updateState();
            EventSystem.notify(null, new Event(EventType.GameEngineStopPlay));
        }
        if (ImGui.menuItem("Play", "", isPlaying, !isPlaying)) {
            currentState = "Play";
            updateState();
            EventSystem.notify(null, new Event(EventType.GameMenuStart));
            // Thiết lập coinCount về 0
            CoinCounter.getInstance().setCoinCount(0);
        }
        ImGui.endMenuBar();


        ImGui.setCursorPos(ImGui.getCursorPosX(), ImGui.getCursorPosY());
        ImVec2 windowSize = getLargestSizeForViewport();
        ImVec2 windowPos = getCenteredPositionForViewport(windowSize);
        ImGui.setCursorPos(windowPos.x, windowPos.y);

        // Lấy tọa độ góc trên bên trái của cửa sổ
        ImVec2 topLeft = new ImVec2();
        ImGui.getCursorScreenPos(topLeft);
        topLeft.x -= ImGui.getScrollX();
        topLeft.y -= ImGui.getScrollY();
        leftX = topLeft.x;
        bottomY = topLeft.y;
        rightX = topLeft.x + windowSize.x;
        topY = topLeft.y + windowSize.y;

        // Hiển thị hình ảnh từ framebuffer lên cửa sổ
        int textureId = Window.getFramebuffer().getTextureId();
        ImGui.image(textureId, windowSize.x, windowSize.y, 0, 1, 1, 0);
        windowIsHovered = ImGui.isItemHovered();

        MouseListener.setGameViewportPos(new Vector2f(topLeft.x, topLeft.y));
        MouseListener.setGameViewportSize(new Vector2f(windowSize.x, windowSize.y));

        if (Window.getScene().getSceneInitializer() instanceof GameMenuSceneInitializer) {

            ImGui.setNextWindowPos(topLeft.x + (windowSize.x / 2) - 50, topLeft.y + (windowSize.y / 2) + 20, ImGuiCond.Always);
            ImGui.begin("Play Game", ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize
                    | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoBackground);
            ImGui.text("Press Enter");
            ImGui.end();
        }

        if (Window.getScene().getSceneInitializer() instanceof LevelSceneInitializer) {
            // Lấy số coin từ CoinCounter và vẽ lên góc của cửa sổ viewport
            ImGui.setNextWindowPos(topLeft.x + 10, topLeft.y + 10, ImGuiCond.Always);
            ImGui.begin("Coin Counter", ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize
                    | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoBackground);
            ImGui.textColored(ImGui.getColorU32(0, 0, 0, 1), "Point: " + CoinCounter.getInstance().getCoinCount() * 100);
            ImGui.end();
        }

        ImGui.end();
    }

    public boolean getWantCaptureMouse() {
        return windowIsHovered;
    }

    private ImVec2 getLargestSizeForViewport() {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / Window.getTargetAspectRatio();
        if (aspectHeight > windowSize.y) {
            // We must switch to pillarbox mode
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * Window.getTargetAspectRatio();
        }

        return new ImVec2(aspectWidth, aspectHeight);
    }

    private ImVec2 getCenteredPositionForViewport(ImVec2 aspectSize) {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);

        float viewportX = (windowSize.x / 2.0f) - (aspectSize.x / 2.0f);
        float viewportY = (windowSize.y / 2.0f) - (aspectSize.y / 2.0f);

        return new ImVec2(viewportX + ImGui.getCursorPosX(), viewportY + ImGui.getCursorPosY());
    }
}