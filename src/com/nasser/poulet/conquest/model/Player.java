package com.nasser.poulet.conquest.model;

import com.nasser.poulet.conquest.controller.BoardController;

/**
 * Created by Thomas on 12/28/13.
 */
public abstract class Player {
    protected Loyalty loyalty = null;
    protected BoardController boardController;
    protected State selected;

    public Player( Loyalty loyalty, Board board ){
        this.loyalty = loyalty;
        this.boardController = new BoardController(board);
    }

    public abstract void start();

    public Loyalty getLoyalty(){
        return this.loyalty;
    }

    public void abort(){
        this.selected = null;
    }

    protected void select( int posX, int posY ){
        this.selected = boardController.select(posX, posY);
        if(this.selected.getUnit() != null){    // One unit
            if(this.selected.getUnit().getLoyalty() != this.loyalty){ // Unit 0 different loyalty try the nex one ;)
                if(this.selected.getUnit(1) != null){
                    if(this.selected.getUnit(1).getLoyalty() != this.loyalty)  // Unit 1 also different loyalty = ABORT
                        this.abort();
                }
                else
                    this.abort();
            }
        }
        else
            this.abort();
    }

    protected void select( State state ){
        this.selected = state;
    }

    protected void action( int posX, int posY ){
        if(this.boardController.action(selected, posX, posY))
            this.abort();
    }
}
