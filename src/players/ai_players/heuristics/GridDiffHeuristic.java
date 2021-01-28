package players.ai_players.heuristics;

import java.util.List;

import game_components.Grid;
import game_components.Square.SVal;
import players.ai_players.support_classes.AbstractCooValFromStreakEstimator;
import players.ai_players.support_classes.AbstractRatedCoosFilter;
import players.ai_players.support_classes.PoweredLengthCooValEstimator;
import players.ai_players.support_classes.RatedCoordinate;

public class GridDiffHeuristic extends AbstractGridHeuristic {


	public GridDiffHeuristic(AbstractSquareHeuristic squareHeuristic, AbstractCooValFromStreakEstimator estimator,
			AbstractRatedCoosFilter ratedCoosFilter) {
		super(squareHeuristic, estimator, ratedCoosFilter);
	}

	@Override
	public double getGridsHeuristicValue(Grid g, SVal currentPlayer, int streakLength) {
		AbstractCooValFromStreakEstimator cooEstimator = new PoweredLengthCooValEstimator(2);
		
		List<RatedCoordinate> potentialCoos = this.squareHeuristic.getRatedCoos(currentPlayer, g, streakLength, cooEstimator);
		List<RatedCoordinate> filtredPotCoos = this.ratedCoosFilter.filterRatedCoos(potentialCoos);
		
		List<RatedCoordinate> opponentsCoos = this.squareHeuristic.getRatedCoos(SVal.getOpposite(currentPlayer), g, streakLength, cooEstimator);
		List<RatedCoordinate> filtredOpponentsCoos = this.ratedCoosFilter.filterRatedCoos(opponentsCoos);
		
		return Common.addAllRatedCoos(filtredPotCoos) - Common.addAllRatedCoos(filtredOpponentsCoos);
	}
	
	
}
