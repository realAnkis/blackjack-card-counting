
import java.awt.*;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class Blackjack {

    static ArrayList<Integer> deck = new ArrayList<>();
    //arrayposition 0 är Player, 1 är Dealer och 2 är Split.
    static int[] total = new int[3];
    static int[] hasAce = new int[3];
    static Consumer<Integer[]> externalDealCardMethod;

    public static int dealCard(int dealingTo) {
        int valdIndex = (int) (deck.size() * Math.random());
        int valtKort = deck.get(valdIndex);
        deck.remove(valdIndex);

        externalDealCardMethod.accept(new Integer[]{valtKort,dealingTo});

        return valtKort;
    }

    public static int deal(int dealingTo) {
        int card = dealCard(dealingTo);
        if (card / 10 == 1) hasAce[dealingTo]++;
        total[dealingTo] += cardValue(card);

        if (total[dealingTo] > 21 && hasAce[dealingTo] != 0) {
            total[dealingTo] -= 10;
            hasAce[dealingTo]--;
        }
        return cardValue(card);
    }

    public static int cardValue(int card) {
        if (card / 10 == 1) {
            return 11;
        } else return Math.min(card / 10, 10);
    }

    public static void shuffleDeck(int deckAmount) {
        deck.clear();
        //k loopar för varje deck, i loopar för kortnummer och j loopar för färg
        for (int k = 0; k < deckAmount; k++) {
            for (int j = 0; j < 4; j++) {
                for (int i = 0; i < 13; i++) {
                    deck.add(52 * k + 13 * j + i, (i + 1) * 10 + j);
                }
            }
        }
        // I listan deck finns alla kort med sit värde i början och ettans positon visar dess färg (0 = hjäter, 1 = klöver, 2 = ruter, 3 = spader)
        // för värdet ta kortets värde delat med 10
        // för färg ta % 10
    }

    public static void print(int allowedActions) {
        System.out.println("\nDealer has: " + total[1]);
        System.out.println("Player has: " + total[0]);
        if (total[2] > 0) System.out.println("Split has: " + total[2]);

        if (allowedActions == 1) System.out.println("hit or stand (h/s)");
        else if (allowedActions == 2) System.out.println("hit, stand or double (h/s/d)");
        else if (allowedActions == 3) System.out.println("hit, stand, double or split (h/s/d/sp)");
    }

    public static void main(String[] args, BiFunction<int[], ArrayList<Integer>, String> actionMethod, BiFunction<int[], ArrayList<Integer>, Integer> insuranceBetMethod, BiFunction<int[], ArrayList<Integer>, Integer> betMethod, Consumer<Integer[]> dealCardMethod) {

        int pengar = 100;
        int numberOfGames = 1000;
        int[] playerFirstCards = new int[2];
        int allowedActions;
        int bet;
        int splitBet = 0;
        int insuranceBet;
        int numberOfDecks = 2;
        double reshufflePercent = 0.25;
        String action;
        int dealer2ndCard;
        externalDealCardMethod = dealCardMethod;

        //String []färg = {"Hjäter","Clöver","Ruter","Spader"};

        System.out.println("===== Black Jack =====");
        for (int game = 0; game < numberOfGames; game++) {
            insuranceBet = 0;
            allowedActions = 0;
            for (int i = 0; i < 3; i++) {
                total[i] = hasAce[i] = 0;
            }

            if(deck.size() <= reshufflePercent * 52 * numberOfDecks) shuffleDeck(numberOfDecks);

            System.out.println("You have " + pengar + "kr to bet");
            System.out.println("Enter your bet:");
            bet = betMethod.apply(new int[]{},deck);
            while (bet > pengar || bet < 1) {
                System.out.println("You can't bet that, try agian");
                bet = betMethod.apply(new int[]{},deck);
            }
            System.out.println("You have bet " + bet + "kr");
            pengar -= bet;

            //playerFirstCards[0] = dealPlayer();
            playerFirstCards[0] = deal(0);

            //dealDealer();
            deal(1);

            playerFirstCards[1] = deal(0);

            dealer2ndCard = dealCard(1);

            //Om dealerns visade kort är ess
            if (total[1] == 11) {
                print(allowedActions);
                System.out.println("Insurance bet?");
                insuranceBet = Math.min(insuranceBetMethod.apply(new int[0],deck), bet / 2);
                pengar -= insuranceBet;
                System.out.println("You have bet " + insuranceBet + "kr");
            }

            //Om dealer har blackjack
            if (total[1] + cardValue(dealer2ndCard) == 21) {
                if (total[0] == 21) {
                    pengar += bet;
                    System.out.println("Dealer has blackjack, tie!");
                } else System.out.println("Dealer has blackjack!");
                pengar += insuranceBet * 2;
                System.out.println("+" + insuranceBet * 2 + "kr from insurance bet");
                continue;
            }

            //allowedActions kommer vara användbart för card counting algoritmerna senare, då det kan användas som en parameter
            allowedActions = 2;
            if (playerFirstCards[0] == playerFirstCards[1]) allowedActions = 3;
            print(allowedActions);

            if (total[0] == 21) {
                System.out.println("Player has blackjack! +" + (int) (bet * 2.5) + "kr");
                pengar += (int) (bet * 2.5);
                continue;
            }
            
            action = actionMethod.apply(new int[]{total[0],total[1],hasAce[0],hasAce[1],allowedActions,numberOfDecks,playerFirstCards[0]},deck);

            while (total[0] < 21 && !action.equals("s")) {
                //double
                if (action.equals("d")) {
                    pengar -= bet;
                    bet *= 2;
                    deal(0);
                    break;
                }
                //hit
                if (action.equals("h")) {
                    allowedActions = 1;
                    deal(0);
                    if (total[0] > 21) allowedActions = 0;
                }
                //split
                if (action.equals("sp")) {
                    total[0] = total[2] = playerFirstCards[0];
                    if (total[2] == 11) hasAce[2]++;
                    deal(0);
                    deal(2);
                    splitBet = bet;
                    pengar -= splitBet;
                    System.out.println("You bet " + splitBet + "kr");
                }
                print(allowedActions);
                if (total[0] < 21) action = actionMethod.apply(new int[]{total[0],total[1],hasAce[0],hasAce[1],allowedActions,numberOfDecks,playerFirstCards[0]},deck);
            }

            //spelar det splittade spelet
            if (total[2] != 0) {
                allowedActions = 2;
                print(allowedActions);
                action = actionMethod.apply(new int[]{total[2],total[1],hasAce[2],hasAce[1],allowedActions,numberOfDecks,playerFirstCards[0]},deck);
                while (total[2] < 21 && !action.equals("s")) {
                    //double
                    if (action.equals("d")) {
                        pengar -= splitBet;
                        splitBet *= 2;
                        deal(2);
                        break;
                    }
                    //hit
                    if (action.equals("h")) {
                        allowedActions = 1;
                        deal(2);
                        if (total[2] > 21) allowedActions = 0;
                    }
                    print(allowedActions);
                    if (total[2] < 21) action = actionMethod.apply(new int[]{total[2],total[1],hasAce[2],hasAce[1],allowedActions,numberOfDecks,playerFirstCards[0]},deck);
                }
            }

            if (total[0] > 21) System.out.println("Player busted!");
            if (total[2] > 21) System.out.println("Split busted!");
            if ((total[0] > 21 && total[2] > 21) || (total[0] > 21 && total[2] == 0)) continue;

            total[1] += cardValue(dealer2ndCard);
            if(cardValue(dealer2ndCard) == 11) hasAce[1]++;
            if (!(playerFirstCards[0] >= 10 && hasAce[0] != 0 && total[0] == 21 && hasAce[2] != 0 && total[2] == 21))
                while (total[1] < 17) {
                    deal(1);
                }

            allowedActions = 0;
            print(allowedActions);

            if (playerFirstCards[0] >= 10 && hasAce[0] != 0 && total[0] == 21) {
                System.out.println("Player has blackjack! +" + (int) (bet * 2.5) + "kr");
                pengar += (int) (bet * 2.5);
            } else if (total[0] <= 21) {
                if (total[0] > total[1]) {
                    System.out.println("Player wins! +" + bet * 2 + "kr");
                    pengar += bet * 2;
                } else if (total[0] == total[1]) {
                    System.out.println("Tie! +" + bet + "kr");
                    pengar += bet;
                } else if (total[1] > 21) {
                    System.out.println("Dealer Busted! +" + bet * 2 + "kr");
                    pengar += bet * 2;
                } else System.out.println("Dealer wins!");
            }

            if (total[2] == 21 && playerFirstCards[0] >= 10 && hasAce[2] != 0) {
                System.out.println("Split has blackjack! +" + (int) (splitBet * 2.5) + "kr");
                pengar += (int) (splitBet * 2.5);
            } else if (total[2] != 0 && total[2] <= 21) {
                if (total[2] > total[1]) {
                    System.out.println("Split wins! +" + splitBet * 2 + "kr");
                    pengar += splitBet * 2;
                } else if (total[2] == total[1]) {
                    System.out.println("Split Tie! +" + splitBet + "kr");
                    pengar += splitBet;
                } else if (total[1] > 21) {
                    System.out.println("Dealer Busted! +" + bet * 2 + "kr");
                    pengar += bet * 2;
                } else System.out.println("Dealer wins over split!");
            }
        }
    }
}