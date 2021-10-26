package players.ai_players.heuristics;

import java.util.List;

import game_components.Grid;
import game_components.Square.SVal;
import grid_computations.Computations;
import grid_computations.PotentialStreak;
import players.ai_players.support_classes.AbstractCooValFromStreakEstimator;
import players.ai_players.support_classes.AbstractRatedCoosFilter;
import players.ai_players.support_classes.PoweredLengthCooValEstimator;
import players.ai_players.support_classes.RatedCoordinate;

public class GridDiffHeuristic extends AbstractGridHeuristic {

	public GridDiffHeuristic(AbstractCooValFromStreakEstimator estimator) {
		super(estimator);
	}



	@Override
	public double getGridsHeuristicValue(Grid g, SVal currentPlayer, int streakLength) {
		int minNumOfFilledCoos = 1;
		List<PotentialStreak> opponentsPotStreaks = Computations.getAllPotentialStreaks(g, SVal.getOpposite(currentPlayer), streakLength, minNumOfFilledCoos);
		List<PotentialStreak> potStreaks = Computations.getAllPotentialStreaks(g, currentPlayer, streakLength, minNumOfFilledCoos);
		
		List<RatedCoordinate> coosForDefending = defend(opponentsPotStreaks, estimator);
		List<RatedCoordinate> coosForAttack = attack(g, potStreaks, estimator);
		
		return HeuristicCommon.addAllRatedCoos(coosForAttack) - HeuristicCommon.addAllRatedCoos(coosForDefending);
	}
	

	
	public static List<RatedCoordinate> attack(Grid g, List<PotentialStreak> potStreaks, AbstractCooValFromStreakEstimator estimator) {
		if(potStreaks == null || potStreaks.isEmpty()) {
			return null;
		}
		
		List<RatedCoordinate> allRatedCoos = HeuristicCommon.getAllRatedCoosFromPotOfPotStreaks(potStreaks, estimator);
		List<RatedCoordinate> combinedCoos = HeuristicCommon.combineAllEqualRatedCoos(allRatedCoos);
		return combinedCoos;
	}
	
	public static List<RatedCoordinate> defend(List<PotentialStreak> opponentsPotStreaks, AbstractCooValFromStreakEstimator estimator){
		List<RatedCoordinate> allRatedCoos = HeuristicCommon.getAllRatedCoosFromPotOfPotStreaks(opponentsPotStreaks, estimator);
		List<RatedCoordinate> combinedCoos = HeuristicCommon.combineAllEqualRatedCoos(allRatedCoos);
		return combinedCoos;
	}



	@Override
	public Object clone() {
		return new GridDiffHeuristic((AbstractCooValFromStreakEstimator) estimator.clone());
	}

}
