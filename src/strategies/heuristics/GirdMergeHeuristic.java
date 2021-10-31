package strategies.heuristics;

import java.util.List;

import game_components.Grid;
import game_components.Square.SVal;
import strategies.support_classes.AbstractCooValFromStreakEstimator;
import strategies.support_classes.AbstractRatedCoosFilter;
import strategies.support_classes.RatedCoordinate;

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

	@Override
	public Object clone() {
		return new GirdMergeHeuristic((AbstractSquareHeuristic) squareHeuristic.clone(),
				(AbstractCooValFromStreakEstimator) estimator.clone(), (AbstractRatedCoosFilter)
				ratedCoosFilter.clone());
	}

}
