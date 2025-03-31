package org.example;

import javax.swing.*;
import java.awt.*;

/**
 * A program fő osztálya
 */
public class GameOfLife extends JFrame {

    private LivingSpace livingSpace; ///< A futáshoz tartozó élettér
    private GameControlPanel controlPanel; ///< A futáshoz tartozó GameControlPanel
    private GameAreaPanel gameAreaPanel; ///< A futáshoz tartozó GameAreaPanel

    /**
     * Konstruktor, beállít pár GUI elemet, egy alapértelmezetten 50x50-es életteret, valamint a livingSpace, controlPanel gameAreaPanel mezőket
     */
    public GameOfLife() {

        livingSpace = new LivingSpace(50); // Default: egy 50x50-es élettér
        setTitle("Életjáték");
        setLayout(new BorderLayout());
        gameAreaPanel = new GameAreaPanel(livingSpace, 10);
        gameAreaPanel.setPreferredSize(new Dimension(550,600));

        JMenuBar menuBar = new JMenuBar();
        controlPanel = new GameControlPanel(livingSpace, gameAreaPanel, menuBar);
        JScrollPane scrollPane = new JScrollPane(gameAreaPanel);
        add(scrollPane, BorderLayout.CENTER);



        add(controlPanel, BorderLayout.EAST); // Menüsáv hozzáadása
        setJMenuBar(menuBar);
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Main metódus, indítja a programot.
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameOfLife::new);
    }
}
