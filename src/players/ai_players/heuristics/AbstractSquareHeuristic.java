package players.ai_players.heuristics;

import java.util.List;

import game_components.Grid;
import game_components.Square.SVal;
import players.ai_players.support_classes.AbstractCooValFromStreakEstimator;
import players.ai_players.support_classes.RatedCoordinate;

/**
 * Heuristics for search returning RatedCoordinates
 * @author glabk
 *
 */
public abstract class AbstractSquareHeuristic {
	
	protected AbstractCooValFromStreakEstimator cooEstimator;
	
	public AbstractSquareHeuristic(AbstractCooValFromStreakEstimator cooEstimator) {
		this.cooEstimator = cooEstimator;
	}

	public abstract List<RatedCoordinate> getRatedCoos(SVal playersSVal, Grid g, int streakLength);
	
}
