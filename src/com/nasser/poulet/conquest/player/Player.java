package com.nasser.poulet.conquest.player;

import com.nasser.poulet.conquest.astar.AStar;
import com.nasser.poulet.conquest.controller.BoardController;
import com.nasser.poulet.conquest.model.Board;
import com.nasser.poulet.conquest.model.Loyalty;
import com.nasser.poulet.conquest.model.State;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

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

    public boolean select( int posX, int posY ){
        this.selected = boardController.select(posX, posY);
        if(this.selected.getUnit() != null){    // One unit
            if(this.selected.getUnit().getLoyalty() != this.loyalty){ // Unit 0 different loyalty try the nex one ;)
                if(this.selected.getUnit(1) != null){
                    if(this.selected.getUnit(1).getLoyalty() != this.loyalty){  // Unit 1 also different loyalty = ABORT
                        this.abort();
                        return false;
                    }
                }
                else{
                    this.abort();
                    return false;
                }
            }
        }
        else{
            this.abort();
            return false;
        }
        return true;
    }

    public int action( int posX, int posY ){
    	ArrayList<State> resultastar = new ArrayList<State>();
        resultastar = AStar.getPath(this.boardController.getBoard(), selected, this.boardController.getBoard().getState(posX, posY));
        if(resultastar != null)
            addMove(resultastar); // If impossible path, dont move
        this.abort();
        return -1;
    }
    
    public int addMove(ArrayList<State> newmove) {
        ArrayList<ArrayList<State>> updateMove = new ArrayList<ArrayList<State>>();
        updateMove = moves;
        
    	for(Iterator<ArrayList<State>> it = updateMove.iterator(); it.hasNext();) {
    		ArrayList<State> move = it.next();
    		if(move.contains(newmove.get(0))) {
    			it.remove();
    		}
    	}
    	
    	updateMove.add(newmove);
        
        moves = updateMove;

        return updateMove.get(updateMove.size()-1).size(); // return the number of moves
    }
    
    public void update() {
    	for(Iterator<ArrayList<State>> it = moves.iterator(); it.hasNext();) {
    		ArrayList<State> move = it.next();
    		
    		if(move.size() > 1) {
    			this.boardController.action(move.get(0), move.get(1).getPosX(), move.get(1).getPosY());
    			move.remove(0);
    		}
    		else{
    			it.remove();
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
