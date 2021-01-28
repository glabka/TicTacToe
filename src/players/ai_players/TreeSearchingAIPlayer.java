package players.ai_players;

import java.util.LinkedList;
import java.util.List;

import game_components.Grid;
import game_components.Move;
import game_components.Square.SVal;
import grid_computations.ValuedCoordinate;
import players.ai_players.heuristics.AbstractGridHeuristic;
import players.ai_players.heuristics.AbstractSquareHeuristic;
import players.ai_players.support_classes.AbstractRatedCoosFilter;
import players.ai_players.support_classes.MoveAndDepth;
import players.ai_players.support_classes.RatedCoordinate;
import players.ai_players.support_classes.RatedCoordinatesValueComparator;
import players.ai_players.support_classes.SortedNodesTree;
import players.ai_players.support_classes.SortedTreeNode;


public abstract class TreeSearchingAIPlayer extends AbstractAIPlayer {

	private int depth;
	
	/**
	 * 
	 * @param playersSVal
	 * @param name
	 * @param streakLength
	 * @param squareHeuristic
	 * @param gridHeuristic
	 * @param ratedCoosFilter
	 * @param depth - depth in this context translate into 1 more depth means one move from playersSval and one move from opponent
	 */
	public TreeSearchingAIPlayer(SVal playersSVal, String name, int streakLength,
			AbstractSquareHeuristic squareHeuristic, AbstractGridHeuristic gridHeuristic,
			AbstractRatedCoosFilter ratedCoosFilter, int depth) {
		super(playersSVal, name, streakLength, squareHeuristic, gridHeuristic, ratedCoosFilter);
		if (depth < 1) {
			throw new IllegalArgumentException("depth < 1 and it should not.");
		}
		this.depth = 2 * depth;
	}

	@Override
	public Move nextMove(Grid g) {
		SortedNodesTree<RatedCoordinate> tree = getSortedNodesTree(g);
		
		return null; // TODO
	}
	
	private SortedNodesTree<RatedCoordinate> getSortedNodesTree(Grid g) {
		g = g.gridsCopy();
		SortedNodesTree<RatedCoordinate> tree = new SortedNodesTree<RatedCoordinate>(null, new RatedCoordinatesValueComparator()); // null because the value of original grid is not interesting for decision making
		
		List<MoveAndDepth> md = nextMovesAndDepth(g, getSVal(), 0);
		SortedTreeNode<RatedCoordinate> parrentNode = tree.getRoot();
		
		List<Move> proceededMoves = new LinkedList<>();
		
		int lastMoveDepth = 0;
		
		while(!md.isEmpty()) {
			MoveAndDepth mvAndDepth = md.get(0);
			md.remove(0);
			parrentNode = getToLowerNodes(mvAndDepth.getDepth(), parrentNode);
			
			// part for deleting proceeded move from Grid g, so it can be reused without creating new grid every step of the way
			int depthDifference = mvAndDepth.getDepth() - lastMoveDepth;
			if(depthDifference <= 0 && proceededMoves.size() > 0) {
				int firstProceededMove = proceededMoves.size() + depthDifference - 1;
				List<Move> movesToBeUndone = proceededMoves.subList(firstProceededMove , proceededMoves.size());
				proceededMoves = proceededMoves.subList(0,firstProceededMove);
//							System.out.println("movesToBeUndone = " + movesToBeUndone); // debug
				g.delete(movesToBeUndone);
			}
			lastMoveDepth = mvAndDepth.getDepth();
			
			g.insert(mvAndDepth.getMove());
			proceededMoves.add(mvAndDepth.getMove());
			
			double gridHeuristicVal = this.gridHeurisric.getGridsHeuristicValue(g, AIPlayersCommon.getPlayer(mvAndDepth.getDepth(), this.getSVal()), this.streakLength);
			tree.addChild(parrentNode, new RatedCoordinate(mvAndDepth.getMove(), gridHeuristicVal));
			
			
			if(mvAndDepth.getDepth() + 1 < this.depth) {
				List<MoveAndDepth> nextMd = nextMovesAndDepth(g, AIPlayersCommon.getPlayer(mvAndDepth.getDepth() + 1, this.getSVal()), mvAndDepth.getDepth() + 1);
				AIPlayersCommon.addToBeginingOfList(md, nextMd);
			}
			
		}
		
		return tree;
	}

	/**
	 * Method return node of certain depth in same branch as startingNode
	 * @param depth
	 * @param startingNode
	 * @return
	 */
	private SortedTreeNode<RatedCoordinate> getToLowerNodes(int depth, SortedTreeNode<RatedCoordinate> startingNode) {
		if (depth > startingNode.getDepth()) {
			throw new IllegalArgumentException("depth > startingNode.getDepth() - that should not happen");
		}
		if(startingNode.getDepth() == depth) {
			return startingNode;
		}
		
		SortedTreeNode<RatedCoordinate> node = startingNode;
		while (startingNode.getDepth() > depth) {
			node = node.getParent();
		}
		
		return node;
		
	}
	
	private List<MoveAndDepth> nextMovesAndDepth(Grid g, SVal val, int depth) {
		List<RatedCoordinate> firstMoveRatedCoos = this.ratedCoosFilter.filterRatedCoos(this.squareHeuristic.getRatedCoos(val, g, streakLength));
		List<MoveAndDepth> md = AIPlayersCommon.mdfromRatedCoosList(firstMoveRatedCoos, val, depth);
		return md;
	}
	
	public void test(Grid g) {
		getSortedNodesTree(g);
	}
	
}
