package BlackjackMedKlasser.playMethods.SemiOptimalSubclasses;

import BlackjackMedKlasser.Card;
import BlackjackMedKlasser.Settings;
import BlackjackMedKlasser.playMethods.PlayMethod;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class SODeck {
    private Card[] cards;
    private int cardAmount;

    public void setCardsFromList(LinkedList<Card> list) {
        cardAmount = list.size();
        cards = new Card[cardAmount];

        for (int i = 0; i < cardAmount; i++) {
            cards[i] = list.get(i);
        }
    }

    public DeckSaveState saveState() {
        return new DeckSaveState(cards,cardAmount);
    }

    public void setState(DeckSaveState ss) {
        cards = ss.getCards();
        cardAmount = ss.getCardAmount();
    }

    public Card deal() {
        int selectedIndex = (int)(Math.random() * cardAmount);
        Card card = cards[selectedIndex];
        cards[selectedIndex] = cards[--cardAmount];
        return card;
    }
}
