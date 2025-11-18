package BlackjackMedKlasser;

import java.util.LinkedList;

public class Hand {
    private LinkedList<Card> cards = new LinkedList<>();
    private int total = 0;
    private int avalabelAces;

    public int getAvalabelAces() {
        return avalabelAces;
    }

    public int getTotal() {
        return total;
    }

    public LinkedList<Card> getCards() {
        return cards;
    }

    public int addCard(Card card) {
        cards.add(card);
        total += card.getValue();
        if (card.getValue() == 11) avalabelAces++;
        if (total > 21 && avalabelAces != 0) {
            total -= 10;
            avalabelAces -= 1;
        }
        return total;
    }

    public void clear() {
        cards.clear();
    }

}
