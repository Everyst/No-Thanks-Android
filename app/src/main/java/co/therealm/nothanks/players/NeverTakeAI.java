package co.therealm.nothanks.players;

import java.util.List;

import co.therealm.nothanks.model.Card;

/**
 * Created by GeoffF on 4/03/2015.
 */
public class NeverTakeAI extends Player{
    private static final String AI_NAME = "NeverTakeAI";

    public NeverTakeAI() {
        super(new String[]{AI_NAME});
    }

    public NeverTakeAI(String[] name) {
        super(name);
    }

    @Override
    protected boolean chooseMove(Card currentCard, List<Player> otherPlayers, int numberOfCardsRemaining) {
        // Never take a card you don't have to.
        return false;
    }

}
