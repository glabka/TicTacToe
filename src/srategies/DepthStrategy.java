package srategies;

import java.util.LinkedList;
import java.util.List;

import game_components.Grid;
import game_components.Square.SVal;
import game_components.ValuedMove;
import game_mechanics.Rules;
import strategies.heuristics.AbstractGridHeuristic;
import strategies.heuristics.AbstractSquareHeuristic;
import strategies.heuristics.HeuristicCommon;
import strategies.support_classes.AbstractRatedCoosFilter;
import strategies.support_classes.MoveAndDepth;
import strategies.support_classes.RatedCoordinate;

public class DepthStrategy extends AbstractStrategy {
	
	protected int depth;

	public DepthStrategy(AbstractSquareHeuristic squareHeuristic, AbstractGridHeuristic gridHeuristic, AbstractRatedCoosFilter ratedCoosFilter, int depth) {
		super(squareHeuristic, gridHeuristic, ratedCoosFilter);
		this.depth = depth;
	}
	
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (!(o instanceof DepthStrategy)) {
			return false;
		}

		DepthStrategy other = (DepthStrategy) o;

		// checking all fields
		if (!this.squareHeuristic.equals(other.squareHeuristic)) {
			return false;
		} else if (!this.gridHeuristic.equals(other.gridHeuristic)) {
			return false;
		} else if (!this.ratedCoosFilter.equals(other.getRatedCoosFilter())) {
			return false;
		} else if (this.depth != other.depth) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		String str = "" + this.squareHeuristic.hashCode() + this.gridHeuristic.hashCode() + this.ratedCoosFilter.hashCode() + depth;
		return str.hashCode();
	}
	
	@Override
	public ValuedMove nextMove(SVal sVal, Grid g, int streakLength) {
		Grid gOrig = g;
		g = g.gridsCopy();
		
		List<MoveAndDepth> md = nextMovesAndDepth(g, sVal, 0);
		
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
				double moveHeuristicValue = this.gridHeuristic.getGridsHeuristicValue(g, StrategiesCommon.getPlayer(mvAndDepth.getDepth(), sVal), streakLength);
				if(bestMoveValue < moveHeuristicValue) {
					bestMove = proceededMoves.get(0);
					bestMoveValue = moveHeuristicValue;
				}
			}
			
			SVal winner = Rules.findWinner(g, streakLength);
			if(winner == sVal && winningMoveDepth < mvAndDepth.getDepth()) {
				bestMove = proceededMoves.get(0);
				winningMoveDepth = mvAndDepth.getDepth();
				bestMoveValue = Double.POSITIVE_INFINITY;
			} else if (winner == SVal.getOpposite(sVal)) {
				continue;
			} else if(Rules.endOfGame(g, streakLength)) {
				continue;
			}
			
//			System.out.println("bestMove so far = " + bestMove + ", value = " + bestMoveValue); // debug
			
			// determining and adding next moves
			if(mvAndDepth.getDepth() + 1 < depth) {
				List<MoveAndDepth> nextMd = nextMovesAndDepth(g, StrategiesCommon.getPlayer(mvAndDepth.getDepth() + 1, sVal), mvAndDepth.getDepth() + 1);
				StrategiesCommon.addToBeginingOfList(md, nextMd);
			}
		}
		
		if(bestMove != null) {
			return bestMove;
		} else {
//			System.out.println("FIRST EMPTY SQUARE MOVE"); // debug
			return new ValuedMove(HeuristicCommon.firtEmptySquare(gOrig), sVal);
		}
	}
	
	private List<MoveAndDepth> nextMovesAndDepth(Grid g, SVal val, int depth) {
		List<RatedCoordinate> firstMoveRatedCoos = this.ratedCoosFilter.filterRatedCoos(this.squareHeuristic.getRatedCoos(val, g, streakLength));
		List<MoveAndDepth> md = StrategiesCommon.mdfromRatedCoosList(firstMoveRatedCoos, val, depth);
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
