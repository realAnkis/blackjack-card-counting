package BlackjackMedKlasser;

import BlackjackMedKlasser.playMethods.*;
import BlackjackMedKlasser.*;

import java.util.LinkedList;

public class Hand {
    private LinkedList<Card> cards = new LinkedList<>();
    private int total = 0;
    private int availabelAces;
    private int bet;

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public int getAvailabelAces() {
        return availabelAces;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int newTotal) {
        total = newTotal;
    }

    public LinkedList<Card> getCards() {
        return cards;
    }

    public int addCard(Card card) {
        cards.add(card);
        total += card.getValue();
        if (card.getValue() == 11) availabelAces++;
        if (total > 21 && availabelAces != 0) {
            total -= 10;
            availabelAces -= 1;
        }
        return total;
    }

    public Card pollCard() {
            return cards.pollLast();
    }

    public boolean hasBlackjack() {
        return cards.size() == 2 && total == 21;
    }

    public void clear() {
        cards.clear();
        bet = 0;
        total = 0;
        availabelAces = 0;
    }

}
