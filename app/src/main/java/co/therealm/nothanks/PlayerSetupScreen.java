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

    private static final int[] BUTTON_ADD_HUMAN = new int[]{180, 400, 300, 100};
    private static final int[] BUTTON_ADD_BOT = new int[]{500, 400, 300, 100};
    private static final int[] BUTTON_REMOVE_LAST_PLAYER = new int[]{820, 400, 300, 100};

    private static final int[] BUTTON_SAVE = new int[]{820, 600, 300, 100};
    private static final int[] BUTTON_CLEAR = new int[]{180, 600, 300, 100};


    Paint paint;
    Paint paintLeft;
    Paint paintRight;

    List<Player> players = new ArrayList<Player>();

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

        for (Player player : ((NoThanksGame) game).getPlayerList()){
            players.add(player);
        }
    }


    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_UP) {

                if (ScreenHelper.inButtonBounds(event, BUTTON_SAVE)) {
                    // Save
                    save();
                } else if (ScreenHelper.inButtonBounds(event, BUTTON_CLEAR)) {
                    // Clear
                    clearPlayers();
                } else if (ScreenHelper.inButtonBounds(event, BUTTON_ADD_HUMAN)) {
                    // Add human player
                    addHuman();
                } else if (ScreenHelper.inButtonBounds(event, BUTTON_ADD_BOT)) {
                    // Add human player
                    addBot();
                } else if (ScreenHelper.inButtonBounds(event, BUTTON_REMOVE_LAST_PLAYER)) {
                    // Remove the last added player
                    removeLastPlayer();
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

        for (int i = 0; i < players.size(); i++) {
            if (i % 2 == 0) {
                g.drawString(players.get(i).getName(), 100, 150 + i * 20, paintLeft);
            } else {
                g.drawString(players.get(i).getName(), 1180, 150 + (i - 1) * 20, paintRight);
            }
        }

        if (players.size() < 2) {
            g.drawString("You need at least two players to play a game", 640, 300, paint);
        }


        if (players.size() < 6) {
            ScreenHelper.drawButton(g, paint, BUTTON_ADD_HUMAN, "Add Human");
            ScreenHelper.drawButton(g, paint, BUTTON_ADD_BOT, "Add Bot");
        }

        if (players.size() > 0){
            ScreenHelper.drawButton(g, paint, BUTTON_REMOVE_LAST_PLAYER, "Remove Last Player");
        }

        ScreenHelper.drawButton(g, paint, BUTTON_CLEAR, "Clear");

        if (players.size() > 1) {
            ScreenHelper.drawButton(g, paint, BUTTON_SAVE, "Save");
        }

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
    }

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
    }*/

    private void save(){
        if (players.size() > 1) {
            ((NoThanksGame) game).setPlayerList(players);
            game.setScreen(new MainMenuScreen(game));
        }
    }

    private void clearPlayers(){
        players = new ArrayList<Player>();
    }

    private void addHuman(){
        if (players.size() < 6) {
            players.add(new HumanPlayer("Player " + (players.size() + 1)));
        }
    }

    private void addBot(){
        if (players.size() < 6) {
            players.add(new GeoffFAI(new String[]{"Bot " + (players.size() + 1)}));
        }
    }

    private void removeLastPlayer(){
        if (players.size() > 0) {
            players.remove(players.size()-1);
        }
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
