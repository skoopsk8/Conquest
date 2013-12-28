package com.nasser.poulet.conquest.model;

/**
 * Created by Thomas on 12/28/13.
 */
public class Human extends Player {
    public Human(Loyalty loyalty, Board board ) {
        super(loyalty, board);
    }

    @Override
    public void start() {

    }

    public void click( int posX, int posY ){
        posX = (int)Math.floor(posX/40);
        posY = (int)Math.floor(posY/40);
        if(this.selected == null)   // No previous selection
            this.select(posX, posY);
        else    // There is a previous selection
            this.action(posX, posY);
    }

}
