package ga.gussio.ld38.earthinvaders.shop;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class ShopEntry {

    private String title;
    private int cost;
    private Sprite texture;

    public ShopEntry(String title, int cost, Sprite texture){
        this.title = title;
        this.cost = cost;
        this.texture = texture;
    }

}
