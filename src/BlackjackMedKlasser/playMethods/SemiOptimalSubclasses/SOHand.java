package BlackjackMedKlasser.playMethods.SemiOptimalSubclasses;

public class SOHand {
    private short cardAmount;
    private byte firstCardValue = 0;
    private byte total = 0;
    private byte availableAces;

    public SOHand(HandSaveState ss) {
        setState(ss);
    }

    public SOHand() {}

    public void setState(HandSaveState ss) {
        cardAmount = ss.getCardAmount();
        firstCardValue = ss.getFirstCard();
        total = ss.getTotal();
        availableAces = ss.getAvailabelAces();
    }

    public byte getAvailableAces() {
        return availableAces;
    }

    public byte getTotal() {
        return total;
    }

    public HandSaveState saveState() {
        return new HandSaveState(cardAmount,total,firstCardValue, availableAces);
    }

    public void setTotal(byte newTotal) {
        total = newTotal;
    }

    public byte getFirstCardValue() {
        return firstCardValue;
    }

    public void addCard(byte card) {
        cardAmount++;
        total += card;
        if (card == 11) availableAces++;
        if (total > 21 && availableAces != 0) {
            total -= 10;
            availableAces -= 1;
        }
    }

    public boolean hasBlackjack() {
        return cardAmount == 2 && total == 21;
    }
}
