package players.ai_players;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import game_components.Grid;
import game_components.Move;
import game_components.Square.SVal;
import game_mechanics.Rules;
import players.ai_players.heuristics.AbstractGridHeuristic;
import players.ai_players.heuristics.AbstractSquareHeuristic;
import players.ai_players.support_classes.AbstractRatedCoosFilter;
import players.ai_players.support_classes.MoveAndDepth;
import players.ai_players.support_classes.RatedCoordinate;
import players.ai_players.support_classes.RatedCoordinatesValueComparator;
import players.ai_players.support_classes.SortedNodesTree;
import players.ai_players.support_classes.SortedTreeNode;


public class TreeEvaluationAIPlayer extends AbstractAIPlayer {

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
	public TreeEvaluationAIPlayer(SVal playersSVal, String name, int streakLength,
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
		evaluateTree(tree);
		
		double biggestNum = Double.NEGATIVE_INFINITY;
		Move bestMove = null;
		for(SortedTreeNode<RatedCoordinate> node : tree.getSortedChildren(tree.getRoot())) {
			if(node.getNodeEvaluationNumber() >= biggestNum) {
				biggestNum = node.getNodeEvaluationNumber();
				bestMove = new Move(node.getVal(), this.getSVal());
			}
		}
		
		return bestMove;
	}
	
	private void evaluateTree(SortedNodesTree<RatedCoordinate> tree) {
		List<SortedTreeNode<RatedCoordinate>> nodesForEvaluation = evaluateLeafs(tree.getLeafs());
		Comparator<SortedTreeNode<RatedCoordinate>> depthComparator = (SortedTreeNode<RatedCoordinate> n1, SortedTreeNode<RatedCoordinate> n2) -> Integer.compare(n1.getDepth(), n2.getDepth());
		int maxLeafDepth = nodesForEvaluation.stream().max(depthComparator).orElse(tree.getRoot()).getDepth();
		
		Comparator<SortedTreeNode<RatedCoordinate>> evalComparator = (SortedTreeNode<RatedCoordinate> n1, SortedTreeNode<RatedCoordinate> n2) -> Double.compare(n1.getNodeEvaluationNumber(), n2.getNodeEvaluationNumber());
		
		IntegerWrapper currentDepth = new IntegerWrapper(maxLeafDepth);
		while(!nodesForEvaluation.isEmpty()) {
//			System.out.println("currentDepth = " + currentDepth.getVal()); // debug
//			System.out.println("nodesForEvaluation: " + nodesForEvaluation); // debug
			List<SortedTreeNode<RatedCoordinate>> currentDepthNodes = nodesForEvaluation.stream().filter(n -> n.getDepth() == currentDepth.getVal()).collect(Collectors.toList());
//			System.out.println("currentDepthNodes: " + currentDepthNodes); // debug
			List<SortedTreeNode<RatedCoordinate>> parents = getParents(currentDepthNodes);
			nodesForEvaluation.removeAll(currentDepthNodes);
			nodesForEvaluation.addAll(parents);
			
			for(SortedTreeNode<RatedCoordinate> parent : parents) {
				List<SortedTreeNode<RatedCoordinate>> children = parent.getChildren();
				
				if(AIPlayersCommon.opponentsTurn(currentDepth.getVal() + 1, this.getSVal())) { // depth + 1 because first node (node 0) doesn't represent any move
					parent.setNodeEvaluationNumber(children.stream().min(evalComparator).orElse(null).getNodeEvaluationNumber());
				} else {
					parent.setNodeEvaluationNumber(children.stream().max(evalComparator).orElse(null).getNodeEvaluationNumber());
				}
			}
			
			currentDepth.setVal(currentDepth.getVal() - 1);
			
		}
	}
	
	private List<SortedTreeNode<RatedCoordinate>> evaluateLeafs(List<SortedTreeNode<RatedCoordinate>> leafs) {
		for(SortedTreeNode<RatedCoordinate> leaf : leafs) {
			if(AIPlayersCommon.opponentsTurn(leaf.getDepth() + 1, this.playersSVal)) { // depth + 1 because first node (node 0) doesn't represent any move
				//if leaf is opponent it means game ended with winning or loosing or tie and is to be set as opposite of what is value for opponent
				leaf.setNodeEvaluationNumber(-leaf.getVal().getValue());
			} else {
				leaf.setNodeEvaluationNumber(leaf.getVal().getValue());
			}
		}
		
		return leafs;
	}
	
