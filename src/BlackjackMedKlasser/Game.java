package BlackjackMedKlasser;

public class Game {
    private Deck deck;

    public Game(Settings settings) {
        deck = new Deck(settings.numberOfDecks);
    }
}
