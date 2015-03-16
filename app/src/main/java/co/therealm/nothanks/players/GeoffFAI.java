package co.therealm.nothanks.players;

import java.util.List;

import co.therealm.nothanks.model.Card;

/**
 * Created by GeoffF on 4/03/2015.
 */
public class GeoffFAI extends Player{
    private static final String AI_NAME = "GeoffFAI";

    public GeoffFAI() {
        super(new String[]{AI_NAME});
    }

    public GeoffFAI(String name[]) {
        super(name);
    }

    @Override
    protected boolean chooseMove(Card currentCard, List<Player> otherPlayers, int numberOfCardsRemaining) {
        boolean haveConsecutiveHigher = hasConsecutiveHigher(currentCard);
        boolean haveConsecutiveLower = hasConsecutiveLower(currentCard);

        if (haveConsecutiveHigher && haveConsecutiveLower){
            // We really want this card, it will save us many points
            return true;
        }

        if (haveConsecutiveHigher){
            // The card will save us a point if we pick it up.
            // But if no one else will want it, then send it around for more tokens.

            // If someone else could take it from us for free, or might gamble one it, take it.
            for (Player otherPlayer : otherPlayers){
                if (otherPlayer.hasConsecutiveLower(currentCard) || otherPlayer.hasConsecutiveLower(currentCard.getValue() + 1)){
                    return true;
                }
            }

            // If it would be a net value of only 3 point before it got back to me, take it.
            if (currentCard.getValue() - otherPlayers.size() + 1 <= 3){
                return true;
            }

            // If it has many tokens we want it.
            // Many means quarter point value in tokens..?
            if (currentCard.getTokens() != 0 && currentCard.getValue()/currentCard.getTokens() < 4){
                return true;
            }

            return false; // It should be safe to send around.

        } else if (haveConsecutiveLower){
            // The card will not cost us a point if we pick it up.

            // If it will benefit someone else, take it.
            for (Player otherPlayer : otherPlayers){
                if (otherPlayer.hasConsecutiveHigher(currentCard)){
                    return true;
                }
            }

            // If it has no tokens on it, send it around.
            if (currentCard.getTokens() == 0){
                return false;
            }
            // If it has many tokens we want it.
            // Many means quarter point value in tokens..?
            if (currentCard.getValue()/currentCard.getTokens() < 4){
                return true;
            }
            // If it has some (but not many) tokens and no one else will want it, then treat it as any other card
            // for the purposes of determining if we need the tokens on it or not.
        }


        // Take the card if it has enough tokens on it to make it worth it.
        // Also consider how many tokens you have and what stage the game is in.

        // Running out of tokens is very bad.
        // Early game "bad" (high value) cards are ok.
        // Late game high value cards which aren't close to what we want are very bad.

        if (numberOfCardsRemaining > 16){ // 23-17 cards
            // Early game
            if (currentCard.getValue() - currentCard.getTokens() < 10){
                return true;
            }
            return false;
        } else if (numberOfCardsRemaining > 9){ // 16-10 cards
            // Mid game
            if (currentCard.getValue() - currentCard.getTokens() <= 1){
                return true; // Worth it
            }
            return false;
        } else if (numberOfCardsRemaining > 3){ // 9-4 cards
            // Late game
            if (currentCard.getValue() - currentCard.getTokens() <= 1){
                return true; // Worth it
            }
            return false; // Don't take cards you don't need/want to.
        } else { // 3-0 cards
            // End game
            if (currentCard.getValue() - currentCard.getTokens() <= 1){
                return true; // Worth it
            }
            return false; // Don't take cards you don't need/want to.
        }
    }
}