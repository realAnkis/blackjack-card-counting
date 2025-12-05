package BlackjackMedKlasser;

import BlackjackMedKlasser.playMethods.PlayMethod;
import BlackjackMedKlasser.playMethods.SpelaSjälv;
import BlackjackMedKlasser.playMethods.TestMethod;

import java.util.Scanner;

public class Game {
    private Deck deck;
    private int money = 0;
    private int betTotal = 0;

    public static void main(String[] args) {
        Game game = new Game(new Settings(), selectPlayMethod());
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

    public void addBetTotal(int addedAmount) {
        betTotal += addedAmount;
    }

    public static PlayMethod selectPlayMethod() {
        System.out.println("Please select one of the following playmethods:");
        System.out.println("- SpelaSjälv");
        System.out.println("- TestMethod");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();

            if (input.equals("SpelaSjälv")) return new SpelaSjälv();
            if (input.equals("TestMethod")) return new TestMethod();
        }
    }
}
