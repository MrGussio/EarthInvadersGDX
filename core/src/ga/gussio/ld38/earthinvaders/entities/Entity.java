package ga.gussio.ld38.earthinvaders.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class Entity {

    protected float x, y;

    public Entity(float x, float y){
        this.x = x;
        this.y = y;
    }

    public abstract void render(SpriteBatch sb, ShapeRenderer sr);
    public abstract void tick();
}
