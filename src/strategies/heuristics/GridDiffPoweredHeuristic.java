package strategies.heuristics;

import java.util.List;

import game_components.Grid;
import game_components.Square.SVal;
import grid_computations.Computations;
import grid_computations.PotentialStreak;
import strategies.support_classes.AbstractCooValFromStreakEstimator;
import strategies.support_classes.RatedCoordinate;

public class GridDiffPoweredHeuristic extends AbstractGridHeuristic {

	private int power;
	
	public GridDiffPoweredHeuristic(AbstractCooValFromStreakEstimator estimator, int power) {
		super(estimator);
		this.power = power;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (!(o instanceof GridDiffPoweredHeuristic)) {
			return false;
		}

		GridDiffPoweredHeuristic other = (GridDiffPoweredHeuristic) o;

		// checking all fields
		if (!this.estimator.equals(other.estimator)) {
			return false;
		} else if (this.power != other.power) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		String str = "" + this.estimator.hashCode() + this.power;
		return str.hashCode();
	}
	
	@Override
	public double getGridsHeuristicValue(Grid g, SVal currentPlayer, int streakLength) {
		int minNumOfFilledCoos = 1;
		List<PotentialStreak> opponentsPotStreaks = Computations.getAllPotentialStreaks(g, SVal.getOpposite(currentPlayer), streakLength, minNumOfFilledCoos);
		List<PotentialStreak> potStreaks = Computations.getAllPotentialStreaks(g, currentPlayer, streakLength, minNumOfFilledCoos);
		
		List<RatedCoordinate> coosForDefending = GridDiffHeuristic.defend(opponentsPotStreaks, estimator);
		List<RatedCoordinate> coosForAttack = GridDiffHeuristic.attack(g, potStreaks, estimator);
		
		return Math.pow(HeuristicCommon.addAllRatedCoos(coosForAttack), power) - Math.pow(HeuristicCommon.addAllRatedCoos(coosForDefending), power);
	}

	@Override
	public Object clone() {
		return new GridDiffPoweredHeuristic((AbstractCooValFromStreakEstimator) estimator.clone(), power);
	}
	
}
