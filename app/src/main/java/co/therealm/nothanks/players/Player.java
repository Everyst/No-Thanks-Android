package co.therealm.nothanks.players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import co.therealm.nothanks.model.Card;

/**
 * Created by GeoffF on 4/03/2015.
 */
public abstract class Player implements Comparable<Player> {
    private String name;

    int tokens = 11;

    List<Card> cards = new ArrayList<Card>();

    protected Player(String[] parameters){
        if (parameters == null){
            this.name = "Default Name";
        } else {
            this.name = parameters[0];

            if (parameters.length > 1) {
                // Send the parameters through without the name at the front
                initialiseValues(Arrays.copyOfRange(parameters, 1, parameters.length));
            }
        }
    }

    protected void initialiseValues(String[] parameters){
        // AI classes may override this to take in parameters
    }

    protected void resetValues(){
        // Child classes may override this to avoid keeping data between games
    }

    /**
     * This method should be overwritten to provide different AI.
     * @param currentCard The current card
     * @param otherPlayers The list of other players
     * @param numberOfCardsRemaining The number of cards remaining
     * @return boolean whether you want to take the card or not.
     */
    protected abstract boolean chooseMove(Card currentCard, List<Player> otherPlayers, int numberOfCardsRemaining);

    /**
     *
     * @param currentCard
     * @param otherPlayers
     * @param numberOfCardsRemaining
     * @return true if the player takes the card
     */
    public final boolean takeTurn(Card currentCard, List<Player> otherPlayers, int numberOfCardsRemaining){
        boolean takeCard = true; // Assume the player must take the card

        // A player may only decide whether or not to take the card if they have at least one token.
        if (getTokens() > 0){
            takeCard = chooseMove(currentCard, otherPlayers, numberOfCardsRemaining);
        }

        if (takeCard){
            addTokens(currentCard.getTokens());
            cards.add(currentCard);
        } else {
            // Put a token on the card
            spendToken();
            currentCard.addToken();
        }

        return takeCard;
    }


    public final int calculatePoints(){
        int points = 0;

        if (cards.size() > 0){
            // Sort the cards into order
            Collections.sort(cards);

            // Previous card seen is the first card
            int previousCard = cards.get(0).getValue();

            for (Card card : cards){
                // Only count subsequent cards which aren't exactly one more than the previous card seen
                // Note that in the first iteration the current card will equal the previous card,
                // and therefore will be counted.
                if (card.getValue() != previousCard+1){
                    points += card.getValue();
                }
                previousCard = card.getValue();
            }
        }

        // Subtract the number of tokens the player has
        points -= getTokens();

        return points;
    }


    public final void reset() {
        tokens = 11;

        cards = new ArrayList<Card>();

        resetValues();
    }


    public final String getName(){
        return name;
    }

    // Everyone can see what cards the player has
    public final List<Card> getCards(){
        Collections.sort(cards);
        return cards;
    }

    public final int getTokens(){
        return tokens;
    }



    public final boolean hasConsecutiveHigher(Card card){
        return hasConsecutiveHigher(card.getValue());
    }

    public final boolean hasConsecutiveHigher(int cardValue){
        for (Card playerCard : cards){
            if (cardValue + 1 == playerCard.getValue()){
                // Player has the card above this card; it will benefit them to pick it up.
                return true;
            }
        }
        return false;
    }

    public final boolean hasConsecutiveLower(Card card){
        return hasConsecutiveLower(card.getValue());
    }

    public final boolean hasConsecutiveLower(int cardValue){
        for (Card playerCard : cards){
            if (cardValue - 1 == playerCard.getValue()){
                // We have the card below this card; it wont cost us a point to pick it up.
                return true;
            }
        }
        return false;
    }




    private final void addTokens(int tokens){
        this.tokens += tokens;
    }

    private final void spendToken(){
        if (getTokens() <= 0){
            throw new RuntimeException("Tried to spend a token when tokens = " + getTokens());
        }
        tokens--;
    }


    public boolean isHuman(){
        return false;
    }

    @Override
    public String toString(){
        return this.name;
    }

    public String getStatus(){
        return "Points: " + calculatePoints() + ", Cards: " + cards + ", Tokens: " + tokens;
    }

    /**
     * This method compares two player's scores and puts them in descending order (lowest first)
     */
    @Override
    public final int compareTo(Player otherPlayer) {
        // Descending order
        return this.calculatePoints() - otherPlayer.calculatePoints();
    }
}
