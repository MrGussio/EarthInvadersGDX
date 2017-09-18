package ga.gussio.ld38.earthinvaders;

import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.awt.event.KeyEvent;

public class InputHandler extends InputListener {

    public boolean left, right, shoot;

    protected int leftKey, rightKey, shootKey;

    public InputHandler(int left, int right, int shoot) {
        leftKey = left;
        rightKey = right;
        shootKey = shoot;
    }

    public void keyDown(KeyEvent e) {
        if (e.getKeyCode() == leftKey) {
            left = true;
        }
        if (e.getKeyCode() == rightKey) {
            right = true;
        }
        if (e.getKeyCode() == shootKey) {
            shoot = true;
        }
    }

    public void keyUp(KeyEvent e) {
        if (e.getKeyCode() == leftKey) {
            left = false;
        }
        if (e.getKeyCode() == rightKey) {
            right = false;
        }
        if (e.getKeyCode() == shootKey) {
            shoot = false;
        }
    }

    public void keyTyped(KeyEvent e) {

    }

}