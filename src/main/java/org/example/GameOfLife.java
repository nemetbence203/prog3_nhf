package org.example;

import javax.swing.*;
import java.awt.*;

public class GameOfLifeGUI extends JFrame {

    public GameOfLifeGUI() {
        setTitle("Game of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Élettér panel (bal oldal)
        JPanel gamePanel = new JPanel();
        gamePanel.setBackground(Color.WHITE); // Az élettér fehér hátterű
        gamePanel.setPreferredSize(new Dimension(600, 600)); // Méret megadása
        add(gamePanel, BorderLayout.CENTER);

        // Menüsáv (jobb oldal)
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS)); // Függőleges elrendezés
        menuPanel.setBackground(Color.LIGHT_GRAY); // Szürkés háttér a menüsávnak
        menuPanel.setPreferredSize(new Dimension(200, 600));

        // Példa kezelőelemek hozzáadása
        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");
        JLabel speedLabel = new JLabel("Sebesség:");
        JSlider speedSlider = new JSlider(1, 10, 5); // 1-10 tartomány, alapértelmezett 5
        JButton resetButton = new JButton("Reset");

        // Elemek hozzáadása a menüsávhoz
        menuPanel.add(Box.createVerticalStrut(20)); // Távolság az elemek között
        menuPanel.add(startButton);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(stopButton);
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(speedLabel);
        menuPanel.add(speedSlider);
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(resetButton);

        add(menuPanel, BorderLayout.EAST);

        // Ablak méretezése és megjelenítése
        setSize(800, 600); // Összesített méret
        setLocationRelativeTo(null); // Ablak középre helyezése
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameOfLifeGUI::new);
    }
}
