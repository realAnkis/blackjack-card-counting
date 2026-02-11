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
    private final int threadAmount;
    private LocalDateTime startTime;
    private static boolean createThreads = false;
    private PlayMethod playMethod;
    private int threadsCompleted;

    public static void main(String[] args) {
        Settings settings = new Settings();
        Game game = new Game(settings);
    }

    public Game(Settings settings) {
        threadAmount = settings.threadAmount;
        deck = new Deck(settings);
        playMethod = selectPlayMethod(settings,deck);
        startTime = LocalDateTime.now();

        if(createThreads) {
            for (int i = 0; i < threadAmount; i++) {
                Thread thread = new RoundRunner(settings, this);
                thread.start();
            }
            return;
        }

        Round round = new Round(deck, playMethod, this);

        for (int i = 0; i < settings.getNumberOfGames(); i++) {
            money += round.playRound();

            round.reset();
        }
        LocalDateTime endTime = LocalDateTime.now();
        Statistics statistics = new Statistics(money, betTotal, playMethod,startTime,endTime);
    }

    public Game() {
        threadAmount = 0;
    }

    public void gatherResults(long money) {
        this.money += money;
        if(++threadsCompleted == threadAmount) {
        LocalDateTime endTime = LocalDateTime.now();
        Statistics statistics = new Statistics(money, betTotal, playMethod,startTime,endTime);
        }
    }

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

            switch (input) {
                case "SpelaSjälv" -> {
                    return new SpelaSjälv(settings);
                }
                case "Visuell" -> {
                    return new SpelaSjälvVisuell(settings);
                }
                case "TestMethod" -> {
                    return new TestMethod(settings);
                }
                case "SemiOptimal" -> {
                    createThreads = true;
                    return new SemiOptimal(settings);
                }
                case "HumanCardCounting" -> {
                    System.out.println("Stating");
                    return new HumanCardCounting(settings, deck);
                }
            }
        }
    }
}
