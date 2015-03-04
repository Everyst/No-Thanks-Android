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
import co.therealm.nothanks.players.GeoffFAI;
import co.therealm.nothanks.players.HumanPlayer;
import co.therealm.nothanks.players.NetValueLessThanXAI;
import co.therealm.nothanks.players.NeverTakeAI;
import co.therealm.nothanks.players.Player;

/**
 * Created by Geoffrey on 3/03/2015.
 */
public class GameScreen extends Screen {
    enum GameState {
        Running, ExitConfirmation, GameOver
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

    Paint paintMiddle;
    Paint paintLeft;
    Paint paintRight;

    public GameScreen(Game game, int numberOfHumanPlayers) {
        super(game);

        // Initialize game objects here

        // Defining paint objects
        paintMiddle = new Paint();
        paintMiddle.setTextSize(30);
        paintMiddle.setTextAlign(Paint.Align.CENTER);
        paintMiddle.setAntiAlias(true);
        paintMiddle.setColor(Color.WHITE);

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


        // Set up the game
        deck = new Deck();

        players = setUpPlayers(numberOfHumanPlayers);
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
        } else if (state == GameState.ExitConfirmation) {
            updateExitConfirmation(touchEvents);
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

                        if (ScreenHelper.inBounds(event, 180, 600, 300, 100)) {
                            // Yes please
                            decisionMade = true;
                            humanPlayer.setTaking(true);
                        } else if (ScreenHelper.inBounds(event, 820, 600, 300, 100)) {
                            // No thanks
                            decisionMade = true;
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

    private void updateExitConfirmation(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (ScreenHelper.inBounds(event, 180, 600, 300, 100)){
                    // Exit
                    nullify();
                    game.setScreen(new MainMenuScreen(game));
                    return;
                } else if (ScreenHelper.inBounds(event, 820, 600, 300, 100)){
                    // Cancel
                    state = GameState.Running;
                }
            }
        }
    }

    private void updateGameOver(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                nullify();
                game.setScreen(new MainMenuScreen(game));
                return;
            }
        }
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
        } else if (state == GameState.ExitConfirmation) {
            drawExitConfirmationUI();
        } else if (state == GameState.GameOver) {
            drawGameOverUI();
        }

    }

    private void nullify() {

        // Set all variables to null. You will be recreating them in the
        // constructor.
        paintMiddle = null;
        paintLeft = null;
        paintRight = null;

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

        g.drawString("It is " + players.get(currentPlayer).getName() + "'s turn", 640, 100, paintMiddle);

        for (int i = 0; i < otherPlayers.size(); i++){
            if (i % 2 == 0) {
                g.drawString(otherPlayers.get(i).getName(), 100, 150+i*40, paintLeft);
                g.drawString("Cards: " + otherPlayers.get(i).getCards().toString(), 100, 180+i*40, paintLeft);
            } else {
                g.drawString(otherPlayers.get(i).getName(), 1180, 150+(i-1)*40, paintRight);
                g.drawString("Cards: " + otherPlayers.get(i).getCards(), 1180, 180+(i-1)*40, paintRight);
            }
        }

        g.drawString("There are " + deck.cardsRemaining() + " cards left. The current card is", 640, 325, paintMiddle);
        g.drawString(""+currentCard.getValue(), 640, 375, paintMiddle);
        g.drawString("with "+currentCard.getTokens()+" token/s", 640, 425, paintMiddle);


        g.drawString("Your cards are: " + players.get(currentPlayer).getCards(), 640, 500, paintMiddle);

        g.drawString("You have " + players.get(currentPlayer).getTokens() + " token/s", 640, 550, paintMiddle);


        g.drawRect(180, 600, 300, 100, Color.DKGRAY);
        g.drawString("Yes please", 320, 650, paintMiddle);

        g.drawRect(820, 600, 300, 100, Color.DKGRAY);
        g.drawString("No thanks", 960, 650, paintMiddle);

    }

    private void drawExitConfirmationUI() {
        Graphics g = game.getGraphics();
        g.drawARGB(155, 0, 0, 0);

        g.drawString("Are you sure you would like to exit the game?", 640, 300, paintMiddle);
        
        g.drawRect(180, 600, 300, 100, Color.DKGRAY);
        g.drawString("Exit", 320, 650, paintMiddle);

        g.drawRect(820, 600, 300, 100, Color.DKGRAY);
        g.drawString("Cancel", 960, 650, paintMiddle);

    }

    private void drawGameOverUI() {
        Graphics g = game.getGraphics();
        g.drawARGB(155, 0, 0, 0);
        g.drawString("GAME OVER", 640, 100, paintMiddle);

        Collections.sort(players);

        g.drawString(players.get(0) + " won!", 640, 200, paintMiddle);

        for (int i = 0; i < numberOfPlayers; i++){
            g.drawString("Player " + players.get(i) + " got " + players.get(i).getStatus(), 640, 300 + 50*i, paintMiddle);
        }

        g.drawString("Tap anywhere to go back to the menu", 640, 600, paintMiddle);

    }


    @Override
    public void exitConfirmation() {
        if (state == GameState.Running) {
            state = GameState.ExitConfirmation;
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
