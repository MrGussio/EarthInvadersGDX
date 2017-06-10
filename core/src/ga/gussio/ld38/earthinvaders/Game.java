package ga.gussio.ld38.earthinvaders;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

import ga.gussio.ld38.earthinvaders.screen.GameScreen;
import ga.gussio.ld38.earthinvaders.screen.MenuScreen;
import ga.gussio.ld38.earthinvaders.screen.Screen;

public class Game extends ApplicationAdapter implements InputProcessor {

	public static int WIDTH = 1920, HEIGHT = 1080;
	public static float aspectRatio;

	SpriteBatch batch;
	ShapeRenderer sr;

	private static ArrayList<InputListener> listeners = new ArrayList<InputListener>();

	private static Screen currentScreen;
	
	@Override
	public void create () {
		aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		setCurrentScreen(new MenuScreen());
		Gdx.graphics.requestRendering();
		Gdx.input.setInputProcessor(this);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); //fixes red screen bug on startup
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		for(InputListener l : listeners){
			l.touchDown(screenX, screenY, pointer, button);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		for(InputListener l : listeners){
			l.touchUp(screenX, screenY, pointer, button);
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		for(InputListener l : listeners){
			l.touchDragged(screenX, screenY, pointer);
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	public static void setCurrentScreen(Screen s){
//		if(currentScreen != null)
////			currentScreen.dispose();
		if(listeners.contains(currentScreen))
			listeners.remove(currentScreen);
		currentScreen = s;
		listeners.add(currentScreen);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
}
