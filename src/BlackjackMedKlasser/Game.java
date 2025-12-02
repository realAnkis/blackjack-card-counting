package BlackjackMedKlasser;

import BlackjackMedKlasser.playMethods.PlayMethod;
import BlackjackMedKlasser.playMethods.SpelaSjälv;
import BlackjackMedKlasser.playMethods.TestMethod;

public class Game {
    private Deck deck;

    public static void main(String[] args) {
        Game game = new Game(new Settings());
    }

    public Game(Settings settings) {
        deck = new Deck(settings);
        PlayMethod playMethod = new TestMethod();
        Round round = new Round(deck,playMethod);

        int money = 0;

        for (int i = 0; i < settings.getNumberOfGames(); i++) {
            money += round.playRound();

            round.reset();
        }

        Statistics statistics = new Statistics(money,round.getBetTotal());
    }

}
