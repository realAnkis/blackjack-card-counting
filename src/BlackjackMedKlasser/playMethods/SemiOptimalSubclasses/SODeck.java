package BlackjackMedKlasser.playMethods.SemiOptimalSubclasses;

import java.util.concurrent.ThreadLocalRandom;

public class SODeck {
    private byte[] cards = new byte[10];
    private short cardAmount;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    public DeckSaveState saveState() {
        return new DeckSaveState(cards,cardAmount);
    }

    public void setState(DeckSaveState ss) {
        cards = ss.getCards();
        cardAmount = ss.getCardAmount();
    }

    public byte deal() {
        int selectedIndex = random.nextInt(10, cardAmount);;
        for (int i = 9; i >= 0; i--) {
            selectedIndex -= cards[i];
            if (selectedIndex >= 0) continue;
            return removeCard(i);
        }
        return removeCard(0);
    }

    private byte removeCard(int index) {
        cards[index]--;
        cardAmount--;
        return (byte) (index + 2);
    }
}
