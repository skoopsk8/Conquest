package com.nasser.poulet.conquest.model;

import com.nasser.poulet.conquest.controller.Turn;

import java.util.Vector;

/**
 * Created by Lord on 10/12/13.
 */
public class Board {
    public State[][] stateArray = new State[20][15];
   // private UnitContainer unitContainer = new UnitContainer();
    static public Vector<Unit> unitVector = new Vector<Unit>();

    public Board(){
        System.out.println("Board created");
        int[][] map = Perlin.generateMap(20,15,0.5f);
        for(int i=0;i<20;i++){
            for(int j=0;j<15;j++){
                stateArray[i][j] = new State(i,j);
                stateArray[i][j].setLoyalty(Loyalty.values()[map[i][j]]);
            }
        }

        int[][] playerPos = StartPositions.place(stateArray, 20, 15);

        // Let's add player start
        stateArray[playerPos[0][0]][playerPos[0][1]].setLoyalty(Loyalty.BLUE);
        stateArray[playerPos[1][0]][playerPos[1][1]].setLoyalty(Loyalty.YELLOW);
        stateArray[playerPos[2][0]][playerPos[2][1]].setLoyalty(Loyalty.GREEN);

        for(int i=0; i<3; i++){
            Turn.addEvent(new Event(-1, stateArray[playerPos[i][0]][playerPos[i][1]].productivity, stateArray[playerPos[i][0]][playerPos[i][1]], new Callback<State>() {
                public void methodCallback(State state) {
                    UnitContainer.addUnit(new Unit(state.getPosX(), state.getPosY(), state.getLoyalty()));
                }
            }));
        }
    }
}
