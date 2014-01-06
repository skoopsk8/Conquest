package com.nasser.poulet.conquest.controller;

import com.nasser.poulet.conquest.model.*;

/**
 * Created by Lord on 14/12/13.
 */
public class BoardController {
    private Board board;

    public BoardController( Board board ){
        this.board = board;
    }

    public State select(int posX, int posY) {
        return board.getState(posX, posY);
    }
    
    

    public Board getBoard() {
		return board;
	}

	public boolean action( State selectedState, int posX, int posY ){
        if(board.getState(posX, posY).canHostUnit()){
            Unit actionUnit = this.move(selectedState, posX, posY);

            // Combat
            if(actionUnit != null){
                if(board.getState(posX, posY).getUnit()!=null){
                    if(board.getState(posX, posY).getUnit().getLoyalty() != actionUnit.getLoyalty()){
                        this.combat(actionUnit, board.getState(posX, posY));
                    }
                }

                // Capture
                if(board.getState(posX, posY).getLoyalty() != Loyalty.NONE && board.getState(posX, posY).getLoyalty() != actionUnit.getLoyalty() && board.getState(posX, posY).getUnit() == actionUnit){
                    this.capture(actionUnit, board.getState(posX, posY));
                }
            }
            else
                return false;

            return true;
        }
        return false;
    }

    private Unit move( State state, int posX, int posY ){
        state.setInCapture(false);   // Abort capture
        Unit unit = state.moveUnit();
        board.getState(posX, posY).addUnit(unit);
        return unit;
    }

    private void capture( Unit unit, State state ){
        state.setProvLoyalty(unit.getLoyalty());
        state.setInCapture(true);
        Turn.addEvent(new com.nasser.poulet.conquest.model.Event(1, state.getProductivity() , state, new Callback<State>(){
            public void methodCallback(State state) {
            if(state.isInCapture()){
                state.setInCapture(false);
                state.setLoyalty(state.getProvLoyalty());
                state.generateUnitSpawnCallback();
            }
            }
        }));
    }

    private void combat( Unit unit, State state){
        System.out.println("Fight : " + board.getCivilizationPower(unit.getLoyalty()) + " vs " +board.getCivilizationPower(state.getUnit().getLoyalty()));

        // Neutral
        if(state.getLoyalty() == Loyalty.EMPTY){
            if(board.getCivilizationPower(unit.getLoyalty())>board.getCivilizationPower(state.getUnit().getLoyalty())) {
                Board.numberOfUnit[state.getUnit(0).getLoyalty().ordinal() - 2]--;
            	state.removeUnit(0);
            }

            else {
                Board.numberOfUnit[state.getUnit(1).getLoyalty().ordinal() - 2]--;
            	state.removeUnit(1);
            }
              
        }

        // Attack
        else{
            if(board.getCivilizationPower(unit.getLoyalty())>board.getCivilizationPower(state.getUnit().getLoyalty())*1.2) {
                Board.numberOfUnit[state.getUnit(0).getLoyalty().ordinal() - 2]--;
                state.removeUnit(0);
            }
            else {
                Board.numberOfUnit[state.getUnit(1).getLoyalty().ordinal() - 2]--;
                state.removeUnit(1);
            }
        }
    }
}
