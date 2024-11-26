package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.LineBorder;

import static javax.swing.SwingConstants.HORIZONTAL;

/**
 * A játék vezérlését végző menü osztálya, JPanelből leszármazva és a Runnable interfészt implementálja
 */
public class GameControlPanel extends JPanel implements Runnable {
    private JSlider speedSlider;
    private JCheckBox fadeEffectCheckbox, gridToggleCheckbox;
    private JTextField bornField, surviveField;
    private JButton startButton, stopButton, clearButton, stepButton, colorPickerLiving, colorPickerDead;
    private LivingSpace livingSpace;
    private JSpinner sizeSpinner;
    private Color livingColor = Color.blue; ///< Alapértelmezetten kékek az élő cellák
    private Color deadColor = Color.white; ///< Alapértelmezetten fehérek a halott cellák
    private GameAreaPanel gameAreaPanel; ///< A vezérelt GameAreaPanel példány
    private boolean running = false;

    /**
     * Konstruktor, beállítja a GUI elemeit, a hozzájuk tartozó eventlistenereket, valamint a paraméterben átvett GameAreaPanel és LivingSpace példányokat
     * @param livingSpace vezérelt élettér
     * @param gameAreaPanel vezérelt GameAreaPanel amihez az élettér is tartozik
     */
    public GameControlPanel(LivingSpace livingSpace, GameAreaPanel gameAreaPanel, JMenuBar menuBar) {
        this.livingSpace = livingSpace;
        this.gameAreaPanel = gameAreaPanel;
        gameAreaPanel.setColors(livingColor, deadColor);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new LineBorder(Color.black, 1));
        setPreferredSize(new Dimension(250, 600)); // Panel szélessége és magassága

        //0. menübar elemek
        JMenu fileMenu = new JMenu("Fájl");
        JMenuItem saveMenuItem = new JMenuItem("Mentés");
        saveMenuItem.addActionListener(e -> save());
        fileMenu.add(saveMenuItem);

        JMenuItem loadMenuItem = new JMenuItem("Betöltés");
        loadMenuItem.addActionListener(e -> load());
        fileMenu.add(loadMenuItem);

        menuBar.add(fileMenu);

        //1. Élettér mérete:
        JPanel sizeSettingPanel = new JPanel();
        sizeSettingPanel.setLayout(new GridLayout(1, 2));
        sizeSettingPanel.setBorder(new LineBorder(Color.black, 1));
        sizeSettingPanel.setPreferredSize(new Dimension(250, 20));
        sizeSettingPanel.setMaximumSize(new Dimension(250, 20));
        sizeSettingPanel.add(new JLabel("Élettér mérete:"));
        sizeSpinner = new JSpinner(new SpinnerNumberModel(50,10,150,1));
        sizeSettingPanel.add(sizeSpinner);
        add(sizeSettingPanel);
        sizeSpinner.addChangeListener(e -> {
            int newSize = (int) sizeSpinner.getValue();
            if (newSize != livingSpace.getSize()) {
                gameAreaPanel.livingSpaceResize(newSize);
                gameAreaPanel.repaint();
            }
        });

        // 2. Játékszabályok
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

        // 3. Játék sebességét megadó slider
        JPanel speedPanel = new JPanel();
        speedPanel.setBorder(new LineBorder(Color.black, 1));
        speedPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Balra igazítás
        speedPanel.setPreferredSize(new Dimension(250, 80));
        speedPanel.setMaximumSize(new Dimension(250, 80));
        speedPanel.add(new JLabel("Játék sebessége"));
        speedSlider = new JSlider(HORIZONTAL, 1, 10, 10);
        speedSlider.setMajorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedPanel.add(speedSlider);
        add(speedPanel);

        // 4. Fade effekt checkbox
        JPanel fadePanel = new JPanel();
        fadePanel.setPreferredSize(new Dimension(250, 40));
        fadePanel.setMaximumSize(new Dimension(250, 40));
        fadePanel.setBorder(new LineBorder(Color.black, 1));
        fadePanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Balra igazítás
        fadeEffectCheckbox = new JCheckBox("Halott cellák elhalványodása");
        fadePanel.add(fadeEffectCheckbox);
        add(fadePanel);
        fadeEffectCheckbox.addActionListener(e -> {
            gameAreaPanel.setFadeOn(fadeEffectCheckbox.isSelected());
            gameAreaPanel.repaint();
        });

