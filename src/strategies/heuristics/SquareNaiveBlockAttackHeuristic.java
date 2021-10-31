package players.ai_players.heuristics;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import game_components.Grid;
import game_components.ValuedMove;
import game_components.Square.SVal;
import grid_computations.Computations;
import grid_computations.Coordinate;
import grid_computations.PotStreakFilledLengthComparator;
import grid_computations.PotentialStreak;
import players.ai_players.support_classes.AbstractCooValFromStreakEstimator;
import players.ai_players.support_classes.RatedCoordinate;

public class SquareNaiveBlockAttackHeuristic extends SquareNaiveAttackHeuristic {

	public SquareNaiveBlockAttackHeuristic(AbstractCooValFromStreakEstimator cooEstimator) {
		super(cooEstimator);
	}

	@Override
	public List<RatedCoordinate> getRatedCoos(SVal playersSVal, Grid g, int streakLength) {
		int minNumOfFilledCoos = 1;
		List<PotentialStreak> opponentsPotStreaks = Computations.getAllPotentialStreaks(g, SVal.getOpposite(playersSVal), streakLength, minNumOfFilledCoos);
		List<PotentialStreak> potStreaks = Computations.getAllPotentialStreaks(g, playersSVal, streakLength, minNumOfFilledCoos);
		
		List<RatedCoordinate> ratedCoosForDefending = defend(opponentsPotStreaks, streakLength - 2, cooEstimator);
		if(ratedCoosForDefending != null) {
			return ratedCoosForDefending;
		} else {
			return attack(potStreaks, g, cooEstimator);
		}
	}

	private static List<RatedCoordinate> defend(List<PotentialStreak> opponentsPotStreaks, int dangerousStreakLength, AbstractCooValFromStreakEstimator cooEstimator) {
		if(opponentsPotStreaks == null || opponentsPotStreaks.isEmpty()) {
			return null;
		}
		
		PotentialStreak fullestStreak = Collections.max(opponentsPotStreaks, new PotStreakFilledLengthComparator());
		if(fullestStreak.filledCoosSize() >= dangerousStreakLength){
			return HeuristicCommon.getAllRatedCoosFromPotOfPotStreaks(opponentsPotStreaks, cooEstimator);
		} else {
			return null;
		}
	}
	
	public Object clone() {
		return new SquareNaiveBlockAttackHeuristic((AbstractCooValFromStreakEstimator) cooEstimator.clone());
	}
}
