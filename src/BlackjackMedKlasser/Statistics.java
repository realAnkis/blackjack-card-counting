package BlackjackMedKlasser;

import BlackjackMedKlasser.playMethods.PlayMethod;

import java.time.LocalDateTime;

public class Statistics {

    public Statistics(long money, long betTotal, PlayMethod playMethod, LocalDateTime startTime, LocalDateTime endTime) {
        printStats(money, betTotal, playMethod, calculateTime(startTime,endTime));
    }

    private static double calculatePlayerEdge(long money, long betTotal) {
        return 100 * (double)money/betTotal;
    }

    private double[] calculateTime(LocalDateTime startTime, LocalDateTime endTime) {
        double[] timeInfo = new double[4];
        Settings settings = new Settings();
        endTime = endTime.minusYears(startTime.getYear());
        endTime = endTime.minusMonths(startTime.getMonthValue());
        endTime = endTime.minusDays(startTime.getDayOfMonth());
        endTime = endTime.minusHours(startTime.getHour());
        endTime = endTime.minusMinutes(startTime.getMinute());
        endTime = endTime.minusSeconds(startTime.getSecond());
        endTime = endTime.minusNanos(startTime.getNano());
        double totalSeconds = endTime.getSecond();
        totalSeconds += endTime.getMinute() * 60;
        totalSeconds += endTime.getHour() * 60 * 60;
        //totalSeconds += endTime.getDayOfMonth() * 60 * 60 * 24;
        //totalSeconds += endTime.getNano() * 0.000000001;

        System.out.println(totalSeconds);

        timeInfo[0] = settings.getNumberOfGames() / totalSeconds;
        timeInfo[1] = endTime.getSecond();
        timeInfo[2] = endTime.getMinute();
        timeInfo[3] = endTime.getHour();

        return timeInfo;
    }

    public static void printStats(long money, long betTotal, PlayMethod playMethod, double[] timeInfo) {
        System.out.println(playMethod.getClass().toString().substring(38) + ":");

        System.out.println("Total time: " + timeInfo[1] + "s " + timeInfo[2] + "m " + timeInfo[3] + "h (" + timeInfo[0] + " rounds/s)");

        System.out.println("Final balance: " + money);
        System.out.println("Total amount bet: " + betTotal);
        System.out.println("Player edge: " + calculatePlayerEdge(money, betTotal) + "%");

    }
}
