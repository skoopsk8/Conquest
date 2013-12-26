package com.nasser.poulet.conquest.model;

/**
 * Created by Lord on 10/12/13.
 */
public class State {
    private int posX, posY;
    public int productivity = 0;
    public Loyalty loyalty;
    private boolean inCapture;

    public boolean isInCapture() {
        return inCapture;
    }

    public void setInCapture(boolean inCapture) {
        this.inCapture = inCapture;
    }

    public Loyalty getProvLoyalty() {
        return provLoyalty;
    }

    public void setProvLoyalty(Loyalty provLoyalty) {
        this.provLoyalty = provLoyalty;
    }

    public Loyalty provLoyalty;

    public State( int x, int y){
        this.posX = x;
        this.posY = y;

        this.productivity = 2000 + (int)(Math.random()*4000);
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
