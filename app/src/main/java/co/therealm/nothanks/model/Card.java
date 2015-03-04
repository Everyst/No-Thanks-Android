package co.therealm.nothanks.model;

/**
 * Created by GeoffF on 4/03/2015.
 */
public class Card implements Comparable<Card>{

    private int value;
    private int tokens = 0; // Represents the tokens put on the card.

    public Card(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public int getTokens(){
        return tokens;
    }

    public void addToken(){
        tokens++;
    }

    @Override
    public String toString(){
        return ""+value;
    }

    @Override
    public int compareTo(Card otherCard) {
        int diff = this.value - otherCard.value;

        if (diff == 0){
            // The cards were the same. This should never happen.
            throw new RuntimeException("Tried to compare two cards which both had the value " + this.value);
        }

        return diff;
    }
}