        // 5. Rácsok ki és bekapcsolása
        JPanel gridTogglePanel = new JPanel();
        gridTogglePanel.setPreferredSize(new Dimension(250, 40));
        gridTogglePanel.setMaximumSize(new Dimension(250, 40));
        gridTogglePanel.setBorder(new LineBorder(Color.black, 1));
        gridTogglePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        gridToggleCheckbox = new JCheckBox("Rács ki-be kapcslása");
        gridToggleCheckbox.setSelected(true);
        gridTogglePanel.add(gridToggleCheckbox);
        add(gridTogglePanel);
        gridToggleCheckbox.addActionListener(e -> {
            gameAreaPanel.setGridOn(gridToggleCheckbox.isSelected());
            gameAreaPanel.repaint();
        });

        // 6. Colorpicker gombok
        JPanel colorPanel = new JPanel();
        colorPanel.setPreferredSize(new Dimension(250, 100));
        colorPanel.setMaximumSize(new Dimension(250, 100));
        colorPanel.setBorder(new LineBorder(Color.black, 1));
        colorPanel.setLayout(new GridLayout(2,1));
        colorPanel.add(new JLabel("Cellaszínek:"));
        JPanel colorButtons = new JPanel();
        colorButtons.setLayout(new GridLayout(1,2));
        colorPickerLiving = new JButton("Élő");
        colorPickerLiving.addActionListener(e -> {
            livingColor = JColorChooser.showDialog(null, "Válaszd ki az élő cellák színét!", livingColor);
            gameAreaPanel.setColors(livingColor, deadColor);
            gameAreaPanel.repaint();
        });
        colorPickerDead = new JButton("Halott");
        colorPickerDead.addActionListener(e -> {
            deadColor = JColorChooser.showDialog(null, "Válaszd ki a halott cellák színét!", deadColor);
            gameAreaPanel.setColors(livingColor, deadColor);
            gameAreaPanel.repaint();
        });
        colorButtons.add(colorPickerLiving);
        colorButtons.add(colorPickerDead);
        colorPanel.add(colorButtons);
        add(colorPanel);

        // 7. Gombok: start, stop, clear és step
        JPanel startStopPanel = new JPanel();
        startStopPanel.setPreferredSize(new Dimension(250, 100));
        startStopPanel.setMaximumSize(new Dimension(250, 100));
        startStopPanel.setBorder(new LineBorder(Color.black, 1));
        startStopPanel.setLayout(new GridLayout(2,2));
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        clearButton = new JButton("Clear");
        stepButton = new JButton("Léptet");

        startButton.addActionListener(e -> startGame());

        stopButton.addActionListener(e -> stopGame());

        clearButton.addActionListener(e -> {
            gameAreaPanel.clearLivingSpace();
            gameAreaPanel.repaint();
        });

        stepButton.addActionListener(e -> {
            if(running) return;
            ruleSetter();
            gameAreaPanel.nextState();
            gameAreaPanel.repaint();
        });

