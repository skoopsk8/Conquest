package com.nasser.poulet.conquest.controller;

import com.nasser.poulet.conquest.model.*;

/**
 * Created by Lord on 14/12/13.
 */
public class BoardController {
    private Board board;
    //private Unit selectedUnit;
    private State selectedState;


    public BoardController( Board board ){
        this.board = board;
    }

    public void click( int x, int y ){

        int clickX = (int)Math.floor(x/40);
        int clickY = (int)Math.floor(y/40);
        System.out.println("Click on loyalty " + board.getState(clickX, clickY).getLoyalty().ordinal());
        if(selectedState==null){
            if(board.getState(clickX, clickY).getUnit()!=null){
                selectedState = board.getState(clickX, clickY);
            }
        }
        else{
            if(board.getState(clickX, clickY).canHostUnit()){   // Unit action only if we can move the unit
                // Combat
                if(board.getState(clickX, clickY).getUnit()!=null){
                    if(board.getState(clickX, clickY).getUnit().getLoyalty()!=selectedState.getUnit().getLoyalty()){
                        System.out.println("Combat");
                    }
                }

                // Capture
                if(selectedState.getUnit().getLoyalty()!=board.getState(clickX, clickY).getLoyalty() && board.getState(clickX, clickY).getLoyalty()!= Loyalty.NONE){
                    board.getState(clickX, clickY).setProvLoyalty(selectedState.getUnit().getLoyalty());
                    board.getState(clickX, clickY).setInCapture(true);
                    Turn.addEvent(new com.nasser.poulet.conquest.model.Event(1, board.getState(clickX, clickY).getProductivity() , board.getState(clickX, clickY), new Callback<State>(){
                        public void methodCallback(State state) {
                            if(state.isInCapture()){
                                state.setInCapture(false);
                                state.setLoyalty(state.getProvLoyalty());
                                state.getEventUnitCallback();
                            }
                        }
                    }));
                }

                // Finally move the unit
                selectedState.setInCapture(false);   // Abort capture
                board.getState(clickX, clickY).addUnit(selectedState.moveUnit());
            }

            selectedState=null;
        }
    }

    public void pause(){

    }
}
