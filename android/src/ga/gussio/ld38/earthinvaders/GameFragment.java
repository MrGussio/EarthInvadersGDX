package ga.gussio.ld38.earthinvaders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

public class GameFragment extends AndroidFragmentApplication{

    private Advertisements ads;

    public GameFragment(){

    }

    public GameFragment(Advertisements ads){
        this.ads = ads;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return initializeForView(new Game(ads));
    }
}
