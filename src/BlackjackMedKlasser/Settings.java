package BlackjackMedKlasser;

public class Settings {
    int numberOfDecks = 6;
    double[] reshufflePercentInterval = new double[]{0.25,0.55};
    int numberOfGames = 10000000;
    int maxBet = 100;
    int minBet = 10;

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

