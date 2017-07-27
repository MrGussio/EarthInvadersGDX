package ga.gussio.ld38.earthinvaders.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ga.gussio.ld38.earthinvaders.Advertisements;
import ga.gussio.ld38.earthinvaders.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		config.resizable = false;
		new LwjglApplication(new Game(new Advertisements() {
			@Override
			public void showAds() {

			}
		}), config);
	}
}
