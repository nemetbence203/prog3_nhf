package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameOfLife extends JFrame {

    private LivingSpace livingSpace;
    private GameControlPanel controlPanel;

    public GameOfLife() {
        livingSpace = new LivingSpace(50); // Kezdetben egy 50x50-es élettér


        setTitle("Game of Life");
        setLayout(new BorderLayout());
        GameAreaPanel gameAreaPanel = new GameAreaPanel(livingSpace, 10);
        controlPanel = new GameControlPanel(livingSpace, gameAreaPanel);
        JScrollPane scrollPane = new JScrollPane(gameAreaPanel);
        add(scrollPane, BorderLayout.CENTER);

        gameAreaPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = e.getY() / gameAreaPanel.getCellSize();
                int col = e.getX() / gameAreaPanel.getCellSize();
                if (row < livingSpace.getSize() && col < livingSpace.getSize()) {
                    // Állapot váltása kattintásra
                    Cell cell = livingSpace.getAt(row, col);
                    cell.flip();
                    gameAreaPanel.repaint(); // Frissítés
                }
            }
        });

        add(gameAreaPanel, BorderLayout.CENTER); // Élettér megjelenítése
        add(controlPanel, BorderLayout.EAST); // Menüsáv hozzáadása

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameOfLife::new);
    }
}
