package BlackjackMedKlasser;

import BlackjackMedKlasser.playMethods.PlayMethod;
import BlackjackMedKlasser.playMethods.SemiOptimal;

public class RoundRunner extends Thread {
    private Settings settings;
    private PlayMethod playMethod;
    private Game game;
    private long money;

    public RoundRunner(Settings settings, Game game) {
        this.settings = settings;
        playMethod = new SemiOptimal(settings);
        this.game = game;
    }

    @Override
    public void run() {
        Deck deck = new Deck(settings);
        Round round = new Round(deck, playMethod, game);

        for (int i = 0; i < settings.getNumberOfGames() / settings.threadAmount; i++) {
            money += round.playRound();

            round.reset();
        }

        game.gatherResults(money);
    }
}
