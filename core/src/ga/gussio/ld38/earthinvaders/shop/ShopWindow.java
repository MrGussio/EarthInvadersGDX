package ga.gussio.ld38.earthinvaders.shop;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

import ga.gussio.ld38.earthinvaders.Game;

public class ShopWindow {

    private ArrayList<ShopEntry> shopEntries;
    private int shopWidth = Game.WIDTH/8;
    private boolean opened = false;

    public ShopWindow() {
        shopEntries = new ArrayList<ShopEntry>();
    }

    public void renderSB(SpriteBatch sb){

    }

    public void renderSR(ShapeRenderer sr){
        if(opened){
            sr.setColor(Color.GRAY);
            sr.rect(Game.WIDTH - shopWidth, 0, shopWidth, Game.HEIGHT);
        }
    }

    public void toggle(){
        if(opened)
            opened = false;
        else
            opened = true;
    }

    public boolean isOpened(){
        return opened;
    }

}
