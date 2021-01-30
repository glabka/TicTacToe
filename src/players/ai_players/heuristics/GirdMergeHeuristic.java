package players.ai_players.heuristics;

import java.util.List;

import game_components.Grid;
import game_components.Square.SVal;
import players.ai_players.support_classes.AbstractCooValFromStreakEstimator;
import players.ai_players.support_classes.AbstractRatedCoosFilter;
import players.ai_players.support_classes.RatedCoordinate;

public class GirdMergeHeuristic extends AbstractGridHeuristic{

	public GirdMergeHeuristic(AbstractSquareHeuristic squareHeuristic, AbstractCooValFromStreakEstimator estimator,
			AbstractRatedCoosFilter ratedCoosFilter) {
		super(squareHeuristic, estimator, ratedCoosFilter);
	}

	@Override
	public double getGridsHeuristicValue(Grid g, SVal currentPlayer, int streakLength) {
		Double d = HeuristicCommon.getInfinityIfWinner(g, currentPlayer, streakLength);
		if(d != null) {
			return (double) d;
		}
		
		List<RatedCoordinate> potentialCoos = this.squareHeuristic.getRatedCoos(currentPlayer, g, streakLength);
		List<RatedCoordinate> filtredPotCoos = this.ratedCoosFilter.filterRatedCoos(potentialCoos);

		return HeuristicCommon.addAllRatedCoos(filtredPotCoos);
	}

}
