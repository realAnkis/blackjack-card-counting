package BlackjackMedKlasser.playMethods.SemiOptimalSubclasses;

import BlackjackMedKlasser.Card;

public class HandSaveState {
    private final short cardAmount;
    private final byte firstCardValue;
    private final byte total;
    private final byte availabelAces;
    private int usedHands = 1;
    private boolean canSplit;

    public int getUsedHands() {
        return usedHands;
    }

    public void setUsedHands(int usedHands) {
        this.usedHands = usedHands;
    }

    public byte getFirstCardValue() {
        return firstCardValue;
    }


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

    public HandSaveState( byte firstCardValue, byte availabelAces, int usedHands) {
        cardAmount = 1;
        total = this.firstCardValue = firstCardValue;
        this.availabelAces = availabelAces;
        this.usedHands = usedHands;
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

    public boolean canSplit() {
        return canSplit;
    }

    public void setCanSplit(boolean canSplit) {
        this.canSplit = canSplit;
    }
}
