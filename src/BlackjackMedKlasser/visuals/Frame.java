package BlackjackMedKlasser.visuals;

import javax.swing.*;
import java.awt.*;

public class Frame {
    private JFrame frame = new JFrame();

    public static void main(String[] args) {
        Frame Frame = new Frame();
    }

    public Frame() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setFocusable(true);
        frame.setSize(1400,700);
        frame.setVisible(true);
        frame.add(new Table());
        frame.repaint();
        Timer frameUpdate = new Timer(16,e-> {
            frame.repaint();
        });
    }
}
