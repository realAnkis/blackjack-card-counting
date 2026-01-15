package BlackjackMedKlasser.visuals;

import javax.swing.*;

public class DisplayedCard {
    private double[] position = new double[2];
    private int suit;
    private int value;
    private boolean isFlipped = false;
    private int cardGroup;
    private Table table;

    private double[] targetPosition = new double[2];
    private double[] startPosition = new double[2];
    private int interpolationDelayFrames;
    private int interpolationDurationFrames;
    private int interpolationDurationFramesTotal;
    private int flipDurationFrames;
    private int flipDurationFramesTotal;
    private int flipDelayFrames;
    private double flipWidth = 1.0;


    public DisplayedCard(double positionX, double positionY, int suit, int value, int cardGroup, Table table) {
        position[0] = positionX;
        position[1] = positionY;
        this.suit = suit;
        this.value = value;
        this.cardGroup = cardGroup;
        this.table = table;
    }

    public double getFlipWidth() {
        return flipWidth;
    }

    public double getX() {
        return position[0];
    }

    public double getY() {
        return position[1];
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

    public void setCardGroup(int cardGroup){
        this.cardGroup = cardGroup;
    }

    public void moveLinear(double xPos, double yPos, int interpolationDurationFrames, int interpolationDelayFrames) {
        this.interpolationDelayFrames = interpolationDelayFrames;
        this.interpolationDurationFrames = interpolationDurationFrames;
        targetPosition[0] = xPos;
        targetPosition[1] = yPos;

        Timer interpolationTimer = new Timer(16, e -> {
            if (this.interpolationDelayFrames > 0) {
                this.interpolationDelayFrames--;
                return;
            }
            if (this.interpolationDurationFrames <= 0) this.interpolationDurationFrames = 1;

            double xDifference = targetPosition[0] - position[0];
            double yDifference = targetPosition[1] - position[1];

            position[0] += xDifference / this.interpolationDurationFrames;
            position[1] += yDifference / this.interpolationDurationFrames;

            this.interpolationDurationFrames--;
            if (this.interpolationDurationFrames == 0) ((Timer) e.getSource()).stop();
        });
        interpolationTimer.start();
    }

    public void moveSmooth(double xPos, double yPos, int interpolationDurationFrames, int interpolationDelayFrames) {
        this.interpolationDelayFrames = interpolationDelayFrames;
        this.interpolationDurationFrames = interpolationDurationFramesTotal = interpolationDurationFrames;
        if (interpolationDurationFramesTotal <= 0) interpolationDurationFramesTotal = 1;
        targetPosition[0] = xPos;
        targetPosition[1] = yPos;
        startPosition[0] = position[0];
        startPosition[1] = position[1];

        Timer interpolationTimer = new Timer(16, e -> {
            if (this.interpolationDelayFrames > 0) {
                this.interpolationDelayFrames--;
                return;
            }

            double interpolationFraction = 1 - (double) this.interpolationDurationFrames / interpolationDurationFramesTotal;
            double fractionTravelled = Math.sin((interpolationFraction - 0.5) * Math.PI) * 0.5 + 0.5;

            position[0] = (targetPosition[0] - startPosition[0]) * fractionTravelled + startPosition[0];
            position[1] = (targetPosition[1] - startPosition[1]) * fractionTravelled + startPosition[1];

            this.interpolationDurationFrames--;
            if (this.interpolationDurationFrames <= 0) ((Timer) e.getSource()).stop();
        });
        interpolationTimer.start();
    }

    public void flip(int flipDurationFrames, int flipDelayFrames) {
        this.flipDelayFrames = flipDelayFrames;
        this.flipDurationFrames = this.flipDurationFramesTotal = flipDurationFrames;

        Timer flipTimer = new Timer(16, e -> {
            if (this.flipDelayFrames > 0) {
                this.flipDelayFrames--;
                return;
            }
            double flipProgress = 1 - (double) this.flipDurationFrames / flipDurationFramesTotal;
            flipWidth = Math.abs(Math.sin((flipProgress - 0.5) * Math.PI));

            if (this.flipDurationFrames == this.flipDurationFramesTotal / 2) isFlipped = !isFlipped;

            this.flipDurationFrames--;
            if (this.flipDurationFrames <= 0) ((Timer) e.getSource()).stop();
        });
        flipTimer.start();
    }

    public void delete(int delayFrames) {
        Timer deletionTimer = new Timer(16 * delayFrames, e -> {
            table.getCards().remove(this);
            ((Timer) e.getSource()).stop();
        });
        deletionTimer.start();
    }
    public void deleteAfterMove() {
        Timer deletionTimer = new Timer(16 * (interpolationDelayFrames + interpolationDurationFrames), e -> {
            table.getCards().remove(this);
            ((Timer) e.getSource()).stop();
        });
        deletionTimer.start();
    }
}
