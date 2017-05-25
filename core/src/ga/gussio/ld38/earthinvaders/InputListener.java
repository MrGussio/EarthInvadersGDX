package ga.gussio.ld38.earthinvaders;

public interface InputListener {

    void touchDown(int screenX, int screenY, int pointer, int button);
    void touchUp(int screenX, int screenY, int pointer, int button);
    void touchDragged(int screenX, int screenY, int pointer);

}
