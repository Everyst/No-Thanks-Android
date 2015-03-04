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
        super(AI_NAME + "_X=" + threshold);

        this.threshold = threshold;
    }

    public NetValueLessThanXAI(String name, int threshold) {
        super(name);

        this.threshold = threshold;
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
