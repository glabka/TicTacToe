package players.ai_players.heuristics;

import java.util.List;

import game_components.Grid;
import game_components.Square.SVal;
import grid_computations.Computations;
import grid_computations.PotentialStreak;
import players.ai_players.support_classes.AbstractCooValFromStreakEstimator;
import players.ai_players.support_classes.RatedCoordinate;

public class GridDiffPoweredHeuristic extends AbstractGridHeuristic {

	private int power;
	
	public GridDiffPoweredHeuristic(AbstractCooValFromStreakEstimator estimator, int power) {
		super(estimator);
		this.power = power;
	}

	@Override
	public double getGridsHeuristicValue(Grid g, SVal currentPlayer, int streakLength) {
		int minNumOfFilledCoos = 1;
		List<PotentialStreak> opponentsPotStreaks = Computations.getAllPotentialStreaks(g, SVal.getOpposite(currentPlayer), streakLength, minNumOfFilledCoos);
		List<PotentialStreak> potStreaks = Computations.getAllPotentialStreaks(g, currentPlayer, streakLength, minNumOfFilledCoos);
		
		List<RatedCoordinate> coosForDefending = GridDiffHeuristic.defend(opponentsPotStreaks, estimator);
		List<RatedCoordinate> coosForAttack = GridDiffHeuristic.attack(g, potStreaks, estimator);
		
		return Math.pow(HeuristicCommon.addAllRatedCoos(coosForAttack), power) - Math.pow(HeuristicCommon.addAllRatedCoos(coosForDefending), power);
	}
	
}
