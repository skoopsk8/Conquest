package com.nasser.poulet.conquest.model;

/**
 * Created by Lord on 10/12/13.
 */
public class State {
    private int posX, posY;
    public int val = 0;
    public Loyalty loyalty;

    public State( int x, int y){
        this.posX = x;
        this.posY = y;
    }

    public int getPosX(){
        return this.posX;
    }
    public int getPosY(){
        return this.posY;
    }

    public Loyalty getLoyalty(){
        return this.loyalty;
    }
    public void setLoyalty( Loyalty loyalty){
        this.loyalty = loyalty;
    }
}
