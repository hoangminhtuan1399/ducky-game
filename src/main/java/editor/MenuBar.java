package editor;

import imgui.ImGui;
import observers.EventSystem;
import observers.events.Event;
import observers.events.EventType;

// Lớp MenuBar chứa phương thức để vẽ thanh menu chính sử dụng ImGui.
public class MenuBar {

    // Phương thức để vẽ thanh menu chính.
    public void imgui() {
        ImGui.beginMainMenuBar();

        // Menu "File"
        if (ImGui.beginMenu("File")) {
            // MenuItem "Save" với phím tắt Ctrl+S
            if (ImGui.menuItem("Save", "Ctrl+S")) {
                EventSystem.notify(null, new Event(EventType.SaveLevel));
            }

            // MenuItem "Load" với phím tắt Ctrl+O
            if (ImGui.menuItem("Load", "Ctrl+O")) {
                EventSystem.notify(null, new Event(EventType.LoadLevel));
            }

            ImGui.endMenu(); // Kết thúc menu "File"
        }

        ImGui.endMainMenuBar(); // Kết thúc thanh menu chính
    }
}
