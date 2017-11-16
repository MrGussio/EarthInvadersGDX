package ga.gussio.ld38.earthinvaders.shop;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ga.gussio.ld38.earthinvaders.buttons.TextButton;

public class ShopEntry {

    private String title;
    private int cost;
    private Sprite texture;
    public TextButton buyButton;
    private BitmapFont font;

    public ShopEntry(String title, int cost, Sprite texture, BitmapFont font) {
        this.title = title;
        this.cost = cost;
        this.texture = texture;
        this.font = font;
        buyButton = new TextButton(0, 0, 235, 90, "buttons/price.png", font, String.valueOf(cost));
    }

    public void renderSB(SpriteBatch sb, int x, int y, int width, int height){
        sb.draw(texture, x, y, width, height);
        buyButton.renderSB(sb);
        buyButton.setOffset(20, 80);
    }

    public String getTitle() {
        return title;
    }

    public int getCost() {
        return cost;
    }

    public Sprite getTexture() {
        return texture;
    }
}
