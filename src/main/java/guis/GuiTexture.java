package guis;

import org.lwjgl.util.vector.Vector2f;

public class GuiTexture {

    private int Texture;
    private Vector2f position;
    private Vector2f scale;

    public GuiTexture(int texture, Vector2f position, Vector2f scale) {
        Texture = texture;
        this.position = position;
        this.scale = scale;
    }

    public int getTexture() {
        return Texture;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getScale() {
        return scale;
    }

}
