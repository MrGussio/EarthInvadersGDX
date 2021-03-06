package ga.gussio.ld38.earthinvaders.entities.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ga.gussio.ld38.earthinvaders.entities.Entity;

public class Particle extends Entity {

    private long expireTime;
    private Color color;
    private int size;
    private int speed;
    private int dir;

    public Particle(float x, float y, int dir, int speed, long timeInMilliseconds, Color color, int size) {
        super(x, y);
        expireTime = System.currentTimeMillis() + timeInMilliseconds;
        if (timeInMilliseconds < 0)
            expireTime = -1;

        this.color = color;
        this.size = size;
        this.dir = dir;
        this.speed = speed;
    }

    @Override
    public void renderSB(SpriteBatch sb) {

    }

    @Override
    public void renderSR(ShapeRenderer sr) {
        sr.setColor(color);
        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.rect(x, y, size, size);
    }

    @Override
    public void tick() {
        x += speed * Math.cos(Math.toRadians(dir));
        y += speed * Math.sin(Math.toRadians(dir));
    }

    @Override
    public void dispose() {

    }

    public boolean hasExpired() {
        if (expireTime < 0)
            return false;

        return System.currentTimeMillis() > expireTime;
    }
}
