
import java.util.*;
import java.util.function.BiFunction;

public class Blackjack {

    static ArrayList<Integer> deck = new ArrayList<>();
    static int finnsEssFörPlayer = 0;
    static int finnsEssFörDealer = 0;
    static int finnsEssFörSplit = 0;
    static int dealerTotal = 0;
    static int playerTotal = 0;
    static int splitTotal = 0;

    public static int dealCard() {
        int valdIndex = (int) (deck.size() * Math.random());
        int valtKort = deck.get(valdIndex);
        deck.remove(valdIndex);

        //här kan valfri mänsklig korträkningsmetod köras med valtKort som parameter

        return valtKort;
    }

    public static int dealPlayer() {
        int card = dealCard();
        if (card / 10 == 1) finnsEssFörPlayer++;
        playerTotal += cardValue(card);

        if (playerTotal > 21 && finnsEssFörPlayer != 0) {
            playerTotal -= 10;
            finnsEssFörPlayer--;
        }
        return cardValue(card);
    }

    public static void dealSplit() {
        int card = dealCard();
        if (card / 10 == 1) finnsEssFörSplit++;
        splitTotal += cardValue(card);

        if (splitTotal > 21 && finnsEssFörSplit != 0) {
            splitTotal -= 10;
            finnsEssFörSplit--;
        }
    }

    public static void dealDealer() {
        int card = dealCard();
        if (card / 10 == 1) finnsEssFörDealer++;
        dealerTotal += cardValue(card);

        if (dealerTotal > 21 && finnsEssFörDealer != 0) {
            dealerTotal -= 10;
            finnsEssFörDealer--;
        }
    }

    public static int cardValue(int card) {
        if (card / 10 == 1) {
            return 11;
        } else return Math.min(card / 10, 10);
    }

    public static void print(int allowedActions) {
        System.out.println("\nDealer has: " + dealerTotal);
        System.out.println("Player has: " + playerTotal);
        if (splitTotal > 0) System.out.println("Split has: " + splitTotal);

        if (allowedActions == 1) System.out.println("hit or stand (h/s)");
        else if (allowedActions == 2) System.out.println("hit, stand or double (h/s/d)");
        else if (allowedActions == 3) System.out.println("hit, stand, double or split (h/s/d/sp)");
    }

