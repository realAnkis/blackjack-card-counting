package BlackjackMedKlasser;

import java.util.Collections;
import java.util.LinkedList;

public class Deck {
    private final int numberOfDecks;
    private LinkedList<Card> cards;

    public Deck(int numberOfDecks) {
        this.numberOfDecks = numberOfDecks;
        shuffle();
    }

    public void shuffle(){
        cards.clear();
        for (int k = 0; k < numberOfDecks; k++) {
            for (int i = 0; i < 4; i++) {
                for (int j = 1; j < 14; j++) {
                    Card c = new Card(j, i);
                    cards.add(c);
                }
            }
        }
        Collections.shuffle(cards);
    }

    public Card deal(){
        return cards.poll();
    }
}
