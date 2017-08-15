package ga.gussio.ld38.earthinvaders.entities.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import ga.gussio.ld38.earthinvaders.entities.Entity;

public class Explosion extends Entity {

    public CopyOnWriteArrayList<Particle> particles = new CopyOnWriteArrayList<Particle>();

    public Explosion(float x, float y) {
        super(x, y);
        Random r = new Random();
        int amount = r.nextInt(60-10+1)+10;
        for(int i = 0; i < amount; i++){
            int dir = r.nextInt(360);
            int speed = r.nextInt(2)+1;
            int duration = r.nextInt(1500-800+1)+800;
            float colorVariant = r.nextInt(255)/255f;
            int size = r.nextInt(5)+1;
            Color color = new Color(colorVariant, colorVariant, colorVariant, 1f);
            particles.add(new Particle(x, y, dir, speed, duration, color, size));
        }
    }

    @Override
    public void renderSB(SpriteBatch sb) {
        for(Particle p : particles){
            p.renderSB(sb);
        }
    }

    public void renderSR(ShapeRenderer sr){
        for(Particle p : particles){
            p.renderSR(sr);
        }
    }

    @Override
    public void tick() {
        for(Particle p : particles){
            p.tick();
            if(p.hasExpired())
                particles.remove(p);
        }
    }

    @Override
    public void dispose() {
        particles.clear();
    }
}
