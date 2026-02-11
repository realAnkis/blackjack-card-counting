package BlackjackMedKlasser.playMethods.SemiOptimalSubclasses;

import java.util.concurrent.ThreadLocalRandom;

public class SODeck {
    private int[] cards = new int[10];
    private int cardAmount;
    private ThreadLocalRandom random = ThreadLocalRandom.current();

    public DeckSaveState saveState() {
        return new DeckSaveState(cards,cardAmount);
    }

    public void setState(DeckSaveState ss) {
        cards = ss.getCards();
        cardAmount = ss.getCardAmount();
    }

    public int deal() {
        int selectedIndex = random.nextInt(10, cardAmount);;
        for (int i = 9; i >= 0; i--) {
            selectedIndex -= cards[i];
            if (selectedIndex >= 0) continue;
            return removeCard(i);
        }
        return removeCard(0);
    }

    private int removeCard(int index) {
        cards[index]--;
        cardAmount--;
        return index + 2;
    }
}
