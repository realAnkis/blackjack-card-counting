package BlackjackMedKlasser;

import java.util.Collections;
import java.util.LinkedList;

public class Deck {
    private final int numberOfDecks;
    private LinkedList<Card> cards = new LinkedList<>();
    double[] reshufflePercentInterval = new double[]{0.25, 0.55};
    double cutOff = 0;



    public Deck(Settings settings) {
        numberOfDecks = settings.numberOfDecks;
        reshufflePercentInterval = settings.reshufflePercentInterval;
        shuffle();
    }

    public void shuffle() {
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
        cutOff = reshufflePercentInterval[0] + Math.random() * (reshufflePercentInterval[1] - reshufflePercentInterval[0]);
    }

    public Card deal() {
        return cards.poll();
    }

    public void CheckReshuffle(){
        if((double) cards.size() /(52*numberOfDecks)<cutOff) shuffle();
    }
}

