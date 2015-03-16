package co.therealm.nothanks.players;

import java.util.List;

import co.therealm.nothanks.model.Card;

/**
 * Created by GeoffF on 4/03/2015.
 */
public class NetValueLessThanXAI extends Player{
    private static final String AI_NAME = "NetValueLessThanXAI";

    private int threshold;

    public NetValueLessThanXAI(int threshold) {
        super(new String[]{AI_NAME + "_X=" + threshold, ""+threshold});
    }

    public NetValueLessThanXAI(String[] parameters) {
        super(parameters);
    }

    @Override
    protected void initialiseValues(String[] parameters) {
        // Take the threshold value from the parameters
        if (parameters.length > 0){
            this.threshold = Integer.valueOf(parameters[0]);
        } else {
            this.threshold = 10; // Default value
        }
    }

    @Override
    protected boolean chooseMove(Card currentCard, List<Player> otherPlayers, int numberOfCardsRemaining) {
        // Take the card if you have the card above it.
        for (Card myCard : cards){
            if (currentCard.getValue() + 1 == myCard.getValue()){
                return true;
            }
        }

        // Take the card if it's net value is less than 10 points.
        if (currentCard.getValue() - currentCard.getTokens() < threshold){
            return true;
        } else {
            return false;
        }
    }

}
