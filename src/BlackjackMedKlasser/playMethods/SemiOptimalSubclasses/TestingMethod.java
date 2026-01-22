package BlackjackMedKlasser.playMethods.SemiOptimalSubclasses;


import BlackjackMedKlasser.*;
import BlackjackMedKlasser.playMethods.PlayMethod;
import BlackjackMedKlasser.playMethods.SemiOptimal;

public class TestingMethod {
    public static void main(String[] args) {
        new TestingMethod();
    }
    public TestingMethod() {
        Settings settings = new Settings();
        Round round = new Round(new Deck(settings),new PlayMethod(),new Game());

        PlayMethod semiOptimal = new SemiOptimal(settings);

        round.getHands()[0].addCard(new Card(10,0));
        round.getHands()[0].addCard(new Card(7,1));

        round.getDealerHand().addCard(new Card(9,1));

        semiOptimal.actionMethod(round,2,0);
    }
}
