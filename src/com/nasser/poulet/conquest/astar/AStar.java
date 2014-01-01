package com.nasser.poulet.conquest.astar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.nasser.poulet.conquest.model.Board;
import com.nasser.poulet.conquest.model.State;

public class AStar {
	public static ArrayList<State> getPath(Board board, State start, State goal) {
		ArrayList<Node> openlist = new ArrayList<Node>();
		ArrayList<Node> closedlist = new ArrayList<Node>();
		
		boolean found = false;
		double F = Integer.MAX_VALUE, Ftemp = Integer.MAX_VALUE;
		
		Node currentNode = new Node();
		currentNode.setG(0);
		currentNode.setState(start);
		currentNode.setParent(null);
		openlist.add(currentNode);
		Node neighbourNode[] = new Node[4];
		
		while (!found)  {
			// consider the best node in the open list (the node with the lowest f value)
			F = Integer.MAX_VALUE;
			for(Node bestNode: openlist) {
				Ftemp = bestNode.getG() + (Math.abs(bestNode.getState().getPosX()-goal.getPosX()) + Math.abs(bestNode.getState().getPosY()-goal.getPosY()));
				if(Ftemp < F) {
					currentNode = bestNode;
					F = Ftemp;
				}
			}
			if(currentNode.getState().getPosX() == goal.getPosX() && currentNode.getState().getPosY() == goal.getPosY()) {
				found = true;
			} else {
				closedlist.add(currentNode); // move the current node to the closed list
				//On met les neighbour dans le tableau de node neighbournode
				if(currentNode.getState().getPosX() > 0 && currentNode.getState().getPosX() < board.getBoardWidth()-1) {
					if(currentNode.getState().getPosY() > 0 && currentNode.getState().getPosY() < board.getBoardHeight()-1) {
						neighbourNode[0].setState(board.getState(currentNode.getState().getPosX(), currentNode.getState().getPosY() - 1));
						neighbourNode[1].setState(board.getState(currentNode.getState().getPosX() - 1, currentNode.getState().getPosY()));
						neighbourNode[2].setState(board.getState(currentNode.getState().getPosX(), currentNode.getState().getPosY() + 1));
						neighbourNode[3].setState(board.getState(currentNode.getState().getPosX() + 1, currentNode.getState().getPosY()));
					}
				}
				if(currentNode.getState().getPosX() == 0 && currentNode.getState().getPosY() == 0) {
					Arrays.fill(neighbourNode, null);
					neighbourNode[0].setState(board.getState(currentNode.getState().getPosX() + 1, currentNode.getState().getPosY()));
					neighbourNode[1].setState(board.getState(currentNode.getState().getPosX(), currentNode.getState().getPosY() + 1));
				}
				if(currentNode.getState().getPosX() == board.getBoardWidth()-1 && currentNode.getState().getPosY() == 0) {
					Arrays.fill(neighbourNode, null);
					neighbourNode[0].setState(board.getState(currentNode.getState().getPosX() - 1, currentNode.getState().getPosY()));
					neighbourNode[1].setState(board.getState(currentNode.getState().getPosX(), currentNode.getState().getPosY() + 1));
				}
				if(currentNode.getState().getPosX() == 0 && currentNode.getState().getPosY() == board.getBoardHeight()-1) {
					Arrays.fill(neighbourNode, null);
					neighbourNode[0].setState(board.getState(currentNode.getState().getPosX(), currentNode.getState().getPosY() - 1));
					neighbourNode[1].setState(board.getState(currentNode.getState().getPosX() + 1, currentNode.getState().getPosY()));
				}
				if(currentNode.getState().getPosX() == board.getBoardWidth()-1 && currentNode.getState().getPosY() == board.getBoardHeight()-1) {
					Arrays.fill(neighbourNode, null);
					neighbourNode[0].setState(board.getState(currentNode.getState().getPosX() - 1, currentNode.getState().getPosY()));
					neighbourNode[1].setState(board.getState(currentNode.getState().getPosX(), currentNode.getState().getPosY() - 1));
				}
				
				if(currentNode.getState().getPosX() > 0 && currentNode.getState().getPosX() < board.getBoardWidth() - 1 && currentNode.getState().getPosY() == 0) {
					Arrays.fill(neighbourNode, null);
					neighbourNode[0].setState(board.getState(currentNode.getState().getPosX() - 1, currentNode.getState().getPosY()));
					neighbourNode[1].setState(board.getState(currentNode.getState().getPosX(), currentNode.getState().getPosY() + 1));
					neighbourNode[2].setState(board.getState(currentNode.getState().getPosX() + 1, currentNode.getState().getPosY()));
				}
				if(currentNode.getState().getPosX() > 0 && currentNode.getState().getPosX() < board.getBoardWidth() - 1 && currentNode.getState().getPosY() == board.getBoardHeight() - 1) {
					Arrays.fill(neighbourNode, null);
					neighbourNode[0].setState(board.getState(currentNode.getState().getPosX() - 1, currentNode.getState().getPosY()));
					neighbourNode[1].setState(board.getState(currentNode.getState().getPosX(), currentNode.getState().getPosY() - 1));
					neighbourNode[2].setState(board.getState(currentNode.getState().getPosX() + 1, currentNode.getState().getPosY()));
				}
				if(currentNode.getState().getPosY() > 1 && currentNode.getState().getPosY() < board.getBoardHeight() - 1 && currentNode.getState().getPosX() == 0) {
					Arrays.fill(neighbourNode, null);
					neighbourNode[0].setState(board.getState(currentNode.getState().getPosX(), currentNode.getState().getPosY() - 1));
					neighbourNode[1].setState(board.getState(currentNode.getState().getPosX() + 1, currentNode.getState().getPosY()));
					neighbourNode[2].setState(board.getState(currentNode.getState().getPosX(), currentNode.getState().getPosY() + 1));
				}
				if(currentNode.getState().getPosY() > 1 && currentNode.getState().getPosY() < board.getBoardHeight() - 1 && currentNode.getState().getPosX() == board.getBoardWidth() - 1) {
					Arrays.fill(neighbourNode, null);
					neighbourNode[0].setState(board.getState(currentNode.getState().getPosX(), currentNode.getState().getPosY() - 1));
					neighbourNode[1].setState(board.getState(currentNode.getState().getPosX() - 1, currentNode.getState().getPosY()));
					neighbourNode[2].setState(board.getState(currentNode.getState().getPosX(), currentNode.getState().getPosY() + 1));
				}
				
				for(Node workingNode: neighbourNode) { // On traite les neighbour les uns après les autres
					if(closedlist.contains(workingNode) && currentNode.getG()+1 < workingNode.getG()) {
						workingNode.setG(currentNode.getG()+1);
						workingNode.setParent(currentNode);
					}
					else if(openlist.contains(workingNode) && currentNode.getG()+1 < workingNode.getG()) {
						workingNode.setG(currentNode.getG()+1);
						workingNode.setParent(currentNode);
					}
					else {
						workingNode.setG(currentNode.getG()+1);
						openlist.add(workingNode);
					}
					Arrays.fill(neighbourNode, null);
				}
			}
		}
		
				
		
		if(found) {
			//return the good path
			ArrayList<State> retour = new ArrayList<State>(); 
			do {
				retour.add(currentNode.getState());
				currentNode = currentNode.getParent();
			} while(currentNode.getState() != start);
			
			Collections.reverse(retour);
			return retour;
		}
		else{
			return null;
		}
	}
}

