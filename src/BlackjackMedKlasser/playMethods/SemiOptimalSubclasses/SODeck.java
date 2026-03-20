package BlackjackMedKlasser.playMethods.SemiOptimalSubclasses;

import BlackjackMedKlasser.Card;
import BlackjackMedKlasser.Settings;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class SODeck {
    private byte[] cards = new byte[10];
    private short cardAmount;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    private int numberOfDecks = 1;

    public DeckSaveState saveState() {
        return new DeckSaveState(cards, cardAmount);
    }

    public void setState(DeckSaveState ss) {
        cards = ss.getCards();
        cardAmount = ss.getCardAmount();
    }

    public SODeck() {
    }

    public SODeck(Settings settings) {
        numberOfDecks = settings.getNumberOfDecks();
        shuffle();
    }

    public void shuffle() {
        for (int i = 0; i < 8; i++) {
            cards[i] = (byte) (4 * numberOfDecks);
        }
        cards[8] = (byte) (16 * numberOfDecks);
        cards[9] = (byte) (4 * numberOfDecks);
        cardAmount = (short) (52 * numberOfDecks);
    }

    public byte deal() {
        int selectedIndex = random.nextInt(0, cardAmount);
        for (int i = 9; i >= 0; i--) {
            selectedIndex -= cards[i];
            if (selectedIndex >= 0) continue;
            return removeCard(i);
        }
        return removeCard(0);
    }

    public byte removeCard(int index) {
        cards[index]--;
        cardAmount--;
        return (byte) (index + 2);
    }

    public double fractionThatIs10() {
        return (double) cards[8] / cardAmount;
    }

    public LinkedList<Card> turnIntoList() {
        LinkedList<Card> list = new LinkedList<>();
        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < cards[j]; i++) {
                list.add(new Card(j + 2, 0));
            }
        }
        for (int i = 0; i < cards[9]; i++) {
            list.add(new Card(1, 0));
        }
        return list;
    }
}
