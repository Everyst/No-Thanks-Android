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
import co.therealm.nothanks.players.RandomAI;

/**
 * Created by GeoffF on 6/03/2015.
 */
public class PlayerSetupScreen extends Screen {

    private static final int[] BUTTON_SINGLE_PLAYER = new int[]{180, 400, 300, 100};
    private static final int[] BUTTON_TWO_PLAYER = new int[]{820, 400, 300, 100};

    private static final int[] BUTTON_SAVE = new int[]{820, 600, 300, 100};

    Paint paint;
    Paint paintLeft;
    Paint paintRight;

    List<Player> players = null;

    public PlayerSetupScreen(Game game) {
        super(game);

        paint = new Paint();
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);

        paintLeft = new Paint();
        paintLeft.setTextSize(20);
        paintLeft.setTextAlign(Paint.Align.LEFT);
        paintLeft.setAntiAlias(true);
        paintLeft.setColor(Color.WHITE);

        paintRight = new Paint();
        paintRight.setTextSize(20);
        paintRight.setTextAlign(Paint.Align.RIGHT);
        paintRight.setAntiAlias(true);
        paintRight.setColor(Color.WHITE);

        players = ((NoThanksGame)game).getPlayerList();
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
                } else if (ScreenHelper.inBounds(event, BUTTON_SINGLE_PLAYER[0], BUTTON_SINGLE_PLAYER[1], BUTTON_SINGLE_PLAYER[2], BUTTON_SINGLE_PLAYER[3])) {
                    // Single player
                    singlePlayer();
                } else if (ScreenHelper.inBounds(event, BUTTON_TWO_PLAYER[0], BUTTON_TWO_PLAYER[1], BUTTON_TWO_PLAYER[2], BUTTON_TWO_PLAYER[3])) {
                    // Two player
                    twoPlayer();
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

        for (int i = 0; i < players.size(); i++){
            if (i % 2 == 0) {
                g.drawString(players.get(i).getName(), 100, 150+i*20, paintLeft);
            } else {
                g.drawString(players.get(i).getName(), 1180, 150+(i-1)*20, paintRight);
            }
        }


        g.drawRect(BUTTON_SAVE[0], BUTTON_SAVE[1], BUTTON_SAVE[2], BUTTON_SAVE[3], Color.DKGRAY);
        g.drawString("Save", 960, 650, paint);

        g.drawRect(BUTTON_SINGLE_PLAYER[0], BUTTON_SINGLE_PLAYER[1], BUTTON_SINGLE_PLAYER[2], BUTTON_SINGLE_PLAYER[3], Color.DKGRAY);
        g.drawString("Single player", 320, 450, paint);

        g.drawRect(BUTTON_TWO_PLAYER[0], BUTTON_TWO_PLAYER[1], BUTTON_TWO_PLAYER[2], BUTTON_TWO_PLAYER[3], Color.DKGRAY);
        g.drawString("Two player", 960, 450, paint);
    }



   /* private static List<Player> setUpPlayers(){
        List<Player> players = new ArrayList<Player>();

        //players.add(new HumanPlayer("Player1"));
        //players.add(new HumanPlayer("Player2"));
        //players.add(new HumanPlayer("Player3"));
        //players.add(new HumanPlayer("Player4"));


        //players.add(new RandomAI("Player1"));
        //players.add(new RandomAI("Player2"));
        //players.add(new RandomAI("Player3"));
        //players.add(new RandomAI("Player4"));



        players.add(new HumanPlayer("Player 1"));
        players.add(new HumanPlayer("Player 2"));

        players.add(new GeoffFAI());

        return players;
    }*/

    private void singlePlayer(){
        players = new ArrayList<Player>();

        players.add(new HumanPlayer("Player 1"));
        players.add(new GeoffFAI());
        players.add(new NetValueLessThanXAI(11));
        players.add(new NeverTakeAI());
        players.add(new RandomAI());
    }

    private void twoPlayer(){
        players = new ArrayList<Player>();

        players.add(new HumanPlayer("Player 1"));
        players.add(new HumanPlayer("Player 2"));

        players.add(new GeoffFAI());
    }

    private void save(){
        ((NoThanksGame)game).setPlayerList(players);
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
        // Go back to the menu screen without saving

        // TODO Add confirmation screen
        game.setScreen(new MainMenuScreen(game));
    }

}
