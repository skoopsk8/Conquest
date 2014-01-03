package com.nasser.poulet.conquest.astar;

import java.util.*;


import com.nasser.poulet.conquest.model.Board;
import com.nasser.poulet.conquest.model.State;

public class AStar {
	public static float getDistanceBetween(Node node1, Node node2) {
        //if the nodes are on top or next to each other, return 1
        if (node1.getState().getPosX() == node2.getState().getPosX() || node1.getState().getPosY() == node2.getState().getPosY()){
                return 1;//*(mapHeight+mapWith);
        } else { //if they are diagonal to each other return diagonal distance: sqrt(1^2+1^2)
                return (float) 1.9;//*(mapHeight+mapWith);
        }
}
	
	public static ArrayList<State> getPath(Board board, State start, State goal) {
		ArrayList<Node> openlist = new ArrayList<Node>();
		ArrayList<Node> closedlist = new ArrayList<Node>();
		
		int i = 0;
		
		Node currentNode = new Node();
		Node startNode = new Node();
		startNode.setG(0);
		startNode.setState(start);
		closedlist.clear();
		openlist.clear();
		openlist.add(startNode);
		Collections.sort(openlist);
		
		ArrayList<Node> neighbourANode = new ArrayList<Node>();
		
		while (openlist.size() != 0)  {
			currentNode = openlist.get(0);
			
			if(currentNode.getState().getPosX() == goal.getPosX() && currentNode.getState().getPosY() == goal.getPosY()) {
				System.out.println("On a trouvvé le bon chemin");
				//return the good path
				ArrayList<State> retour = new ArrayList<State>(); 
				
				do {
					retour.add(currentNode.getState());
					currentNode = currentNode.getParent();
				} while(currentNode != null);
				
				Collections.reverse(retour);
				
				return retour;
			} 
			//On met les neighbour dans le tableau de node neighbournode
			if(currentNode.getState().getPosX() > 0 && currentNode.getState().getPosX() < board.getBoardWidth()-1) {
				if(currentNode.getState().getPosY() > 0 && currentNode.getState().getPosY() < board.getBoardHeight()-1) {
					neighbourANode.clear();
					neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX(), currentNode.getState().getPosY() - 1)));
					neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() - 1, currentNode.getState().getPosY())));
					neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX(), currentNode.getState().getPosY() + 1)));
					neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() + 1, currentNode.getState().getPosY())));
					
					neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() - 1, currentNode.getState().getPosY() - 1)));
					neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() - 1, currentNode.getState().getPosY() + 1)));
					neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() + 1, currentNode.getState().getPosY() - 1)));
					neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() + 1, currentNode.getState().getPosY() + 1)));
				}
			}
			if(currentNode.getState().getPosX() == 0 && currentNode.getState().getPosY() == 0) {
				neighbourANode.clear();
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() + 1, currentNode.getState().getPosY())));
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX(), currentNode.getState().getPosY() + 1)));
				
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() + 1, currentNode.getState().getPosY() + 1)));
			}
			if(currentNode.getState().getPosX() == board.getBoardWidth()-1 && currentNode.getState().getPosY() == 0) {
				neighbourANode.clear();
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() - 1, currentNode.getState().getPosY())));
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX(), currentNode.getState().getPosY() + 1)));
				
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() - 1, currentNode.getState().getPosY() + 1)));
			}
			if(currentNode.getState().getPosX() == 0 && currentNode.getState().getPosY() == board.getBoardHeight()-1) {
				neighbourANode.clear();
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX(), currentNode.getState().getPosY() - 1)));
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() + 1, currentNode.getState().getPosY())));
				
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() + 1, currentNode.getState().getPosY() - 1)));
			}
			if(currentNode.getState().getPosX() == board.getBoardWidth()-1 && currentNode.getState().getPosY() == board.getBoardHeight()-1) {
				neighbourANode.clear();
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() - 1, currentNode.getState().getPosY())));
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX(), currentNode.getState().getPosY() - 1)));

				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() - 1, currentNode.getState().getPosY() - 1)));
			}
			
			if(currentNode.getState().getPosX() > 0 && currentNode.getState().getPosX() < board.getBoardWidth() - 1 && currentNode.getState().getPosY() == 0) {
				neighbourANode.clear();
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() - 1, currentNode.getState().getPosY())));
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX(), currentNode.getState().getPosY() + 1)));
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() + 1, currentNode.getState().getPosY())));
				
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() - 1, currentNode.getState().getPosY() + 1)));
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() + 1, currentNode.getState().getPosY() + 1)));
			}
			if(currentNode.getState().getPosX() > 0 && currentNode.getState().getPosX() < board.getBoardWidth() - 1 && currentNode.getState().getPosY() == board.getBoardHeight() - 1) {
				neighbourANode.clear();
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() - 1, currentNode.getState().getPosY())));
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX(), currentNode.getState().getPosY() - 1)));
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() + 1, currentNode.getState().getPosY())));
				
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() - 1, currentNode.getState().getPosY() - 1)));
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() + 1, currentNode.getState().getPosY() - 1)));
			}
			if(currentNode.getState().getPosY() > 1 && currentNode.getState().getPosY() < board.getBoardHeight() - 1 && currentNode.getState().getPosX() == 0) {
				neighbourANode.clear();
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX(), currentNode.getState().getPosY() - 1)));
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() + 1, currentNode.getState().getPosY())));
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX(), currentNode.getState().getPosY() + 1)));
				
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() + 1, currentNode.getState().getPosY() - 1)));
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() + 1, currentNode.getState().getPosY() + 1)));
			}
			if(currentNode.getState().getPosY() > 1 && currentNode.getState().getPosY() < board.getBoardHeight() - 1 && currentNode.getState().getPosX() == board.getBoardWidth() - 1) {
				neighbourANode.clear();
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX(), currentNode.getState().getPosY() - 1)));
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() - 1, currentNode.getState().getPosY())));
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX(), currentNode.getState().getPosY() + 1)));
				
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() - 1, currentNode.getState().getPosY() - 1)));
				neighbourANode.add(new Node(board.getState(currentNode.getState().getPosX() - 1, currentNode.getState().getPosY() + 1)));
			}
			
			openlist.remove(currentNode);
			closedlist.add(currentNode);
			
			i = 0;
			while(i < neighbourANode.size()) { // On traite les neighbour les uns après les autres
				boolean neighborIsBetter;
				
				if(closedlist.contains(neighbourANode.get(i))) {
					i++;
					continue;
				}
				
				if(neighbourANode.get(i).getWeight() < 10) {
					double neighbourDistanceFromStart = (currentNode.getG() + getDistanceBetween(currentNode, neighbourANode.get(i)));
						
					if(!openlist.contains(neighbourANode.get(i))) {
						openlist.add(neighbourANode.get(i));
						Collections.sort(openlist);
						neighborIsBetter = true;
					}
					
					else if (neighbourDistanceFromStart * neighbourANode.get(i).getWeight() < currentNode.getG()) {
						//System.out.println(currentNode.getState().getPosX() + " " + currentNode.getState().getPosY());
						//System.out.println(neighbourANode.get(i).getState().getPosX() + " " + neighbourANode.get(i).getState().getPosY());
						//System.out.println(neighbourDistanceFromStart + " " + currentNode.getG());
						neighborIsBetter = true;
					}
					else {
						neighborIsBetter = false;
					}
					
					if(neighborIsBetter) {
						neighbourANode.get(i).setParent(currentNode);
						neighbourANode.get(i).setG(neighbourDistanceFromStart*neighbourANode.get(i).getWeight());
						neighbourANode.get(i).setF(Math.sqrt(Math.pow((neighbourANode.get(i).getState().getPosX() - goal.getPosX()), 2) + Math.pow((neighbourANode.get(i).getState().getPosY() - goal.getPosY()), 2)));
					}
				}
				i++;
			}
		}
		return null;
	}
}

