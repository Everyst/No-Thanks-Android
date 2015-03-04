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
        Graphics g = game.getGraphics();
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();


        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                //START GAME
                game.setScreen(new GameScreen(game));
            }
        }
    }


    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();

        g.drawARGB(155, 0, 0, 0);

        g.drawString("Tap anywhere to play against 3 AI players", 640, 300, paint);

        //g.drawImage(Assets.menu, 0, 0);
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
