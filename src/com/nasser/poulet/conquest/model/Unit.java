package com.nasser.poulet.conquest.model;

/**
 * Created by Lord on 11/12/13.
 */
public class Unit {
    public int posX;
    public int posY;
    private Loyalty loyalty;

    public Loyalty getLoyalty() {
        return loyalty;
    }

    public void setLoyalty(Loyalty loyalty) {
        this.loyalty = loyalty;
    }

    public Unit(int posX, int posY, Loyalty loyalty) {
        this.posX = posX;
        this.posY = posY;
        this.loyalty = loyalty;

    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public boolean isUnit( int x, int y ){
        if(this.posX == x && this.posY == y) return true;
        return false;
    }
}
