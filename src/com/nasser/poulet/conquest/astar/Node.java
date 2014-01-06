package com.nasser.poulet.conquest.astar;

import java.util.ArrayList;

import com.nasser.poulet.conquest.model.Board;
import com.nasser.poulet.conquest.model.State;
import com.nasser.poulet.conquest.model.Loyalty;

public class Node implements Comparable<Node> {
	State state;
	double weight; // 1 if normal, 3 if water, 10 if ennemy
	double G; // Distance from start node
	double F; // Heuristic distance to the goal
	
	public double getF() {
		return F;
	}

	public void setF(double f) {
		F = f;
	}

	Node parent = null;
	
	public Node(){
		state = null;
		weight = 1;
		G = Integer.MAX_VALUE;
		
	}
	
	public Node(State state){
		this.state = state;
		this.weight = 0;
		G = Integer.MAX_VALUE;
	
	}
	
	public Node(State state, int weight) {
		this(state);
		this.weight = weight;
	}
	
	public State getState() {
		return state;
	}
	
	@Override
	public boolean equals(Object arg) {
		if (this.getState().getPosX() == ((Node) arg).getState().getPosX() && this.getState().getPosY() == ((Node) arg).getState().getPosY()) {
			return true;
		}
		else {
			return false;
		}
	}

