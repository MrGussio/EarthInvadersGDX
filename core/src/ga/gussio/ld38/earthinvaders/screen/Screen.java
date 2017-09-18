package ga.gussio.ld38.earthinvaders.screen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;

import ga.gussio.ld38.earthinvaders.InputListener;

public abstract class Screen implements InputListener {

    public OrthographicCamera camera;
    public Viewport viewport;

    public abstract void render(SpriteBatch batch, ShapeRenderer sr);

    public abstract void tick();

    public abstract void dispose();

    public abstract void pause();

    public abstract void resume();


}