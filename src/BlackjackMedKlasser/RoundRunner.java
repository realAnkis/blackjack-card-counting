package BlackjackMedKlasser;

import BlackjackMedKlasser.playMethods.PlayMethod;

public class RoundRunner extends Thread {
    private Settings settings;
    private PlayMethod playMethod;
    private Game game;
    private long money;

    public RoundRunner(Settings settings, PlayMethod playMethod, Game game) {
        this.settings = settings;
        this.playMethod = playMethod;
        this.game = game;
    }

    @Override
    public void run() {
        Deck deck = new Deck(settings);
        Round round = new Round(deck, playMethod, game);

        for (int i = 0; i < settings.getNumberOfGames(); i++) {
            money += round.playRound();

            round.reset();
        }
    }
}
