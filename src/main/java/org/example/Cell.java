package org.example;

import java.io.Serializable;

/**
 * Szerializálható interfészt megvalósító Cell vagy sejt osztály
 */
public class Cell implements Serializable{
    private boolean isalive; ///< Cella állapota, false ha halott, true ha él
    private int deadSince; ///< Cella halála óta eltelt generációk, a fade effekthez kell
    private static final int MAX_DEAD_SINCE = 17; ///< Konstans, a deadSince max értéke
    /**
     * Konstruktor, alapértelmezetten halottként inicializálja a cellát
     */
    public Cell() {
        isalive = false;
        deadSince = MAX_DEAD_SINCE;
    }

    /**
     * Copy konstruktor
     * @param c másolandó cella
     */
    public Cell(Cell c){
        this.isalive = c.isalive;
        this.deadSince = c.deadSince;
    }

    /**
     * @return a cella állapota
     */
    public boolean isAlive() {
        return isalive;
    }

    /**
     * A jelenlegi állapot ellentétére váltja a cellát.
     */
    public void flip(){
        if (isalive) {
            kill();
        } else {
            setAlive();
        }
    }

    /**
     * Megöli a cellát és egyre állítja a deadSince értékét
     */
    public void kill(){
        isalive = false;
        deadSince = 1;
    }

    public void clearKill(){
        isalive = false;
        deadSince = MAX_DEAD_SINCE;
    }

    /**
     * Növeli a halott cella deadSince értékét, legfeljebb ötig
     */
    public void increaseDeadSince(){
        if(!isalive) {
            deadSince = Math.min(deadSince + 1, MAX_DEAD_SINCE);
        }
    }

    /**
     * Feléleszti a cellát, a deadSince értékét nullára állítja
     */
    public void setAlive() {
        isalive = true;
        deadSince = 0;
    }

    /**
     * Szegélyen levő cellák kezelését segíti, megöli a cellát és a deadSince értékét maxra állítja
     */
    public void borderOperation(){
        kill();
        deadSince = MAX_DEAD_SINCE;
    }
    /**
     * Visszaadja a deadSince értékét
     * @return deadSince értéke
     */
    public int deadSince() { return deadSince; }
}
