package BlackjackMedKlasser;

import BlackjackMedKlasser.playMethods.PlayMethod;
import BlackjackMedKlasser.playMethods.SpelaSjälv;

public class Game {
    private Deck deck;

    public static void main(String[] args) {
        Game game = new Game(new Settings());
    }

    public Game(Settings settings) {
        deck = new Deck(settings);
        PlayMethod playMethod = new SpelaSjälv();
    }

}
