package ga.gussio.ld38.earthinvaders.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ga.gussio.ld38.earthinvaders.screen.GameScreen;

public class ChaseBullet extends Bullet {

    private Meteorite target;
    private float dx, dy;

    public ChaseBullet(float x, float y, float angle, Meteorite target) {
        super(x, y, angle);
        this.target = target;
    }

    @Override
    public void renderSB(SpriteBatch sb) {

    }

    @Override
    public void renderSR(ShapeRenderer sr) {
        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.RED);
        sr.rect(x, y, 5, 5);
    }

    @Override
    public void tick() {
        calculateVelocity();
        x += velX;
        y += velY;
        checkCollision();
        if(target != null && target.getHealth() <= 0)
            GameScreen.entities.remove(this);
    }

    private void calculateVelocity(){
        if(target != null) {
            dx = target.collision.getXCenter() - x;
            dy = target.collision.getYCenter()   - y;
            double distance = Math.sqrt((dx * dx) + (dy * dy));
            double speed = 2 / distance;
            velX = speed * dx;
            velY = speed * dy;
        }
    }
}
