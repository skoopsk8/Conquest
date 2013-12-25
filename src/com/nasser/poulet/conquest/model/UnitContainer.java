package com.nasser.poulet.conquest.model;

/**
 * Created by Thomas on 12/25/13.
 */
public class UnitContainer {
    public static Unit[][][] unitBoard = new Unit[20][15][2];

    public static void addUnit(Unit unit){
        if(unitBoard[unit.getPosX()][unit.getPosY()][0]==null){
            unitBoard[unit.getPosX()][unit.getPosY()][0]=unit;
        }
        else if(unitBoard[unit.getPosX()][unit.getPosY()][1]==null){
            unitBoard[unit.getPosX()][unit.getPosY()][1]=unit;
        }
        else return;
    }

    public static boolean move( Unit unit, int posX, int posY){
        if(unitBoard[posX][posY][0]==null){
            unitBoard[unit.getPosX()][unit.getPosY()][0]=null;
            unitBoard[posX][posY][0]=unit;
            if(unitBoard[unit.getPosX()][unit.getPosY()][1]!=null){
                unitBoard[unit.getPosX()][unit.getPosY()][0]=unitBoard[unit.getPosX()][unit.getPosY()][1];
                unitBoard[unit.getPosX()][unit.getPosY()][1]=null;
                unitBoard[posX][posY][0].posX=posX;
                unitBoard[posX][posY][0].posY=posY;
            }
            unit.posX=posX;
            unit.posY=posY;
            return true;
        }
        else if(unitBoard[posX][posY][1]==null){
            unitBoard[unit.getPosX()][unit.getPosY()][1]=null;
            unitBoard[posX][posY][1]=unit;
            unit.posX=posX;
            unit.posY=posY;

            return true;
        }
        else return false;
    }
}
