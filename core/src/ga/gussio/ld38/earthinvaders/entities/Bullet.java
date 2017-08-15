package ga.gussio.ld38.earthinvaders.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ga.gussio.ld38.earthinvaders.screen.GameScreen;

public class Bullet extends Entity {
    protected double velX, velY;
    protected int speed = 8;
    protected int lifetime = 10000;

    public Bullet(float x, float y, double angle) {
        super(x, y);
/*		velX = (int) (speed*Math.cos(angle+Math.toRadians(90)));
//		velY = (int) (speed*Math.sin(angle+Math.toRadians(90)));*/
//		double angle = Math.Atan2(mouseX - originX, mouseY - originY);
        velY = (speed) * Math.cos(angle);
        velX = (speed) * Math.sin(angle);

        double ticksLeft = (GameScreen.earth.getRadius()+25)/speed; //radius of earth + player size
        while(ticksLeft > 0){
            tick();
            ticksLeft--;
        }
    }

    @Override
    public void renderSB(SpriteBatch sb) {

    }

    @Override
    public void renderSR(ShapeRenderer sr){
        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.RED);
        sr.rect(x, y, 5, 5);
    }

    @Override
    public void tick() {
        x += velX;
        y += velY;
        checkCollision();
        lifetime--;
        if(lifetime < 0){
            GameScreen.entities.remove(this);
        }
    }

    @Override
    public void dispose() {

    }

    public void checkCollision(){
        for(Entity e : GameScreen.entities) {
            if (e instanceof Meteorite) {
                Meteorite m = (Meteorite) e;
                double dx = Math.abs(x - m.collision.getXCenter());
                double dy = Math.abs(y - m.collision.getYCenter());
                double radius = m.collision.getRadius();
                if (Math.pow(dx, 2) + Math.pow(dy, 2) <= Math.pow(radius, 2) || dx + dy <= radius) {
                    m.damage();
                    GameScreen.addScore(5);
                    GameScreen.entities.remove(this);
                }
            }
        }
    }
}
