package BlackjackMedKlasser.playMethods;

import BlackjackMedKlasser.*;
import BlackjackMedKlasser.playMethods.SemiOptimalSubclasses.SimulatedRound;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class SemiOptimal extends PlayMethod {
    private final int betSimulationAmount = 1000;
    private final int actionSimulationAmount = 100;
    private final int actionDepthSimulationAmount = 10;

    private Settings settings;
    private Deck predictedGameDeck;
    private Deck betDeck;
    private Deck simulatedDeck;
    private Round simulatedRound;
    private Hand simulatedDealerHand;
    private Hand simulatedPlayerHand;

    public SemiOptimal(Settings settings) {
        this.settings = settings;
        betDeck = new Deck(settings);
        simulatedDeck = new Deck(settings);
        predictedGameDeck = new Deck(settings);
        if (!isSimulated()) simulatedRound = new Round(betDeck, new SimulatedRound(settings), new Game());
        simulatedDealerHand = new Hand();
        simulatedPlayerHand = new Hand();
    }

    public boolean isSimulated() {
        return false;
    }

    //körs när ett kort delas ut från kortleken
    @Override
    public void cardDealtMethod(Card card) {
        predictedGameDeck.getCards().remove(card);
    }

    //körs när kortleken blandas
    @Override
    public void reshuffleMethod() {
        predictedGameDeck.shuffle();
    }

    //körs när en aktion i spelet behöver bestämmas, bör returna "s", "h", "d" eller "sp"
    //allowedActions har värdet 3 ifall alla är tillåtna, 2 ifall "h, s or d" är tillåtet och 1 om bara "h or s" är tillåtet
    //handIndex är den hand som just nu spelas, bör användas i till exempel: round.getHands()[handIndex].getTotal()
    @Override
    public String actionMethod(Round round, int allowedActions, int handIndex) {
        String obviousAction = checkIfActionIsObvious(round.getHands()[handIndex].getTotal(), allowedActions);
        if (!obviousAction.equals("none")) {
            return obviousAction;
        }

        double[] winnings = tryActions(round, allowedActions, actionSimulationAmount, new LinkedList<>(round.getHands()[handIndex].getCards()), predictedGameDeck);

        String[] actions = new String[]{"s", "h", "d", "sp"};

        System.out.println(Arrays.toString(winnings) + " " + round.getHands()[handIndex].getAvailabelAces() + " " + actions[indexWithHighestValue(winnings)] + " p: " + round.getHands()[handIndex].getTotal() + " d: " + round.getDealerCard());

        return actions[indexWithHighestValue(winnings)];
    }

    public double[] tryActions(Round round, int allowedActions, int iterationsPerAction, LinkedList<Card> startingCards, Deck startingDeck) {
        prepareForAction(startingCards, startingDeck);

        String obviousAction = checkIfActionIsObvious(simulatedPlayerHand.getTotal(), allowedActions);

        if (simulatedPlayerHand.getTotal() > 21) return new double[]{-2, -2, -2, -2};

        double[] winnings = new double[4]; //index 0 = stand, 1 = hit, 2 = double, 3 = split

        //stand
        if(obviousAction.equals("s") || obviousAction.equals("none")) {
        for (int i = 0; i < iterationsPerAction; i++) {
            prepareForAction(startingCards, startingDeck);

            winnings[0] += playSimulatedDealerHand(round, 1);
        }
    }
        winnings[0] /= iterationsPerAction;
        if (obviousAction.equals("s")) {
            winnings[1] = -100000000;
            winnings[2] = -100000000;
            winnings[3] = -100000000;
            return winnings;
        }
        //hit
        for (int i = 0; i < iterationsPerAction; i++) {
            prepareForAction(startingCards, startingDeck);

            simulatedPlayerHand.addCard(simulatedDeck.deal(this, false));
            Deck newPredictedDeck = new Deck(settings);
            newPredictedDeck.setCards(simulatedDeck.getCards());
            double[] hitWinnings = tryActions(round, 1, actionDepthSimulationAmount, new LinkedList<>(simulatedPlayerHand.getCards()), newPredictedDeck);
            winnings[1] += hitWinnings[indexWithHighestValue(hitWinnings)];
        }
        winnings[1] /= iterationsPerAction;

        if (allowedActions == 1 || obviousAction.equals("h")) {
            winnings[2] = -100000000;
            winnings[3] = -100000000;
            return winnings;
        }
        //double
        for (int i = 0; i < iterationsPerAction; i++) {
            prepareForAction(startingCards, startingDeck);

            simulatedPlayerHand.addCard(simulatedDeck.deal(this, false));

            winnings[2] += playSimulatedDealerHand(round, 2);
        }
        winnings[2] /= iterationsPerAction;

        if (allowedActions == 2 || obviousAction.equals("d")) {
            winnings[3] = -100000000;
            return winnings;
        }
        //split
        for (int i = 0; i < iterationsPerAction; i++) {
            prepareForAction(startingCards, startingDeck);

            simulatedPlayerHand.pollCard();
            simulatedPlayerHand.setTotal(simulatedPlayerHand.getCards().getFirst().getValue());
            simulatedPlayerHand.addCard(simulatedDeck.deal(this, false));

            Card secondCard = simulatedDeck.deal(this, false);

            Deck newPredictedDeck = new Deck(settings);
            newPredictedDeck.setCards(simulatedDeck.getCards());
            double[] splitWinnings = tryActions(round, 2, actionDepthSimulationAmount, new LinkedList<>(simulatedPlayerHand.getCards()), newPredictedDeck);

            /*
            System.out.println();
            for (Card card : simulatedPlayerHand.getCards()) {
                System.out.print(card.getValue() + " ");
            }
            System.out.println(Arrays.toString(splitWinnings));

             */

            winnings[3] += splitWinnings[indexWithHighestValue(splitWinnings)];

            prepareForAction(startingCards, startingDeck);
            simulatedPlayerHand.pollCard();
            simulatedPlayerHand.setTotal(simulatedPlayerHand.getCards().getFirst().getValue());
            simulatedPlayerHand.addCard(secondCard);
            /*
            System.out.println();
            for (Card card : simulatedPlayerHand.getCards()) {
                System.out.print(card.getValue() + " ");
            }
            System.out.println(Arrays.toString(splitWinnings));

             */

            splitWinnings = tryActions(round, 2, actionDepthSimulationAmount, new LinkedList<>(simulatedPlayerHand.getCards()), newPredictedDeck);
            winnings[3] += splitWinnings[indexWithHighestValue(splitWinnings)];
        }
        winnings[3] /= iterationsPerAction;
        return winnings;
    }

    public int indexWithHighestValue(double[] array) {
        int highestIndex = 0;

        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[highestIndex]) highestIndex = i;
        }

        return highestIndex;
    }

    public String checkIfActionIsObvious(int total, int allowedActions) {
        if (allowedActions != 3 && total > 17) return "s";
        if (allowedActions == 1 && total <= 11) return "h";
        if (total == 20) return "s";

        return "none";
    }

    public int playSimulatedDealerHand(Round round, int betMultiplier) {
        //plays the dealer hand and then returns winnings for given hand
        simulatedDealerHand.clear();
        simulatedDealerHand.addCard(round.getDealerHand().getCards().getFirst());

        simulatedDealerHand.addCard(simulatedDeck.deal(this, false));

        //dealer blackjack or hand busted
        if (simulatedDealerHand.getTotal() == 21 || simulatedPlayerHand.getTotal() > 21) return -2 * betMultiplier;
        //player blackjack
        if (simulatedPlayerHand.getTotal() == 21 && simulatedPlayerHand.getCards().size() == 2)
            return 3 * betMultiplier;

        //deal dealer cards
        while (simulatedDealerHand.getTotal() < 17) {
            simulatedDealerHand.addCard(simulatedDeck.deal(this, false));
        }
        //if dealer busted or player has higher total
        if (simulatedDealerHand.getTotal() > 21 || simulatedDealerHand.getTotal() < simulatedPlayerHand.getTotal())
            return 2 * betMultiplier;
        //tie
        if (simulatedDealerHand.getTotal() == simulatedPlayerHand.getTotal()) return 0;
        return -2 * betMultiplier;
    }

    public void prepareForAction(LinkedList<Card> startingCards, Deck startingDeck) {
        //set starting cards
        simulatedPlayerHand.clear();
        for (Card card : startingCards) {
            simulatedPlayerHand.addCard(card);
        }
        //reset simulated deck and reshuffle the containing cards
        simulatedDeck.setCards(startingDeck.getCards());
        Collections.shuffle(simulatedDeck.getCards());
    }

    //körs när det ursprungliga bettet ska bestämmas
    @Override
    public int betMethod(Round round) {
        return settings.getMinBet();
        /*
        int simulatedWinnings = 0;
        for (int i = 0; i < betSimulationAmount; i++) {
            betDeck.setCards(predictedGameDeck.getCards());
            betDeck.shuffle();
            simulatedWinnings += simulatedRound.playRound();
            simulatedRound.reset();
        }
        if (simulatedWinnings > 0) return settings.getMaxBet();
        return settings.getMinBet();

         */
    }

    //körs ifall möjlighet för ett insurance bet finns (dvs. ifall dealern har ett ess)
    @Override
    public int insuranceBetMethod(Round round) {
        int cardsWithValue10 = 0;
        for (Card card : predictedGameDeck.getCards()) {
            if (card.getValue() == 10) cardsWithValue10++;
        }
        if ((double) (cardsWithValue10) / predictedGameDeck.getSizeOfDeck() > 0.5) return settings.getMaxBet();
        return 0;
    }

}
