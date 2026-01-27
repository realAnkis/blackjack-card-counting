package BlackjackMedKlasser.playMethods.SemiOptimalSubclasses;


import BlackjackMedKlasser.*;
import BlackjackMedKlasser.playMethods.PlayMethod;
import BlackjackMedKlasser.playMethods.SemiOptimal;

public class TestingMethod {
    public static void main(String[] args) {
        new TestingMethod();
    }
    public TestingMethod() {
        int testAmount = 10;

        Settings settings = new Settings();
        Round round = new Round(new Deck(settings),new PlayMethod(),new Game());

        PlayMethod semiOptimal = new SemiOptimal(settings);

        for (int i = 0; i < testAmount; i++) {

        round.getHands()[0].addCard(new Card(6,0));
        round.getHands()[0].addCard(new Card(6,1));

        round.getDealerHand().addCard(new Card(7,1));

        semiOptimal.actionMethod(round,3,0);

        round.getDeck().shuffle();
        semiOptimal.reshuffleMethod();
        round.getHands()[0].clear();
        round.getDealerHand().clear();
        }
    }
}
