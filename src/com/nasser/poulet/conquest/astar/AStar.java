package com.nasser.poulet.conquest.astar;

import java.util.*;

import com.nasser.poulet.conquest.model.Board;
import com.nasser.poulet.conquest.model.Loyalty;
import com.nasser.poulet.conquest.model.State;

public class AStar {
	public static double getDistanceBetween(Node node1, Node node2) {
        //if the nodes are on top or next to each other, return 1
        if (node1.getState().getPosX() == node2.getState().getPosX() || node1.getState().getPosY() == node2.getState().getPosY()){
                return 1;
        } else { //if they are diagonal to each other return diagonal distance: sqrt(1^2+1^2)
                return (double) 1.9;
        }
	}
	
	public static ArrayList<State> getPath(Board board, State start, State goal) {
		
        // openlist The list of Nodes not searched yet, sorted by their distance to the goal as guessed by our heuristic.
		ArrayList<Node> openlist = new ArrayList<Node>();
		ArrayList<Node> closedlist = new ArrayList<Node>();
		
		int i = 0;
		
		Node currentNode = new Node();
		Node startNode = new Node();
		Node goalNode = new Node();
		
		//Initialize the starting node as the first node in the openlist
		startNode.setG(0);
		startNode.setState(start);
		closedlist.clear();
		openlist.clear();
		openlist.add(startNode);
		Collections.sort(openlist); // We sort the openlist each time we add something in it (useless here)
		
		ArrayList<Node> neighbourANode = new ArrayList<Node>();
		
		if(goal.getLoyalty() != Loyalty.EMPTY && goal.getLoyalty() != Loyalty.NONE) {
			goalNode.setState(goal);
			goalNode.setWeight(1);
		}
		else {
			goalNode.setState(goal);
		}

			
		
		while (openlist.size() != 0)  {
			currentNode = openlist.get(0);
			
			if(currentNode.getState().getPosX() == goalNode.getState().getPosX() && currentNode.getState().getPosY() == goalNode.getState().getPosY()) {
				// we return the good path by starting from the last node and going to its parents until the first node, then we reverse the list
				ArrayList<State> retour = new ArrayList<State>(); 
				
				do {
					retour.add(currentNode.getState());
					if(currentNode.getWeight() == 3 && currentNode.getParent() != null) retour.add(currentNode.getState());// If we are in the water, a movement takes 2 moves, so we double it
					currentNode = currentNode.getParent();
				} while(currentNode != null);
				//if(takeTerritory) retour.add(goal);
				
				Collections.reverse(retour);
				
				return retour;
			} 
			
			// We put all the neighbors in a arraylist of Nodes
			neighbourANode = currentNode.getNeighbor(board);
			
			openlist.remove(currentNode);
			closedlist.add(currentNode);
			
			i = 0;
			while(i < neighbourANode.size()) { // We analyze all the neighbours of the current node
				boolean neighborIsBetter;
				
				if(closedlist.contains(neighbourANode.get(i))) {
					i++;
					continue;
				}
				
				if(neighbourANode.get(i).getWeight() < 10 || neighbourANode.get(i).getState() == goal) {
					double neighbourDistanceFromStart = (currentNode.getG() + getDistanceBetween(currentNode, neighbourANode.get(i)));
						
					if(!openlist.contains(neighbourANode.get(i))) {
						openlist.add(neighbourANode.get(i));
						Collections.sort(openlist);
						neighborIsBetter = true;
					}
					
					//We look the best way to go to the point, looking btw to the wight of the Nodes, to find the cheapest path
					else if (neighbourDistanceFromStart * neighbourANode.get(i).getWeight() < currentNode.getG()) { 
						neighborIsBetter = true;
					}
					else {
						neighborIsBetter = false;
					}
					
					if(neighborIsBetter) {
						// We update the neighbor, so that in the open list it will be ordered the right way
						neighbourANode.get(i).setParent(currentNode);
						neighbourANode.get(i).setG(neighbourDistanceFromStart*neighbourANode.get(i).getWeight());
						neighbourANode.get(i).setF(Math.sqrt(Math.pow((neighbourANode.get(i).getState().getPosX() - goal.getPosX()), 2) + Math.pow((neighbourANode.get(i).getState().getPosY() - goal.getPosY()), 2)));
					}
				}
				i++;
			}
		}
		return null; // If no result, return null
	}
}

