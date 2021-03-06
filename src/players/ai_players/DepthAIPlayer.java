package players.ai_players;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import game_components.Grid;
import game_components.ValuedMove;
import game_components.Square.SVal;
import game_mechanics.Rules;
import players.ai_players.heuristics.AbstractGridHeuristic;
import players.ai_players.heuristics.AbstractSquareHeuristic;
import players.ai_players.heuristics.HeuristicCommon;
import players.ai_players.support_classes.AbstractCooValFromStreakEstimator;
import players.ai_players.support_classes.AbstractRatedCoosFilter;
import players.ai_players.support_classes.PoweredLengthCooValEstimator;
import players.ai_players.support_classes.RatedCoordinate;
import players.ai_players.support_classes.MoveAndDepth;

public class DepthAIPlayer extends AbstractAIPlayer {
	
	protected int depth;

	public DepthAIPlayer(SVal playersSVal, String name, int streakLength, AbstractSquareHeuristic squareHeuristic, AbstractGridHeuristic gridHeuristic, AbstractRatedCoosFilter ratedCoosFilter, int depth) {
		super(playersSVal, name, streakLength, squareHeuristic, gridHeuristic, ratedCoosFilter);
		this.depth = depth;
	}
	
	@Override
	public ValuedMove nextMove(Grid g) {
		Grid gOrig = g;
		g = g.gridsCopy();
		
		List<MoveAndDepth> md = nextMovesAndDepth(g, this.getSVal(), 0);
		
		List<ValuedMove> proceededMoves = new LinkedList<>();
		
		// variables for storage of best move information
		ValuedMove bestMove = null;
		int winningMoveDepth = -1; // depth of move lead by best move (or best move itself) that was a win
		double bestMoveValue = Double.NEGATIVE_INFINITY; // it is winning move when this variable is positive infinity
		// variables for storage of best move information - end
		
//		int debug = 0; // debug 
		int lastMoveDepth = 0;
		while(!md.isEmpty()) {
//			debug++; // debug
//			System.out.println("========"); // debug
//			System.out.println(md); // debug
//			System.out.println("int debug : " + debug); // debug
			MoveAndDepth mvAndDepth = md.get(0);
			md.remove(0);
			
			int depthDifference = mvAndDepth.getDepth() - lastMoveDepth;
			if(depthDifference <= 0 && proceededMoves.size() > 0) {
				int firstProceededMove = proceededMoves.size() + depthDifference - 1;
				List<ValuedMove> movesToBeUndone = proceededMoves.subList(firstProceededMove , proceededMoves.size());
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
				double moveHeuristicValue = this.gridHeurisric.getGridsHeuristicValue(g, AIPlayersCommon.getPlayer(mvAndDepth.getDepth(), this.getSVal()), streakLength);
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
				List<MoveAndDepth> nextMd = nextMovesAndDepth(g, AIPlayersCommon.getPlayer(mvAndDepth.getDepth() + 1, this.getSVal()), mvAndDepth.getDepth() + 1);
				AIPlayersCommon.addToBeginingOfList(md, nextMd);
			}
		}
		
		if(bestMove != null) {
			return bestMove;
		} else {
//			System.out.println("FIRST EMPTY SQUARE MOVE"); // debug
			return new ValuedMove(HeuristicCommon.firtEmptySquare(gOrig), this.getSVal());
		}
	}
	
	private List<MoveAndDepth> nextMovesAndDepth(Grid g, SVal val, int depth) {
		List<RatedCoordinate> firstMoveRatedCoos = this.ratedCoosFilter.filterRatedCoos(this.squareHeuristic.getRatedCoos(val, g, streakLength));
		List<MoveAndDepth> md = AIPlayersCommon.mdfromRatedCoosList(firstMoveRatedCoos, val, depth);
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
	
}
