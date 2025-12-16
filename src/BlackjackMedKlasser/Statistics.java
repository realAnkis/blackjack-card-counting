package BlackjackMedKlasser;

import BlackjackMedKlasser.playMethods.PlayMethod;

public class Statistics {

    public Statistics(long money, long betTotal, PlayMethod playMethod) {
        printStats(money, betTotal, playMethod);
    }

    private static double calculatePlayerEdge(long money, long betTotal) {
        return 100 * (double)money/betTotal;
    }

    public static void printStats(long money, long betTotal, PlayMethod playMethod) {
        System.out.println(playMethod.getClass().toString().substring(38) + ":");

        System.out.println("Final balance: " + money);
        System.out.println("Total amount bet: " + betTotal);
        System.out.println("Player edge: " + calculatePlayerEdge(money, betTotal) + "%");

    }
}
