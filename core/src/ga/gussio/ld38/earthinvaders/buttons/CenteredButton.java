package ga.gussio.ld38.earthinvaders.buttons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ga.gussio.ld38.earthinvaders.Game;

public class CenteredButton extends Button {
    public CenteredButton(int y, String filepath) {
        super(0, 0, filepath);
        x = (int) (Game.WIDTH / 2 - (img.getWidth() * scale) / 2);
        this.y = y;
    }

    @Override
    public void renderSB(SpriteBatch sb) {
        sb.draw(img, x, y, (int) (img.getWidth() * scale), (int) (img.getHeight() * scale));
    }

    @Override
    public void setScale(float scale) {
        this.scale = scale;
        x = (int) (Game.WIDTH / 2 - (img.getWidth() * scale) / 2);
        width = (int) (img.getWidth() * scale);
        height = (int) (img.getHeight() * scale);
        collision.set(x, y, width, height);
    }

}
