package players.ai_players;

import game_components.Grid;
import game_components.Move;
import game_components.Square.SVal;
import grid_computations.ValuedCoordinate;
import players.ai_players.heuristics.AbstractSquareHeuristic;
import players.ai_players.support_classes.SortedNodesTree;


public abstract class AbstractTreeSearchingAIPlayer extends AbstractAIPlayer {

	public AbstractTreeSearchingAIPlayer(SVal playersSVal, String name, int streakLength, AbstractSquareHeuristic heuristic) {
		super(playersSVal, name, streakLength, heuristic);
	}

	
	
	@Override
	public Move nextMove(Grid g) {
		SortedNodesTree<ValuedCoordinate> tree = getSortedNodesTree(g);
		
		return null; // TODO
	}
	
	private static SortedNodesTree<ValuedCoordinate> getSortedNodesTree(Grid g) {
		SortedNodesTree<ValuedCoordinate> tree;
		
		return null; // TODO
	}

}
