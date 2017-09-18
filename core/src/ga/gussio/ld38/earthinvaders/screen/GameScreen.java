package ga.gussio.ld38.earthinvaders.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
import ga.gussio.ld38.earthinvaders.entities.Sentry;
import ga.gussio.ld38.earthinvaders.entities.particles.Particle;
import ga.gussio.ld38.earthinvaders.math.Circle;
import ga.gussio.ld38.earthinvaders.shop.ShopWindow;

public class GameScreen extends Screen implements InputListener {

    public static Circle earth;
    public Sprite earthTexture;
    public static CopyOnWriteArrayList<Entity> entities = new CopyOnWriteArrayList<Entity>();
    private HashMap<Integer, Integer> pointers = new HashMap<Integer, Integer>();

    public static float maxHealth = 100;
    public static float health = maxHealth;
    private static int dmgAnimation = 0;

    private Sprite[] meteoriteSprites, warningSprites;
    private Sprite musicMuted, musicUnmuted;
    private BitmapFont scoreFont;
    private Particle[] background;

    private Button leftButton, rightButton, shopButton, exit, pauseExit, retry, resume, music;
    private Player player;

    private long spawnTimer;
    private long startTime;
    private int spawnFactor = 6000;

    private static int score = 0;
    private int scoreTimer = 0;
    private boolean appliedScore = false;

    private Music soundtrack;
    private Sound meteorDestroySound;

    private boolean paused = false;
    private boolean showedAds = false;

    private boolean shopButtonUsed = false;
    private ShopWindow shopWindow;

    public GameScreen() {
        entities.clear();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Game.WIDTH, Game.HEIGHT, camera);
        viewport.apply();
        camera.position.set(Game.WIDTH / 2, Game.HEIGHT / 2, 0);
        camera.update();

        earthTexture = new Sprite(new Texture(Gdx.files.internal("world.png")));
        musicMuted = new Sprite(new Texture(Gdx.files.internal("buttons/music_muted.png")));
        musicUnmuted = new Sprite(new Texture(Gdx.files.internal("buttons/music_unmuted.png")));
        earth = new Circle((float) (Game.WIDTH / 2 - Game.HEIGHT * 0.2), (float) (Game.HEIGHT / 2 - Game.HEIGHT * 0.2), (float) (Game.HEIGHT * 0.2));
        this.player = new Player();
        entities.add(player);
        entities.add(new Sentry());
        Texture full = new Texture(Gdx.files.internal("meteorite.png"));
        meteoriteSprites = new Sprite[4];
        for (int i = 0; i < meteoriteSprites.length; i++) {
            TextureRegion region = new TextureRegion(full, i * 20, 0, 20, 20);
            meteoriteSprites[i] = new Sprite(region);
        }
        Texture full2 = new Texture(Gdx.files.internal("warning.png"));
        warningSprites = new Sprite[2];
        warningSprites[0] = new Sprite(new TextureRegion(full2, 0, 0, 9, 9));
        warningSprites[1] = new Sprite(new TextureRegion(full2, 9, 0, 9, 9));

        scoreFont = new BitmapFont(Gdx.files.internal("score.fnt"), Gdx.files.internal("score.png"), false);
        scoreFont.getData().setScale(0.8f);

        leftButton = new Button(10, 10, 180, "buttons/control_button.png");
        rightButton = new Button(210, 10, "buttons/control_button.png");
        shopButton = new Button(1500, 10, "buttons/shop_button.png");

        leftButton.setScale(1.5f);
        rightButton.setScale(1.5f);
        shopButton.setScale(1.5f);

        exit = new Button(1230, 450, "buttons/exit.png");
        pauseExit = new Button(1230, 450, "buttons/exit.png");
        retry = new Button(500, 450, "buttons/retry.png");
        resume = new Button(500, 450, "buttons/resume.png");
        music = new Button(Game.WIDTH - 130, 15, Game.musicMuted() ? musicMuted : musicUnmuted);
        music.setScale(4f);

        soundtrack = Gdx.audio.newMusic(Gdx.files.internal("sound/soundtrack.wav"));
        meteorDestroySound = Gdx.audio.newSound(Gdx.files.internal("sound/explosion.wav"));
        soundtrack.setLooping(true);
        if (!Game.musicMuted())
            soundtrack.play();

