package org.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * LivingSpace vagy magyarul élettér osztály. Tartalmazza a sejtautomata mátrixát, annak méretét és celláit,
 * a szimuláció szabályait. A Serializable interfészt megvalósítja, a fájlba menthetőség érdekében
 */
public class LivingSpace implements Serializable {
    private ArrayList<ArrayList<Cell>> cells; ///< Cellákból álló kétdimenziós ArrayList
    private List<Integer> born; ///< Születési szabályok
    private List<Integer> survive; ///< Túlélési szabályok
    private int size; ///< Élettér mérete

    /**
     * LivingSpace konstruktora. Minden cellát halottként inicializál
     * @param size Az élettér mérete. Mivel mindig négyzetes, csak egy paraméter
     */
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

    /**
     * Beállítja a születési szabályokat
     * @param born Egy egész számokból álló lista.
     */
    public void setBornRule(List<Integer> born) {
        this.born = born;
    }

    /**
     * Beállítja a túlélési szabályokat
     * @param survive Egy egész számokból álló lista
     */
    public void setSurviveRule(List<Integer> survive) {
        this.survive = survive;
    }

    /**
     * Következő állapotba lépteti az életteret az aktuális szabályok szerint
     * A szegélyen levő cellákat minden körben megöli, ezzel adva határt az élettérnek
     */
    public void nextState() {

        ArrayList<ArrayList<Cell>> nextCells = getCells();
        for (int i = 0; i < cells.size(); i++) {
            for (int j = 0; j < cells.get(i).size(); j++) {
                Cell currentCell = cells.get(i).get(j);
                Cell nextCell = nextCells.get(i).get(j);
                if(i == 0 || i == cells.size()-1 || j == 0 || j == cells.get(i).size()-1){
                    nextCell.borderOperation();
                    continue;
                }
                int aliveNeighbors = livingNeighbours(i, j);

                if (currentCell.isAlive() && !survive.contains(aliveNeighbors)) {
                    nextCell.kill();
                } else if (!currentCell.isAlive() && born.contains(aliveNeighbors)) {
                    nextCell.setAlive();
                }else if(!currentCell.isAlive()){
                    nextCell.increaseDeadSince();
                }
            }
        }
        cells = nextCells;
    }

    /**
     * Visszaad egy másolatot az élettér celláiról
     * @return Kétdimenziós ArrayList az élettér aktuális állapotát reprezentálva
     */
    private ArrayList<ArrayList<Cell>> getCells() {
        ArrayList<ArrayList<Cell>> nextCells = new ArrayList<>();
        for (ArrayList<Cell> row : cells) {
            ArrayList<Cell> newRow = new ArrayList<>();
            for (Cell cell : row) {
                Cell newCell = new Cell(cell);
                newRow.add(newCell);
            }
            nextCells.add(newRow);
        }
        return nextCells;
    }

    /**
     * Összeszámolja egy koordinátáival megadott cella élő szomszédait
     * @param row Cella Y koordinátája
     * @param col Cella X koordinátája
     * @return Élő szomszédok száma
     */
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

    /**
     * Átméretezi az életteret. A bal felső sarok felé zsugorítja vagy a jobb alsó sarok felé nyújtja.
     * Átmásolja a jelenlegi állapotnak azt a részét, ami belefér az új élettérbe
     * @param newSize Az élettér új mérete
     */
    public void resize(int newSize) {
        if (newSize < 1) {
            throw new IllegalArgumentException("Érvénytelen méret: "+newSize);
        }

        ArrayList<ArrayList<Cell>> newCells = new ArrayList<>();

        for (int i = 0; i < newSize; i++) {
            ArrayList<Cell> row = new ArrayList<>();
            for (int j = 0; j < newSize; j++) {
                if (i < cells.size() && j < cells.get(i).size()) {
                    row.add(cells.get(i).get(j));
                } else {
                    row.add(new Cell());
                }
            }
            newCells.add(row);
        }

        this.cells = newCells;
        this.size = newSize;
    }

    /**
     * Visszaad egy koordinátáival megadott cellát.
     * @param x X kooridnáta
     * @param y Y koordináta
     * @return Cella a megadott helyen
     */
    public Cell getAt(int x, int y) {
        return cells.get(x).get(y);
    }

    /**
     * Megöli a koordinátáival megadott cellát
     * @param x X koordináta
     * @param y Y koordináta
     */
    public void killAt(int x, int y) {
        cells.get(x).get(y).kill();
    }

    /**
     * Feléleszti a koordinátáival megadott cellát
     * @param x X koordináta
     * @param y Y koordináta
     */
    public void reviveAt(int x, int y) {
        cells.get(x).get(y).setAlive();
    }

    /**
     * Megöli az élettér összes celláját
     */
    public void killAll(){
        for (ArrayList<Cell> row : cells) {
            for (Cell cell : row) {
                cell.clearKill();
            }
        }
    }

    /**
     * Visszaadja az élettér méretét
     * @return Az élettér mérete
     */
    public int getSize() {
        return size;
    }
}
