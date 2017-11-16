package ga.gussio.ld38.earthinvaders.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import ga.gussio.ld38.earthinvaders.Game;
import ga.gussio.ld38.earthinvaders.screen.GameScreen;

public class ShopWindow {

    private ShopEntry[] shopEntries;
    private int shopWidth = Game.WIDTH / 5;
    private boolean opened = false;
    private BitmapFont priceFont;

    private int entryHeight = 0;

    public ShopWindow() {
        priceFont = new BitmapFont(Gdx.files.internal("score.fnt"), Gdx.files.internal("score.png"), false);
        shopEntries = new ShopEntry[1];
        shopEntries[0] = new ShopEntry("Sentry", 100, new Sprite(new Texture(Gdx.files.internal("sentry.png"))), priceFont);
    }

    public void renderSB(SpriteBatch sb) {
        if (opened) {
            for (int i = 0; i < shopEntries.length; i++) {
                shopEntries[i].buyButton.setLocation(Game.WIDTH - shopWidth + 120, Game.HEIGHT - 120 - i * entryHeight);
                shopEntries[i].renderSB(sb, Game.WIDTH - shopWidth + 15, Game.HEIGHT - 120 - i * entryHeight, 100, 100);
            }
        }
    }

    public void renderSR(ShapeRenderer sr) {
        if (opened) {
            sr.setColor(Color.GRAY);
            sr.rect(Game.WIDTH - shopWidth, 0, shopWidth, Game.HEIGHT);
        }
    }

    public void tick(){
        if(opened){
            for(int i = 0; i < shopEntries.length; i++){
                if(shopEntries[i].buyButton.clicked){
                    shopEntries[i].buyButton.clicked = false;
                    if(GameScreen.getMoney() >= shopEntries[i].getCost()){
                        GameScreen.addMoney(-shopEntries[i].getCost());
                        if(shopEntries[i].getTitle().equalsIgnoreCase("Sentry")){
                            GameScreen.spawnNewSentry();
                        }
                    }
                }
            }
        }
    }

    public void toggle() {
        if (opened)
            opened = false;
        else
            opened = true;
    }

    public void click(Vector2 p){
        for(int i = 0; i < shopEntries.length; i++){
            shopEntries[i].buyButton.click(p);
        }
    }

    public void release(Vector2 p){
        for(int i = 0; i < shopEntries.length; i++){
            shopEntries[i].buyButton.release(p);
        }
    }

    public boolean isOpened() {
        return opened;
    }

    public int getShopWidth(){
        return shopWidth;
    }

}
