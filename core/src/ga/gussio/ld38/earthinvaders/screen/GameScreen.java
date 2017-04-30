package ga.gussio.ld38.earthinvaders.screen;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.concurrent.CopyOnWriteArrayList;

import ga.gussio.ld38.earthinvaders.Game;
import ga.gussio.ld38.earthinvaders.entities.Entity;
import ga.gussio.ld38.earthinvaders.entities.Meteorite;
import ga.gussio.ld38.earthinvaders.entities.Player;
import ga.gussio.ld38.earthinvaders.math.Circle;

public class GameScreen extends Screen{

    public static Circle earth;

    public static CopyOnWriteArrayList<Entity> entities = new CopyOnWriteArrayList<Entity>();

    public static int health = 100;
    private static int dmgAnimation = 0;

    public GameScreen() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Game.WIDTH, Game.HEIGHT, camera);
        viewport.apply();
        camera.position.set(Game.WIDTH/2, Game.HEIGHT/2, 0);
        camera.update();
        earth = new Circle((float) (Game.WIDTH/2-Game.HEIGHT*0.2), (float) (Game.HEIGHT/2-Game.HEIGHT*0.2), (float) (Game.HEIGHT*0.2)); //shaperenderer werkt hier anders, rendert vanuit het midden, dus radius niet van x/y afhalen.
        entities.add(new Player());
        entities.add(new Meteorite());
    }

    @Override
    public void render(SpriteBatch g, ShapeRenderer sr) {
        g.setProjectionMatrix(camera.combined);
        sr.setAutoShapeType(true);
        sr.setProjectionMatrix(camera.combined);
        sr.begin();
        sr.setProjectionMatrix(camera.combined);
        sr.set(ShapeRenderer.ShapeType.Filled);
        //actual rendering
        sr.setColor(new Color(23/255f, 23/255f, 23/255f, 1f));
        sr.rect(0, 0, Game.WIDTH, Game.HEIGHT);

        sr.setColor(Color.WHITE);
        sr.circle(earth.getXCenter(), earth.getYCenter(), earth.getRadius());

        sr.end();
        for(Entity e : entities){
            e.render(g, sr);
        }
    }

    @Override
    public void tick() {
        for(Entity e : entities){
            e.tick();
        }
        if(dmgAnimation > 0){
            health--;
            dmgAnimation--;
        }
    }

    @Override
    public void dispose() {

    }

    public static void damageEarth(int hits){
        dmgAnimation+=hits;
    }
}
