package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameOfLife extends JFrame {

    private LivingSpace livingSpace;
    private GameControlPanel controlPanel;

    public GameOfLife() {

        livingSpace = new LivingSpace(50); // Default: egy 50x50-es élettér
        setTitle("Életjáték");
        setLayout(new BorderLayout());
        GameAreaPanel gameAreaPanel = new GameAreaPanel(livingSpace, 10);
        gameAreaPanel.setPreferredSize(new Dimension(550,600));


        controlPanel = new GameControlPanel(livingSpace, gameAreaPanel);
        JScrollPane scrollPane = new JScrollPane(gameAreaPanel);
        add(scrollPane, BorderLayout.CENTER);



        add(controlPanel, BorderLayout.EAST); // Menüsáv hozzáadása

        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameOfLife::new);
    }
}
