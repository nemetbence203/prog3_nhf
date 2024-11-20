package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GameControlPanel extends JPanel {
    private JSlider speedSlider;
    private JCheckBox fadeEffectCheckbox;
    private JTextField bornField, surviveField;
    private JButton startButton, stopButton;
    private LivingSpace livingSpace;

    public GameControlPanel(LivingSpace livingSpace) {
        this.livingSpace = livingSpace;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(250, 600)); // Panel szélessége és magassága

        // 1. Játékszabályok
        JPanel rulesPanel = new JPanel();
        rulesPanel.setLayout(new GridLayout(2, 2)); // 2 sor, 2 oszlop
        rulesPanel.setPreferredSize(new Dimension(250, 40));
        rulesPanel.add(new JLabel("Born rules (0-8):"));
        bornField = new JTextField("3");
        bornField.setPreferredSize(new Dimension(100, 20)); // Méret csökkentése egy sorra
        rulesPanel.add(bornField);
        rulesPanel.add(new JLabel("Survive rules (0-8):"));
        surviveField = new JTextField("2,3");
        surviveField.setPreferredSize(new Dimension(100, 20)); // Méret csökkentése egy sorra
        rulesPanel.add(surviveField);
        add(rulesPanel);

        // 2. Játék sebességét megadó slider
        JPanel speedPanel = new JPanel();
        speedPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Balra igazítás
        speedPanel.add(new JLabel("Game Speed:"));
        speedSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 5); // Sebesség 1-10 között, alapértelmezett 5
        speedSlider.setMajorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedPanel.add(speedSlider);
        add(speedPanel);

        // 3. Fade effekt checkbox
        JPanel fadePanel = new JPanel();
        fadePanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Balra igazítás
        fadeEffectCheckbox = new JCheckBox("Enable fade effect");
        fadePanel.add(fadeEffectCheckbox);
        add(fadePanel);

        // 4. Két gomb: játék indítása és megállítása
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopGame();
            }
        });

        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        add(buttonPanel);
    }

    // Játék indítása
    private void startGame() {
        // Beállítjuk a játékszabályokat
        try {
            List<Integer> bornRules = parseRules(bornField.getText());
            List<Integer> surviveRules = parseRules(surviveField.getText());
            livingSpace.setBornRule(bornRules);
            livingSpace.setSurviveRule(surviveRules);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid rules format!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Játék sebesség beállítása (pl. 1-10 közötti sebesség)
        int speed = speedSlider.getValue();
        // A sebesség változtatásának logikáját implementálhatod itt (pl. szálak vagy időzítők kezelésével)

        // Fade effekt engedélyezése
        boolean fadeEffectEnabled = fadeEffectCheckbox.isSelected();
        // Itt beállíthatod a `LivingSpace` objektumban, hogy aktiválódjon a fade effekt, ha szükséges

        // Indíthatod a szimulációt itt
        // livingSpace.startSimulation(speed, fadeEffectEnabled);
    }

    // Játék megállítása
    private void stopGame() {
        // Megállíthatod a szimulációt itt
        // livingSpace.stopSimulation();
    }

    // Segédmetódus a szabályok parse-olásához
    private List<Integer> parseRules(String rulesText) {
        String[] ruleStrings = rulesText.split(",");
        List<Integer> rules = new ArrayList<>();
        for (String rule : ruleStrings) {
            rules.add(Integer.parseInt(rule.trim()));
        }
        return rules;
    }
}