	public void setState(State state) {
		this.state = state;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getG() {
		return G;
	}

	public void setG(double g) {
		G = g;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}



	public int compareTo(Node otherNode) {
	    double thisTotalDistanceFromGoal = F + G;
	    double otherTotalDistanceFromGoal = otherNode.getF() + otherNode.getG();
	    
	    if (thisTotalDistanceFromGoal < otherTotalDistanceFromGoal) {
	            return -1;
	    } else if (thisTotalDistanceFromGoal > otherTotalDistanceFromGoal) {
	            return 1;
	    } else {
	            return 0;
	    }
	}
	
	public int computeWeight(State state) {
		Loyalty nodeLoyalty = this.getState().getLoyalty(); 
		Loyalty neighborLoyalty = state.getLoyalty();
		
		if(neighborLoyalty == Loyalty.NONE) {
			return 3;
		}
		
		else if(neighborLoyalty == Loyalty.EMPTY) {
			return 1;
		}
		
		else if(neighborLoyalty == nodeLoyalty && state.canHostUnit()) {
			return 1;
		}
		
		else{
			return 10;
		}
	}
	
	public ArrayList<Node> getNeighbor(Board board) {
		ArrayList<Node> neighbourANode = new ArrayList<Node>();
		
		
		
		// We look if it's in the middle of the map
		if(this.getState().getPosX() > 0 && this.getState().getPosX() < board.getBoardWidth()-1) {
			if(this.getState().getPosY() > 0 && this.getState().getPosY() < board.getBoardHeight()-1) {
				neighbourANode.clear();
				neighbourANode.add(new Node(board.getState(this.getState().getPosX(), this.getState().getPosY() - 1), computeWeight(board.getState(this.getState().getPosX(), this.getState().getPosY() - 1)))); // UP 
				neighbourANode.add(new Node(board.getState(this.getState().getPosX() - 1, this.getState().getPosY()), computeWeight(board.getState(this.getState().getPosX() - 1, this.getState().getPosY())))); // LEFT 
				neighbourANode.add(new Node(board.getState(this.getState().getPosX(), this.getState().getPosY() + 1), computeWeight(board.getState(this.getState().getPosX(), this.getState().getPosY() + 1)))); // DOWN
				neighbourANode.add(new Node(board.getState(this.getState().getPosX() + 1, this.getState().getPosY()), computeWeight(board.getState(this.getState().getPosX() + 1, this.getState().getPosY())))); // RIGHT
				
				neighbourANode.add(new Node(board.getState(this.getState().getPosX() - 1, this.getState().getPosY() - 1), computeWeight(board.getState(this.getState().getPosX() - 1, this.getState().getPosY() - 1)))); // UP LEFT 
				neighbourANode.add(new Node(board.getState(this.getState().getPosX() - 1, this.getState().getPosY() + 1), computeWeight(board.getState(this.getState().getPosX() - 1, this.getState().getPosY() + 1)))); // DOWN LEFT
				neighbourANode.add(new Node(board.getState(this.getState().getPosX() + 1, this.getState().getPosY() - 1), computeWeight(board.getState(this.getState().getPosX() + 1, this.getState().getPosY() - 1)))); // UP RIGHT
				neighbourANode.add(new Node(board.getState(this.getState().getPosX() + 1, this.getState().getPosY() + 1), computeWeight(board.getState(this.getState().getPosX() + 1, this.getState().getPosY() + 1)))); // DOWN RIGHT
			}
		}
		
		// We look if it's in the left upper corner
		if(this.getState().getPosX() == 0 && this.getState().getPosY() == 0) {
			neighbourANode.clear();
			neighbourANode.add(new Node(board.getState(this.getState().getPosX() + 1, this.getState().getPosY()), computeWeight(board.getState(this.getState().getPosX() + 1, this.getState().getPosY())))); // RIGHT
			neighbourANode.add(new Node(board.getState(this.getState().getPosX(), this.getState().getPosY() + 1), computeWeight(board.getState(this.getState().getPosX(), this.getState().getPosY() + 1)))); // DOWN
			
			neighbourANode.add(new Node(board.getState(this.getState().getPosX() + 1, this.getState().getPosY() + 1), computeWeight(board.getState(this.getState().getPosX() + 1, this.getState().getPosY() + 1)))); // DOWN RIGHT
		}
		
		// We look if it's in right upper corner
		if(this.getState().getPosX() == board.getBoardWidth()-1 && this.getState().getPosY() == 0) {
			neighbourANode.clear();
			neighbourANode.add(new Node(board.getState(this.getState().getPosX() - 1, this.getState().getPosY()), computeWeight(board.getState(this.getState().getPosX() - 1, this.getState().getPosY())))); // LEFT
			neighbourANode.add(new Node(board.getState(this.getState().getPosX(), this.getState().getPosY() + 1), computeWeight(board.getState(this.getState().getPosX(), this.getState().getPosY() + 1)))); // DOWN
			
			neighbourANode.add(new Node(board.getState(this.getState().getPosX() - 1, this.getState().getPosY() + 1), computeWeight(board.getState(this.getState().getPosX() - 1, this.getState().getPosY() + 1)))); // DOWN LEFT
		}
		
		// We look if it's in the left down corner
		if(this.getState().getPosX() == 0 && this.getState().getPosY() == board.getBoardHeight()-1) {
			neighbourANode.clear();
			neighbourANode.add(new Node(board.getState(this.getState().getPosX(), this.getState().getPosY() - 1), computeWeight(board.getState(this.getState().getPosX(), this.getState().getPosY() - 1)))); // UP
			neighbourANode.add(new Node(board.getState(this.getState().getPosX() + 1, this.getState().getPosY()), computeWeight(board.getState(this.getState().getPosX() + 1, this.getState().getPosY())))); // RIGHT
			
			neighbourANode.add(new Node(board.getState(this.getState().getPosX() + 1, this.getState().getPosY() - 1), computeWeight(board.getState(this.getState().getPosX() + 1, this.getState().getPosY() - 1)))); // UP RIGHT
		}
		
		// We look if it's in the right down corner
		if(this.getState().getPosX() == board.getBoardWidth()-1 && this.getState().getPosY() == board.getBoardHeight()-1) {
			neighbourANode.clear();
			neighbourANode.add(new Node(board.getState(this.getState().getPosX() - 1, this.getState().getPosY()), computeWeight(board.getState(this.getState().getPosX() - 1, this.getState().getPosY())))); // LEFT
			neighbourANode.add(new Node(board.getState(this.getState().getPosX(), this.getState().getPosY() - 1), computeWeight(board.getState(this.getState().getPosX(), this.getState().getPosY() - 1)))); // UP

			neighbourANode.add(new Node(board.getState(this.getState().getPosX() - 1, this.getState().getPosY() - 1), computeWeight(board.getState(this.getState().getPosX() - 1, this.getState().getPosY() - 1)))); // UP LEFT
		}
		
		// We look if it's in the upper part
		if(this.getState().getPosX() > 0 && this.getState().getPosX() < board.getBoardWidth() - 1 && this.getState().getPosY() == 0) {
			neighbourANode.clear();
			neighbourANode.add(new Node(board.getState(this.getState().getPosX() - 1, this.getState().getPosY()), computeWeight(board.getState(this.getState().getPosX() - 1, this.getState().getPosY())))); // LEFT
			neighbourANode.add(new Node(board.getState(this.getState().getPosX(), this.getState().getPosY() + 1), computeWeight(board.getState(this.getState().getPosX(), this.getState().getPosY() + 1)))); // DOWN
			neighbourANode.add(new Node(board.getState(this.getState().getPosX() + 1, this.getState().getPosY()), computeWeight(board.getState(this.getState().getPosX() + 1, this.getState().getPosY())))); // RIGHT
			
			neighbourANode.add(new Node(board.getState(this.getState().getPosX() - 1, this.getState().getPosY() + 1), computeWeight(board.getState(this.getState().getPosX() - 1, this.getState().getPosY() + 1)))); // DOWN LEFT
			neighbourANode.add(new Node(board.getState(this.getState().getPosX() + 1, this.getState().getPosY() + 1), computeWeight(board.getState(this.getState().getPosX() + 1, this.getState().getPosY() + 1)))); // DOWN RIGHT
		}
		
		// We look if it's in lower part
		if(this.getState().getPosX() > 0 && this.getState().getPosX() < board.getBoardWidth() - 1 && this.getState().getPosY() == board.getBoardHeight() - 1) {
			neighbourANode.clear();
			neighbourANode.add(new Node(board.getState(this.getState().getPosX() - 1, this.getState().getPosY()), computeWeight(board.getState(this.getState().getPosX() - 1, this.getState().getPosY())))); // LEFT
			neighbourANode.add(new Node(board.getState(this.getState().getPosX(), this.getState().getPosY() - 1), computeWeight(board.getState(this.getState().getPosX(), this.getState().getPosY() - 1)))); // UP
			neighbourANode.add(new Node(board.getState(this.getState().getPosX() + 1, this.getState().getPosY()), computeWeight(board.getState(this.getState().getPosX() + 1, this.getState().getPosY())))); // RIGHT
			
			neighbourANode.add(new Node(board.getState(this.getState().getPosX() - 1, this.getState().getPosY() - 1), computeWeight(board.getState(this.getState().getPosX() - 1, this.getState().getPosY() - 1)))); // UP LEFT
			neighbourANode.add(new Node(board.getState(this.getState().getPosX() + 1, this.getState().getPosY() - 1), computeWeight(board.getState(this.getState().getPosX() + 1, this.getState().getPosY() - 1)))); // UP RIGHT
		}
		
		// We look if it's in the left part
		if(this.getState().getPosY() > 0 && this.getState().getPosY() < board.getBoardHeight() - 1 && this.getState().getPosX() == 0) {
			neighbourANode.clear();
			neighbourANode.add(new Node(board.getState(this.getState().getPosX(), this.getState().getPosY() - 1), computeWeight(board.getState(this.getState().getPosX(), this.getState().getPosY() - 1)))); // UP
			neighbourANode.add(new Node(board.getState(this.getState().getPosX() + 1, this.getState().getPosY()), computeWeight(board.getState(this.getState().getPosX() + 1, this.getState().getPosY())))); // RIGHT
			neighbourANode.add(new Node(board.getState(this.getState().getPosX(), this.getState().getPosY() + 1), computeWeight(board.getState(this.getState().getPosX(), this.getState().getPosY() + 1)))); // DOWN
			
			neighbourANode.add(new Node(board.getState(this.getState().getPosX() + 1, this.getState().getPosY() - 1), computeWeight(board.getState(this.getState().getPosX() + 1, this.getState().getPosY() - 1)))); // UP RIGHT
			neighbourANode.add(new Node(board.getState(this.getState().getPosX() + 1, this.getState().getPosY() + 1), computeWeight(board.getState(this.getState().getPosX() + 1, this.getState().getPosY() + 1)))); // DOWN RIGHT
		}
		
		// We look if it's in the right part
		if(this.getState().getPosY() > 0 && this.getState().getPosY() < board.getBoardHeight() - 1 && this.getState().getPosX() == board.getBoardWidth() - 1) {
			neighbourANode.clear();
			neighbourANode.add(new Node(board.getState(this.getState().getPosX(), this.getState().getPosY() - 1), computeWeight(board.getState(this.getState().getPosX(), this.getState().getPosY() - 1)))); // UP
			neighbourANode.add(new Node(board.getState(this.getState().getPosX() - 1, this.getState().getPosY()), computeWeight(board.getState(this.getState().getPosX() - 1, this.getState().getPosY())))); // LEFT
			neighbourANode.add(new Node(board.getState(this.getState().getPosX(), this.getState().getPosY() + 1), computeWeight(board.getState(this.getState().getPosX(), this.getState().getPosY() + 1)))); // DOWN
			
			neighbourANode.add(new Node(board.getState(this.getState().getPosX() - 1, this.getState().getPosY() - 1), computeWeight(board.getState(this.getState().getPosX() - 1, this.getState().getPosY() - 1)))); // UP LEFT
			neighbourANode.add(new Node(board.getState(this.getState().getPosX() - 1, this.getState().getPosY() + 1), computeWeight(board.getState(this.getState().getPosX() - 1, this.getState().getPosY() + 1)))); // DOWN LEFT
		}
		
		return neighbourANode;
	}

}


