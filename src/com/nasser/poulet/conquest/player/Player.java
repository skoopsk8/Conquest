package com.nasser.poulet.conquest.player;

import com.nasser.poulet.conquest.astar.AStar;
import com.nasser.poulet.conquest.controller.BoardController;
import com.nasser.poulet.conquest.model.Board;
import com.nasser.poulet.conquest.model.Loyalty;
import com.nasser.poulet.conquest.model.State;

import java.util.ArrayList;

/**
 * Created by Thomas on 12/28/13.
 */
public abstract class Player {
    protected Loyalty loyalty = null;
    protected BoardController boardController;
    protected State selected;
    protected ArrayList<ArrayList<State>> moves = new ArrayList<ArrayList<State>>();

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

    public void action( int posX, int posY ){
    	ArrayList<State> resultastar = new ArrayList<State>();
        resultastar = AStar.getPath(this.boardController.getBoard(), selected, this.boardController.getBoard().getState(posX, posY));
        if(resultastar != null) addMove(resultastar); // If impossible path, dont move
        this.abort();
    }
    
    public void addMove(ArrayList<State> newmove) {    	
    	int i = 0;
    	
    	for(ArrayList<State> move: moves) {
    		if(move.contains(newmove.get(0))) {
    			moves.remove(i);	
    			moves.add(i, newmove);
    		}
    		i++;
    	}	
    	moves.add(newmove);
    }
    
    public void update() {
    	for(int j = 0; j < moves.size(); j++) {
    		if(moves.get(j).size() > 1) {
    			this.boardController.action(moves.get(j).get(0), moves.get(j).get(1).getPosX(), moves.get(j).get(1).getPosY());
        		moves.get(j).remove(0);
    		}
    		else {
    			moves.remove(j);
    			j--;
    		}
    	}
        return;
    }

    public State getSelected() {
        return selected;
    }

    public void setSelected(State selected) {
        this.selected = selected;
    }
}
