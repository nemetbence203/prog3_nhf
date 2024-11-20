package org.example;

import javax.swing.*;
import java.awt.*;

public class GameOfLife extends JFrame {

    private LivingSpace livingSpace;
    private GameControlPanel controlPanel;

    public GameOfLife() {
        livingSpace = new LivingSpace(50); // Kezdetben egy 50x50-es élettér
        controlPanel = new GameControlPanel(livingSpace);

        setTitle("Game of Life");
        setLayout(new BorderLayout());
        //add(new GameAreaPanel(livingSpace), BorderLayout.CENTER); // Élettér megjelenítése
        add(controlPanel, BorderLayout.EAST); // Menüsáv hozzáadása

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameOfLife::new);
    }
}
