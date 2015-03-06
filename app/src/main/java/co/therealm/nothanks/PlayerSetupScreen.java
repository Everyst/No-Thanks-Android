package co.therealm.nothanks;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.therealm.framework.Game;
import co.therealm.framework.Graphics;
import co.therealm.framework.Input;
import co.therealm.framework.Screen;
import co.therealm.nothanks.players.GeoffFAI;
import co.therealm.nothanks.players.HumanPlayer;
import co.therealm.nothanks.players.NetValueLessThanXAI;
import co.therealm.nothanks.players.NeverTakeAI;
import co.therealm.nothanks.players.Player;

/**
 * Created by GeoffF on 6/03/2015.
 */
public class PlayerSetupScreen extends Screen {

    private static final int[] BUTTON_SAVE = new int[]{820, 600, 300, 100};

    Paint paint;

    public PlayerSetupScreen(Game game) {
        super(game);

        paint = new Paint();
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
    }


    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_UP) {

                if (ScreenHelper.inBounds(event, BUTTON_SAVE[0], BUTTON_SAVE[1], BUTTON_SAVE[2], BUTTON_SAVE[3])) {
                    // Save
                    save();
                }

            }
        }
    }


    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();

        //g.drawImage(Assets.menu, 0, 0);
        g.drawARGB(155, 0, 0, 0);

        g.drawString("Set up the players for the game", 640, 100, paint);


        g.drawRect(BUTTON_SAVE[0], BUTTON_SAVE[1], BUTTON_SAVE[2], BUTTON_SAVE[3], Color.DKGRAY);
        g.drawString("Save", 960, 650, paint);
    }



    private static List<Player> setUpPlayers(int numberOfHumanPlayers){
        List<Player> players = new ArrayList<Player>();

        //players.add(new HumanPlayer("Player1"));
        //players.add(new HumanPlayer("Player2"));
        //players.add(new HumanPlayer("Player3"));
        //players.add(new HumanPlayer("Player4"));


        //players.add(new RandomAI("Player1"));
        //players.add(new RandomAI("Player2"));
        //players.add(new RandomAI("Player3"));
        //players.add(new RandomAI("Player4"));


        if (numberOfHumanPlayers == 1) {
            players.add(new HumanPlayer());

            players.add(new GeoffFAI());
            players.add(new NetValueLessThanXAI(11));
            players.add(new NeverTakeAI());
        } else if (numberOfHumanPlayers == 2) {
            players.add(new HumanPlayer("Player 1"));
            players.add(new HumanPlayer("Player 2"));

            players.add(new GeoffFAI());
        }




        return players;
    }


    private void save(){
        game.setScreen(new MainMenuScreen(game));
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
        // Go back to the menu screen
        save();
    }

}
