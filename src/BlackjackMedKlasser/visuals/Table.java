package BlackjackMedKlasser.visuals;

import BlackjackMedKlasser.Hand;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

public class Table extends JPanel {
    private LinkedList<DisplayedCard> cards = new LinkedList<>();
    private int cardHeight;
    private int cardWidth;
    private int screenHeight;
    private int screenWidth;
    private double[] deckPos = new double[]{0.7, 0.22};
    private BufferedImage cardBack = null;
    private BufferedImage cardFront = null;
    private BufferedImage deck = null;
    private double[] cardGroupCentersX = new double[]{0.5, 0.4, 0.3, 0.2};

    public double[] getCardGroupCentersX() {
        return cardGroupCentersX;
    }

    public Table() {
        try {
            cardBack = ImageIO.read(getClass().getResource("/images/cardsbg.png"));
            cardFront = ImageIO.read(getClass().getResource("/images/cards.png"));
            deck = ImageIO.read(getClass().getResource("/images/deck.png"));
        } catch (IOException e) {
            System.out.println("problem");
        }

        this.setBackground(new Color(44, 128, 8));
        this.setPreferredSize(new Dimension(1300, 350));


        //cards.add(new DisplayedCard(0.5,0.5, 2, 2, 0));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Point mousePos = getMousePosition().getLocation();
            }
        });

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        cardHeight = this.getSize().height / 3;
        cardWidth = (cardHeight * 63) / 88;

        screenHeight = this.getSize().height;
        screenWidth = this.getSize().width;

        g.drawImage(deck, (int) (screenWidth * deckPos[0] - (cardWidth / 2.0)), (int) (screenHeight * deckPos[1] - (cardHeight / 2.0)), (int) (screenWidth * deckPos[0] + (cardWidth / 2.0)), (int) (screenHeight * deckPos[1] + (cardHeight / 2.0)) + cardHeight * 69 / 352, 0, 0, 252, 421, null);


        for (DisplayedCard card : cards) {
            if (card.isFlipped())
                g.drawImage(cardFront, (int) (screenWidth * card.getX() - (cardWidth * card.getFlipWidth() / 2.0)), (int) (screenHeight * card.getY() - (cardHeight / 2.0)), (int) (screenWidth * card.getX() + (cardWidth * card.getFlipWidth() / 2.0)), (int) (screenHeight * card.getY() + (cardHeight / 2.0)), (card.getValue() - 1) * 63, card.getSuit() * 88, card.getValue() * 63, (card.getSuit() + 1) * 88, null);
            else
                g.drawImage(cardBack, (int) (screenWidth * card.getX() - (cardWidth * card.getFlipWidth() / 2.0)), (int) (screenHeight * card.getY() - (cardHeight / 2.0)), (int) (screenWidth * card.getX() + (cardWidth * card.getFlipWidth() / 2.0)), (int) (screenHeight * card.getY() + (cardHeight / 2.0)), 0, 0, 252, 352, null);
        }
    }

    public void moveGroup(int cardGroup, double relativeX, double relativeY, int interpolationDurationFrames, int interpolationDelayFrames, int rippleDelayFrames) {
        if(cardGroup < 4) cardGroupCentersX[cardGroup] += relativeX;
        int numberOfCards = 0;
        for (DisplayedCard card : cards) {
            if (card.getCardGroup() == cardGroup)
                card.moveSmooth(card.getX() + relativeX, card.getY() + relativeY, interpolationDurationFrames, interpolationDelayFrames + (rippleDelayFrames * numberOfCards++));
        }
    }

    public void addCard(DisplayedCard card) {
        cards.add(card);
    }

    public LinkedList<DisplayedCard> getCards() {
        return cards;
    }

    public double[] getDeckPos() {
        return deckPos;
    }

    public int numberOfCardsInGroup(int cardGroup) {
        int numberOfCards = 0;
        for (DisplayedCard card : cards) {
            if (card.getCardGroup() == cardGroup) numberOfCards++;
        }
        return numberOfCards;
    }

    public DisplayedCard[] getCardsInGroup(int cardGroup) {
        DisplayedCard[] cards = new DisplayedCard[numberOfCardsInGroup(cardGroup)];
        int cardIndex = 0;
        for (DisplayedCard card : cards) {
            if (card.getCardGroup() == cardGroup) cards[cardIndex++] = card;
        }
        return cards;
    }

    public void clearTable(int interpolationDurationFrames, int interpolationDelayFrames) {
        for (int i = 0; i < 4; i++) {
            moveGroup(i,0,0.5,interpolationDurationFrames, interpolationDelayFrames,5);
        }
        moveGroup(4,0,-0.5,interpolationDurationFrames, interpolationDelayFrames,5);
        for (DisplayedCard card : cards) {
            card.deleteAfterMove();
        }
    }
}
