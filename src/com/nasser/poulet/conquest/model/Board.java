package com.nasser.poulet.conquest.model;

import com.nasser.poulet.conquest.controller.Turn;

import java.util.ArrayList;

/**
 * Created by Lord on 10/12/13.
 */

public class Board {
    // Board representation
    public State[][] stateArray;    // For easy pathfinding and generation
    private ArrayList<State> stateArrayList[] = new ArrayList[3];    // For easy gamelogic
    public boolean lock = false;

    private int boardWidth, boardHeight;

    public static int[] numberOfUnit = {0, 0, 0};

    public Board( int width, int height, boolean generate ){
        this.boardWidth = width;
        this.boardHeight = height;

        // Init Board Array
        stateArray = new State[this.boardWidth][this.boardHeight];

        // Init Array List
        stateArrayList[0] = new ArrayList<State>(); // Blue
        stateArrayList[1] = new ArrayList<State>(); // Yellow
        stateArrayList[2] = new ArrayList<State>(); // Green

        if(generate){
            // Generate Board
            generateBoard(boardWidth, boardHeight);

            // Generate Player start
            generateStartState();
        }
    }

    public Board(){
         // Init Array List
        stateArrayList[0] = new ArrayList<State>(); // Blue
        stateArrayList[1] = new ArrayList<State>(); // Yellow
        stateArrayList[2] = new ArrayList<State>(); // Green
    }

    private void generateBoard( int width, int height ){
        int[][] map = Perlin.generateMap(width, height, 0.5f);
        for(int i=0;i<width;i++) for (int j = 0; j < height; j++) stateArray[i][j] = new State(i, j, Loyalty.values()[map[i][j]]);
    }

    private void generateStartState(){
        int[][] playerPos = StartPositions.place(stateArray, this.boardWidth, this.boardHeight);   // Generation

        // Let's add player start
        this.addState(Loyalty.BLUE, playerPos[0][0], playerPos[0][1]);
        this.addState(Loyalty.YELLOW, playerPos[1][0], playerPos[1][1]);
        this.addState(Loyalty.GREEN, playerPos[2][0], playerPos[2][1]);
    }

    public void generateUnitSpawnCallback( State state ){
        state.generateUnitSpawnCallback();
    }

    public State addState( Loyalty loyalty, int posX, int posY ){
        stateArray[posX][posY] = new State(posX, posY, loyalty);
        stateArrayList[loyalty.ordinal()-2].add(stateArray[posX][posY]);
        generateUnitSpawnCallback(stateArray[posX][posY]);
        return stateArray[posX][posY];
    }

    public State addState( Loyalty loyalty, int posX, int posY, int productivity){
        stateArray[posX][posY] = new State(posX, posY, loyalty, productivity);
        stateArrayList[loyalty.ordinal()-2].add(stateArray[posX][posY]);
        generateUnitSpawnCallback(stateArray[posX][posY]);
        return stateArray[posX][posY];
    }

    public State replaceState( Loyalty loyalty, int posX, int posY ){
        stateArrayList[stateArray[posX][posY].getLoyalty().ordinal()-2].remove(stateArray[posX][posY]);
        return stateArray[posX][posY];
    }

    public float getCivilizationPower( Loyalty loyalty ){
        float power = 0;
        for(State state :  stateArrayList[loyalty.ordinal()-2]) power += state.getProductivity();
        return power;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public State getState( int posX, int posY) {
        return stateArray[posX][posY];
    }

    public ArrayList<State>[] getStateArrayList() {
        return stateArrayList;
    }

    public int[][] explodeBoard(){
        int[][] returnValue = new int[boardWidth][boardHeight];

        for(int i=0;i<boardWidth;i++) for (int j = 0; j < boardHeight; j++) returnValue[i][j] = stateArray[i][j].getLoyalty().ordinal();

        return returnValue;
    }

    public int[][] explodeUnits(){
        int[][] returnValue = new int[boardWidth][boardHeight];

        for(int i=0;i<boardWidth;i++){
            for (int j = 0; j < boardHeight; j++){
                returnValue[i][j] = 0;
                if(stateArray[i][j].getUnit(0) != null){
                    returnValue[i][j] = stateArray[i][j].getUnit(0).getLoyalty().ordinal();
                    if(stateArray[i][j].getUnit(1) != null)
                        returnValue[i][j] *= 3;
                }
            }
        }

        return returnValue;
    }

    public int[][] explodeProductivity(){
        int[][] returnValue = new int[boardWidth][boardHeight];

        for(int i=0;i<boardWidth;i++) for (int j = 0; j < boardHeight; j++) returnValue[i][j] = stateArray[i][j].getProductivity();

        return returnValue;
    }

    public void formatUnit(int width, int height, int[][] unit){
        while(lock){}
        this.boardWidth = width;
        this.boardHeight = height;

        for(int i=0;i<width;i++){
            for (int j = 0; j < height; j++){
                stateArray[i][j].removeUnit(0);
                stateArray[i][j].removeUnit(1);
                if(unit[i][j] != 0){

                }
                if(unit[i][j] == 2 || unit[i][j] == 3 || unit[i][j] == 4){

                    stateArray[i][j].addUnit(new Unit(Loyalty.values()[unit[i][j]]));
                }
                else if(unit[i][j] == 6 || unit[i][j] == 9 || unit[i][j] == 12){
                    stateArray[i][j].addUnit(new Unit(Loyalty.values()[unit[i][j]/3]));
                    stateArray[i][j].addUnit(new Unit(Loyalty.values()[unit[i][j]/3]));
                }
            }
        }
    }

    public void format(int width, int height, int[][] board, int[][] prod){
        lock = true;
        this.boardWidth = width;
        this.boardHeight = height;


        for(int i=0;i<width;i++) for (int j = 0; j < height; j++){
            stateArray[i][j] = new State(i, j, Loyalty.values()[board[i][j]]);
            if(board[i][j]>=2){
                this.addState(Loyalty.values()[board[i][j]], i, j, prod[i][j]);
            }
            stateArray[i][j].setProductivity(prod[i][j]);
        }
        lock=false;
    }

    public int numberOfEmpty(){
        int returnValue = 0;
        for(int i=0;i<boardWidth;i++) for (int j = 0; j < boardHeight; j++) if(stateArray[i][j].getLoyalty()==Loyalty.EMPTY) returnValue++;

        return returnValue;
    }

    public String getwinner(){
        if(stateArrayList[0].size()>stateArrayList[1].size() && stateArrayList[0].size()>stateArrayList[2].size())
            return "blue";
        if(stateArrayList[1].size()>stateArrayList[0].size() && stateArrayList[1].size()>stateArrayList[2].size())
            return "green";
        if(stateArrayList[2].size()>stateArrayList[1].size() && stateArrayList[2].size()>stateArrayList[0].size())
            return "yellow";
        else
            return "no winner";
    }
}