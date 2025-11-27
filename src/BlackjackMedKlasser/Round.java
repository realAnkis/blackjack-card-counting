package BlackjackMedKlasser;

public class Round {
    private Hand[] hands = new Hand[4];
    private Hand dealerHand = new Hand();

    public Round(Deck deck) {
    dealerHand.addCard(deck.deal());
    hands[0].addCard((deck.deal()));
    }

}
