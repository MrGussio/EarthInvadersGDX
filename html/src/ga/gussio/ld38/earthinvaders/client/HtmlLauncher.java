package ga.gussio.ld38.earthinvaders.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import ga.gussio.ld38.earthinvaders.Advertisements;
import ga.gussio.ld38.earthinvaders.Game;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new Game(new Advertisements() {
                        @Override
                        public void showAds() {

                        }
                });
        }
}