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
    public void kill(){
        isalive = false;
    }
    public void setAlive() {
        isalive = true;
    }
}
