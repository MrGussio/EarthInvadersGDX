package ga.gussio.ld38.earthinvaders.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ga.gussio.ld38.earthinvaders.Game;

public class SplashScreen extends Screen {

    private Sprite logo;
    private long initiateTime;
    private GameScreen newGameScreen;

    public SplashScreen() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Game.WIDTH, Game.HEIGHT, camera);
        viewport.apply();
        camera.position.set(Game.WIDTH / 2, Game.HEIGHT / 2, 0);
        camera.update();
        logo = new Sprite(new Texture(Gdx.files.internal("gussio.png")));
        initiateTime = System.currentTimeMillis();
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        sb.setProjectionMatrix(camera.combined);
        sr.setAutoShapeType(true);
        sr.setProjectionMatrix(camera.combined);

        //BACKGROUND
        sr.begin();
        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.WHITE);
        sr.rect(0, 0, Game.WIDTH, Game.HEIGHT);
        sr.end();

        //LOGO
        sb.begin();
        sb.draw(logo, Game.WIDTH / 2 - logo.getWidth() * 20 / 2, Game.HEIGHT / 2 - logo.getHeight() * 20 / 2, logo.getWidth() * 20, logo.getHeight() * 20);
        sb.end();
    }

    @Override
    public void tick() {
        if (initiateTime + 3000 <= System.currentTimeMillis()) {
            Game.setCurrentScreen(new MenuScreen());
        }
    }

    @Override
    public void dispose() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void touchDown(int screenX, int screenY, int pointer, int button) {

    }

    @Override
    public void touchUp(int screenX, int screenY, int pointer, int button) {

    }

    @Override
    public void touchDragged(int screenX, int screenY, int pointer) {

    }
}
