package BlackjackMedKlasser.playMethods;

import BlackjackMedKlasser.*;

import java.util.Scanner;

public class SpelaSjälv extends PlayMethod {
    private Settings settings;

    public SpelaSjälv(Settings settings) {
        this.settings = settings;
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
        System.out.println("\nDealer total: " + round.getDealerHand().getCards().getFirst().getValue());
        System.out.println("Your total: " + round.getHands()[handIndex].getTotal());
        if (allowedActions == 3) System.out.println("h, s, d or sp");
        else if (allowedActions == 2) System.out.println("h, s or d");
        else System.out.println("h or s");
        return scanner.nextLine();
    }

    //körs när det ursprungliga bettet ska bestämmas
    @Override
    public int betMethod(Round round) {
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
            System.out.println("Hand " + (i + 1)  + " total: " + round.getHands()[i].getTotal());
        }
        if(round.getEndOfRoundProfit() != 0) {
            int profit = round.getEndOfRoundProfit();
            if(profit > 0) System.out.println("+" + profit + "kr");
            else System.out.println(profit + "kr");
        }
    }
}
