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
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (!(o instanceof GirdMergeHeuristic)) {
			return false;
		}

		GirdMergeHeuristic other = (GirdMergeHeuristic) o;

		// checking all fields
		if (!this.squareHeuristic.equals(other.squareHeuristic)) {
			return false;
		} else if (!this.estimator.equals(other.estimator)) {
			return false;
		} else if (!this.ratedCoosFilter.equals(other.ratedCoosFilter)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		String str = "" + this.squareHeuristic.hashCode() + this.estimator.hashCode() + this.ratedCoosFilter.hashCode();
		return str.hashCode();
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

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(squareHeuristic = " + squareHeuristic + ", estimator = " + estimator + ", ratedCoosFilter = " + ratedCoosFilter + ")";
	}
}