        startStopPanel.add(startButton);
        startStopPanel.add(stopButton);
        startStopPanel.add(clearButton);
        startStopPanel.add(stepButton);
        add(startStopPanel);
    }

    /**
     * Szimuláció folyamatos futását implementáló függvény. Sebessége a speedSlider értékének függvénye,
     * amit folyamatosan lekér
     */
    @Override
    public void run() {
        while(running) {
            synchronized (this) {
                try {
                    ruleSetter();
                    wait(1000 / speedSlider.getValue());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (NumberFormatException e) {
                    stopGame();
                    bornField.setText("3");
                    surviveField.setText("2,3");
                    JOptionPane.showMessageDialog(this, "Szabályok formátuma: számjegyek 0-tól 8-ig, vesszővel elválasztva!", "Hiba", JOptionPane.ERROR_MESSAGE);
                }
                gameAreaPanel.nextState();
                gameAreaPanel.repaint();
            }
        }
    }

    /**
     * Megállítja a szimulációt és egy felugró ablakban kiválasztott fájlba elmenti annak jelenlegi állapotát,
     * szerializálással.
     */
    private void save() {
        stopGame();
        SwingUtilities.invokeLater(()-> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            fileChooser.setDialogTitle("Válassz helyet az élettér mentéséhez");

            int result = fileChooser.showSaveDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                if (!selectedFile.getName().endsWith(".dat")) {
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".dat");
                }

                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(selectedFile))) {
                    oos.writeObject(livingSpace);
                    JOptionPane.showMessageDialog(null,
                            "Sikeresen elmentve: " + selectedFile.getAbsolutePath(),
                            "Mentés sikeres",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Hiba történt a mentés során: " + e.getMessage(),
                            "Mentési hiba",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Mentési művelet megszakítva!",
                        "Művelet megszakítva",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    /**
     * Megállítja a szimulációt, egy felugró ablakban kiválasztott .dat kiterjesztésű fájlból betölt egy abba
     * szerializálva mentett élettér példányt, majd frissíti a szükséges referenciákat.
     */
    private void load() {
        stopGame(); // Megállítja a játékot a betöltés előtt
        SwingUtilities.invokeLater(()-> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            fileChooser.setDialogTitle("Válassz fájlt az élettér betöltéséhez");

            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(selectedFile))) {

                    livingSpace = (LivingSpace) ois.readObject();
                    ruleSetter();
                    gameAreaPanel.setLivingSpace(livingSpace);
                    gameAreaPanel.repaint(); // Frissíti a játékterületet
                    sizeSpinner.setValue(livingSpace.getSize());

                    JOptionPane.showMessageDialog(null,
                            "Sikeresen betöltve: " + selectedFile.getAbsolutePath(),
                            "Betöltés sikeres",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Hiba történt a betöltés során: " + e.getMessage(),
                            "Betöltési hiba",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Betöltési művelet megszakítva!",
                        "Művelet megszakítva",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    /**
     * Beállítja az szimuláciü szabályait a két vonatkozó szövegmező tartalma szerint
     */
    private void ruleSetter() {
        List<Integer> bornRules = parseRules(bornField.getText());
        List<Integer> surviveRules = parseRules(surviveField.getText());
        livingSpace.setBornRule(bornRules);
        livingSpace.setSurviveRule(surviveRules);
    }

    /**
     * Elindít egy szálat a szimuláció folyamatos futásához
     */
    private void startGame() {
        if(running) return;
        running = true;
        Thread thread = new Thread(this);
        thread.start();
    }


    /**
     * Megállítja a szimulációt, a hozzá tartozó szálat interruptolja
     */
    private void stopGame() {
        running = false;
        Thread.currentThread().interrupt();
    }

    /**
     * Segédmetódus a szabálymező parseolásához. Szigorúan csak "0,1,2,3..." formátumban fogadja el a szabályokat
     * Ha parseoláskor hiba lép fel, az alapételmezett Conway-féle szabályokat állítja be (B3/S23)
     * @param rulesText String ami a szabályokat tartalmazza
     * @return List a rulesTextből kiválogatott Integerekből
     * @throws NumberFormatException ha sikertelen a parseolás
     */
    private List<Integer> parseRules(String rulesText) throws NumberFormatException{
        String[] ruleStrings = rulesText.split(",");
        List<Integer> rules = new ArrayList<>();
        for (String rule : ruleStrings) {
            int newRule = Integer.parseInt(rule.trim());
            if(0<= newRule && newRule <= 8) {
                rules.add(newRule);
            }else{
                bornField.setText("3");
                surviveField.setText("2,3");
                JOptionPane.showMessageDialog(this, "Szabályok formátuma: számjegyek 0-tól 8-ig, vesszővel elválasztva!", "Hiba", JOptionPane.ERROR_MESSAGE);
            }
        }
        return rules;
    }
}
