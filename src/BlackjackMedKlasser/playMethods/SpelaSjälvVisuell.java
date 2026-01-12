package BlackjackMedKlasser.playMethods;

import BlackjackMedKlasser.Card;
import BlackjackMedKlasser.Round;
import BlackjackMedKlasser.Settings;
import BlackjackMedKlasser.visuals.*;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

public class SpelaSjälvVisuell extends PlayMethod {
    private Settings settings;
    private Table table;
    private Round round;
    private DisplayedCard dealer1stCard;
    private DisplayedCard dealer2ndCard;
    private Timer t = new Timer();

    public SpelaSjälvVisuell(Settings settings) {
        this.settings = settings;
        Frame frame = new Frame();
        table = frame.getTable();
    }

    Scanner scanner = new Scanner(System.in);

    //körs när ett kort delas ut från kortleken
    @Override
    public void cardDealtMethod(Card card) {
    }

    //körs när en aktion i spelet behöver bestämmas, bör returna "s", "h", "d" eller "sp"
    //allowedActions har värdet 3 ifall alla är tillåtna, 2 ifall "h, s or d" är tillåtet och 1 om bara "h or s" är tillåtet
    //handIndex är den hand som just nu spelas, bör användas i till exempel: round.getHands()[handIndex]
    @Override
    public String actionMethod(Round round, int allowedActions, int handIndex) {
        updateDisplayedCards(round);

        System.out.println("\nDealer total: " + round.getDealerHand().getCards().getFirst().getValue());
        System.out.println("Your total: " + round.getHands()[handIndex].getTotal());
        if (allowedActions == 3) System.out.println("h, s, d or sp");
        else if (allowedActions == 2) System.out.println("h, s or d");
        else System.out.println("h or s");
        String action = scanner.nextLine();
        if (action.equals("sp")) {
            table.getCardsInGroup(handIndex)[1].moveSmooth(table.getCardGroupCentersX()[getNextEmptyHand(round)],0.75,50,0);
            table.getCardsInGroup(handIndex)[1].setCardGroup(getNextEmptyHand(round));
            for (int i = handIndex; i >= 0; i--) {
                table.moveGroup(i,0.1,0,50,table.numberOfCardsInGroup(i) * 5,-5);
            }
        }
        return action;
    }

    //körs när det ursprungliga bettet ska bestämmas
    @Override
    public int betMethod(Round round) {
        TimerTask clearTable = new TimerTask() {
            @Override
            public void run() {
                table.clearTable(80,80);
            }
        };
        if(!table.getCards().isEmpty()) t.schedule(clearTable,100);
        this.round = round;
        System.out.println("Enter your bet:");
        int bet = scanner.nextInt();
        scanner.nextLine();
        System.out.println("You have bet " + bet + "kr");
        return bet;
    }

    //körs ifall möjlighet för ett insurance bet finns (dvs. ifall dealern har ett ess)
    @Override
    public int insuranceBetMethod(Round round) {
        System.out.println("\nDealer total: " + round.getDealerHand().getCards().getFirst().getValue());
        System.out.println("Your total: " + round.getHands()[0].getTotal());
        System.out.println("Insurance bet?");
        return betMethod(round);
    }


    //körs ibland när info skulle kunna tänkas ges till en spelare (ej relevant ifall inte en riktig människa ska spela)
    @Override
    public void gameStatusMethod(Round round) {
        System.out.println("\nDealer total: " + round.getDealerHand().getTotal());
        for (int i = 0; i < 4; i++) {
            if (round.getHands()[i].getTotal() == 0) break;
            System.out.println("Hand " + (i + 1) + " total: " + round.getHands()[i].getTotal());
        }

        if (table.numberOfCardsInGroup(4) == 2) {
            dealer1stCard.moveSmooth(0.49, 0.25, 30, 0);
            dealer2ndCard.moveSmooth(0.51, 0.25, 30, 10);
            dealer2ndCard.flip(20, 15);
        }
        int dealerCardAmount = round.getDealerHand().getCards().size();
        if (dealerCardAmount > table.numberOfCardsInGroup(4)) {
            DisplayedCard dealerCard = new DisplayedCard(table.getDeckPos()[0], table.getDeckPos()[1], round.getDealerHand().getCards().getLast().getSuit(), round.getDealerHand().getCards().getLast().getActualValue(), 4,table);
            table.addCard(dealerCard);
            dealerCard.moveSmooth(0.47 + (0.02 * dealerCardAmount), 0.25, 40, (dealerCardAmount - 2) * 50);
            dealerCard.flip(30, (dealerCardAmount - 2) * 50 + 10);
        }

        if (round.getEndOfRoundProfit() != 0) {
            int profit = round.getEndOfRoundProfit();
            if (profit > 0) System.out.println("+" + profit + "kr");
            else System.out.println(profit + "kr");
        }
    }

    private int getNextEmptyHand(Round round) {
        for (int i = 0; i < 4; i++) {
            if(round.getHands()[i].getCards().isEmpty()) return i;
        }
        return -1;
    }

    private void updateDisplayedCards(Round round) {
        if (table.getCards().isEmpty()) {
            dealer1stCard = new DisplayedCard(table.getDeckPos()[0], table.getDeckPos()[1], round.getDealerHand().getCards().getFirst().getSuit(), round.getDealerHand().getCards().getFirst().getActualValue(), 4,table);
            table.addCard(dealer1stCard);
            dealer1stCard.moveSmooth(0.45, 0.25, 40, 20);
            dealer1stCard.flip(40, 100);

            dealer2ndCard = new DisplayedCard(table.getDeckPos()[0], table.getDeckPos()[1], round.getDealerHand().getCards().get(1).getSuit(), round.getDealerHand().getCards().get(1).getActualValue(), 4,table);
            table.addCard(dealer2ndCard);
            dealer2ndCard.moveSmooth(0.55, 0.25, 40, 60);
        }

        for (int i = 0; i < 4; i++) {
            int cardAmount = round.getHands()[i].getCards().size();
            if (cardAmount > table.numberOfCardsInGroup(i)) {
                DisplayedCard card = new DisplayedCard(table.getDeckPos()[0], table.getDeckPos()[1], round.getDealerHand().getCards().getLast().getSuit(), round.getHands()[i].getCards().get(table.numberOfCardsInGroup(i)).getActualValue(), i,table);
                table.addCard(card);
                card.moveSmooth(table.getCardGroupCentersX()[i] + (0.02 * (table.numberOfCardsInGroup(i) - 1)), 0.75, 40, 0);
                card.flip(30, 10);
            }
            if (cardAmount > table.numberOfCardsInGroup(i)) {
                DisplayedCard card = new DisplayedCard(table.getDeckPos()[0], table.getDeckPos()[1], round.getDealerHand().getCards().getLast().getSuit(), round.getHands()[i].getCards().get(table.numberOfCardsInGroup(i)).getActualValue(), i,table);
                table.addCard(card);
                card.moveSmooth(table.getCardGroupCentersX()[i] + (0.02 * (table.numberOfCardsInGroup(i) - 1)), 0.75, 40, 40);
                card.flip(30, 50);
            }
        }
    }
}