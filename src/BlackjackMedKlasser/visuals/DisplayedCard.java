package BlackjackMedKlasser.visuals;

public class DisplayedCard {
    private double[] position = new double[2];
    private int suit;
    private int value;
    private boolean isFlipped = false;
    private int cardGroup;

    public DisplayedCard(double[] position, int suit, int value, int cardGroup) {
        this.position = position;
        this.suit = suit;
        this.value = value;
        this.cardGroup = cardGroup;
    }

    public double getX() {
        return position[0];
    }
    public double getY() {
        return position[1];
    }
    public double[] getPosition() {
        return position;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public int getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    public int getCardGroup() {
        return cardGroup;
    }

    public void moveLinear(double xPos, double yPos, int interpolationDuration, int interpolationDelay) {
        position[0] += xPos;
    }
}
