package ga.gussio.ld38.earthinvaders.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.concurrent.CopyOnWriteArrayList;

import ga.gussio.ld38.earthinvaders.Game;
import ga.gussio.ld38.earthinvaders.buttons.Button;
import ga.gussio.ld38.earthinvaders.entities.Entity;
import ga.gussio.ld38.earthinvaders.entities.Meteorite;
import ga.gussio.ld38.earthinvaders.entities.Player;
import ga.gussio.ld38.earthinvaders.math.Circle;

public class GameScreen extends Screen{

    public static Circle earth;
    public Sprite earthTexture;
    public static CopyOnWriteArrayList<Entity> entities = new CopyOnWriteArrayList<Entity>();

    public static int health = 100;
    private static int dmgAnimation = 0;

    private Sprite[] meteoriteSprites;

    private Button leftButton, rightButton;

    public GameScreen() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Game.WIDTH, Game.HEIGHT, camera);
        viewport.apply();
        camera.position.set(Game.WIDTH/2, Game.HEIGHT/2, 0);
        camera.update();
        earthTexture = new Sprite(new Texture(Gdx.files.internal("world.png")));
        earth = new Circle((float) (Game.WIDTH/2-Game.HEIGHT*0.2), (float) (Game.HEIGHT/2-Game.HEIGHT*0.2), (float) (Game.HEIGHT*0.2));
        entities.add(new Player());
        Texture full = new Texture(Gdx.files.internal("meteorite.png"));
        meteoriteSprites = new Sprite[4];
        for(int i = 0; i < meteoriteSprites.length; i++){
            TextureRegion region = new TextureRegion(full, i*20, 0, 20, 20);
            meteoriteSprites[i] = new Sprite(region);
        }
        entities.add(new Meteorite(meteoriteSprites));
        leftButton = new Button(10, 60, 180, "control_button.png");
        rightButton = new Button(150, 60, "control_button.png");
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        sb.setProjectionMatrix(camera.combined);
        sr.setAutoShapeType(true);
        sr.setProjectionMatrix(camera.combined);
        //actual rendering
        sr.begin();
        sr.set(ShapeRenderer.ShapeType.Filled);
        //rendering background
        sr.setColor(new Color(23/255f, 23/255f, 23/255f, 1f));
        sr.rect(0, 0, Game.WIDTH, Game.HEIGHT);
        sr.end();

        //rendering earth
        sb.begin();
        sb.draw(earthTexture, earth.getX(), earth.getY(), earth.getRadius()*2, earth.getRadius()*2);
        sb.end();
        for(Entity e : entities){
            e.render(sb, sr);
        }
        leftButton.render(sb, sr);
        rightButton.render(sb, sr);
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
        for(int i = 0; i < meteoriteSprites.length; i++){
            meteoriteSprites[i].getTexture().dispose();
        }
    }

    public static void damageEarth(int hits){
        dmgAnimation+=hits;
    }
}
