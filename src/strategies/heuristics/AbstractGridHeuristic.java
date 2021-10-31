package players.ai_players.heuristics;

import game_components.Grid;
import game_components.Square.SVal;
import players.ai_players.support_classes.AbstractCooValFromStreakEstimator;
import players.ai_players.support_classes.AbstractRatedCoosFilter;

public abstract class AbstractGridHeuristic implements Cloneable{
	
	protected AbstractSquareHeuristic squareHeuristic;
	protected AbstractCooValFromStreakEstimator estimator;
	protected AbstractRatedCoosFilter ratedCoosFilter;
	
	public AbstractGridHeuristic(AbstractCooValFromStreakEstimator estimator) {
		this.estimator = estimator;
	}
	
	public AbstractGridHeuristic(AbstractSquareHeuristic squareHeuristic, AbstractCooValFromStreakEstimator estimator, AbstractRatedCoosFilter ratedCoosFilter) {
		this.squareHeuristic = squareHeuristic;
		this.estimator = estimator;
		this.ratedCoosFilter = ratedCoosFilter;
	}
	
	public abstract double getGridsHeuristicValue(Grid g, SVal currentPlayer, int streakLength);
	
	public abstract Object clone();

}
