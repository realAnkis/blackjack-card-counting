package BlackjackMedKlasser;

import BlackjackMedKlasser.playMethods.PlayMethod;

public class Statistics {

    public Statistics(int money, int betTotal, PlayMethod playMethod) {
        printStats(money, betTotal, playMethod);
    }

    private static double calculatePlayerEdge(int money, int betTotal) {
        return 100 * (double)money/betTotal;
    }

    public static void printStats(int money, int betTotal, PlayMethod playMethod) {
        System.out.println(playMethod.getClass().toString().substring(38) + ":");

        System.out.println("Final balance: " + money);
        System.out.println("Total amount bet: " + betTotal);
        System.out.println("Player edge: " + calculatePlayerEdge(money, betTotal) + "%");

    }
}
