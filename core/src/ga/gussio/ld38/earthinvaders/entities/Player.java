package ga.gussio.ld38.earthinvaders.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Random;

import ga.gussio.ld38.earthinvaders.Game;
import ga.gussio.ld38.earthinvaders.screen.GameScreen;

public class Player extends Entity {

    public int size = 30;
    private float radius = GameScreen.earth.getRadius()+size/2-3;
    private float angle = 0;

    private int maxShootInterval = 20;
    private int shootInterval = maxShootInterval;

    public Player() {
        super(0, 0);
        this.x = (float) (GameScreen.earth.getX() + radius*Math.sin(angle));
        this.y = (float) (GameScreen.earth.getY() + radius*Math.cos(angle));
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        sr.setProjectionMatrix(sb.getProjectionMatrix());
        sr.begin();
        sr.setColor(Color.RED);
        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.rect(x-size/2, y-size/2, size/2, size/2, size, size, 1.0f, 1.0f, (float) -Math.toDegrees((double) angle));
        sr.end();
    }

    @Override
    public void tick() {
        angle += Math.toRadians(1);
        x = (int)(GameScreen.earth.getX() + radius*Math.sin(angle));
        y = (int)(GameScreen.earth.getY() + radius*Math.cos(angle));
        if(Gdx.input.justTouched())
            shoot();

        if(shootInterval != maxShootInterval){
            shootInterval--;
            if(shootInterval <= 0){
                shootInterval = maxShootInterval;
            }
        }
    }

    public void shoot(){
        if(shootInterval == maxShootInterval){
            GameScreen.entities.add(new Bullet(Game.WIDTH/2, Game.HEIGHT/2, angle));
            shootInterval--;
        }
    }
}
