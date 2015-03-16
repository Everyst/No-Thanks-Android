package co.therealm.nothanks;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.List;

import co.therealm.framework.Game;
import co.therealm.framework.Graphics;
import co.therealm.framework.Input.TouchEvent;
import co.therealm.framework.Screen;

/**
 * Created by Geoffrey on 3/03/2015.
 */
public class MainMenuScreen extends Screen{

    private static final int[] BUTTON_PLAY = new int[]{820, 600, 300, 100};
    private static final int[] BUTTON_PLAYER_SETUP = new int[]{180, 600, 300, 100};


    Paint paint;

    public MainMenuScreen(Game game) {
        super(game);

        paint = new Paint();
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
    }


    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {

                if (ScreenHelper.inBounds(event, BUTTON_PLAY[0], BUTTON_PLAY[1], BUTTON_PLAY[2], BUTTON_PLAY[3])) {
                    // Play
                    game.setScreen(new GameScreen(game));
                } else if (ScreenHelper.inBounds(event, BUTTON_PLAYER_SETUP[0], BUTTON_PLAYER_SETUP[1], BUTTON_PLAYER_SETUP[2], BUTTON_PLAYER_SETUP[3])) {
                    // Player setup
                    game.setScreen(new PlayerSetupScreen(game));
                }

            }
        }
    }


    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();

        //g.drawImage(Assets.menu, 0, 0);
        g.drawARGB(155, 0, 0, 0);

        g.drawString("No Thanks!", 640, 300, paint);

        g.drawRect(BUTTON_PLAYER_SETUP[0], BUTTON_PLAYER_SETUP[1], BUTTON_PLAYER_SETUP[2], BUTTON_PLAYER_SETUP[3], Color.DKGRAY);
        g.drawString("Setup players", 320, 650, paint);

        g.drawRect(BUTTON_PLAY[0], BUTTON_PLAY[1], BUTTON_PLAY[2], BUTTON_PLAY[3], Color.DKGRAY);
        g.drawString("Play", 960, 650, paint);
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
        // Exit the game
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
