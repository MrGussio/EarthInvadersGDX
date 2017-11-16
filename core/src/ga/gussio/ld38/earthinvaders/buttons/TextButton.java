package ga.gussio.ld38.earthinvaders.buttons;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextButton extends Button {

    private BitmapFont font;
    private String text;
    private int xOffset = 0, yOffset = 0;

    public TextButton(int x, int y, int width, int height, String filepath, BitmapFont font, String text) {
        super(x, y, width, height, filepath);
        this.font = font;
        this.text = text;
    }

    @Override
    public void renderSB(SpriteBatch sb) {
        super.renderSB(sb);
        font.draw(sb, text, x+xOffset, y+yOffset);
        collision.set(x, y, width, height);
    }

    public void setOffset(int x, int y){
        xOffset = x;
        yOffset = y;
    }

    public int getXOffset(){
        return xOffset;
    }

    public int getYOffset(){
        return yOffset;
    }
}
