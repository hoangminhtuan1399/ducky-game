package components;

import org.joml.Vector2f;
import renderer.Texture;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Spritesheet {
    private Texture texture;
    private List<Sprite> sprites;

    public Spritesheet(Texture texture, int spriteWith, int spriteHeight, int numSprites, int spacing){
        this.sprites = new ArrayList<>();

        this.texture = texture;

        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight;
        for (int i=0 ; i< numSprites ; i++){
            float topY = (currentY + spriteHeight)/ (float)texture.getHeight();
            float rightX = (currentX + spriteWith)/ (float)texture.getWidth();
            float leftX= currentX/ (float)texture.getWidth();
            float bottomY= currentY/ (float)texture.getHeight();

            Vector2f[] texCoords ={
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)

            };
            Sprite sprite = new Sprite(this.texture, texCoords);
            this.sprites.add(sprite);

            currentX += spriteWith  + spacing;
            if (currentX >= texture.getWidth()){
                currentX = 0;
                currentY -=spriteHeight + spacing;
            }
        }

    }

    public  Sprite getSprice(int index) {
        return  this.sprites.get(index);
    }
}
