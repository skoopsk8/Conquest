package com.nasser.poulet.conquest.controller;

import com.nasser.poulet.conquest.model.Board;
import com.nasser.poulet.conquest.model.Loyalty;
import com.nasser.poulet.conquest.model.Unit;

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
            for(Unit unit: board.unitVector){
                if(unit.isUnit(clickX, clickY))
                    selectedUnit = unit;
            }
        }
        else{
            selectedUnit.setPosX(clickX);
            selectedUnit.setPosY(clickY);

            // Capture
            if(selectedUnit.getLoyalty()!=board.stateArray[clickX][clickY].getLoyalty() && board.stateArray[clickX][clickY].getLoyalty()!= Loyalty.NONE){
                board.stateArray[clickX][clickY].setLoyalty(selectedUnit.getLoyalty());
            }

            selectedUnit=null;
        }
    }

    public void pause(){

    }
}
