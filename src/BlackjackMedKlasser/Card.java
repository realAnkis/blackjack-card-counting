package BlackjackMedKlasser;

public class Card {
    private final int value;
    private final int suit;

    public Card(int value, int suit) {
        this.value = value;
        this.suit = suit;
    }

    public int getSuit() {
        return suit;
    }

    public int getValue() {
        if(value == 1) return 11;
        return Math.min(value,10);
    }



}
