package com.nasser.poulet.conquest.player;

import com.nasser.poulet.conquest.model.Board;
import com.nasser.poulet.conquest.model.Loyalty;
import com.nasser.poulet.conquest.player.Player;

/**
 * Created by Thomas on 12/28/13.
 */
public class Human extends Player {
    public int fromPosX, fromPosY, toPosX, toPosY;

    public Human(Loyalty loyalty, Board board ) {
        super(loyalty, board);
    }

    @Override
    public void start() {

    }

    public boolean click( int posX, int posY, int tileSize ){
        posX = (int)Math.floor(posX/tileSize);
        posY = (int)Math.floor(posY/tileSize);
        System.out.println(posX +" "+ posY);

        if(this.selected == null){   // No previous selection
            if(this.select(posX, posY)){
                fromPosX = posX;
                fromPosY = posY;
                System.out.println("Select Click");
            }
        }
        else{    // There is a previous selection
            //this.action(posX, posY);
            toPosX = posX;
            toPosY = posY;
            System.out.println("Action click");
            return true;
        }
        return false;
    }

}
