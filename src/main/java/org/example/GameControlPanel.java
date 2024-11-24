package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.LineBorder;

public class GameControlPanel extends JPanel implements Runnable {
    private JSlider speedSlider;
    private JCheckBox fadeEffectCheckbox;
    private JTextField bornField, surviveField;
    private JButton startButton, stopButton, clearButton, colorPickerLiving, colorPickerDead;
    private LivingSpace livingSpace;
    private JSpinner sizeSpinner;
    private Color livingColor = Color.blue, deadColor = Color.white;
    private GameAreaPanel gameAreaPanel;
    private boolean running = false;

    public GameControlPanel(LivingSpace livingSpace, GameAreaPanel gameAreaPanel) {
        this.livingSpace = livingSpace;
        this.gameAreaPanel = gameAreaPanel;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new LineBorder(Color.black, 1));
        setPreferredSize(new Dimension(250, 600)); // Panel szélessége és magassága

        //0. Élettér mérete:
        JPanel sizeSettingPanel = new JPanel();
        sizeSettingPanel.setLayout(new GridLayout(1, 2));
        sizeSettingPanel.setBorder(new LineBorder(Color.black, 1));
        sizeSettingPanel.setPreferredSize(new Dimension(250, 20));
        sizeSettingPanel.setMaximumSize(new Dimension(250, 20));
        sizeSettingPanel.add(new JLabel("Élettér mérete:"));
        sizeSpinner = new JSpinner(new SpinnerNumberModel(50,10,150,1));
        sizeSettingPanel.add(sizeSpinner);
        add(sizeSettingPanel);

        // 1. Játékszabályok
        JPanel rulesPanel = new JPanel();
        rulesPanel.setBorder(new LineBorder(Color.black, 1));
        rulesPanel.setLayout(new GridLayout(2, 2));
        rulesPanel.setPreferredSize(new Dimension(250, 40));
        rulesPanel.setMaximumSize(new Dimension(250, 40));
        rulesPanel.add(new JLabel("Születési szabály (0-8):"));
        bornField = new JTextField("3");
        bornField.setPreferredSize(new Dimension(80, 20));
        bornField.setMaximumSize(new Dimension(80, 20));
        rulesPanel.add(bornField);
        rulesPanel.add(new JLabel("Túlélési szabály (0-8):"));
        surviveField = new JTextField("2,3");
        surviveField.setPreferredSize(new Dimension(80, 20));
        surviveField.setMaximumSize(new Dimension(80, 20));
        rulesPanel.add(surviveField);
        add(rulesPanel);

        // 2. Játék sebességét megadó slider
        JPanel speedPanel = new JPanel();
        speedPanel.setBorder(new LineBorder(Color.black, 1));
        speedPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Balra igazítás
        speedPanel.setPreferredSize(new Dimension(250, 80));
        speedPanel.setMaximumSize(new Dimension(250, 80));
        speedPanel.add(new JLabel("Játék sebessége"));
        speedSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 5); // Sebesség 1-10 között, alapértelmezett 5
        speedSlider.setMajorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedPanel.add(speedSlider);
        add(speedPanel);

        // 3. Fade effekt checkbox
        JPanel fadePanel = new JPanel();
        fadePanel.setPreferredSize(new Dimension(250, 40));
        fadePanel.setMaximumSize(new Dimension(250, 40));
        fadePanel.setBorder(new LineBorder(Color.black, 1));
        fadePanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Balra igazítás
        fadeEffectCheckbox = new JCheckBox("Halott cellák elhalványodása");
        fadePanel.add(fadeEffectCheckbox);
        add(fadePanel);

        // 4. Colorpicker gombok
        JPanel colorPanel = new JPanel();
        colorPanel.setPreferredSize(new Dimension(250, 100));
        colorPanel.setMaximumSize(new Dimension(250, 100));
        colorPanel.setBorder(new LineBorder(Color.black, 1));
        colorPanel.setLayout(new GridLayout(2,1));
        colorPanel.add(new JLabel("Cellaszínek:"));
        JPanel colorButtons = new JPanel();
        colorButtons.setLayout(new GridLayout(1,2));
        colorPickerLiving = new JButton("Élő");
        colorPickerLiving.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                livingColor = JColorChooser.showDialog(null, "Válaszd ki az élő cellák színét!", livingColor);
                gameAreaPanel.setColors(livingColor, deadColor);
                gameAreaPanel.repaint();
            }
        });
        colorPickerDead = new JButton("Halott");
        colorPickerDead.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deadColor = JColorChooser.showDialog(null, "Válaszd ki a halott cellák színét!", deadColor);
                gameAreaPanel.setColors(livingColor, deadColor);
                gameAreaPanel.repaint();
            }
        });
        colorButtons.add(colorPickerLiving);
        colorButtons.add(colorPickerDead);
        colorPanel.add(colorButtons);
        add(colorPanel);

        // 5. Két gomb: játék indítása és megállítása
        JPanel startStopPanel = new JPanel();
        startStopPanel.setPreferredSize(new Dimension(250, 50));
        startStopPanel.setMaximumSize(new Dimension(250, 50));
        startStopPanel.setBorder(new LineBorder(Color.black, 1));
        startStopPanel.setLayout(new GridLayout(1,3));
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        clearButton = new JButton("Clear");

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

        clearButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               livingSpace.killAll();
               gameAreaPanel.repaint();
           }
        });

        startStopPanel.add(startButton);
        startStopPanel.add(stopButton);
        startStopPanel.add(clearButton);
        add(startStopPanel);




    }

    @Override
    public void run() {

    }

    // Játék indítása
    private void startGame() {
        // Beállítjuk a játékszabályokat
        try {
            List<Integer> bornRules = parseRules(bornField.getText());
            List<Integer> surviveRules = parseRules(surviveField.getText());
            livingSpace.setBornRule(bornRules);
            livingSpace.setSurviveRule(surviveRules);
            livingSpace.nextState();
            gameAreaPanel.setFadeOn(fadeEffectCheckbox.isSelected());
            gameAreaPanel.repaint();
            running = true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Szabályok formátuma: számjegyek 0-tól 8-ig, vesszővel elválasztva!", "Hiba", JOptionPane.ERROR_MESSAGE);
            running = false;
            return;
        }

        // Játék sebesség beállítása (pl. 1-10 közötti sebesség)
        int speed = speedSlider.getValue();

        //boolean fadeEffectEnabled = fadeEffectCheckbox.isSelected();


        // Indíthatod a szimulációt itt


    }


    // Játék megállítása
    private void stopGame() {
        // Megállíthatod a szimulációt itt
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
