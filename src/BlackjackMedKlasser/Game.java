package BlackjackMedKlasser;

import BlackjackMedKlasser.playMethods.*;
import BlackjackMedKlasser.playMethods.HumanCardCountingPack.HumanCardCounting;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Scanner;

public class Game {
    private Deck deck;
    private long money = 0;
    private long betTotal = 0;
    private final int threadAmount = 4;

    public static void main(String[] args) {
        Settings settings = new Settings();
        Game game = new Game(settings);
    }

    public Game(Settings settings) {
        deck = new Deck(settings);
        PlayMethod playMethod = selectPlayMethod(settings,deck);
        Round round = new Round(deck, playMethod, this);
        LocalDateTime startTime = LocalDateTime.now();

        for (int i = 0; i < settings.getNumberOfGames(); i++) {
            money += round.playRound();

            round.reset();
        }
        LocalDateTime endTime = LocalDateTime.now();
        Statistics statistics = new Statistics(money, betTotal, playMethod,startTime,endTime);
    }

    public Game() {}

    public void addBetTotal(int addedAmount) {
        betTotal += addedAmount;
    }

    public static PlayMethod selectPlayMethod(Settings settings, Deck deck) {
        System.out.println("Please select one of the following playmethods:");
        System.out.println("- SpelaSjälv");
        System.out.println("- Visuell");
        System.out.println("- TestMethod");
        System.out.println("- SemiOptimal");
        System.out.println("- HumanCardCounting");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();

            if (input.equals("SpelaSjälv")) return new SpelaSjälv(settings);
            if (input.equals("Visuell")) return new SpelaSjälvVisuell(settings);
            if (input.equals("TestMethod")) return new TestMethod(settings);
            if (input.equals("SemiOptimal")) return new SemiOptimal(settings);
            if (input.equals("HumanCardCounting")) {
                System.out.println("Stating"); return new HumanCardCounting(settings, deck);}
        }
    }
}
