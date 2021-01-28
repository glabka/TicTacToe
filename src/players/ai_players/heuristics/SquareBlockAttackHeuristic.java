package players.ai_players.heuristics;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import game_components.Grid;
import game_components.Move;
import game_components.Square.SVal;
import grid_computations.Computations;
import grid_computations.Coordinate;
import grid_computations.PotStreakFilledLengthComparator;
import grid_computations.PotentialStreak;
import players.ai_players.support_classes.AbstractCooValFromStreakEstimator;
import players.ai_players.support_classes.RatedCoordinate;

public class SquareBlockAttackHeuristic extends SquareAttackHeuristic {

	@Override
	public List<RatedCoordinate> getRatedCoos(SVal playersSVal, Grid g, int streakLength, AbstractCooValFromStreakEstimator cooEstimator) {
		List<PotentialStreak> opponentsPotStreaks = Computations.getAllPotentialStreaks(g, SVal.getOpposite(playersSVal), streakLength);
		List<PotentialStreak> potStreaks = Computations.getAllPotentialStreaks(g, playersSVal, streakLength);
		
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
			return Common.getAllRatedCoosFromPotOfPotStreaks(opponentsPotStreaks, cooEstimator);
		} else {
			return null;
		}
	}
	
}
