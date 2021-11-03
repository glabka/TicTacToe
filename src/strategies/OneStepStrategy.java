package strategies;

import java.util.Collections;
import java.util.List;

import game_components.Grid;
import game_components.Square.SVal;
import game_components.ValuedMove;
import grid_computations.Coordinate;
import strategies.heuristics.AbstractSquareHeuristic;
import strategies.support_classes.RatedCoordinate;
import strategies.support_classes.RatedCoordinatesValueComparator;

public class OneStepStrategy extends AbstractStrategy {

	public OneStepStrategy(AbstractSquareHeuristic squareHeuristic) {
		super(squareHeuristic);
	}

	@Override
	public ValuedMove nextMove(SVal sVal, Grid g, int streakLength) {
		List<RatedCoordinate> suggestedCoos = this.squareHeuristic.getRatedCoos(sVal, g, streakLength);
//		if(suggestedCoos != null) { // debug
//			Collections.sort(suggestedCoos, new RatedCoordinatesValueComparator()); // debug
//			Collections.reverse(suggestedCoos); // debug
//			System.out.println(suggestedCoos);// debug
//		} // debug
		Coordinate bestCoo = Collections.max(suggestedCoos, new RatedCoordinatesValueComparator());
		return new ValuedMove(bestCoo, sVal);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (!(o instanceof OneStepStrategy)) {
			return false;
		}

		OneStepStrategy other = (OneStepStrategy) o;
		// checking fields
		if (!this.gridHeuristic.equals(other.gridHeuristic)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return ("" + this.gridHeuristic.hashCode()).hashCode();
	}

}
