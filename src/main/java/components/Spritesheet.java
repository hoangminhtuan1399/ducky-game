package components;

import org.joml.Vector2f;
import renderer.Texture;

import java.util.ArrayList;
import java.util.List;

// Lớp Spritesheet được sử dụng để quản lý một tập hợp các Sprite trên một texture.
public class Spritesheet {
    private Texture texture;
    private List<Sprite> sprites;

    // Constructor của Spritesheet, tạo danh sách các Sprite từ texture với kích thước và số lượng xác định.
    public Spritesheet(Texture texture, int spriteWidth, int spriteHeight, int numSprites, int spacing) {
        this.sprites = new ArrayList<>();

        this.texture = texture;
        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight;

        // Vòng lặp để tạo các Sprite từ texture.
        for (int i=0; i < numSprites; i++) {
            float topY = (currentY + spriteHeight) / (float)texture.getHeight();
            float rightX = (currentX + spriteWidth) / (float)texture.getWidth();
            float leftX = currentX / (float)texture.getWidth();
            float bottomY = currentY / (float)texture.getHeight();

            Vector2f[] texCoords = {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };
            Sprite sprite = new Sprite();
            sprite.setTexture(this.texture);
            sprite.setTexCoords(texCoords);
            sprite.setWidth(spriteWidth);
            sprite.setHeight(spriteHeight);
            this.sprites.add(sprite);

            currentX += spriteWidth + spacing;
            if (currentX >= texture.getWidth()) {
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }
    }

    // Phương thức để lấy một Sprite từ danh sách dựa trên chỉ số.
    public Sprite getSprite(int index) {
        return this.sprites.get(index);
    }

    // Phương thức trả về số lượng Sprite trong danh sách.
    public int size() {
        return sprites.size();
    }
}
