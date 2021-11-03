package strategies;

import game_components.Grid;
import game_components.Square.SVal;
import game_components.ValuedMove;
import strategies.heuristics.AbstractGridHeuristic;
import strategies.heuristics.AbstractSquareHeuristic;
import strategies.support_classes.AbstractRatedCoosFilter;

public abstract class AbstractStrategy {

	protected int streakLength;
	protected AbstractSquareHeuristic squareHeuristic;
	protected AbstractGridHeuristic gridHeuristic;
	protected AbstractRatedCoosFilter ratedCoosFilter;

	public AbstractStrategy() {}
	
	public AbstractStrategy(AbstractSquareHeuristic squareHeuristic) {
		this.squareHeuristic = squareHeuristic;
	}
	
	public AbstractStrategy(AbstractSquareHeuristic squareHeuristic, AbstractGridHeuristic gridHeuristic, AbstractRatedCoosFilter ratedCoosFilter) {
		this.squareHeuristic = squareHeuristic;
		this.gridHeuristic = gridHeuristic;
		this.ratedCoosFilter = ratedCoosFilter;
	}
	
	public abstract ValuedMove nextMove(SVal sVal, Grid g, int streakLength);
	
	public abstract boolean equals(Object o);
	
	public abstract int hashCode();
	
	public AbstractSquareHeuristic getSquareHeuristic() {
		if (squareHeuristic == null) {
			return null;
		} else {
			return (AbstractSquareHeuristic) squareHeuristic.clone();
		}
	}

	public AbstractGridHeuristic getGridHeuristic() {
		if (gridHeuristic == null) {
			return null;
		} else {
			return (AbstractGridHeuristic) gridHeuristic.clone();
		}
	}

	public AbstractRatedCoosFilter getRatedCoosFilter() {
		if (ratedCoosFilter == null) {
			return null;
		} else {
			return (AbstractRatedCoosFilter) ratedCoosFilter.clone();
		}
	}
}
