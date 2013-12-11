package com.nasser.poulet.conquest.model;

import java.util.Vector;

/**
 * Created by Lord on 10/12/13.
 */
public class Board {
    public Vector<State> stateVector = new Vector<State>();
    public State[][] stateArray = new State[20][15];

    public Board(){
        System.out.println("Board created");
        for(int i=0;i<20;i++){
            for(int j=0;j<15;j++){
                stateArray[i][j] = new State(i,j);
//                stateArray[i][j].val = (int)(Math.random()*4);
                stateArray[i][j].setLoyalty(Loyalty.values()[(int)(Math.random()*2)]);
            }
        }

        // Let's add player start
        stateArray[4][2].setLoyalty(Loyalty.BLUE);
        // Enemy?
        stateArray[9][11].setLoyalty(Loyalty.GREEN);
        stateArray[10][2].setLoyalty(Loyalty.BARBARIAN);
    }

    public void addState( State state ){
        this.stateVector.add(state);
    }
}