	public static List<SortedTreeNode<RatedCoordinate>> getParents(List<SortedTreeNode<RatedCoordinate>> children) {
		List<SortedTreeNode<RatedCoordinate>> parents = new LinkedList<>();
		for(SortedTreeNode<RatedCoordinate> child: children) {
			SortedTreeNode<RatedCoordinate> parent = child.getParent();
			if(parent != null && !parents.contains(parent)) {
				parents.add(parent);
			}
		}
		return parents;
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
			if (mvAndDepth.getDepth() < parrentNode.getDepth()) {
				parrentNode = getToLowerNodes(mvAndDepth.getDepth(), parrentNode);
			}
			// part for deleting proceeded move from Grid g, so it can be reused without creating new grid every step of the way
			int depthDifference = mvAndDepth.getDepth() - lastMoveDepth;
			if(depthDifference <= 0 && proceededMoves.size() > 0) {
				int firstProceededMove = proceededMoves.size() + depthDifference - 1;
				List<Move> movesToBeUndone = proceededMoves.subList(firstProceededMove , proceededMoves.size());
				proceededMoves = proceededMoves.subList(0, firstProceededMove);
//							System.out.println("movesToBeUndone = " + movesToBeUndone); // debug
				g.delete(movesToBeUndone);
			}
			lastMoveDepth = mvAndDepth.getDepth();
			
			g.insert(mvAndDepth.getMove());
//			System.out.println("debug: move depth = " + mvAndDepth.getDepth()); // debug
//			System.out.println("tried move" + mvAndDepth.getMove()); // debug
//			g.printGridDebug();// debug
			proceededMoves.add(mvAndDepth.getMove());
			
			double gridHeuristicVal = this.gridHeurisric.getGridsHeuristicValue(g, AIPlayersCommon.getPlayer(mvAndDepth.getDepth(), this.getSVal()), this.streakLength);		
			SortedTreeNode<RatedCoordinate> child = tree.addChild(parrentNode, new RatedCoordinate(mvAndDepth.getMove(), gridHeuristicVal));
			
			
			if(mvAndDepth.getDepth() + 2 < this.depth && child.getVal().getValue() != Double.NEGATIVE_INFINITY
					&& child.getVal().getValue() != Double.POSITIVE_INFINITY && !Rules.endOfGame(g, streakLength)) { // + 1 because of next step + 1 because of no step root of tree
				List<MoveAndDepth> nextMd = nextMovesAndDepth(g, AIPlayersCommon.getPlayer(mvAndDepth.getDepth() + 1, this.getSVal()), mvAndDepth.getDepth() + 1);
				AIPlayersCommon.addToBeginingOfList(md, nextMd);
				parrentNode = child;
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
		while (node.getDepth() > depth) {
			node = node.getParent();
		}
		
		return node;
		
	}
	
	private List<MoveAndDepth> nextMovesAndDepth(Grid g, SVal val, int depth) {
		List<RatedCoordinate> firstMoveRatedCoos = this.ratedCoosFilter.filterRatedCoos(this.squareHeuristic.getRatedCoos(val, g, streakLength));
		List<MoveAndDepth> md = AIPlayersCommon.mdfromRatedCoosList(firstMoveRatedCoos, val, depth);
		return md;
	}
	
//	public void test(Grid g) {
//		SortedNodesTree<RatedCoordinate> tree = getSortedNodesTree(g);
//		System.out.println(tree);
//		evaluateTree(tree);
//		System.out.println("evaluated tree: ");
//		System.out.println(tree);
//	}
	
	// because only constants or effectively constants can be used in lambda expression
	class IntegerWrapper {
		private int val;
		
		public IntegerWrapper(int val) {
			this.val = val;
		}
		
		public int getVal() {
			return val;
		}
		
		public void setVal(int val) {
			this.val = val;
		}
	}
	
}
