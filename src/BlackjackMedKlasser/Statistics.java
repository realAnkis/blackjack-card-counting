package BlackjackMedKlasser;

public class Statistics {

    public Statistics(int money, int betTotal) {
        printStats(money, betTotal);
    }

    private static double calculatePlayerEdge(int money, int betTotal) {
        return 100 * (double)money/betTotal;
    }

    public static void printStats(int money, int betTotal) {
        System.out.println("Final balance: " + money);
        System.out.println("Total amount bet: " + betTotal);
        System.out.println("Player edge: " + calculatePlayerEdge(money, betTotal) + "%");
    }
}
