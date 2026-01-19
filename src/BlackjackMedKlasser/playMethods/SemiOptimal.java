package BlackjackMedKlasser.playMethods;

import BlackjackMedKlasser.*;
import BlackjackMedKlasser.playMethods.SemiOptimalSubclasses.SimulatedRound;

import java.util.Collections;
import java.util.LinkedList;

public class SemiOptimal extends PlayMethod {
    private final int betSimulationAmount = 1000;
    private final int actionSimulationAmount = 100;
    private final int actionDepthSimulationAmount = 1;
    private int looping;

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
        if(!isSimulated()) simulatedRound = new Round(betDeck, new SimulatedRound(settings), new Game());
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
        int[] winnings = tryActions(round, allowedActions, actionSimulationAmount, new LinkedList<>(round.getHands()[handIndex].getCards()));

        String[] actions = new String[]{"s","h","d","sp"};

        return actions[indexWithHighestValue(winnings)];
    }

    public int[] tryActions(Round round, int allowedActions, int iterationsPerAction, LinkedList<Card> startingCards) {
        prepareForAction(startingCards);


        int[] winnings = new int[4]; //index 0 = stand, 1 = hit, 2 = double, 3 = split

        for (int i = 0; i < iterationsPerAction; i++) {
            prepareForAction(startingCards);

            winnings[0] += playSimulatedDealerHand(round, iterationsPerAction);
        }
        for (int i = 0; i < iterationsPerAction; i++) {
            prepareForAction(startingCards);

            simulatedPlayerHand.addCard(simulatedDeck.deal(this, false));
            System.out.println(looping++);
            int[] hitWinnings = tryActions(round, 1, actionDepthSimulationAmount, new LinkedList<>(simulatedPlayerHand.getCards()));

            winnings[1] += iterationsPerAction * hitWinnings[indexWithHighestValue(hitWinnings)] / actionDepthSimulationAmount;
        }
        if(allowedActions == 1) {
            winnings[2] = -100000000;
            winnings[3] = -100000000;
            return winnings;
        }
        for (int i = 0; i < iterationsPerAction; i++) {
            prepareForAction(startingCards);
            if(simulatedPlayerHand.getTotal() > 21) return new int[]{0,0,0,0};

            simulatedPlayerHand.addCard(simulatedDeck.deal(this, false));

            winnings[2] += playSimulatedDealerHand(round, 2 * iterationsPerAction);
        }
        if(allowedActions == 2) {
            winnings[3] = -100000000;
            return winnings;
        }
        for (int i = 0; i < iterationsPerAction; i++) {
            prepareForAction(startingCards);

            simulatedPlayerHand.pollCard();

            int[] splitWinnings = tryActions(round, 2, actionDepthSimulationAmount, new LinkedList<>(simulatedPlayerHand.getCards()));

            winnings[3] += 2 * iterationsPerAction * splitWinnings[indexWithHighestValue(splitWinnings)] / actionDepthSimulationAmount;
        }
        return winnings;
    }

    public int indexWithHighestValue(int[] array) {
        int highestIndex = 0;

        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[highestIndex]) highestIndex = i;
        }

        return highestIndex;
    }

    public int playSimulatedDealerHand(Round round, int betMultiplier) {
        //plays the dealer hand and then returns winnings for given hand
        simulatedDealerHand.clear();
        simulatedDealerHand.addCard(round.getDealerHand().getCards().getFirst());

        simulatedDealerHand.addCard(simulatedDeck.deal(this, false));

        //dealer blackjack or hand busted
        if (simulatedDealerHand.getTotal() == 21 || simulatedPlayerHand.getTotal() > 21) return 0;
        //player blackjack
        if (simulatedPlayerHand.getTotal() == 21 && simulatedPlayerHand.getCards().size() == 2)
            return 5 * betMultiplier;

        //deal dealer cards
        while (simulatedDealerHand.getTotal() < 17) {
            simulatedDealerHand.addCard(simulatedDeck.deal(this, false));
        }
        //if dealer busted or player has higher total
        if (simulatedDealerHand.getTotal() > 21 || simulatedDealerHand.getTotal() < simulatedPlayerHand.getTotal())
            return 4 * betMultiplier;
        return 0;
    }

    public void prepareForAction(LinkedList<Card> startingCards) {
        //set starting cards
        simulatedPlayerHand.getCards().clear();
        simulatedPlayerHand.getCards().addAll(startingCards);
        //reset simulated deck and reshuffle the containing cards
        simulatedDeck.setCards(predictedGameDeck.getCards());
        Collections.shuffle(predictedGameDeck.getCards());
    }

    //körs när det ursprungliga bettet ska bestämmas
    @Override
    public int betMethod(Round round) {
        int simulatedWinnings = 0;
        for (int i = 0; i < betSimulationAmount; i++) {
            betDeck.setCards(predictedGameDeck.getCards());
            betDeck.shuffle();
            simulatedWinnings += simulatedRound.playRound();
            simulatedRound.reset();
        }
        if (simulatedWinnings > 0) return settings.getMaxBet();
        return settings.getMinBet();
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
