package ga.gussio.ld38.earthinvaders.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Button {

    protected int x, y, width, height, rotation;
    protected float scale = 1.0f;
    protected String filepath;
    protected Sprite img;
    protected Rectangle collision;
    public boolean clicked;

    public Button(int x, int y, String filepath) {
        this.x = x;
        this.y = y;
        this.filepath = filepath;
        img = new Sprite(new Texture(Gdx.files.internal(filepath)));
        this.width = img.getTexture().getWidth();
        this.height = img.getTexture().getHeight();
        this.rotation = 0;
        collision = new Rectangle(x, y, width, height);
    }

    public Button(int x, int y, int width, int height, String filepath) {
        this(x, y, filepath);
        this.width = width;
        this.height = height;
    }

    public Button(int x, int y, int width, int height, int rotationInDegr, String filepath) {
        this(x, y, width, height, filepath);
        this.rotation = rotationInDegr;
    }

    public Button(int x, int y, int rotationInDegr, String filepath){
        this(x, y, filepath);
        this.rotation = rotationInDegr;
    }

    public void render(SpriteBatch sb, ShapeRenderer sr) {
        sb.begin();
        sb.draw(img, x, y, width/2, height/2, width, height, 1.0f, 1.0f, rotation);
        sb.end();
    }

    public void tick() {

    }

    public void dispose() {
        img.getTexture().dispose();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
        width = (int) (img.getWidth() * scale);
        height = (int) (img.getHeight() * scale);
        collision.set(x, y, width, height);
    }

    public int getRotation(){
        return rotation;
    }

    public void setRotation(int rotation){
        this.rotation = rotation;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getFilepath() {
        return filepath;
    }

    public void click(Vector2 p) {
        if (collision.contains(p)) {
            this.clicked = true;
        }

    }
}
