package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * JPanelből származtatott osztály, az élettér grafikus megjelenítése a szerepe
 */
public class GameAreaPanel extends JPanel {
    private LivingSpace livingSpace; ///< A panelhoz tartozó élettér példány
    private int cellSize; ///< Egy cella pixelben megadott mérete
    private Color livingColor; ///< Élő cellák színe
    private Color deadColor; ///< Halott cellák színe
    private boolean isFadeOn = false; ///< Halott cellák fokozatos elhalványozásának engedélyezése
    private boolean isGridOn = true; ///< Rács kirajzolásának engedélyezése
    private int lastRow = -1, lastCol = -1; ///< Utoljára rajzolt sor és oszlop
    private boolean isDrawing = false;

    /**
     * GameAreaPanel konstruktora. Beállítja az eventlistenereket az egérműveletekhez (Kattintással rajzolás, görgetéssel zoom)
     * @param livingSpace A panelhez tartozó élettér példány
     * @param initcellSize A kezdeti cellaméret (zoom miatt később változhat)
     */
    public GameAreaPanel(LivingSpace livingSpace, int initcellSize) {
        this.livingSpace = livingSpace;
        this.cellSize = initcellSize;

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    isDrawing = true;
                    toggleCell(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    isDrawing = false;
                    lastRow = -1; // Reset az utolsó cellára
                    lastCol = -1;
                }
            }
        });
        addMouseWheelListener(e -> {
            int rotation = e.getWheelRotation();
            if (rotation < 0) {
                // Görgő felfelé: nagyítás
                cellSize = Math.min(cellSize + 2, 50); // Maximum cellaméret
            } else {
                // Görgő lefelé: kicsinyítés
                cellSize = Math.max(cellSize - 2, 10);  // Minimum cellaméret
            }
            revalidate(); // Méret frissítése
            repaint();    // Újrarajzolás
        });

        // Egérmozgás figyelése
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDrawing) {
                    toggleCell(e);
                }
            }
        });
    }

    private void toggleCell(MouseEvent e) {
        int row = e.getY() / cellSize;
        int col = e.getX() / cellSize;

        if (row >= 0 && col >= 0 && row < livingSpace.getSize() && col < livingSpace.getSize()) {
            if (row != lastRow || col != lastCol) {
                livingSpace.getAt(row, col).flip();
                lastRow = row;
                lastCol = col;
                repaint();
            }
        }
    }

    /**
     * Lépteti a panelhez tartozó életteret, majd frissíti a panel megjelenését.
     */
    public void nextState(){
        livingSpace.nextState();
        repaint();
    }

    /**
     * @return boolean, be van-e kapcsolva az elhalványodás effekt
     */
    public boolean isFadeOn() {
        return isFadeOn;
    }

    /**
     * @return boolean, be van-e kapcsolva a rács kirajzolása
     */
    public boolean isGridOn() {
        return isGridOn;
    }

    /**
     * Beállítja az elhalványodás effekt engedélyezését
     * @param fadeOn effekt állapota
     */
    public void setFadeOn(boolean fadeOn) {
        this.isFadeOn = fadeOn;
    }

    /**
     * Beállítja a rács kirajzolásának engedélyezését
     * @param gridOn rácsrajzolás állapota
     */
    public void setGridOn(boolean gridOn) {
        isGridOn = gridOn;
    }

    /**
     * Beállítja a cellák színeit
     * @param livingColor élő cellák színe
     * @param deadColor halott cellák színe
     */
    public void setColors(Color livingColor, Color deadColor) {
        this.livingColor = livingColor;
        this.deadColor = deadColor;
    }

    /**
     * Frissíti a referenciát a paraméterként megadott új élettérre, és frissíti a hozzá tartozó eventlistenereket
     * @param livingSpace új élettér referenciája
     */
    public void setLivingSpace(LivingSpace livingSpace) {
        this.livingSpace = livingSpace;
        for(MouseListener ml : this.getMouseListeners() ){
            this.removeMouseListener(ml);
        }
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    isDrawing = true;
                    toggleCell(e);
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    isDrawing = false;
                    lastRow = -1; // Reset az utolsó cellára
                    lastCol = -1;
                }
            }
        });
        for(MouseWheelListener mwl : this.getMouseWheelListeners() ){
            this.removeMouseWheelListener(mwl);
        }
        addMouseWheelListener(e -> {
            int rotation = e.getWheelRotation();
            if (rotation < 0) {
                cellSize = Math.min(cellSize + 5, 50); // Maximum cellaméret
            } else {
                cellSize = Math.max(cellSize - 5, 5);  // Minimum cellaméret
            }
            revalidate();
            repaint();
        });
        repaint();
    }

    /**
     * Visszaadja az aktuális cellaméretet
     * @return cellaméret
     */
    public int getCellSize(){
        return cellSize;
    }

    /**
     * Megöli a panelhez tartozó élettér összes celláját
     */
    public void clearLivingSpace(){
        livingSpace.killAll();
    }

    /**
     * Kirajzolja az élettér jelenlegi állapotát a beállított grid és fade változókat figyelembe véve
     * @param g egy Graphics példány ami a rajzolást végzi
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int rows = livingSpace.getSize();
        int cols = livingSpace.getSize();
        g2d.setColor(deadColor);
        g2d.fillRect(0, 0, cols * cellSize, rows * cellSize);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell current = livingSpace.getAt(i, j);
                if(isFadeOn){
                    g2d.setColor(fadeColor(livingColor, current.getDeadSince()));
                }else {
                    if(current.isAlive()){
                        g2d.setColor(livingColor);
                    }else{
                        g2d.setColor(deadColor);
                    }
                }
                g2d.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }
        if(isGridOn) {
            g2d.setColor(Color.LIGHT_GRAY);
            for (int i = 0; i <= rows; i++) {
                g2d.drawLine(0, i * cellSize, cols * cellSize, i * cellSize);
            }
            for (int j = 0; j <= cols; j++) {
                g2d.drawLine(j * cellSize, 0, j * cellSize, rows * cellSize);
            }
        }
    }

    /**
     * Átméretezi a panelhez tartozó életteret
     * @param newSize új méret
     */
    public void livingSpaceResize(int newSize){
        livingSpace.resize(newSize);
    }

    /**
     * Beállítja a panel méretét az élettér és a cellaméret szerint
     * @return Dimension példány a számított méretekkel
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(livingSpace.getSize() * cellSize, livingSpace.getSize() * cellSize);
    }

    /**
     * Egy szín elhalványodását megoldó segédfüggvény
     * @param baseColor módosítani kívánt szín
     * @param deadSince fade mértéke, 0 és 5 között
     * @return kívánt mértékben elhalványított szín
     */
    public static Color fadeColor(Color baseColor, int deadSince) {
        if (deadSince < 0 || deadSince > 17) {
            throw new IllegalArgumentException("deadSince értékének 0 és 5 között kell lennie.");
        }
        int alpha = (int) ((1.0 - (deadSince / 17.0)) * 255);
        return new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), alpha);
    }

}

