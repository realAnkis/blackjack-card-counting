package BlackjackMedKlasser;

import BlackjackMedKlasser.playMethods.*;

public class Round {
    private Hand[] hands = new Hand[4];
    private Hand dealerHand = new Hand();
    private Deck deck;
    private Game game;
    int totalRoundBet = 0;
    private int currentHandIndex;
    private PlayMethod playMethod;
    private int nextEmptyHand = 1;
    private int endOfRoundProfit;

    public Hand[] getHands() {
        return hands;
    }

    public Hand getDealerHand() {
        return dealerHand;
    }

    public int getDealerCard() {
        return dealerHand.getCards().getFirst().getValue();
    }

    public Deck getDeck() {
        return deck;
    }

    public int getCurrentHandIndex() {
        return currentHandIndex;
    }

    public int getEndOfRoundProfit() {
        return endOfRoundProfit;
    }

    public Round(Deck deck, PlayMethod playMethod, Game game) {
        this.deck = deck;
        this.playMethod = playMethod;
        this.game = game;

        for (int i = 0; i < 4; i++) {
            hands[i] = new Hand();
        }
    }

    public int playRound() {
        endOfRoundProfit = 0;

        deck.CheckReshuffle(playMethod);

        hands[0].setBet(playMethod.betMethod(this));

        hands[0].addCard(deck.deal(playMethod));
        dealerHand.addCard(deck.deal(playMethod));
        hands[0].addCard((deck.deal(playMethod)));
        dealerHand.addCard(deck.deal(playMethod));


        int insuranceBet = 0;
        if (dealerHand.getCards().getFirst().getValue() == 11) {
            insuranceBet = playMethod.insuranceBetMethod(this);
            game.addBetTotal(insuranceBet);
        }

        if (dealerHand.hasBlackjack()) return calculateNetProfit(insuranceBet * 2);

        playHand(0);

        playDealerHand();

        playMethod.gameStatusMethod(this); // endast för spelmetoder som vill ha någon slags visuell output

        return calculateNetProfit(insuranceBet * -1);
    }

    private void playHand(int handIndex) {

        String action = "";
        int allowedActions = 2;
        if (hands[handIndex].getCards().get(0).getValue() == hands[handIndex].getCards().get(1).getValue() && nextEmptyHand != 4)
            allowedActions = 3;

        while (hands[handIndex].getTotal() < 21 && !action.equals("s")) {
            currentHandIndex = handIndex;
            action = playMethod.actionMethod(this, allowedActions, handIndex);
            //double
            if (action.equals("d")) {
                hands[handIndex].setBet(hands[handIndex].getBet() * 2);
                hands[handIndex].addCard(deck.deal(playMethod));
                break;
            }
            //hit
            if (action.equals("h")) {
                allowedActions = 1;
                hands[handIndex].addCard(deck.deal(playMethod));
            }
            //split
            if (action.equals("sp")) {
                hands[nextEmptyHand].addCard(hands[handIndex].pollCard()); // tar ett kort från ena handen till andra
                hands[handIndex].setTotal(hands[handIndex].getCards().getFirst().getValue()); // ser till att totalen stämmer

                hands[handIndex].addCard(deck.deal(playMethod));
                hands[nextEmptyHand].addCard(deck.deal(playMethod));

                hands[nextEmptyHand].setBet(hands[handIndex].getBet());

                nextEmptyHand++;
                playHand(nextEmptyHand - 1);
                playHand(handIndex);
                break;
            }
        }
    }

    private void playDealerHand() {
        if (allHandsHaveBlackjackOrBusted()) return;
        while (dealerHand.getTotal() < 17) {
            dealerHand.addCard(deck.deal(playMethod));
            playMethod.gameStatusMethod(this); // endast för spelmetoder som vill ha någon slags visuell output
        }
    }

    private int calculateNetProfit(int insuranceBetWinnings) {
        int totalBetAmount = 0;
        int totalWinnings = 0;

        for (int i = 0; i < 4; i++) {
            if (hands[i].getBet() == 0) continue;
            totalBetAmount += hands[i].getBet();

            if(hands[i].getTotal() > 21) continue;

            if (hands[i].hasBlackjack() && !dealerHand.hasBlackjack()) {
                totalWinnings += (int) (hands[i].getBet() * 2.5);
            }
            else if (hands[i].getTotal() == dealerHand.getTotal()) totalWinnings += hands[i].getBet();
            else if (hands[i].getTotal() > dealerHand.getTotal() || dealerHand.getTotal() > 21) totalWinnings += hands[i].getBet() * 2;
        }
        endOfRoundProfit = totalWinnings + insuranceBetWinnings - totalBetAmount;
        game.addBetTotal(totalBetAmount);
        playMethod.gameStatusMethod(this); // endast för spelmetoder som vill ha någon slags visuell output
        return endOfRoundProfit;
    }

    private boolean allHandsHaveBlackjackOrBusted() {
        for (int i = 0; i < 4; i++) {
            if (!hands[i].hasBlackjack() && hands[i].getTotal() != 0 && hands[i].getTotal() <= 21) return false;
        }
        return true;
    }

    public void reset() {
        for (int i = 0; i < 4; i++) {
            hands[i].clear();
        }
        dealerHand.clear();
        nextEmptyHand = 1;
        totalRoundBet = 0;
    }
}
