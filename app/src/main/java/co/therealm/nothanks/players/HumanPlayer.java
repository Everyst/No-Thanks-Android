package co.therealm.nothanks.players;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import co.therealm.nothanks.model.Card;

/**
 * Created by GeoffF on 4/03/2015.
 */
public class HumanPlayer extends Player{
    private static final String AI_NAME = "Human";

    private boolean taking = true;

    public HumanPlayer() {
        super(AI_NAME);
    }

    public HumanPlayer(String name) {
        super(name);
    }

    @Override
    public boolean isHuman(){
        return true;
    }

    @Override
    protected boolean chooseMove(Card currentCard, List<Player> otherPlayers, int numberOfCardsRemaining) {
        return taking;
    }

    public void setTaking(boolean decision){
        taking = decision;
    }
}
