package co.therealm.nothanks;

import co.therealm.framework.Game;
import co.therealm.framework.Graphics;
import co.therealm.framework.Graphics.ImageFormat;
import co.therealm.framework.Screen;

/**
 * Created by GeoffF on 3/03/2015.
 */
public class LoadingScreen extends Screen {

    public LoadingScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();

        //Assets.menu = g.newImage("haru.png", ImageFormat.RGB565);

        game.setScreen(new MainMenuScreen(game));
    }


    @Override
    public void paint(float deltaTime) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public void backButton() {
    }

}
