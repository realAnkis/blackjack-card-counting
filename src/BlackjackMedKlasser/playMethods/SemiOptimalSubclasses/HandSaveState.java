package BlackjackMedKlasser.playMethods.SemiOptimalSubclasses;

import BlackjackMedKlasser.Card;

public class HandSaveState {
    private final int cardAmount;
    private final int firstCardValue;
    private final int total;
    private final int availabelAces;

    public HandSaveState(int cardAmount, int total, int firstCardValue, int availabelAces) {
        this.cardAmount = cardAmount;
        this.total = total;
        this.firstCardValue = firstCardValue;
        this.availabelAces = availabelAces;
    }

    public int getCardAmount() {
        return cardAmount;
    }

    public int getFirstCard() {
        return firstCardValue;
    }

    public int getTotal() {
        return total;
    }

    public int getAvailabelAces() {
        return availabelAces;
    }
}
