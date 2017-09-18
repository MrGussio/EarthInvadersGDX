package ga.gussio.ld38.earthinvaders.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Random;

import ga.gussio.ld38.earthinvaders.Game;
import ga.gussio.ld38.earthinvaders.screen.GameScreen;

public class Player extends Entity {

    public int size = 50;
    private float radius = GameScreen.earth.getRadius() + size / 2 - 3;
    private float angle = 0;
    private int dir = 0; //0 is not moving, -1 is counterclockwise, 1 is clockwise

    private int maxShootInterval = 20;
    private int shootInterval = maxShootInterval;

    private Sprite img;

    private Sound[] shoot;

    public Player() {
        super(0, 0);
        this.x = (float) (GameScreen.earth.getXCenter() + radius * Math.sin(angle));
        this.y = (float) (GameScreen.earth.getYCenter() + radius * Math.cos(angle));
        img = new Sprite(new Texture(Gdx.files.internal("player.png")));
        shoot = new Sound[4];
        for (int i = 1; i <= shoot.length; i++) {
            shoot[i - 1] = Gdx.audio.newSound(Gdx.files.internal("sound/shoot" + i + ".wav"));
        }
    }

    @Override
    public void renderSB(SpriteBatch sb) {
        sb.draw(img, x - size / 2, y - size / 2, size / 2, size / 2, size, size, 1.0f, 1.0f, (float) -Math.toDegrees((double) angle));
    }

    public void renderSR(ShapeRenderer sr) {
    }

    @Override
    public void tick() {
        angle += Math.toRadians(dir * 1.5);
        x = (int) (GameScreen.earth.getXCenter() + radius * Math.sin(angle));
        y = (int) (GameScreen.earth.getYCenter() + radius * Math.cos(angle));

        if (shootInterval != maxShootInterval) {
            shootInterval--;
            if (shootInterval <= 0) {
                shootInterval = maxShootInterval;
            }
        }
    }

    @Override
    public void dispose() {
        img.getTexture().dispose();
        for (int i = 0; i < shoot.length; i++) {
            shoot[i].dispose();
        }
    }

    public void shoot() {
        if (shootInterval == maxShootInterval) {
            GameScreen.entities.add(new Bullet(Game.WIDTH / 2, Game.HEIGHT / 2, angle));
            shootInterval--;
            Random r = new Random();
            if (!Game.musicMuted())
                shoot[r.nextInt(3) + 1].play(0.6f);
        }
    }

    public void setDirection(int dir) {
        if (dir >= -1 && dir <= 1)
            this.dir = dir;
        else
            Gdx.app.error("EarthInvaders", "Error: Invalid Direction [" + dir + "]");
    }

}
