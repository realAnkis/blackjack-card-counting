package BlackjackMedKlasser.playMethods.SemiOptimalSubclasses;

import BlackjackMedKlasser.Card;

import java.util.LinkedList;

public class DeckSaveState {
    private int[] cards;
    private int cardAmount;

    public DeckSaveState(int[] cards, int cardAmount) {
        this.cards = cards;
        this.cardAmount = cardAmount;
    }

    public DeckSaveState(LinkedList<Card> list) {
        cardAmount = list.size();
        cards = new int[10];

        for (Card card : list) {
            cards[card.getValue()-2]++;
        }
    }

    public int[] getCards() {
        int[] newCards = new int[10];
        System.arraycopy(cards, 0, newCards, 0, 10);
        return newCards;
    }

    public int getCardAmount() {
        return cardAmount;
    }
}
