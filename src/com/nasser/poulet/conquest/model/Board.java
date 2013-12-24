package com.nasser.poulet.conquest.model;

import java.util.Vector;

/**
 * Created by Lord on 10/12/13.
 */
public class Board {
    public Vector<State> stateVector = new Vector<State>();
    public State[][] stateArray = new State[20][15];
    public Vector<Unit> unitVector = new Vector<Unit>();

    public Board(){
        System.out.println("Board created");
        int[][] map = Perlin.generateMap(20,15,0.5f);
        for(int i=0;i<20;i++){
            for(int j=0;j<15;j++){
                stateArray[i][j] = new State(i,j);
//                stateArray[i][j].val = (int)(Math.random()*4);
//                stateArray[i][j].setLoyalty(Loyalty.values()[(int)(Math.random()*2)]);
                stateArray[i][j].setLoyalty(Loyalty.values()[map[i][j]]);
            }
        }

        int[][] playerPos = StartPositions.place(stateArray, 20, 15);

        // Let's add player start
        stateArray[playerPos[0][0]][playerPos[0][1]].setLoyalty(Loyalty.BLUE);
        stateArray[playerPos[1][0]][playerPos[1][1]].setLoyalty(Loyalty.YELLOW);
        stateArray[playerPos[2][0]][playerPos[2][1]].setLoyalty(Loyalty.GREEN);

        unitVector.add(new Unit(4,2,Loyalty.BLUE));
    }

    public void addState( State state ){
        this.stateVector.add(state);
    }
}