    public static void main(String[] args, BiFunction<int[], ArrayList<Integer>, String> actionMethod, BiFunction<int[], ArrayList<Integer>, Integer> betMethod) {

        int pengar = 100;
        int[] playerFirstCards = new int[2];
        int allowedActions;
        int antalDeck = 2;
        int bet;
        int splitBet = 0;
        int insuranceBet;
        Scanner scanner = new Scanner(System.in);
        String action = "";
        int dealer2ndCard;

        //String []färg = {"Hjäter","Clöver","Ruter","Spader"};

        //k loopar för varje deck, i loopar för kortnummer och j loopar för färg
        for (int k = 0; k < antalDeck; k++) {
            for (int j = 0; j < 4; j++) {
                for (int i = 0; i < 13; i++) {
                    deck.add(52 * k + 13 * j + i, (i + 1) * 10 + j);
                }
            }
        }

        // I listan deck finns alla kort med sit värde i början och ettans positon visar des färg (0=hjäter, 1=clöver, 2=ruter, 3= spader),
        // för värdet ta kortets värde delat med 10
        // för färg ta % 10
        System.out.println("===== Black Jack =====");
        while (!action.equals("stopp")) {
            insuranceBet = 0;
            allowedActions = 0;
            splitTotal = 0;
            finnsEssFörSplit = 0;
            finnsEssFörDealer = 0;
            finnsEssFörPlayer = 0;
            playerTotal = 0;
            dealerTotal = 0;

            System.out.println("You have " + pengar + "kr to bet");
            System.out.println("Enter your bet:");
            bet = betMethod.apply(new int[]{},deck);
            while (bet > pengar || bet < 1) {
                System.out.println("You can't bet that, try agian");
                bet = betMethod.apply(new int[]{},deck);
            }
            System.out.println("You have bet " + bet + "kr");
            pengar -= bet;

            playerFirstCards[0] = dealPlayer();

            dealDealer();

            playerFirstCards[1] = dealPlayer();

            dealer2ndCard = dealCard();

            //Om dealerns visade kort är ess
            if (dealerTotal == 11) {
                print(allowedActions);
                System.out.println("Insurance bet?");
                insuranceBet = Math.min(betMethod.apply(new int[]{},deck), bet / 2);
                pengar -= insuranceBet;
                System.out.println("You have bet " + insuranceBet + "kr");
            }

            //Om dealer har blackjack
            if (dealerTotal + cardValue(dealer2ndCard) == 21) {
                if (playerTotal == 21) {
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

            if (playerTotal == 21) {
                System.out.println("Player has blackjack! +" + (int) (bet * 2.5) + "kr");
                pengar += (int) (bet * 2.5);
                continue;
            }
            
            action = actionMethod.apply(new int[]{},deck);

            while (playerTotal < 21 && !action.equals("s")) {
                //double
                if (action.equals("d")) {
                    pengar -= bet;
                    bet *= 2;
                    dealPlayer();
                    break;
                }
                //hit
                if (action.equals("h")) {
                    allowedActions = 1;
                    dealPlayer();
                    if (playerTotal > 21) allowedActions = 0;
                }
                //split
                if (action.equals("sp")) {
                    playerTotal = splitTotal = playerFirstCards[0];
                    if (splitTotal == 11) finnsEssFörSplit++;
                    dealPlayer();
                    dealSplit();
                    splitBet = bet;
                    pengar -= splitBet;
                    System.out.println("You bet " + splitBet + "kr");
                }
                print(allowedActions);
                if (playerTotal < 21) action = actionMethod.apply(new int[]{},deck);
            }

            //spelar det splittade spelet
            if (splitTotal != 0) {
                allowedActions = 2;
                print(allowedActions);
                action = actionMethod.apply(new int[]{},deck);
                while (splitTotal < 21 && !action.equals("s")) {
                    //double
                    if (action.equals("d")) {
                        pengar -= splitBet;
                        splitBet *= 2;
                        dealSplit();
                        break;
                    }
                    //hit
                    if (action.equals("h")) {
                        allowedActions = 1;
                        dealSplit();
                        if (splitTotal > 21) allowedActions = 0;
                    }
                    print(allowedActions);
                    if (splitTotal < 21) action = actionMethod.apply(new int[]{},deck);
                }
            }

            if (playerTotal > 21) System.out.println("Player busted!");
            if (splitTotal > 21) System.out.println("Split busted!");
            if (playerTotal > 21 && splitTotal > 21) continue;

            dealerTotal += cardValue(dealer2ndCard);
            if (!(playerFirstCards[0] >= 10 && finnsEssFörPlayer != 0 && playerTotal == 21 && finnsEssFörSplit != 0 && splitTotal == 21))
                while (dealerTotal < 17) {
                    dealDealer();
                }

            allowedActions = 0;
            print(allowedActions);

            if (playerFirstCards[0] >= 10 && finnsEssFörPlayer != 0 && playerTotal == 21) {
                System.out.println("Player has blackjack! +" + (int) (bet * 2.5) + "kr");
                pengar += (int) (bet * 2.5);
            } else if (playerTotal <= 21) {
                if (playerTotal > dealerTotal) {
                    System.out.println("Player wins! +" + bet * 2 + "kr");
                    pengar += bet * 2;
                } else if (playerTotal == dealerTotal) {
                    System.out.println("Tie! +" + bet + "kr");
                    pengar += bet;
                } else if (dealerTotal > 21) {
                    System.out.println("Dealer Busted! +" + bet * 2 + "kr");
                    pengar += bet * 2;
                } else System.out.println("Dealer wins!");
            }

            if (splitTotal == 21 && playerFirstCards[0] >= 10 && finnsEssFörSplit != 0) {
                System.out.println("Split has blackjack! +" + (int) (splitBet * 2.5) + "kr");
                pengar += (int) (splitBet * 2.5);
            } else if (splitTotal != 0 && splitTotal <= 21) {
                if (splitTotal > dealerTotal) {
                    System.out.println("Split wins! +" + splitBet * 2 + "kr");
                    pengar += splitBet * 2;
                } else if (splitTotal == dealerTotal) {
                    System.out.println("Split Tie! +" + splitBet + "kr");
                    pengar += splitBet;
                } else if (dealerTotal > 21) {
                    System.out.println("Dealer Busted! +" + bet * 2 + "kr");
                    pengar += bet * 2;
                } else System.out.println("Dealer wins over split!");
            }
        }
    }
}