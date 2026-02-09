package BlackjackMedKlasser.playMethods.SemiOptimalSubclasses;

import BlackjackMedKlasser.Card;

import java.util.LinkedList;

public class SOHand {
    private int cardAmount;
    private int firstCardValue = 0;
    private int total = 0;
    private int availabelAces;

    public SOHand(HandSaveState ss) {
        setState(ss);
    }

    public SOHand() {}

    public void setState(HandSaveState ss) {
        cardAmount = ss.getCardAmount();
        firstCardValue = ss.getFirstCard();
        total = ss.getTotal();
        availabelAces = ss.getAvailabelAces();
    }

    public int getAvailabelAces() {
        return availabelAces;
    }

    public int getTotal() {
        return total;
    }

    public HandSaveState saveState() {
        return new HandSaveState(cardAmount,total,firstCardValue,availabelAces);
    }

    public void setTotal(int newTotal) {
        total = newTotal;
    }

    public int getFirstCardValue() {
        return firstCardValue;
    }

    public int addCard(Card card) {
        cardAmount++;
        total += card.getValue();
        if (card.getValue() == 11) availabelAces++;
        if (total > 21 && availabelAces != 0) {
            total -= 10;
            availabelAces -= 1;
        }
        return total;
    }

    public void addCard(int card) {
        cardAmount++;
        total += card;
        if (card == 11) availabelAces++;
        if (total > 21 && availabelAces != 0) {
            total -= 10;
            availabelAces -= 1;
        }
    }

    public boolean hasBlackjack() {
        return cardAmount == 2 && total == 21;
    }
}
