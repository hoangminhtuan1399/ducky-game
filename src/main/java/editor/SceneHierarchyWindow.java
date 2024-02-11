package editor;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import jade.GameObject;
import jade.Window;

import java.util.List;

public class SceneHierarchyWindow {
    private static String payLoadDragDropType = "SceneHierarchy";

    public void imgui() {
        ImGui.begin("Scene Hierarchy");

        List<GameObject> gameObjects = Window.getScene().getGameObjects();
        int index = 0;
        for (GameObject obj : gameObjects) {
            if (!obj.doSerialization()) {
                continue;
            }

            boolean treeNodeOpen = doTreeNode(obj, index);

            if (treeNodeOpen) {
                ImGui.treePop();
            }

            index++;
        }

        ImGui.end();
    }

    private boolean doTreeNode(GameObject obj, int index) {
        ImGui.pushID(index);
        boolean treeNodeOpen = ImGui.treeNodeEx(
                obj.name,
                ImGuiTreeNodeFlags.DefaultOpen |
                        ImGuiTreeNodeFlags.FramePadding |
                        ImGuiTreeNodeFlags.OpenOnArrow |
                        ImGuiTreeNodeFlags.SpanAvailWidth,
                obj.name
        );

        ImGui.popID();

        /** Drag and drop (dnd) đối tượng */
        if (ImGui.beginDragDropSource()) {
            /** Gán đối tượng được dnd vào payload */
            ImGui.setDragDropPayloadObject(payLoadDragDropType, obj);

            /** Hiển thị tên đối tượng được dnd */
            ImGui.text(obj.name);

            ImGui.endDragDropSource();
        }

        /** Dnd mục tiêu */
        if (ImGui.beginDragDropTarget()) {

            /** Kiểm tra xem mục tiêu được dnd có cùng payload không */
            Object payloadObj = ImGui.acceptDragDropPayloadObject(payLoadDragDropType);
            if (payloadObj != null) {

                /** Kiểm tra xem mục tiêu được dnd có phải GameObject không */
                if (payloadObj.getClass().isAssignableFrom(GameObject.class)) {
                    GameObject playerGameObj = (GameObject) payloadObj;
                    System.out.println("Payload accepted " + playerGameObj.name);
                }
            }
            ImGui.endDragDropTarget();
        }

        return treeNodeOpen;
    }
}
