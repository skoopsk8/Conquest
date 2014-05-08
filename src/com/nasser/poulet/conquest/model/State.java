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
    private Event eventUnitCallback;
    private Unit[] units = new Unit[2];

    public State( int x, int y, Loyalty loyalty){
        this.posX = x;
        this.posY = y;
        this.loyalty = loyalty;
        eventUnitCallback = null;

        this.productivity = 2000 + (int)(Math.random()*4000);
    }

    public State( int x, int y, Loyalty loyalty, int productivity){
        this.posX = x;
        this.posY = y;
        this.loyalty = loyalty;
        eventUnitCallback = null;

        this.productivity = productivity;
    }

    @Override
	public boolean equals(Object arg) {
    	if (this.getPosX() == ((State) arg).getPosX() && this.getPosY() == ((State) arg).getPosY()) {
    		return true;
    	}
    	else {
    		return false;
    	}
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

    public void setProductivity(int productivity) {
        this.productivity = productivity;
    }

    public Loyalty getLoyalty(){
        return this.loyalty;
    }

    public void setLoyalty( Loyalty loyalty){
        this.loyalty = loyalty;
    }

    public Event getEventUnitCallback() {
        return eventUnitCallback;
    }

    public void setEventUnitCallback(Event eventUnitCallback) {
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
        if(eventUnitCallback!=null) Turn.removeEvent(eventUnitCallback);   // Clear actual Callback
        this.setEventUnitCallback(Turn.addEvent(new Event(-1, productivity, this, new Callback<State>() {
            public void methodCallback(State state) {
            	if(Board.numberOfUnit[state.getLoyalty().ordinal() - 2] < 10) {
            		if(state.addUnit(new Unit(state.getLoyalty()))){
     	        	   Board.numberOfUnit[state.getLoyalty().ordinal() - 2]++;
                        System.out.println("Spawned Unit");
                    }
            	}
            }
        })));
    }
}
