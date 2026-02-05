package BlackjackMedKlasser.playMethods;

import BlackjackMedKlasser.*;
import BlackjackMedKlasser.playMethods.SemiOptimalSubclasses.*;

public class SemiOptimal extends PlayMethod {
    private final int betSimulationAmount = 100;
    private final int actionSimulationAmount = 100;
    private final int actionDepthSimulationAmount = 10;

    private final Settings settings;
    private final Deck predictedGameDeck;
    private Deck betDeck;
    private final SODeck simulatedDeck;
    private Round simulatedRound;
    private final SOHand simulatedDealerHand;
    private final SOHand simulatedPlayerHand;
    private HandSaveState dealerhss;

    public SemiOptimal(Settings settings) {
        this.settings = settings;
        betDeck = new Deck(settings);
        predictedGameDeck = new Deck(settings);
        if (!isSimulated()) simulatedRound = new Round(betDeck, new SimulatedRound(settings), new Game());
        simulatedDealerHand = new SOHand();
        simulatedPlayerHand = new SOHand();
        simulatedDeck = new SODeck();
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
        String obviousAction = checkIfActionIsObvious(round.getHands()[handIndex].getTotal(), allowedActions, round.getHands()[handIndex].getAvailabelAces());
        if (!obviousAction.equals("none")) {
            return obviousAction;
        }
            if(allowedActions == 3 && round.getHands()[handIndex].getCards().getFirst().getValue() <= 6 && round.getDealerCard() >= 8) allowedActions = 2;
        dealerhss = new HandSaveState(1, round.getDealerCard(), round.getDealerCard(), round.getDealerHand().getAvailabelAces());
        HandSaveState hss = new HandSaveState(round.getHands()[handIndex].getCards().size(), round.getHands()[handIndex].getTotal(), round.getHands()[handIndex].getCards().getFirst().getValue(), round.getHands()[handIndex].getAvailabelAces());
        DeckSaveState dss = new DeckSaveState(predictedGameDeck.getCards());
        double[] winnings = tryActions(allowedActions, actionSimulationAmount, hss, dss);

        String[] actions = new String[]{"s", "h", "d", "sp"};

        //System.out.println(Arrays.toString(winnings) + " " + round.getHands()[handIndex].getAvailabelAces() + " " + actions[indexWithHighestValue(winnings)] + " p: " + round.getHands()[handIndex].getTotal() + " d: " + round.getDealerCard());

        return actions[indexWithHighestValue(winnings)];
    }

    public double[] tryActions(int allowedActions, int iterationsPerAction, HandSaveState hss, DeckSaveState dss) {
        simulatedDeck.setState(dss);
        simulatedPlayerHand.setState(hss);

        if (simulatedPlayerHand.getTotal() > 21) return new double[]{-2, -2, -2, -2};

        String obviousAction = checkIfActionIsObvious(simulatedPlayerHand.getTotal(), allowedActions, simulatedPlayerHand.getAvailabelAces());

        double[] winnings = new double[4]; //index 0 = stand, 1 = hit, 2 = double, 3 = split

        //stand
        if (obviousAction.equals("s") || obviousAction.equals("none")) {
            for (int i = 0; i < iterationsPerAction; i++) {
                simulatedDeck.setState(dss);
                winnings[0] += playSimulatedDealerHandTest(1);
            }
        winnings[0] /= iterationsPerAction;
        } else winnings[0] = -100000000;
        if (obviousAction.equals("s")) {
            winnings[1] = -100000000;
            winnings[2] = -100000000;
            winnings[3] = -100000000;
            return winnings;
        }
        //hit
        if (obviousAction.equals("h") || obviousAction.equals("none")) {
            for (int i = 0; i < iterationsPerAction; i++) {
                simulatedDeck.setState(dss);
                simulatedPlayerHand.setState(hss);

                simulatedPlayerHand.addCard(simulatedDeck.deal());
                DeckSaveState newPredictedDeck = simulatedDeck.saveState();
                HandSaveState newHand = simulatedPlayerHand.saveState();
                double[] hitWinnings = tryActions(1, actionDepthSimulationAmount, newHand, newPredictedDeck);
                winnings[1] += hitWinnings[indexWithHighestValue(hitWinnings)];
            }
            winnings[1] /= iterationsPerAction;
        } else winnings[1] = -100000000;
        if (allowedActions == 1 || obviousAction.equals("h")) {
            winnings[2] = -100000000;
            winnings[3] = -100000000;
            return winnings;
        }
        //double
        if (obviousAction.equals("d") || obviousAction.equals("none")) {
            for (int i = 0; i < iterationsPerAction; i++) {
                simulatedDeck.setState(dss);
                simulatedPlayerHand.setState(hss);

                simulatedPlayerHand.addCard(simulatedDeck.deal());

                winnings[2] += playSimulatedDealerHandTest(2);
            }
            winnings[2] /= iterationsPerAction;
        } else winnings[2] = -100000000;
        if (allowedActions == 2 || obviousAction.equals("d")) {
            winnings[3] = -100000000;
            return winnings;
        }
        //split
        for (int i = 0; i < iterationsPerAction; i++) {
            simulatedDeck.setState(dss);
            simulatedPlayerHand.setState(hss);

            HandSaveState beginningState = new HandSaveState(1, simulatedPlayerHand.getFirstCardValue(), simulatedPlayerHand.getFirstCardValue(), simulatedPlayerHand.getAvailabelAces());

            simulatedPlayerHand.addCard(simulatedDeck.deal());

            int secondCard = simulatedDeck.deal();

            DeckSaveState newdss = simulatedDeck.saveState();

            double[] splitWinnings = tryActions(2, actionDepthSimulationAmount, simulatedPlayerHand.saveState(), newdss);

            winnings[3] += splitWinnings[indexWithHighestValue(splitWinnings)];

            simulatedPlayerHand.setState(beginningState);
            simulatedPlayerHand.addCard(secondCard);

            splitWinnings = tryActions(2, actionDepthSimulationAmount, simulatedPlayerHand.saveState(), newdss);
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

    public String checkIfActionIsObvious(int total, int allowedActions, int availableAces) {
        if (total == 20) return "s";
        if (allowedActions != 3) {
            if (total > 17 && availableAces != 1) return "s";
            if (total < 8) return "h";
        }
        if (allowedActions == 1 && total <= 11) return "h";

        return "none";
    }

    public int playSimulatedDealerHandTest(int betMultiplier) {
        //plays the dealer hand and then returns winnings for given hand
        simulatedDealerHand.setState(dealerhss);

        simulatedDealerHand.addCard(simulatedDeck.deal());

        //dealer blackjack or hand busted
        if (simulatedDealerHand.getTotal() == 21 || simulatedPlayerHand.getTotal() > 21)
            return -2 * betMultiplier;
        //player blackjack
        if (simulatedPlayerHand.hasBlackjack())
            return 3 * betMultiplier;

        //deal dealer cards
        while (simulatedDealerHand.getTotal() < 17) {
            simulatedDealerHand.addCard(simulatedDeck.deal());
        }
        //if dealer busted or player has higher total
        if (simulatedDealerHand.getTotal() > 21 || simulatedDealerHand.getTotal() < simulatedPlayerHand.getTotal())
            return 2 * betMultiplier;
        //tie
        if (simulatedDealerHand.getTotal() == simulatedPlayerHand.getTotal()) return 0;
        return -2 * betMultiplier;
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
