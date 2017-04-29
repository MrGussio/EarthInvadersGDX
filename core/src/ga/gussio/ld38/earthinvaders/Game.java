package ga.gussio.ld38.earthinvaders;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ga.gussio.ld38.earthinvaders.screen.GameScreen;
import ga.gussio.ld38.earthinvaders.screen.Screen;

public class Game extends ApplicationAdapter {

	public static int WIDTH = 1920, HEIGHT = 1080;
	public static float aspectRatio;

	SpriteBatch batch;
	ShapeRenderer sr;

	private static Screen currentScreen;
	
	@Override
	public void create () {
		aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		currentScreen = new GameScreen();
		Gdx.graphics.requestRendering();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); //fixes red screen bug on startup
		if(currentScreen != null){
			currentScreen.tick();
			currentScreen.render(batch, sr);
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		currentScreen.dispose();
	}
}
