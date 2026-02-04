package BlackjackMedKlasser.playMethods.SemiOptimalSubclasses;

import BlackjackMedKlasser.Card;

import java.util.LinkedList;

public class DeckSaveState {
    private Card[] cards;
    private int cardAmount;

    public DeckSaveState(Card[] cards, int cardAmount) {
        this.cards = cards;
        this.cardAmount = cardAmount;
    }

    public DeckSaveState(LinkedList<Card> list) {
        cardAmount = list.size();
        cards = new Card[cardAmount];

        for (int i = 0; i < cardAmount; i++) {
            cards[i] = list.get(i);
        }
    }

    public Card[] getCards() {
        Card[] newCards = new Card[cardAmount];
        System.arraycopy(cards, 0, newCards, 0, cardAmount);
        return newCards;
    }

    public int getCardAmount() {
        return cardAmount;
    }
}
