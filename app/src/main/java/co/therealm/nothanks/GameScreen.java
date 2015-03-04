package co.therealm.nothanks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;

import co.therealm.framework.Game;
import co.therealm.framework.Graphics;
import co.therealm.framework.Input.TouchEvent;
import co.therealm.framework.Screen;
import co.therealm.nothanks.model.Card;
import co.therealm.nothanks.model.Deck;
import co.therealm.nothanks.players.HumanPlayer;
import co.therealm.nothanks.players.Player;
import co.therealm.nothanks.players.RandomAI;

/**
 * Created by Geoffrey on 3/03/2015.
 */
public class GameScreen extends Screen {
    enum GameState {
        Running, Paused, GameOver
    }

    GameState state = GameState.Running;

    // Variable Setup
    // You would create game objects here.

    private int currentPlayer;
    private int numberOfPlayers;
    private List<Player> players;
    private List<Player> otherPlayers;
    private HumanPlayer humanPlayer;
    private boolean decisionMade;
    private Deck deck;
    private Card currentCard;


    Paint paint;

    public GameScreen(Game game) {
        super(game);

        // Initialize game objects here

        // Defining a paint object
        paint = new Paint();
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);


        // Set up the deck
        deck = new Deck();

        players = setUpPlayers();
        numberOfPlayers = players.size();

        otherPlayers = new ArrayList<Player>(numberOfPlayers-1); // Will store all players who aren't the current player.

        // Get the first card out
        currentCard = deck.nextCard();

        currentPlayer = 0;
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        // We have four separate update methods in this example.
        // Depending on the state of the game, we call different update methods.
        // Refer to Unit 3's code. We did a similar thing without separating the
        // update methods.

        if (state == GameState.Running) {
            updateRunning(touchEvents, deltaTime);
        } else if (state == GameState.Paused) {
            updatePaused(touchEvents);
        } else if (state == GameState.GameOver) {
            updateGameOver(touchEvents);
        }
    }


    private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {

        otherPlayers.clear();
        otherPlayers.addAll(players);
        otherPlayers.remove(currentPlayer);


        if (players.get(currentPlayer).isHuman()){

            decisionMade = false;

            // Check for touch events
            int len = touchEvents.size();
            if (len != 0) {

                humanPlayer = (HumanPlayer) players.get(currentPlayer);

                for (int i = 0; i < len; i++) {
                    TouchEvent event = touchEvents.get(i);
                    if (event.type == TouchEvent.TOUCH_UP) {

                        decisionMade = true;

                        if (event.x < 640) {
                            // Yes please
                            humanPlayer.setTaking(true);
                        } else if (event.x >= 640) {
                            // No thanks
                            humanPlayer.setTaking(false);
                        }
                    }
                }

                if (decisionMade) {
                    if (humanPlayer.takeTurn(currentCard, otherPlayers, deck.cardsRemaining())) {
                        if ((currentCard = deck.nextCard()) == null) {
                            // No more cards, end the game
                            state = GameState.GameOver;
                        }
                    } else {
                        currentPlayer = nextPlayer();
                    }
                }
            }

        } else {
            // While the current player is willing to take the cards, he keeps taking them.
            while (players.get(currentPlayer).takeTurn(currentCard, otherPlayers, deck.cardsRemaining())) {
                if ((currentCard = deck.nextCard()) == null) {
                    // No more cards, end the game
                    state = GameState.GameOver;
                    break;
                }
            }

            if (currentCard != null){
                // The player put a token on the card
                currentPlayer = nextPlayer();
            }
        }



    }

    private void updatePaused(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                state = GameState.Running;
            }
        }
    }

    private void updateGameOver(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x > 300 && event.x < 980 && event.y > 100
                        && event.y < 500) {
                    nullify();
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
        }
    }



    private static List<Player> setUpPlayers(){
        List<Player> players = new ArrayList<Player>();

        players.add(new HumanPlayer("Player1"));
        players.add(new HumanPlayer("Player2"));
        players.add(new HumanPlayer("Player3"));
        players.add(new HumanPlayer("Player4"));


        //players.add(new RandomAI("Player1"));
        //players.add(new RandomAI("Player2"));
        //players.add(new RandomAI("Player3"));
        //players.add(new RandomAI("Player4"));

        //players.add(new GeoffFAI());
        //players.add(new NetValueLessThanXAI(11));
        //players.add(new NeverTakeAI());


        Collections.shuffle(players); // shuffle the order of players

        return players;
    }

    private int nextPlayer(){
        currentPlayer++;
        if (currentPlayer % numberOfPlayers == 0){
            currentPlayer = 0;
        }
        return currentPlayer;
    }


    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();

        // First draw the game elements.

        // Example:
        // g.drawImage(Assets.background, 0, 0);
        // g.drawImage(Assets.character, characterX, characterY);

        // Secondly, draw the UI above the game elements.
        if (state == GameState.Running) {
            drawRunningUI();
        } else if (state == GameState.Paused) {
            drawPausedUI();
        } else if (state == GameState.GameOver) {
            drawGameOverUI();
        }

    }

    private void nullify() {

        // Set all variables to null. You will be recreating them in the
        // constructor.
        paint = null;

        currentPlayer = 0;
        numberOfPlayers = 0;
        players = null;
        otherPlayers = null;
        deck = null;
        currentCard = null;

        // Call garbage collector to clean up memory.
        System.gc();
    }

    private void drawRunningUI() {
        Graphics g = game.getGraphics();
        g.drawARGB(155, 0, 0, 0);

        g.drawString(players.get(currentPlayer).getName() + " takes their turn", 640, 100, paint);

        g.drawString("The current card is", 640, 200, paint);
        g.drawString(""+currentCard.getValue(), 640, 250, paint);
        g.drawString("with "+currentCard.getTokens()+" tokens", 640, 300, paint);

        g.drawString(players.get(currentPlayer).getName() + " has " + players.get(currentPlayer).getTokens() + " tokens", 640, 450, paint);

        g.drawString("Yes please", 320, 600, paint);
        g.drawString("No thanks", 960, 600, paint);

    }

    private void drawPausedUI() {
        Graphics g = game.getGraphics();
        g.drawARGB(155, 0, 0, 0);

        g.drawString("Paused", 640, 300, paint);

    }

    private void drawGameOverUI() {
        Graphics g = game.getGraphics();
        g.drawARGB(155, 0, 0, 0);
        g.drawString("GAME OVER", 640, 100, paint);

        Collections.sort(players);

        g.drawString(players.get(0) + " won!", 640, 200, paint);

        for (int i = 0; i < numberOfPlayers; i++){
            g.drawString("Player " + players.get(i) + " got " + players.get(i).getStatus(), 640, 250 + 50*i, paint);
        }

    }


    @Override
    public void pause() {
        if (state == GameState.Running) {
            state = GameState.Paused;
        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void backButton() {
        pause();
    }
}
