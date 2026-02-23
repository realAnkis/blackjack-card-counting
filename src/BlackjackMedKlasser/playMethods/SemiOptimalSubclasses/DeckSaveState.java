package BlackjackMedKlasser.playMethods.SemiOptimalSubclasses;

import BlackjackMedKlasser.Card;

import java.util.LinkedList;

public class DeckSaveState {
    private final byte[] cards;
    private final short cardAmount;

    public DeckSaveState(byte[] cards, short cardAmount) {
        this.cards = cards;
        this.cardAmount = cardAmount;
    }

    public DeckSaveState(LinkedList<Card> list) {
        cardAmount = (short) list.size();
        cards = new byte[10];

        for (Card card : list) {
            cards[card.getValue()-2]++;
        }
    }

    public byte[] getCards() {
        byte[] newCards = new byte[10];
        System.arraycopy(cards, 0, newCards, 0, 10);
        return newCards;
    }

    public short getCardAmount() {
        return cardAmount;
    }
}
