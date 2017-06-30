package ga.gussio.ld38.earthinvaders.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Random;

import javax.swing.text.html.Option;

import ga.gussio.ld38.earthinvaders.Game;
import ga.gussio.ld38.earthinvaders.InputListener;
import ga.gussio.ld38.earthinvaders.buttons.Button;
import ga.gussio.ld38.earthinvaders.buttons.CenteredButton;
import ga.gussio.ld38.earthinvaders.entities.particles.Particle;

public class MenuScreen extends Screen implements InputListener {

    private Particle[] background;
    private Sprite logo, musicMuted, musicUnmuted;
    private Button play, music;
    private BitmapFont betaText;
    public MenuScreen(){
        camera = new OrthographicCamera();
        viewport = new FitViewport(Game.WIDTH, Game.HEIGHT, camera);
        viewport.apply();
        camera.position.set(Game.WIDTH/2, Game.HEIGHT/2, 0);
        camera.update();
        betaText = new BitmapFont(Gdx.files.internal("score.fnt"), Gdx.files.internal("score.png"), false);
        betaText.getData().setScale(0.35f);
        logo = new Sprite(new Texture("logo.png"));
        Random r = new Random();
        background = new Particle[r.nextInt(55-45)+45];
        for(int i = 0; i < background.length; i++){
            int size = r.nextInt(4)+1;
            int x = r.nextInt(Game.WIDTH);
            int y = r.nextInt(Game.HEIGHT);
            background[i] = new Particle(x, y, 0, 0, -1, new Color(207/255f, 187/255f, 20/255f, 1f), size);
        }
        musicMuted = new Sprite(new Texture(Gdx.files.internal("buttons/music_muted.png")));
        musicUnmuted = new Sprite(new Texture(Gdx.files.internal("buttons/music_unmuted.png")));
        play = new CenteredButton(500, "buttons/play.png");
        music = new Button(Game.WIDTH-130, 15, Game.musicMuted() ? musicMuted : musicUnmuted);
        music.setScale(4f);
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        sb.setProjectionMatrix(camera.combined);
        sr.setAutoShapeType(true);
        sr.setProjectionMatrix(camera.combined);
        //SHAPERENDERER
        sr.begin();
        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.setColor(new Color(23/255f, 23/255f, 23/255f, 1f));
        sr.rect(0, 0, Game.WIDTH, Game.HEIGHT);
        for(int i = 0; i < background.length; i++){
            background[i].renderSR(sr);
        }
        play.renderSR(sr);
        music.renderSR(sr);
        sr.end();

        //SPRITEBATCH
        sb.begin();
        sb.draw(logo, Game.WIDTH/2-(logo.getTexture().getWidth()*10)/2, Game.HEIGHT-50-logo.getHeight()*10, logo.getWidth()*10, logo.getHeight()*10);
        play.renderSB(sb);
        music.renderSB(sb);
        betaText.draw(sb, "Beta Release - Copyright 2017 Gussio. All rights reserved. Visit https://gussio.nl/ for more info.", 10, 25);
        sb.end();
    }

    @Override
    public void tick() {
        if(play.clicked){
            GameScreen gs = new GameScreen();
            Game.setCurrentScreen(gs);
            play.clicked = false;
        }
        if(music.released){
            music.setSprite(Game.musicMuted() ? musicUnmuted : musicMuted);
            music.setScale(4f);
            Game.setMuted(!Game.musicMuted());
            Game.save();
            music.released = false;
        }
        play.tick();
        music.tick();
    }

    @Override
    public void dispose() {
        play.dispose();
        logo.getTexture().dispose();
    }

    @Override
    public void touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 coords = camera.unproject(new Vector3(screenX, screenY, 0));
        play.click(new Vector2(coords.x, coords.y));
        music.click(new Vector2(coords.x, coords.y));
    }

    @Override
    public void touchUp(int screenX, int screenY, int pointer, int button) {
        Vector3 coords = camera.unproject(new Vector3(screenX, screenY, 0));
        play.release(new Vector2(coords.x, coords.y));
        music.release(new Vector2(coords.x, coords.y));
    }

    @Override
    public void touchDragged(int screenX, int screenY, int pointer) {
        Vector3 coords = camera.unproject(new Vector3(screenX, screenY, 0));
        play.drag(new Vector2(coords.x, coords.y));
        music.drag(new Vector2(coords.x, coords.y));
    }
}
