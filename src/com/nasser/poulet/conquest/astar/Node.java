package com.nasser.poulet.conquest.astar;

import com.nasser.poulet.conquest.model.State;
import com.nasser.poulet.conquest.model.Loyalty;

public class Node {
	State state;
	int weight;
	int G;
	Node parent;
	
	public Node(){
		state = null;
		weight = 0;
		G = Integer.MAX_VALUE;
		parent = null;
	}

	public Node(State state, int G){
		this.state = state;
		if(true) { // normalement le test de loyalty
			this.weight = 1;
		}
		else if (false){
			this.weight = 10;
		}
		else {
			this.weight = Integer.MAX_VALUE;
		}
		this.G = G;
		this.parent = null;
	}
	
	public Node(State state, int G, Node parent){
		this.state = state;
		if(true) {
			this.weight = 1;
		}
		else if (false){
			this.weight = 10;
		}
		else {
			this.weight = Integer.MAX_VALUE;
		}
		this.G = G;
		this.parent = parent;
	}
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getG() {
		return G;
	}

	public void setG(int g) {
		G = g;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}
}
