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
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import ga.gussio.ld38.earthinvaders.Game;
import ga.gussio.ld38.earthinvaders.InputListener;
import ga.gussio.ld38.earthinvaders.buttons.Button;
import ga.gussio.ld38.earthinvaders.entities.Entity;
import ga.gussio.ld38.earthinvaders.entities.Meteorite;
import ga.gussio.ld38.earthinvaders.entities.Player;
import ga.gussio.ld38.earthinvaders.entities.particles.Particle;
import ga.gussio.ld38.earthinvaders.math.Circle;

public class GameScreen extends Screen implements InputListener {

    public static Circle earth;
    public Sprite earthTexture;
    public static CopyOnWriteArrayList<Entity> entities = new CopyOnWriteArrayList<Entity>();
    private HashMap<Integer, Integer> pointers = new HashMap<Integer, Integer>();

    public static int health = 100;
    private static int dmgAnimation = 0;

    private Sprite[] meteoriteSprites, warningSprites;
    private Particle[] background;

    private Button leftButton, rightButton;
    private Player player;

    private long spawnTimer;
    private long startTime;
    private int spawnFactor = 6000;

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
        Texture full2 = new Texture(Gdx.files.internal("warning.png"));
        warningSprites = new Sprite[2];
        warningSprites[0] = new Sprite(new TextureRegion(full2, 0, 0, 9, 9));
        warningSprites[1] = new Sprite(new TextureRegion(full2, 9, 0, 9, 9));

        leftButton = new Button(10, 60, 180, "control_button.png");
        rightButton = new Button(150, 60, "control_button.png");

        //generating randomized background
        Random r = new Random();
        background = new Particle[r.nextInt(55-45)+45];
        for(int i = 0; i < background.length; i++){
            int size = r.nextInt(4)+1;
            int x = r.nextInt(Game.WIDTH);
            int y = r.nextInt(Game.HEIGHT);
            background[i] = new Particle(x, y, 0, 0, -1, new Color(207/255f, 187/255f, 20/255f, 1f), size);
        }
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        sb.setProjectionMatrix(camera.combined);
        sr.setAutoShapeType(true);
        sr.setProjectionMatrix(camera.combined);
        //SHAPERENDERER RENDERING
        sr.begin();
        sr.set(ShapeRenderer.ShapeType.Filled);
        //rendering background
        sr.setColor(new Color(23/255f, 23/255f, 23/255f, 1f));
        sr.rect(0, 0, Game.WIDTH, Game.HEIGHT);

        //rendering background particles
        for(int i = 0; i < background.length; i++){
            background[i].renderSR(sr);
        }
        for(Entity e : entities){
            e.renderSR(sr);
        }
        leftButton.renderSR(sr);
        rightButton.renderSR(sr);
        sr.end();

        //SPRITEBATCH RENDERING
        sb.begin();
        sb.draw(earthTexture, earth.getX(), earth.getY(), earth.getRadius()*2, earth.getRadius()*2);
        for(Entity e : entities){
            e.renderSB(sb);
        }
        leftButton.renderSB(sb);
        rightButton.renderSB(sb);
        sb.end();

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

        if(System.currentTimeMillis() > spawnTimer){
            entities.add(new Meteorite(meteoriteSprites, warningSprites));
            long dtime = System.currentTimeMillis()-startTime;
            if(dtime > 10000){
                startTime = System.currentTimeMillis();
            }
            spawnTimer = System.currentTimeMillis()+spawnFactor;
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
        int currentValue = pointers.get(pointer);
        pointers.remove(pointer);
        if(left) {
            pointers.put(pointer, 1);
            leftButton.clicked = true;
            rightButton.clicked = false;
        }else if(right){
            pointers.put(pointer, 2);
            leftButton.clicked = false;
            rightButton.clicked = true;
        }else{
            pointers.put(pointer, 0);
            if(currentValue == 1)
                leftButton.clicked = false;
            else if(currentValue == 2)
                rightButton.clicked = false;
        }
    }
}
