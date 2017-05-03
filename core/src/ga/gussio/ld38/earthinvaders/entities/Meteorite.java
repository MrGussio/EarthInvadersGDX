package ga.gussio.ld38.earthinvaders.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

import ga.gussio.ld38.earthinvaders.Game;
import ga.gussio.ld38.earthinvaders.entities.particles.Explosion;
import ga.gussio.ld38.earthinvaders.math.Circle;
import ga.gussio.ld38.earthinvaders.screen.GameScreen;

public class Meteorite extends Entity {

    private int radius = 50;
    private int speed = 1;
    private float velX, velY;
    private float health;
    private float rotationSpeed = 1;
    private float rotation = 0;
    public Circle collision;
    private Sprite[] img;

    public Meteorite(Sprite[] textures) {
        super(0, 0);
        Random r = new Random();
        int direction = r.nextInt(360+1);
        health = r.nextInt(2)+3;

        float tempVelY = (float) (10*Math.cos(Math.toRadians(direction)));
        float tempVelX = (float) (10*Math.sin(Math.toRadians(direction)));

        if(r.nextInt(2) == 0)
            tempVelX*=-1;
        if(r.nextInt(2) == 0)
            tempVelY*=-1;
        Rectangle collision = new Rectangle((int) GameScreen.earth.getXCenter(),
                (int)GameScreen.earth.getYCenter(), radius*2, radius*2);
        Rectangle screen = new Rectangle(0, 0, Game.WIDTH, Game.HEIGHT);
        while(screen.contains(collision)){
            collision.setPosition(collision.x+tempVelX, collision.y+tempVelY);
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
        this.img = textures;
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        sb.begin();
        sb.draw(this.img[(int) (4-health)], collision.getX(), collision.getY(),
                collision.getRadius(), collision.getRadius(), collision.getRadius()*2,
                collision.getRadius()*2, 1.0f, 1.0f, rotation);
        sb.end();
    }

    @Override
    public void tick() {
        x += velX;
        y += velY;
        collision.setX(x);
        collision.setY(y);
        if(collision.hasCollision(GameScreen.earth)){
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
        GameScreen.entities.add(new Explosion((int) collision.getXCenter(),
                (int) collision.getYCenter()));
        GameScreen.entities.remove(this);
    }
}
