package com.nasser.poulet.conquest.player;

import com.nasser.poulet.conquest.controller.BoardController;
import com.nasser.poulet.conquest.controller.Turn;
import com.nasser.poulet.conquest.model.Board;
import com.nasser.poulet.conquest.model.Callback;
import com.nasser.poulet.conquest.model.Event;
import com.nasser.poulet.conquest.model.Loyalty;

/**
 * Created by Thomas on 12/26/13.
 */
public class IA extends Player {
    public IA( Loyalty loyalty, BoardController board ){
        super(loyalty, board);
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
        for(int i=0;i<20;i++){
            for(int j=0;j<15;j++){
                /*if(UnitContainer.unitBoard[i][j][0] != null){
                    if(UnitContainer.unitBoard[i][j][0].getLoyalty() == this.loyalty){
                        boardC.click(i*40,j*40);
                        boardC.click(i*40,(j+1)*40);
                        return;
                    }
                }*/
            }
        }
    }
}
