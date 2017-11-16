package ga.gussio.ld38.earthinvaders.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import ga.gussio.ld38.earthinvaders.Game;
import ga.gussio.ld38.earthinvaders.screen.GameScreen;


public class Sentry extends Entity {

    private Sprite img;
    private int size = 25;
    private float angle = (float) Math.toRadians(90);
    private float viewAngle = 60;
    private float radius = GameScreen.earth.getRadius() + size / 2 - 3;
    private Meteorite target = null;
    private Polygon view; //viewing angle of the sentry
    private int fireRate = 2000; //fire rate in milliseconds
    private long lastFire = 0; //timestamp of last fire in milliseconds

    public Sentry(float angle) {
        super(0, 0);
        this.x = (float) (GameScreen.earth.getXCenter() + radius * Math.sin(angle));
        this.y = (float) (GameScreen.earth.getYCenter() + radius * Math.cos(angle));
        img = new Sprite(new Texture(Gdx.files.internal("sentry.png")));
        int minDistance = 1080;
        float opposite = (float) Math.tan(viewAngle) * minDistance;
        view = new Polygon(new float[]{x, y, x - opposite, y + minDistance, x + opposite, y + minDistance});
        this.angle = angle;
    }

    @Override
    public void renderSB(SpriteBatch sb) {
        sb.draw(img, x - size / 2, y - size / 2, size / 2, size / 2, size, size, 1.0f, 1.0f, (float) -Math.toDegrees((double) angle));
    }

    @Override
    public void renderSR(ShapeRenderer sr) {
        sr.setColor(Color.GRAY);
//        sr.polygon(view.getTransformedVertices());
    }

    @Override
    public void tick() {
        x = (int) (GameScreen.earth.getXCenter() + radius * Math.sin(angle));
        y = (int) (GameScreen.earth.getYCenter() + radius * Math.cos(angle));
        if (target != null) {
            if (target.getHealth() <= 0 || !view.contains(new Vector2(target.getX(), target.getY()))) { // check if target is still in range, if not so remove target.
                target = null;
            } else if (lastFire + fireRate < System.currentTimeMillis() & !target.isStillInWarningTime()) {
                lastFire = System.currentTimeMillis();
                GameScreen.entities.add(new ChaseBullet(Game.WIDTH / 2, Game.HEIGHT / 2, angle, target));
            }
        } else {
            selectNewTarget();
        }
//        angle+= Math.toRadians(1.5);
    }

    @Override
    public void dispose() {
        img.getTexture().dispose();

    }

    public void selectNewTarget() {
        view.setOrigin(x, y);
        view.setRotation((float) -Math.toDegrees(angle));
        for (Entity e : GameScreen.entities) {
            if (e instanceof Meteorite) {
                Meteorite m = (Meteorite) e;
                if (view.contains(new Vector2(m.getX(), m.getY()))) {//inside viewing angle
                    target = m;
                    return;
                }
            }
            target = null;
        }
    }
}
