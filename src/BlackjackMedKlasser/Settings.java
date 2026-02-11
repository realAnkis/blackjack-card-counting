package BlackjackMedKlasser;

public class Settings {
    int numberOfDecks = 2;
    double[] reshufflePercentInterval = new double[]{0.25,0.55};
    int numberOfGames = 10000000;
    //                  10000000  = 10 000 000
    int maxBet = 100;
    int minBet = 10;
    int threadAmount = 6;
    boolean blackjackAfterSplit = true;
    double blackjackPayout = 1.5;


    public int getMaxBet() {
        return maxBet;
    }

    public int getMinBet() {
        return minBet;
    }

    public int getNumberOfDecks() {
        return numberOfDecks;
    }

    public double[] getReshufflePercentInterval() {
        return reshufflePercentInterval;
    }

    public int getNumberOfGames() {
        return numberOfGames;
    }
}

