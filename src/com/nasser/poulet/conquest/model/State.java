package com.nasser.poulet.conquest.model;

import com.nasser.poulet.conquest.controller.Turn;

/**
 * Created by Lord on 10/12/13.
 */
public class State {
    private int posX, posY;
    private int productivity;
    private Loyalty loyalty, provLoyalty;
    private boolean inCapture;
    private int eventUnitCallback;
    private Unit[] units = new Unit[2];

    public State( int x, int y, Loyalty loyalty){
        this.posX = x;
        this.posY = y;
        this.loyalty = loyalty;
        eventUnitCallback = -1;

        this.productivity = 2000 + (int)(Math.random()*4000);
    }

    public int getPosX(){
        return this.posX;
    }

    public int getPosY(){
        return this.posY;
    }

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

    public int getProductivity() {
        return productivity;
    }

    public Loyalty getLoyalty(){
        return this.loyalty;
    }

    public void setLoyalty( Loyalty loyalty){
        this.loyalty = loyalty;
    }

    public int getEventUnitCallback() {
        return eventUnitCallback;
    }

    public void setEventUnitCallback(int eventUnitCallback) {
        this.eventUnitCallback = eventUnitCallback;
    }

    public boolean addUnit( Unit unit ){
        if(this.units[0] == null)   // No units
            this.units[0] = unit;
        else if(this.units[1] == null)  // Only one
            this.units[1] = unit;
        else
            return false;   // Too many units on the state

        return true;
    }

    public void removeUnit( int level ){
        this.units[level] = null;
        if(level == 0 && units[1] != null){
            units[0] = units[1];
            units[1] = null;
        }
    }

    public Unit moveUnit(){
        Unit prov = this.units[0];
        if(this.units[1] != null){
            prov = this.units[1];
            this.units[1] = null;
        }
        else if(this.units[0] != null){
            this.units[0] = null;
        }
        return prov;
    }

    public Unit getUnit(){
        return this.units[0];
    }

    public Unit getUnit( int index ){
        return units[index];
    }

    public boolean canHostUnit(){
        return this.units[1]==null;
    }

    public void generateUnitSpawnCallback(){
        if(eventUnitCallback!=-1) Turn.removeEvent(eventUnitCallback);   // Clear actual Callback
        this.setEventUnitCallback(Turn.addEvent(new Event(-1, productivity, this, new Callback<State>() {
            public void methodCallback(State state) {
                state.addUnit(new Unit(state.getLoyalty()));
            }
        })));
    }
}
