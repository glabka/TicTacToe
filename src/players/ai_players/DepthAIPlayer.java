package players.ai_players;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import game_components.Grid;
import game_components.Move;
import game_components.Square.SVal;
import game_mechanics.Rules;
import players.ai_players.heuristics.AbstractGridHeuristic;
import players.ai_players.heuristics.AbstractSquareHeuristic;
import players.ai_players.heuristics.Common;
import players.ai_players.support_classes.AbstractCooValFromStreakEstimator;
import players.ai_players.support_classes.AbstractRatedCoosFilter;
import players.ai_players.support_classes.PoweredLengthCooValEstimator;
import players.ai_players.support_classes.RatedCoordinate;

public class DepthAIPlayer extends AbstractAIPlayer {
	
	protected int depth;

	public DepthAIPlayer(SVal playersSVal, String name, int streakLength, AbstractSquareHeuristic squareHeuristic, AbstractGridHeuristic gridHeuristic, AbstractRatedCoosFilter ratedCoosFilter, int depth) {
		super(playersSVal, name, streakLength, squareHeuristic, gridHeuristic, ratedCoosFilter);
		this.depth = depth;
	}
	
	@Override
	public Move nextMove(Grid g) {
		Grid gOrig = g;
		g = g.gridsCopy();
		
		List<MoveAndDepth> md = nextMovesAndDepth(g, this.getSVal(), 0);
		
		List<Move> proceededMoves = new LinkedList<>();
		
		// variables for storage of best move information
		Move bestMove = null;
		int winningMoveDepth = -1; // depth of move lead by best move (or best move itself) that was a win
		double bestMoveValue = Double.NEGATIVE_INFINITY; // it is winning move when this variable is positive infinity
		// variables for storage of best move information - end
		
		int debug = 0; // debug 
		int lastMoveDepth = 0;
		while(!md.isEmpty()) {
			debug++;
//			System.out.println("========"); // debug
//			System.out.println(md); // debug
//			System.out.println("int debug : " + debug); // debug
			MoveAndDepth mvAndDepth = md.get(0);
			md.remove(0);
			
			int depthDifference = mvAndDepth.getDepth() - lastMoveDepth;
			if(depthDifference <= 0 && proceededMoves.size() > 0) {
				int firstProceededMove = proceededMoves.size() + depthDifference - 1;
				List<Move> movesToBeUndone = proceededMoves.subList(firstProceededMove , proceededMoves.size());
				proceededMoves = proceededMoves.subList(0,firstProceededMove);
//				System.out.println("movesToBeUndone = " + movesToBeUndone); // debug
				g.delete(movesToBeUndone);
			}
			lastMoveDepth = mvAndDepth.getDepth();
			
//			if (debug > 10) { //debug
//				return null; //debug
//			} //debug
			
			g.insert(mvAndDepth.getMove());
//			System.out.println("debug: depth = " + mvAndDepth.getDepth()); // debug
//			System.out.println("tried move" + mvAndDepth.getMove()); // debug
//			g.printGridDebug();// debug
			proceededMoves.add(mvAndDepth.getMove());
//			System.out.println("proceededMoves = " + proceededMoves); // debug
			
			if(mvAndDepth.getDepth() == depth - 1 && !Rules.endOfGame(g, streakLength)) {
				// evaluating where move leads to depth
				double moveHeuristicValue = this.gridHeurisric.getGridsHeuristicValue(g, getPlayer(mvAndDepth.depth), streakLength);
				if(bestMoveValue < moveHeuristicValue) {
					bestMove = proceededMoves.get(0);
					bestMoveValue = moveHeuristicValue;
				}
			}
			
			SVal winner = Rules.findWinner(g, streakLength);
			if(winner == this.getSVal() && winningMoveDepth < mvAndDepth.getDepth()) {
				bestMove = proceededMoves.get(0);
				winningMoveDepth = mvAndDepth.getDepth();
				bestMoveValue = Double.POSITIVE_INFINITY;
			} else if (winner == SVal.getOpposite(this.getSVal())) {
				continue;
			} else if(Rules.endOfGame(g, streakLength)) {
				continue;
			}
			
//			System.out.println("bestMove so far = " + bestMove + ", value = " + bestMoveValue); // debug
			
			// determining and adding next moves
			if(mvAndDepth.getDepth() + 1 < depth) {
				List<MoveAndDepth> nextMd = nextMovesAndDepth(g, getPlayer(mvAndDepth.getDepth() + 1), mvAndDepth.getDepth() + 1);
				addToBeginingOfList(md, nextMd);
			}
		}
		
		if(bestMove != null) {
			return bestMove;
		} else {
			System.out.println("FIRST EMPTY SQUARE MOVE"); // debug
			return new Move(Common.firtEmptySquare(gOrig), this.getSVal());
		}
	}
	
	private SVal getPlayer(int depth) {
		if(depth % 2 == 0) {
			return this.getSVal();
		} else {
			return SVal.getOpposite(this.getSVal());
		}
	}
	
	private <T> void addToBeginingOfList(List<T> listForAddingTo, List<T> listToBeAdded) {
		Collections.reverse(listToBeAdded);
		for(T t : listToBeAdded) {
			listForAddingTo.add(0, t);
		}
	}
	
	private List<MoveAndDepth> nextMovesAndDepth(Grid g, SVal val, int depth) {
		AbstractCooValFromStreakEstimator cooEstimator = new PoweredLengthCooValEstimator(2);
		List<RatedCoordinate> firstMoveRatedCoos = this.ratedCoosFilter.filterRatedCoos(this.squareHeuristic.getRatedCoos(val, g, streakLength, cooEstimator));
		List<MoveAndDepth> md = mdfromRatedCoosList(firstMoveRatedCoos, val, depth);
		return md;
		// option when only best option count for simulated opponent
//		if (depth % 2 == 0) {
//			// it's this players turn
//			return md;
//		} else {
//			// it's simulating other players turn - only best estimation counts
//			return md.subList(0, 1);
//		}
	}
	
	private List<MoveAndDepth> mdfromRatedCoosList(List<RatedCoordinate> ratedCoos, SVal val, int depth) {
		List<MoveAndDepth> list = new LinkedList<>();
		
		for(RatedCoordinate coo : ratedCoos) {
			list.add(new MoveAndDepth(new Move(coo, val), depth));
		}
		
		return list;
	}
	
	class MoveAndDepth {
			
			private int depth;
			private Move mv;
			
			public MoveAndDepth(Move mv, int depth) {
				this.mv = mv;
				this.depth = depth;
			}
			
			public Move getMove() {
				return mv;
			}
			
			public int getDepth() {
				return depth;
			}
			
			public String toString() {
				return mv +" d:" + depth;
			}
			
		}
	
}