        shopWindow = new ShopWindow();
        //generating randomized background
        Random r = new Random();
        background = new Particle[r.nextInt(55 - 45) + 45];
        for (int i = 0; i < background.length; i++) {
            int size = r.nextInt(4) + 1;
            int x = r.nextInt(Game.WIDTH);
            int y = r.nextInt(Game.HEIGHT);
            background[i] = new Particle(x, y, 0, 0, -1, new Color(207 / 255f, 187 / 255f, 20 / 255f, 1f), size);
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
        sr.setColor(new Color(23 / 255f, 23 / 255f, 23 / 255f, 1f));
        sr.rect(0, 0, Game.WIDTH, Game.HEIGHT);

        //rendering background particles
        for (int i = 0; i < background.length; i++) {
            background[i].renderSR(sr);
        }
        for (Entity e : entities) {
            e.renderSR(sr);
        }
        leftButton.renderSR(sr);
        rightButton.renderSR(sr);
        //HUD
        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.GRAY);
        sr.rect(10 - 3, Game.HEIGHT - 10 - 50 - 3, 300 + 6, 50 + 6);
        sr.setColor(Color.RED);
        sr.rect(10, Game.HEIGHT - 10 - 50, 300, 50);
        sr.setColor(Color.GREEN);
        sr.rect(10, Game.HEIGHT - 10 - 50, 300 * (float) (health / maxHealth), 50);

        shopWindow.renderSR(sr);

        sr.end();

        //SPRITEBATCH RENDERING
        sb.begin();
        sb.draw(earthTexture, earth.getX(), earth.getY(), earth.getRadius() * 2, earth.getRadius() * 2);
        for (Entity e : entities) {
            e.renderSB(sb);
        }

        leftButton.renderSB(sb);
        rightButton.renderSB(sb);
        shopButton.renderSB(sb);

        scoreFont.draw(sb, "Score: " + score, 360, Game.HEIGHT - 10);
        shopWindow.renderSB(sb);
        sb.end();

        if (health <= 0) {
            GlyphLayout gameOverLayout = new GlyphLayout(scoreFont, "Game Over! Score: " + score);
            float gameOverX = Game.WIDTH / 4 + (Game.WIDTH / 2 - gameOverLayout.width) / 2;

            GlyphLayout highscoreLayout = new GlyphLayout(scoreFont, "Highscore: " + Game.getHighscore());
            float highscoreX = Game.WIDTH / 4 + (Game.WIDTH / 2 - highscoreLayout.width) / 2;

            sr.begin();
            sr.set(ShapeRenderer.ShapeType.Filled);
            sr.setColor(Color.GRAY);
            sr.rect(Game.WIDTH / 4, Game.HEIGHT * 4 / 10, Game.WIDTH / 2, Game.HEIGHT * 4 / 10);
            sr.end();

            sb.begin();
            retry.renderSB(sb);
            exit.renderSB(sb);
            scoreFont.draw(sb, "Game Over! Score: " + score, gameOverX, Game.HEIGHT * 7 / 10);
            scoreFont.draw(sb, "Highscore: " + Game.getHighscore(), highscoreX, Game.HEIGHT * 7 / 10 - 60);
            sb.end();
        }

