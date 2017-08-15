package ga.gussio.ld38.earthinvaders.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Entity {

    protected float x, y;

    public Entity(float x, float y){
        this.x = x;
        this.y = y;
    }

    public abstract void renderSB(SpriteBatch sr);
    public abstract void renderSR(ShapeRenderer sr);
    public abstract void tick();

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
