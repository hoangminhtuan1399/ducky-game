package components;

import com.google.gson.*;
import components.Component;

import java.lang.reflect.Type;

// Lớp ComponentDeserializer là một đối tượng Gson để chuyển đổi giữa JSON và đối tượng Component.
public class ComponentDeserializer implements JsonSerializer<Component>, JsonDeserializer<Component> {

    // Phương thức deserialize chuyển đổi JSON thành đối tượng Component.
    @Override
    public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");

        try {
            // Sử dụng reflection để tạo ra đối tượng Component từ loại và thuộc tính đã xác định.
            return context.deserialize(element, Class.forName(type));
        } catch (ClassNotFoundException e) {
            // Nếu không tìm thấy loại Component, bắt ngoại lệ và thông báo lỗi.
            throw new JsonParseException("Unknown element type " + type, e);
        }
    }

    // Phương thức serialize chuyển đổi đối tượng Component thành JSON.
    @Override
    public JsonElement serialize(Component src, Type type, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        // Lưu tên đầy đủ của lớp Component để có thể khôi phục sau này.
        result.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
        // Chuyển đổi thuộc tính của Component thành JSON.
        result.add("properties", context.serialize(src, src.getClass()));
        return result;
    }
}
