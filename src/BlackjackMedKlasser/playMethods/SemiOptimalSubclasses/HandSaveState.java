package BlackjackMedKlasser.playMethods.SemiOptimalSubclasses;

import BlackjackMedKlasser.Card;

public class HandSaveState {
    private final short cardAmount;
    private final byte firstCardValue;
    private final byte total;
    private final byte availabelAces;

    public HandSaveState(short cardAmount, byte total, byte firstCardValue, byte availabelAces) {
        this.cardAmount = cardAmount;
        this.total = total;
        this.firstCardValue = firstCardValue;
        this.availabelAces = availabelAces;
    }

    public HandSaveState( byte firstCardValue, byte availabelAces) {
        cardAmount = 1;
        total = this.firstCardValue = firstCardValue;
        this.availabelAces = availabelAces;
    }

    public short getCardAmount() {
        return cardAmount;
    }

    public byte getFirstCard() {
        return firstCardValue;
    }

    public byte getTotal() {
        return total;
    }

    public byte getAvailabelAces() {
        return availabelAces;
    }
}
