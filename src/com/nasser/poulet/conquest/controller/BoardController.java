package com.nasser.poulet.conquest.controller;

import com.nasser.poulet.conquest.model.*;

/**
 * Created by Lord on 14/12/13.
 */
public class BoardController {
    private Board board;
    private Unit selectedUnit;

    public BoardController( Board board ){
        this.board = board;
    }

    public void click( int x, int y ){
        int clickX = (int)Math.floor(x/40);
        int clickY = (int)Math.floor(y/40);
        System.out.println("Click on loyalty " + board.stateArray[clickX][clickY].loyalty.ordinal());
        if(selectedUnit==null){
            for(int k=0;k<1;k++){
                if(UnitContainer.unitBoard[clickX][clickY][k]!=null){
                    selectedUnit = UnitContainer.unitBoard[clickX][clickY][k];
                }
            }
        }
        else{
            UnitContainer.move(selectedUnit,clickX,clickY);

            // Capture
            if(selectedUnit.getLoyalty()!=board.stateArray[clickX][clickY].getLoyalty() && board.stateArray[clickX][clickY].getLoyalty()!= Loyalty.NONE){
                board.stateArray[clickX][clickY].setProvLoyalty(selectedUnit.getLoyalty());
                Turn.addEvent(new com.nasser.poulet.conquest.model.Event(1, board.stateArray[clickX][clickY].productivity , board.stateArray[clickX][clickY], new Callback<State>(){
                    public void methodCallback(State state) {
                        state.setLoyalty(state.getProvLoyalty());
                        Turn.addEvent(new Event(-1, state.productivity, state, new Callback<State>() {
                            public void methodCallback(State state) {
                                UnitContainer.addUnit(new Unit(state.getPosX(), state.getPosY(), state.getLoyalty()));
                            }
                        }));
                    }
                }));
            }

            selectedUnit=null;
        }
    }

    public void pause(){

    }
}
