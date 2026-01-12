package BlackjackMedKlasser.visuals;

import javax.swing.*;
import java.awt.*;

public class Frame {
    private JFrame frame = new JFrame();
    private Table table;

    public static void main(String[] args) {
        Frame Frame = new Frame();
    }

    public Frame() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setFocusable(true);
        //frame.setSize(1400,350);
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        table = new Table();
        frame.add(table);
        frame.pack();
        frame.repaint();
        Timer frameUpdate = new Timer(16,e-> {
            frame.repaint();
        });
        frameUpdate.start();
    }

    public Table getTable() {
        return table;
    }
}