        if (paused) {
            sr.begin();
            sr.set(ShapeRenderer.ShapeType.Filled);
            sr.setColor(Color.GRAY);
            sr.rect(Game.WIDTH / 4, Game.HEIGHT * 4 / 10, Game.WIDTH / 2, Game.HEIGHT * 4 / 10);
            sr.end();

            GlyphLayout textLayout = new GlyphLayout(scoreFont, "Game paused.");
            float textX = Game.WIDTH / 4 + (Game.WIDTH / 2 - textLayout.width) / 2;
            sb.begin();
            scoreFont.draw(sb, "Game paused.", textX, Game.HEIGHT * 7 / 10);
            resume.renderSB(sb);
            pauseExit.renderSB(sb);
            music.renderSB(sb);
            sb.end();
        }
    }

    @Override
    public void tick() {
        if (!paused) {
            if (health > 0) {
                if (leftButton.clicked)
                    player.setDirection(-1);
                else if (rightButton.clicked)
                    player.setDirection(1);
                else
                    player.setDirection(0);
                for (Entity e : entities) {
                    e.tick();
                }
                if (dmgAnimation > 0) {
                    health--;
                    dmgAnimation--;
                }

                if (shopButton.clicked & !shopButtonUsed) {
                    shopWindow.toggle();
                    shopButtonUsed = true;
                }

                if (shopButton.released) {
                    shopButtonUsed = false;
                }

                if (System.currentTimeMillis() > spawnTimer) {
                    entities.add(new Meteorite(meteoriteSprites, warningSprites, meteorDestroySound));
                    long dtime = System.currentTimeMillis() - startTime;
                    if (dtime > 10000) {
                        startTime = System.currentTimeMillis();
                    }
                    spawnTimer = System.currentTimeMillis() + spawnFactor;
                }

                scoreTimer++;
                if (scoreTimer > 60) {
                    score++;
                    scoreTimer = 0;
                }
            } else {
                if (!appliedScore) {
                    Game.checkHighscore(score);
                    appliedScore = true;
                }
                retry.tick();
                exit.tick();

                if (retry.clicked) {
                    retry.clicked = false;
                    Game.setCurrentScreen(new GameScreen());
                }

                if (exit.clicked) {
                    exit.clicked = false;
                    Game.setCurrentScreen(new MenuScreen());
                }

                if (!showedAds) {
                    showedAds = true;
                    Game.ads.showAds();
                }
            }
        } else {
            if (resume.clicked) {
                resume.clicked = false;
                paused = false;
                if (!Game.musicMuted())
                    soundtrack.play();
            }
            if (pauseExit.clicked) {
                pauseExit.clicked = false;
                Game.setCurrentScreen(new MenuScreen());
            }
            if (music.released) {
                music.setSprite(Game.musicMuted() ? musicUnmuted : musicMuted);
                music.setScale(4f);
                Game.setMuted(!Game.musicMuted());
                Game.save();
                music.released = false;
            }
        }

        if (Gdx.input.isKeyJustPressed(Keys.BACK)) {
            if (paused) {
                Game.setCurrentScreen(new MenuScreen());
                paused = false;
            } else {
                paused = true;
                soundtrack.pause();
            }
        }
    }

    @Override
    public void dispose() {
        for (int i = 0; i < meteoriteSprites.length; i++) {
            meteoriteSprites[i].getTexture().dispose();
        }

        for (Entity e : entities) {
            e.dispose();
        }

        health = maxHealth;
        score = 0;
        dmgAnimation = 0;
        soundtrack.stop();
        soundtrack.dispose();
        meteorDestroySound.dispose();
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {

    }

    public static void damageEarth(int hits) {
        dmgAnimation += hits;
    }

    public static void addScore(int score) {
        GameScreen.score += score;
    }

    @Override
    public void touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 coords = camera.unproject(new Vector3(screenX, screenY, 0));
        boolean left = leftButton.click(new Vector2(coords.x, coords.y));
        boolean right = rightButton.click(new Vector2(coords.x, coords.y));
        if (!left & !right & !paused) {//didnt hit a button and game not paused
            player.shoot();
        }
        System.out.println(pointer);
        if (left)
            pointers.put(pointer, 1);
        else if (right)
            pointers.put(pointer, 2);
        else
            pointers.put(pointer, 0);
        shopButton.click(new Vector2(coords.x, coords.y));
        if (health <= 0) {
            retry.click(new Vector2(coords.x, coords.y));
            exit.click(new Vector2(coords.x, coords.y));
        }
        if (paused) {
            pauseExit.click(new Vector2(coords.x, coords.y));
            resume.click(new Vector2(coords.x, coords.y));
            music.click(new Vector2(coords.x, coords.y));
        }
    }

    @Override
    public void touchUp(int screenX, int screenY, int pointer, int button) {
        Vector3 coords = camera.unproject(new Vector3(screenX, screenY, 0));
        leftButton.release(new Vector2(coords.x, coords.y));
        rightButton.release(new Vector2(coords.x, coords.y));
        if (pointers.containsKey(pointer)) {
            switch (pointers.get(pointer)) {
                case 1:
                    leftButton.clicked = false;
                    break;
                case 2:
                    rightButton.clicked = false;
                    break;
            }
            pointers.remove(pointer);
        }
        shopButton.release(new Vector2(coords.x, coords.y));
        if (health <= 0) {
            retry.release(new Vector2(coords.x, coords.y));
            exit.release(new Vector2(coords.x, coords.y));
        }
        if (paused) {
            pauseExit.release(new Vector2(coords.x, coords.y));
            resume.release(new Vector2(coords.x, coords.y));
            music.release(new Vector2(coords.x, coords.y));
        }
    }

    @Override
    public void touchDragged(int screenX, int screenY, int pointer) {
        Vector3 coords = camera.unproject(new Vector3(screenX, screenY, 0));
        boolean left = leftButton.drag(new Vector2(coords.x, coords.y));
        boolean right = rightButton.drag(new Vector2(coords.x, coords.y));
        if (pointers.containsKey(pointer)) {
            int currentValue = pointers.get(pointer);
            pointers.remove(pointer);
            if (left) {
                pointers.put(pointer, 1);
                leftButton.clicked = true;
                rightButton.clicked = false;
            } else if (right) {
                pointers.put(pointer, 2);
                leftButton.clicked = false;
                rightButton.clicked = true;
            } else {
                pointers.put(pointer, 0);
                if (currentValue == 1)
                    leftButton.clicked = false;
                else if (currentValue == 2)
                    rightButton.clicked = false;
            }
        }
        shopButton.drag(new Vector2(coords.x, coords.y));
        if (health <= 0) {
            retry.drag(new Vector2(coords.x, coords.y));
            exit.drag(new Vector2(coords.x, coords.y));
        }
        if (paused) {
            pauseExit.drag(new Vector2(coords.x, coords.y));
            resume.drag(new Vector2(coords.x, coords.y));
            music.drag(new Vector2(coords.x, coords.y));
        }
    }
}
