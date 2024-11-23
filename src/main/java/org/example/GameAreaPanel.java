package org.example;

import javax.swing.*;
import java.awt.*;

public class GameAreaPanel extends JPanel {
    private final LivingSpace livingSpace;
    private final int cellSize; // Egy cella mérete (pixel)

    public GameAreaPanel(LivingSpace livingSpace, int cellSize) {
        this.livingSpace = livingSpace;
        this.cellSize = cellSize;
    }

    public int getCellSize(){
        return cellSize;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.LIGHT_GRAY); // Rács színe

        int rows = livingSpace.getSize();
        int cols = livingSpace.getSize();

        // Rácsvonalak rajzolása
        for (int i = 0; i <= rows; i++) {
            g2d.drawLine(0, i * cellSize, cols * cellSize, i * cellSize); // Vízszintes vonalak
        }
        for (int j = 0; j <= cols; j++) {
            g2d.drawLine(j * cellSize, 0, j * cellSize, rows * cellSize); // Függőleges vonalak
        }

        // Cellák rajzolása
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (livingSpace.getAt(i, j).isAlive()) {
                    g2d.setColor(Color.BLUE); // Élő cellák színe
                    g2d.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        // A panel méretének beállítása az élettér mérete alapján
        return new Dimension(livingSpace.getSize() * cellSize, livingSpace.getSize() * cellSize);
    }
}

