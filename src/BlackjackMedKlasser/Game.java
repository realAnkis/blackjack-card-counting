package BlackjackMedKlasser;

import BlackjackMedKlasser.playMethods.*;

import java.util.Scanner;

public class Game {
    private Deck deck;
    private int money = 0;
    private int betTotal = 0;

    public static void main(String[] args) {
        Settings settings = new Settings();
        Game game = new Game(settings, selectPlayMethod(settings));
    }

    public Game(Settings settings, PlayMethod playMethod) {
        deck = new Deck(settings);
        Round round = new Round(deck, playMethod, this);

        for (int i = 0; i < settings.getNumberOfGames(); i++) {
            money += round.playRound();

            round.reset();
        }

        Statistics statistics = new Statistics(money, betTotal, playMethod);
    }

    public Game() {}

    public void addBetTotal(int addedAmount) {
        betTotal += addedAmount;
    }

    public static PlayMethod selectPlayMethod(Settings settings) {
        System.out.println("Please select one of the following playmethods:");
        System.out.println("- SpelaSjälv");
        System.out.println("- TestMethod");
        System.out.println("- SemiOptimal");
        System.out.println("- HumanCardCounting");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();

            if (input.equals("SpelaSjälv")) return new SpelaSjälv(settings);
            if (input.equals("TestMethod")) return new TestMethod(settings);
            if (input.equals("SemiOptimal")) return new SemiOptimal(settings);
            if (input.equals("HumanCardCounting")) return new HumanCardCounting(settings);
        }
    }
}
