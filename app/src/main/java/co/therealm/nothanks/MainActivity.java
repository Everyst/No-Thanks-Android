package co.therealm.nothanks;

import co.therealm.framework.Screen;
import co.therealm.framework.implementation.AndroidGame;


public class MainActivity extends AndroidGame {

    @Override
    public Screen getInitScreen() {
        //test
        return new LoadingScreen(this);
    }

}
