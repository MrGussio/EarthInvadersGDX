package ga.gussio.ld38.earthinvaders.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.awt.Rectangle;
import java.util.Random;

import ga.gussio.ld38.earthinvaders.Game;
import ga.gussio.ld38.earthinvaders.entities.particles.Explosion;
import ga.gussio.ld38.earthinvaders.math.Circle;
import ga.gussio.ld38.earthinvaders.screen.GameScreen;

public class Meteorite extends Entity {

    private int radius = 50;
    private int speed = 3;
    private double velX, velY;
    private double health;
    private double rotationSpeed = 1;
    private double rotation = 0;
    private Circle collision;

    public Meteorite() {
        super(0, 0);
        Random r = new Random();
        int direction = r.nextInt(360+1);
        health = r.nextInt(2)+3;

        double tempVelY = 10*Math.cos(Math.toRadians(direction));
        double tempVelX = 10*Math.sin(Math.toRadians(direction));

        if(r.nextInt(2) == 0)
            tempVelX*=-1;
        if(r.nextInt(2) == 0)
            tempVelY*=-1;
        Rectangle collision = new Rectangle((int) GameScreen.earth.getXCenter(), (int)GameScreen.earth.getYCenter(), radius*2, radius*2);
        Rectangle screen = new Rectangle(0, 0, Game.WIDTH, Game.HEIGHT);
        while(collision.intersects(screen)){
            collision.translate((int) tempVelX, (int) tempVelY);
        }
        x = (float) collision.getX();
        y = (float) collision.getY();
        float xSpeed = (Game.WIDTH/2-x) / 1.0f;
        float ySpeed = (Game.HEIGHT/2-y) / 1.0f;
        float factor = (float) (speed / Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed));
        xSpeed *= factor;
        ySpeed *= factor;

        velX = xSpeed;
        velY = ySpeed;
        this.collision = new Circle(x-radius, y-radius, radius);
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        sr.begin();
        sr.setColor(Color.YELLOW);
//        sr.rotate(Math.toRadians(rotation), collision.getXCenter(), collision.getYCenter());
        sr.circle(collision.getXCenter(), collision.getYCenter(), collision.getRadius());
//        sr.rotate(-Math.toRadians(rotation), collision.getXCenter(), collision.getYCenter());
        sr.end();
    }

    @Override
    public void tick() {
        x += velX;
        y += velY;
        collision.setX(x);
        collision.setY(y);
        if(collision.hasCollision(GameScreen.earth)){
            System.out.println("test");
            GameScreen.damageEarth((int)health*3);
//            Game.playSound("METEOR1.wav", false, -20);
            destroy();
        }
        rotation += rotationSpeed;
    }

    public void damage(){
        health--;
        if(health <= 0){
            destroy();
        }
    }

    public void destroy(){
        GameScreen.entities.add(new Explosion((int) collision.getXCenter(), (int) collision.getYCenter()));
        GameScreen.entities.remove(this);
    }
}
