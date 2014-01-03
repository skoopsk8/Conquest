package com.nasser.poulet.conquest.astar;

import com.nasser.poulet.conquest.model.State;
import com.nasser.poulet.conquest.model.Loyalty;

public class Node implements Comparable<Node> {
	State state;
	double weight;
	double G;
	double F;
	
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
		if(this.state.getLoyalty() == Loyalty.EMPTY) { // normalement le test de loyalty
			this.weight = 1;
		}
		else if (this.state.getLoyalty() == Loyalty.NONE){
			this.weight = 8;
		}
		else if((this.state.getLoyalty() != Loyalty.BLUE)) {
			this.weight = 10;
		}
		else {
			this.weight = 1;
		}
		G = Integer.MAX_VALUE;
	
	}

	public Node(State state, int G){
		this.state = state;
		if(this.state.getLoyalty() == Loyalty.EMPTY) { // normalement le test de loyalty
			this.weight = 1;
		}
		else if (this.state.getLoyalty() == Loyalty.NONE){
			this.weight = 8;
		}
		else if((this.state.getLoyalty() != Loyalty.BLUE)) {
			this.weight = 10;
		}
		else {
			this.weight = 1;
		}
		this.G = G;
		
	}
	
	public Node(State state, int G, Node parent){
		this.state = state;
		if(this.state.getLoyalty() == Loyalty.EMPTY) { // normalement le test de loyalty
			this.weight = 1;
		}
		else if (this.state.getLoyalty() == Loyalty.NONE){
			this.weight = 8;
		}
		else if((this.state.getLoyalty() != Loyalty.BLUE)) {
			this.weight = 10;
		}
		else {
			this.weight = 1;
		}
		this.G = G;
		this.parent = parent;
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

}


