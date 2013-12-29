package com.nasser.poulet.conquest.model;

import com.nasser.poulet.conquest.controller.BoardController;
import com.nasser.poulet.conquest.controller.Turn;

/**
 * Created by Thomas on 12/26/13.
 */
public class IA extends Player {
    private Board board;

    public IA( Loyalty loyalty, Board board ){
        super(loyalty, board);
        this.board = board;
    }

    public void start(){
        Turn.addEvent(new Event(-1, 1000, this, new Callback<IA>() {
            public void methodCallback(IA ia) {
                ia.play();
            }
        }));
    }

    private void play(){
        System.out.println("IA "+this.loyalty+" play");
        if(board.getCivilizationPower(this.loyalty)<15000)
            this.expand();
    }

    private void expand(){
        State state = board.getState(this.loyalty, 0);
        if(state.getUnit() != null){
            this.select(state);
            this.action(state.getPosX()+1, state.getPosY()+1);
        }
    }
}
