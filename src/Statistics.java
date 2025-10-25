import java.sql.SQLOutput;

public class Statistics {

    private static double calculatePlayerEdge(int money, int betTotal) {
        return 100 * (double)money/betTotal;
    }

    public static void printStats(int money, int betTotal) {
        System.out.println("Final balance: " + money);
        System.out.println("Total amount bet: " + betTotal);
        System.out.println("Player edge: " + calculatePlayerEdge(money, betTotal) + "%");

    }

    public static void main(String[] args) {

    }
}
