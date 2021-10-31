package strategies.heuristics;

import java.util.List;

import game_components.Grid;
import game_components.Square.SVal;
import game_mechanics.Rules;
import strategies.support_classes.AbstractCooValFromStreakEstimator;
import strategies.support_classes.AbstractRatedCoosFilter;
import strategies.support_classes.RatedCoordinate;

/**
 * probably quite bad idea this heuristic
 * @author glabk
 *
 */
public class GridDiffRatedValuesHeuristic extends AbstractGridHeuristic {


	public GridDiffRatedValuesHeuristic(AbstractSquareHeuristic squareHeuristic, AbstractCooValFromStreakEstimator estimator,
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
		
		List<RatedCoordinate> opponentsCoos = this.squareHeuristic.getRatedCoos(SVal.getOpposite(currentPlayer), g, streakLength);
		List<RatedCoordinate> filtredOpponentsCoos = this.ratedCoosFilter.filterRatedCoos(opponentsCoos);
		
		return HeuristicCommon.addAllRatedCoos(filtredPotCoos) - HeuristicCommon.addAllRatedCoos(filtredOpponentsCoos);
	}

	@Override
	public Object clone() {
		return new GridDiffRatedValuesHeuristic((AbstractSquareHeuristic) squareHeuristic.clone(), (AbstractCooValFromStreakEstimator)
				estimator.clone(), (AbstractRatedCoosFilter) ratedCoosFilter.clone());
	}
	
	
}
