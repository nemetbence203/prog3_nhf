package org.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LivingSpace implements Serializable {
    private ArrayList<ArrayList<Cell>> cells;

    //Játékszabály Born - Survive formátumban
    private List<Integer> born;
    private List<Integer> survive;
    private int size;

    public LivingSpace(int size) {
        this.size = size;
        cells = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ArrayList<Cell> row = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                row.add(new Cell());
            }
            cells.add(row);
        }
    }

    public void setBornRule(List<Integer> born) {
        this.born = born;
    }
    public void setSurviveRule(List<Integer> survive) {
        this.survive = survive;
    }

    public void nextState() {

        // Új élettér létrehozása az aktuális cellák másolatával
        ArrayList<ArrayList<Cell>> nextCells = getCells();

        // Minden cella frissítése az új élettérben
        for (int i = 0; i < cells.size(); i++) {
            for (int j = 0; j < cells.get(i).size(); j++) {
                Cell currentCell = cells.get(i).get(j);
                Cell nextCell = nextCells.get(i).get(j);
                if(i == 0 || i == cells.size()-1 || j == 0 || j == cells.get(i).size()-1){
                    nextCell.kill();
                    continue;
                }
                int aliveNeighbors = livingNeighbours(i, j);


                // Új állapot szabályok alapján
                if (currentCell.isAlive() && !survive.contains(aliveNeighbors)) {
                    nextCell.kill();
                } else if (!currentCell.isAlive() && born.contains(aliveNeighbors)) {
                    nextCell.setAlive();
                }
                if (!currentCell.isAlive()) {
                    currentCell.increaseDeadSince();
                }
            }
        }

        cells = nextCells;
    }

    private ArrayList<ArrayList<Cell>> getCells() {
        ArrayList<ArrayList<Cell>> nextCells = new ArrayList<>();
        for (ArrayList<Cell> row : cells) {
            ArrayList<Cell> newRow = new ArrayList<>();
            for (Cell cell : row) {
                Cell newCell = new Cell(); // Új cella azonos állapottal
                if (cell.isAlive()) {
                    newCell.setAlive();
                } else {
                    newCell.kill();
                }
                newRow.add(newCell);
            }
            nextCells.add(newRow);
        }
        return nextCells;
    }

    private int livingNeighbours(int row, int col) {
        int count = 0;
        int[] directions = {-1, 0, 1};

        for (int x : directions) {
            for (int y : directions) {
                if (x == 0 && y == 0) continue;

                int newRow = row + x;
                int newCol = col + y;

                if (newRow >= 0 && newRow < cells.size() &&
                        newCol >= 0 && newCol < cells.get(0).size() &&
                        cells.get(newRow).get(newCol).isAlive()) {
                    count++;
                }
            }
        }

        return count;
    }

    public void resize(int newSize) {
        if (newSize < 1) {
            throw new IllegalArgumentException("The size must be at least 1.");
        }

        ArrayList<ArrayList<Cell>> newCells = new ArrayList<>();

        for (int i = 0; i < newSize; i++) {
            ArrayList<Cell> row = new ArrayList<>();
            for (int j = 0; j < newSize; j++) {
                if (i < cells.size() && j < cells.get(i).size()) {
                    // Másolja a meglévő cellát, ha az index még az eredeti mátrixon belül van
                    row.add(cells.get(i).get(j));
                } else {
                    // Új cella inicializálása az új sorokba/oszlopokba
                    row.add(new Cell());
                }
            }
            newCells.add(row);
        }

        // Frissítsük az élettér méretét
        this.cells = newCells;
        this.size = newSize;
    }

    public Cell getAt(int x, int y) {
        return cells.get(x).get(y);
    }
    public void killAt(int x, int y) {
        cells.get(x).get(y).kill();
    }
    public void reviveAt(int x, int y) {
        cells.get(x).get(y).setAlive();
    }
    public void killAll(){
        for (ArrayList<Cell> row : cells) {
            for (Cell cell : row) {
                cell.kill();
            }
        }
    }

    public int getSize() {
        return size;
    }
}
