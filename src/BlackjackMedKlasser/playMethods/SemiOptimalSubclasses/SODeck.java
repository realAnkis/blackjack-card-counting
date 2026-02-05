package BlackjackMedKlasser.playMethods.SemiOptimalSubclasses;

public class SODeck {
    private int[] cards = new int[10];
    private int cardAmount;

    public DeckSaveState saveState() {
        return new DeckSaveState(cards,cardAmount);
    }

    public void setState(DeckSaveState ss) {
        cards = ss.getCards();
        cardAmount = ss.getCardAmount();
    }

    public int deal() {
        int selectedIndex = (int) (Math.random() * cardAmount);
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
