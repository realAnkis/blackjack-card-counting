import java.awt.*;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class Blackjack {

    static ArrayList<Integer> deck = new ArrayList<>();
    //arrayposition 0 är Player, 1-3 är Split och 4 är Dealer.
    static int[] total = new int[5];
    static int[] hasAce = new int[5];
    static int[][] playerFirstCards = new int[4][2];
    static int[] bet = new int[4];
    static int nextEmptyHand;
    static String action = "";
    static Consumer<Integer[]> externalDealCardMethod;

    private static int dealCard(int dealingTo) {
        int valdIndex = (int) (deck.size() * Math.random());
        int valtKort = deck.get(valdIndex);
        deck.remove(valdIndex);

        externalDealCardMethod.accept(new Integer[]{valtKort, dealingTo});

        return valtKort;
    }

    private static int deal(int dealingTo) {
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

    private static void shuffleDeck(int deckAmount) {
        deck.clear();
        //k loopar för varje deck, i loopar för kortnummer och j loopar för färg
        for (int k = 0; k < deckAmount; k++) {
            for (int j = 0; j < 4; j++) {
                for (int i = 0; i < 13; i++) {
                    deck.add((i + 1) * 10 + j);
                }
            }
        }
        // I listan deck finns alla kort med sit värde i början och ettans positon visar dess färg (0 = hjäter, 1 = klöver, 2 = ruter, 3 = spader)
        // för värdet ta kortets värde delat med 10
        // för färg ta % 10
    }

    private static void print(int allowedActions, int currentHand) {
        System.out.println("\nDealer has: " + total[4]);

        if(allowedActions == 0) {
            for (int i = 0; i < 4; i++) {
                if (total[i] == 0) break;
                System.out.println("Hand " + (i+1) + " has: " + total[i]);
            }
        } else System.out.println("Player has: " + total[currentHand]);

        if (allowedActions == 1) System.out.println("hit or stand (h/s)");
        else if (allowedActions == 2) System.out.println("hit, stand or double (h/s/d)");
        else if (allowedActions == 3) System.out.println("hit, stand, double or split (h/s/d/sp)");
    }

    private static boolean allHandsHaveBlackjack() {
        int total;
        for (int i = 0; i < 4; i++) {
            total = playerFirstCards[i][0] + playerFirstCards[i][1];
            if (total == 0) return true;
            if (total != 21) return false;
        }
        return true;
    }

    private static boolean allHandsHaveBusted(boolean printGameStatus) {
        boolean allBusted = true;
        for (int i = 0; i < 4; i++) {
            if (total[i] <= 21) allBusted = false;
            else if (printGameStatus) System.out.println("Hand " + (i + 1) + " busted!");
        }
        return allBusted;
    }

    private static void playHand(int handIndex, boolean printGameStatus, BiFunction<int[], ArrayList<Integer>, String> actionMethod, int numberOfDecks, int dealer2ndCard) {
        int allowedActions = 2;
        if (playerFirstCards[handIndex][0] == playerFirstCards[handIndex][1] && nextEmptyHand != 4) allowedActions = 3;

        action = "";

        while (total[handIndex] < 21 && !action.equals("s")) {
            if (printGameStatus) print(allowedActions, handIndex);
            action = actionMethod.apply(new int[]{total[handIndex], total[4], hasAce[handIndex], hasAce[4], allowedActions, numberOfDecks, playerFirstCards[handIndex][0], dealer2ndCard}, deck);
            //double
            if (action.equals("d")) {
                bet[handIndex] *= 2;
                deal(handIndex);
                break;
            }
            //hit
            if (action.equals("h")) {
                allowedActions = 1;
                deal(handIndex);
                if (total[handIndex] > 20) allowedActions = 0;
            }
            //split
            if (action.equals("sp")) {
                total[handIndex] = total[nextEmptyHand] = playerFirstCards[nextEmptyHand][0] = playerFirstCards[handIndex][0];
                if (total[nextEmptyHand] == 11) hasAce[nextEmptyHand]++;
                playerFirstCards[handIndex][1] = deal(handIndex);
                playerFirstCards[nextEmptyHand][1] = deal(nextEmptyHand);
                bet[nextEmptyHand] = bet[handIndex];
                nextEmptyHand++;
                playHand(nextEmptyHand - 1, printGameStatus, actionMethod, numberOfDecks, dealer2ndCard);
                playHand(handIndex, printGameStatus, actionMethod, numberOfDecks, dealer2ndCard);
                break;
            }
        }
    }

    public static void main(String[] args, boolean printGameStatus, BiFunction<int[], ArrayList<Integer>, String> actionMethod, BiFunction<int[], ArrayList<Integer>, Integer> betMethod, BiFunction<int[], ArrayList<Integer>, Integer> insuranceBetMethod, Consumer<Integer[]> dealCardMethod) {

        int pengar = 0;
        int numberOfGames = 1000000;
        int insuranceBet;
        int numberOfDecks = 2;
        double reshufflePercent = 0.25;
        int dealer2ndCard;
        externalDealCardMethod = dealCardMethod;

        //String []färg = {"Hjäter","Clöver","Ruter","Spader"};

        System.out.println("===== Black Jack =====");
        for (int game = 0; game < numberOfGames; game++) {
            insuranceBet = 0;
            nextEmptyHand = 1;

            for (int i = 0; i < 5; i++) {
                total[i] = hasAce[i] = 0;
                if (i == 4) break;
                playerFirstCards[i][0] = playerFirstCards[i][1] = 0;
            }

            if (deck.size() <= reshufflePercent * 52 * numberOfDecks) shuffleDeck(numberOfDecks);
            bet[0] = betMethod.apply(new int[]{}, deck);

            playerFirstCards[0][0] = deal(0);

            deal(4);

            playerFirstCards[0][1] = deal(0);

            dealer2ndCard = dealCard(4);

            //Om dealerns visade kort är ess
            if (total[4] == 11) {
                if (printGameStatus) print(0, 0);
                insuranceBet = Math.min(insuranceBetMethod.apply(new int[0], deck), bet[0] / 2);
                pengar -= insuranceBet;
            }

            //Om dealer har blackjack
            if (total[4] + cardValue(dealer2ndCard) == 21) {
                if (total[1] == 21) {
                    if (printGameStatus) System.out.println("Dealer has blackjack, tie!");
                } else {
                    if (printGameStatus) System.out.println("Dealer has blackjack!");
                    pengar -= bet[0];
                }
                pengar += insuranceBet * 2;
                if (printGameStatus && insuranceBet != 0) System.out.println("+" + insuranceBet * 2 + "kr from insurance bet");
                continue;
            }

            playHand(0, printGameStatus, actionMethod, numberOfDecks, dealer2ndCard);

            total[4] += cardValue(dealer2ndCard);
            if (cardValue(dealer2ndCard) == 11) hasAce[4]++;

            if (!allHandsHaveBlackjack() || !allHandsHaveBusted(printGameStatus))
                while (total[4] < 17) {
                    deal(4);
                }

            if (printGameStatus) print(0,0);

            for (int i = 0; i < 4; i++) {
                if (playerFirstCards[i][0] == 0) break;

                if (playerFirstCards[i][0] + playerFirstCards[i][1] == 21) {
                    if (printGameStatus)
                        System.out.println("Hand " + (i + 1) + " has blackjack! +" + (int) (bet[i] * 2.5) + "kr");
                    pengar += (int) (bet[i] * 1.5);
                } else if (total[i] <= 21) {
                    if (total[i] > total[4]) {
                        if (printGameStatus) System.out.println("Hand " + (i + 1) + " wins! +" + bet[i] * 2 + "kr");
                        pengar += bet[i];
                    } else if (total[i] == total[4]) {
                        if (printGameStatus) System.out.println("Hand " + (i + 1) + " is a tie! +" + bet[i] + "kr");
                    } else if (total[4] > 21) {
                        if (printGameStatus) System.out.println("Dealer Busted! +" + bet[i] * 2 + "kr");
                        pengar += bet[i];
                    } else {
                        if (printGameStatus) System.out.println("Dealer wins hand " + (i + 1) + "!");
                        pengar -= bet[i];
                    }
                } else pengar -= bet[i];
            }
        }
            System.out.println(pengar);
    }
}