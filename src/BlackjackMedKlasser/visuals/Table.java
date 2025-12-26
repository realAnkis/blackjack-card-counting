package BlackjackMedKlasser.visuals;

import BlackjackMedKlasser.Hand;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

public class Table extends JPanel {
    private LinkedList<DisplayedCard> cards = new LinkedList<>();
    private int cardHeight;
    private int cardWidth;
    private int screenHeight;
    private int screenWidth;
    private BufferedImage cardBack = null;
    private BufferedImage cardFront = null;

    public Table() {
        try {
            cardBack = ImageIO.read(getClass().getResource("/images/cardsbg.png"));
            cardFront = ImageIO.read(getClass().getResource("/images/cards.png"));
        } catch (IOException e) {
            System.out.println("problem");
        }

        this.setBackground(new Color(44, 128, 8));
        this.setSize(1400,700);

        cards.add(new DisplayedCard(new double[]{0.5, 0.5},2,2,0));

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        cardHeight = this.getSize().height / 3;
        cardWidth = (cardHeight * 63) / 88;

        screenHeight = this.getSize().height;
        screenWidth = this.getSize().width;

        for (DisplayedCard card : cards) {
            if(card.isFlipped()) g.drawImage(cardFront,(int)(screenWidth * card.getX() - (cardWidth / 2.0)),(int)(screenHeight * card.getY() - (cardHeight / 2.0)),(int)(screenWidth * card.getX() + (cardWidth / 2.0)),(int)(screenHeight * card.getY() + (cardHeight / 2.0)),card.getValue() * 63, card.getSuit() * 88,(card.getValue() + 1) * 63, (card.getSuit() + 1) * 88, null);
            else g.drawImage(cardBack,(int)(screenWidth * card.getX() - (cardWidth / 2.0)),(int)(screenHeight * card.getY() - (cardHeight / 2.0)),(int)(screenWidth * card.getX() + (cardWidth / 2.0)),(int)(screenHeight * card.getY() + (cardHeight / 2.0)),0, 0,252, 352, null);
            card.moveLinear(0.001,0,0,0);
        }
    }
}
