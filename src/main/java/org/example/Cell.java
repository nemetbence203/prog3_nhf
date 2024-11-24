package org.example;

import java.io.Serializable;

public class Cell implements Serializable{
    private boolean isalive;
    private int deadSince;
    public Cell() {
        isalive = false;
        deadSince = 0;
    }
    public boolean isAlive() {
        return isalive;
    }
    public void flip(){
        isalive = !isalive;
        if(!isalive){
            deadSince = 1;
        }
    }
    public void kill(){
        isalive = false;
        deadSince = 1;
    }
    public void increaseDeadSince(){
        if(deadSince > 0) {
            deadSince = Math.min(deadSince + 1, 5);
        }
    }

    public void setAlive() {
        isalive = true;
        deadSince = 0;
    }
    public int deadSince() { return deadSince; }
}
