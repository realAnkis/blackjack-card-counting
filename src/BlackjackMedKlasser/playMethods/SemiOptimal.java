package BlackjackMedKlasser.playMethods;

import BlackjackMedKlasser.*;

public class SemiOptimal extends PlayMethod {
    private final int betSimulationAmount = 1000;
    private final int actionSimulationAmount = 100;
    private final int actionDepthSimulationAmount = 20;

    private Settings settings;
    private Deck simulatedDeck;
    private Round simulatedRound;

    public SemiOptimal(Settings settings) {
        this.settings = settings;
        simulatedDeck = new Deck(settings);
        simulatedRound = new Round(simulatedDeck,this,new Game());
    }

    //körs när ett kort delas ut från kortleken
    @Override
    public void cardDealtMethod(Card card) {
    }

    //körs när kortleken blandas
    @Override
    public void reshuffleMethod() {
    }

    //körs när en aktion i spelet behöver bestämmas, bör returna "s", "h", "d" eller "sp"
    //allowedActions har värdet 3 ifall alla är tillåtna, 2 ifall "h, s or d" är tillåtet och 1 om bara "h or s" är tillåtet
    //handIndex är den hand som just nu spelas, bör användas i till exempel: round.getHands()[handIndex].getTotal()
    @Override
    public String actionMethod(Round round, int allowedActions, int handIndex) {
        if(round.getHands()[handIndex].getTotal() <= 10) return "h";
        return "s";
    }

    //körs när det ursprungliga bettet ska bestämmas
    @Override
    public int betMethod(Round round) {
        int simulatedWinnings = 0;
        simulatedDeck.setCards(round.getDeck().getCards());
        for (int i = 0; i < betSimulationAmount; i++) {
            simulatedWinnings += simulatedRound.playRound();
        }
        if(simulatedWinnings > 0) return settings.getMaxBet();
        return settings.getMinBet();
    }

    //körs ifall möjlighet för ett insurance bet finns (dvs. ifall dealern har ett ess)
    @Override
    public int insuranceBetMethod(Round round) {
        return 0;
    }

}
