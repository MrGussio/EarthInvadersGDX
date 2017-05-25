package ga.gussio.ld38.earthinvaders.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import ga.gussio.ld38.earthinvaders.Game;
import ga.gussio.ld38.earthinvaders.InputListener;
import ga.gussio.ld38.earthinvaders.buttons.Button;
import ga.gussio.ld38.earthinvaders.entities.Entity;
import ga.gussio.ld38.earthinvaders.entities.Meteorite;
import ga.gussio.ld38.earthinvaders.entities.Player;
import ga.gussio.ld38.earthinvaders.math.Circle;

public class GameScreen extends Screen implements InputListener {

    public static Circle earth;
    public Sprite earthTexture;
    public static CopyOnWriteArrayList<Entity> entities = new CopyOnWriteArrayList<Entity>();
    private HashMap<Integer, Integer> pointers = new HashMap<Integer, Integer>();

    public static int health = 100;
    private static int dmgAnimation = 0;

    private Sprite[] meteoriteSprites;

    private Button leftButton, rightButton;
    private Player player;

    public GameScreen() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Game.WIDTH, Game.HEIGHT, camera);
        viewport.apply();
        camera.position.set(Game.WIDTH/2, Game.HEIGHT/2, 0);
        camera.update();
        earthTexture = new Sprite(new Texture(Gdx.files.internal("world.png")));
        earth = new Circle((float) (Game.WIDTH/2-Game.HEIGHT*0.2), (float) (Game.HEIGHT/2-Game.HEIGHT*0.2), (float) (Game.HEIGHT*0.2));
        this.player = new Player();
        entities.add(player);
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
        if(leftButton.clicked)
            player.setDirection(-1);
        else if(rightButton.clicked)
            player.setDirection(1);
        else
            player.setDirection(0);
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

    @Override
    public void touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 coords = camera.unproject(new Vector3(screenX, screenY, 0));
        boolean left = leftButton.click(new Vector2(coords.x, coords.y));
        boolean right = rightButton.click(new Vector2(coords.x, coords.y));
        if(!left &! right){//didnt hit a button
            player.shoot();
        }
        System.out.println(pointer);
        if(left)
            pointers.put(pointer, 1);
        else if(right)
            pointers.put(pointer, 2);
        else
            pointers.put(pointer, 0);
    }

    @Override
    public void touchUp(int screenX, int screenY, int pointer, int button) {
        Vector3 coords = camera.unproject(new Vector3(screenX, screenY, 0));
        leftButton.release(new Vector2(coords.x, coords.y));
        rightButton.release(new Vector2(coords.x, coords.y));
        if(pointers.containsKey(pointer)) {
            switch(pointers.get(pointer)){
                case 1:
                    leftButton.clicked = false;
                    break;
                case 2:
                    rightButton.clicked = false;
                    break;
            }
            pointers.remove(pointer);
        }
    }

    @Override
    public void touchDragged(int screenX, int screenY, int pointer) {
        Vector3 coords = camera.unproject(new Vector3(screenX, screenY, 0));
        boolean left = leftButton.drag(new Vector2(coords.x, coords.y));
        boolean right = rightButton.drag(new Vector2(coords.x, coords.y));
        pointers.remove(pointer);
        if(left) {
            pointers.put(pointer, 1);
        }else if(right){
            pointers.put(pointer, 2);
        }else{
            pointers.put(pointer, 0);
        }
    }
}
