package com.nasser.poulet.conquest.controller;

import com.nasser.poulet.conquest.model.*;

/**
 * Created by Lord on 14/12/13.
 */
public class BoardController {
    private Board board;

    private boolean endGameTimer = false;
    private Loyalty winnerLoyalty = null;

    public boolean isEndGameTimer() {
        return endGameTimer;
    }

    public Loyalty getWinnerLoyalty() {
        return winnerLoyalty;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public BoardController( Board board ){
        this.board = board;

        //TODO : Client protection
        if(board.getStateArrayList()[0].size()>0){
            generateUnitSpawnCallback(board.getStateArrayList()[0].get(0));
            generateUnitSpawnCallback(board.getStateArrayList()[1].get(0));
            generateUnitSpawnCallback(board.getStateArrayList()[2].get(0));
        }
    }

    public State select(int posX, int posY) {
        return board.getState(posX, posY);
    }

    public Board getBoard() {
		return board;
	}

    public void checkEndGame(){
        if(board.numberOfEmpty()==10&& !endGameTimer){
            generateEndgameCallback();
            endGameTimer = true;
        }
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

        // Capture callback
        board.eventTurnBaseds.add(new EventTurnBased(1, 4 , state, new Callback<State>(){
            public void methodCallback(State state) {
                if(state.isInCapture()){
                    state.setInCapture(false);
                    if(state.getLoyalty() != state.getProvLoyalty() && state.getLoyalty() != Loyalty.EMPTY && state.getLoyalty() != Loyalty.NONE) {
                        board.getStateArrayList()[state.getLoyalty().ordinal() - 2].remove(state);
                    }
                    state.setLoyalty(state.getProvLoyalty());
                    board.getStateArrayList()[state.getLoyalty().ordinal() - 2].add(state);
                    generateUnitSpawnCallback(state);
                }
            }
        }));
    }

    public void generateEndgameCallback(){
        EventTurnBased temp = new EventTurnBased(1, 120, this, new Callback<BoardController>() {
            public void methodCallback(BoardController boardController) {
                winnerLoyalty = boardController.board.getwinner();
                System.out.println(winnerLoyalty.toString());
            }
        });
        board.eventTurnBaseds.add(temp);
    }


    public void generateUnitSpawnCallback( State state ){
        //state.generateUnitSpawnCallback();
        EventTurnBased temp = new EventTurnBased(-1, 3, state, new Callback<State>() {
            public void methodCallback(State state) {
                if(board.numberOfUnit[state.getLoyalty().ordinal() - 2] < 10) {
                    if(state.addUnit(new Unit(state.getLoyalty()))){
                        board.numberOfUnit[state.getLoyalty().ordinal() - 2]++;
                    }
                }
            }
        });
        state.setEventTurnBased(temp);
        board.eventTurnBaseds.add(temp);
    }

    private void combat( Unit unit, State state){
        // Neutral
        if(state.getLoyalty() == Loyalty.EMPTY){
            if(board.getCivilizationPower(unit.getLoyalty())>board.getCivilizationPower(state.getUnit().getLoyalty())) {
                board.numberOfUnit[state.getUnit(0).getLoyalty().ordinal() - 2]--;
            	state.removeUnit(0);
            }

            else {
                board.numberOfUnit[state.getUnit(1).getLoyalty().ordinal() - 2]--;
            	state.removeUnit(1);
            }
              
        }

        // Attack
        else{
            if(board.getCivilizationPower(unit.getLoyalty())>board.getCivilizationPower(state.getUnit().getLoyalty())*1.2) {
                board.numberOfUnit[state.getUnit(0).getLoyalty().ordinal() - 2]--;
                state.removeUnit(0);
            }
            else {
                board.numberOfUnit[state.getUnit(1).getLoyalty().ordinal() - 2]--;
                state.removeUnit(1);
            }
        }
    }
}
