package BlackjackMedKlasser;

import java.util.LinkedList;

public class Hand {
    private LinkedList<Card> cards;
    private int total = 0;
    private int avalabelAces;

    public int getAvalabelAces() {
        return avalabelAces;
    }

    public int getTotal() {
        return total;
    }

    public int Hand(Card card) {
        cards.add(card);
        total += card.getValue();
        if (card.getValue() == 11) avalabelAces++;
        if (total > 21 && avalabelAces != 0) {
            total -= 10;
            avalabelAces -= 1;
        }
        return total;
    }

}
