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

    public String getString() {
        return cards[0] + "-" + cards[1] + "-" + cards[2] + "-" + cards[3] + "-" + cards[4] + "-" + cards[5] + "-" + cards[6] + "-" + cards[7] + "-" + cards[8] + "-" + cards[9];
    }
}
