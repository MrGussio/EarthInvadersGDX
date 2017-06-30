package ga.gussio.ld38.earthinvaders;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

import ga.gussio.ld38.earthinvaders.screen.GameScreen;
import ga.gussio.ld38.earthinvaders.screen.MenuScreen;
import ga.gussio.ld38.earthinvaders.screen.Screen;
//test
public class Game extends ApplicationAdapter implements InputProcessor {

	public static int WIDTH = 1920, HEIGHT = 1080;
	public static float aspectRatio;

	SpriteBatch batch;
	ShapeRenderer sr;

	private static ArrayList<InputListener> listeners = new ArrayList<InputListener>();

	private static Screen currentScreen;

	private static SaveFileDescriptor currentDescriptor;
	private static FileHandle saveFile;
	
	@Override
	public void create () {
		saveFile = Gdx.files.local("bin/saves.dat");
		if(saveFile.exists()){
			load();
		}else{
			currentDescriptor = newSaveFileDescriptor();
			save();
		}
		aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		setCurrentScreen(new MenuScreen());
		Gdx.graphics.requestRendering();
		Gdx.input.setInputProcessor(this);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); //fixes red screen bug on startup
        Gdx.input.setCatchBackKey(true);
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

	public static void save(){
		Json json = new Json();
		saveFile.writeString(Base64Coder.encodeString(json.prettyPrint(currentDescriptor)), false);
	}

	public static void load(){
		Json json = new Json();
		currentDescriptor = json.fromJson(SaveFileDescriptor.class, Base64Coder.decodeString(saveFile.readString()));
	}

	public static void checkHighscore(int score){
		if(score > currentDescriptor.highscore) {
			currentDescriptor.highscore = score;
			save();
		}
	}

	public static int getHighscore(){
		return currentDescriptor.highscore;
	}

	public SaveFileDescriptor newSaveFileDescriptor(){
		SaveFileDescriptor desc = new SaveFileDescriptor();
		desc.highscore = 0;
		desc.musicMuted = false;
		return desc;
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
		if(currentScreen != null)
			currentScreen.dispose();
		if(listeners.contains(currentScreen))
			listeners.remove(currentScreen);
		currentScreen = s;
		listeners.add(currentScreen);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public static void setMuted(boolean muted){
		currentDescriptor.musicMuted = muted;
	}

	public static boolean musicMuted(){
		return currentDescriptor.musicMuted;
	}
}
