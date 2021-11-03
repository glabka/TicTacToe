package strategies.heuristics;

import java.util.List;

import game_components.Grid;
import game_components.Square.SVal;
import strategies.support_classes.AbstractCooValFromStreakEstimator;
import strategies.support_classes.RatedCoordinate;

/**
 * Heuristics for search returning RatedCoordinates
 * @author glabk
 *
 */
public abstract class AbstractSquareHeuristic implements Cloneable {
	
	protected AbstractCooValFromStreakEstimator cooEstimator;
	
	public AbstractSquareHeuristic(AbstractCooValFromStreakEstimator cooEstimator) {
		this.cooEstimator = cooEstimator;
	}

	public abstract List<RatedCoordinate> getRatedCoos(SVal playersSVal, Grid g, int streakLength);
	
	public abstract Object clone();

	public abstract boolean equals(Object o);

	public abstract int hashCode();
}
