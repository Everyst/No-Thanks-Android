package co.therealm.nothanks;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.therealm.framework.Game;
import co.therealm.framework.Graphics;
import co.therealm.framework.Input.TouchEvent;
import co.therealm.framework.Screen;
import co.therealm.nothanks.players.GeoffFAI;
import co.therealm.nothanks.players.HumanPlayer;
import co.therealm.nothanks.players.NetValueLessThanXAI;
import co.therealm.nothanks.players.NeverTakeAI;
import co.therealm.nothanks.players.Player;

/**
 * Created by Geoffrey on 3/03/2015.
 */
public class MainMenuScreen extends Screen{

    private static final int[] SINGLE_PLAYER = new int[]{180, 600, 300, 100};
    private static final int[] TWO_PLAYER = new int[]{820, 600, 300, 100};


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

                if (ScreenHelper.inBounds(event, SINGLE_PLAYER[0], SINGLE_PLAYER[1], SINGLE_PLAYER[2], SINGLE_PLAYER[3])) {
                    // Single player
                    game.setScreen(new GameScreen(game, setUpPlayers(1)));
                } else if (ScreenHelper.inBounds(event, TWO_PLAYER[0], TWO_PLAYER[1], TWO_PLAYER[2], TWO_PLAYER[3])) {
                    // Two player
                    game.setScreen(new GameScreen(game, setUpPlayers(2)));
                }

            }
        }
    }


    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();

        //g.drawImage(Assets.menu, 0, 0);
        g.drawARGB(155, 0, 0, 0);

        g.drawString("Choose game type", 640, 300, paint);

        g.drawString("Single player is against 3 AI players", 640, 450, paint);
        g.drawString("Two player has 1 AI player", 640, 500, paint);


        g.drawRect(SINGLE_PLAYER[0], SINGLE_PLAYER[1], SINGLE_PLAYER[2], SINGLE_PLAYER[3], Color.DKGRAY);
        g.drawString("Single player", 320, 650, paint);

        g.drawRect(TWO_PLAYER[0], TWO_PLAYER[1], TWO_PLAYER[2], TWO_PLAYER[3], Color.DKGRAY);
        g.drawString("Two player", 960, 650, paint);
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


        Collections.shuffle(players); // shuffle the order of players

        return players;
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
