package BlackjackMedKlasser;

public class Settings {
    int numberOfDecks = 6;
    double[] reshufflePercentInterval = new double[]{0.25,0.55};
    int numberOfGames = 10000000;


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

