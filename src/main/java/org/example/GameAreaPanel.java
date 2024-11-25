package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameAreaPanel extends JPanel {
    private LivingSpace livingSpace;
    private final int cellSize; // Egy cella mérete (pixel)
    private Color livingColor;
    private Color deadColor;
    private boolean isFadeOn = false;
    private boolean isGridOn = true;

    public GameAreaPanel(LivingSpace livingSpace, int cellSize) {
        this.livingSpace = livingSpace;
        this.cellSize = cellSize;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = e.getY() / cellSize;
                int col = e.getX() / cellSize;
                if (row < livingSpace.getSize() && col < livingSpace.getSize()) {
                    livingSpace.getAt(row, col).flip();
                    repaint();
                }
            }
        });
    }

    public void nextState(){
        livingSpace.nextState();
        repaint();
    }

    public boolean isFadeOn() {
        return isFadeOn;
    }

    public boolean isGridOn() {
        return isGridOn;
    }

    public void setFadeOn(boolean fadeOn) {
        this.isFadeOn = fadeOn;
    }

    public void setGridOn(boolean gridOn) {
        isGridOn = gridOn;
    }

    public void setColors(Color livingColor, Color deadColor) {
        this.livingColor = livingColor;
        this.deadColor = deadColor;
    }
    public void setLivingSpace(LivingSpace livingSpace) {
        this.livingSpace = livingSpace;
        for(MouseListener ml : this.getMouseListeners() ){
            this.removeMouseListener(ml);
        }
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = e.getY() / cellSize;
                int col = e.getX() / cellSize;
                if (row < livingSpace.getSize() && col < livingSpace.getSize()) {
                    livingSpace.getAt(row, col).flip();
                    repaint();
                }
            }
        });
        repaint();
    }
    public int getCellSize(){
        return cellSize;
    }

    public void clearLivingSpace(){
        livingSpace.killAll();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int rows = livingSpace.getSize();
        int cols = livingSpace.getSize();


        // Cellák rajzolása
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell current = livingSpace.getAt(i, j);
                if (current.isAlive()) {
                    g2d.setColor(livingColor); // Élő cellák színe
                    g2d.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                }else{
                    if(isFadeOn && current.deadSince() > 0 && current.deadSince() <= 5){
                        g2d.setColor(interpolateColor(livingColor, deadColor, current.deadSince()));
                        /// TODO valami itt elbaszódik
                    }else {
                        g2d.setColor(deadColor);
                    }
                    g2d.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                }
            }
        }
        g2d.setColor(Color.LIGHT_GRAY); // Rács színe
        // Rácsvonalak rajzolása
        if(isGridOn) {
            for (int i = 0; i <= rows; i++) {
                g2d.drawLine(0, i * cellSize, cols * cellSize, i * cellSize); // Vízszintes vonalak
            }
            for (int j = 0; j <= cols; j++) {
                g2d.drawLine(j * cellSize, 0, j * cellSize, rows * cellSize); // Függőleges vonalak
            }
        }
    }

    public void livingSpaceResize(int newSize){
        livingSpace.resize(newSize);
    }

    @Override
    public Dimension getPreferredSize() {
        // A panel méretének beállítása az élettér mérete alapján
        return new Dimension(livingSpace.getSize() * cellSize, livingSpace.getSize() * cellSize);
    }

    public static Color interpolateColor(Color livingColor, Color deadColor, int deadSince) {
        // deadSince értékének arányosítása 0 és 1 között
        float t = Math.clamp(deadSince / 5.0f, 0, 1);
        // Színek RGB komponenseinek interpolációja
        int red = (int) (livingColor.getRed() * (1 - t) + deadColor.getRed() * t);
        int green = (int) (livingColor.getGreen() * (1 - t) + deadColor.getGreen() * t);
        int blue = (int) (livingColor.getBlue() * (1 - t) + deadColor.getBlue() * t);

        // Interpolált szín visszaadása
        return new Color(red, green, blue);
    }
}

