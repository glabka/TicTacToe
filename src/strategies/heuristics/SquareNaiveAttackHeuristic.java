package strategies.heuristics;

import java.util.LinkedList;
import java.util.List;

import game_components.Grid;
import game_components.ValuedMove;
import game_components.Square.SVal;
import grid_computations.Computations;
import grid_computations.Coordinate;
import grid_computations.PotentialStreak;
import strategies.support_classes.AbstractCooValFromStreakEstimator;
import strategies.support_classes.RatedCoordinate;

public class SquareNaiveAttackHeuristic extends AbstractSquareHeuristic {

	public SquareNaiveAttackHeuristic(AbstractCooValFromStreakEstimator cooEstimator) {
		super(cooEstimator);
	}


	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (!(o instanceof SquareNaiveAttackHeuristic)) {
			return false;
		}

		SquareNaiveAttackHeuristic other = (SquareNaiveAttackHeuristic) o;

		// checking all fields
		if (!this.cooEstimator.equals(other.cooEstimator)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		String str = "" + this.cooEstimator.hashCode() + 6;
		return str.hashCode();
	}

	@Override
	public List<RatedCoordinate> getRatedCoos(SVal playersSVal, Grid g, int streakLength) {
		
		int minNumOfFilledCoos = 1;
		List<PotentialStreak> potStreaks = Computations.getAllPotentialStreaks(g, playersSVal, streakLength, minNumOfFilledCoos);
		
		return attack(potStreaks, g, cooEstimator);
	}
	
	static List<RatedCoordinate> attack(List<PotentialStreak> potStreaks, Grid g, AbstractCooValFromStreakEstimator cooEstimator) {
		List<RatedCoordinate> ratedCoos = new LinkedList<>();
		if(potStreaks == null || potStreaks.isEmpty()) {
			Coordinate midOrFirstCoo = HeuristicCommon.getMiddleOrFirstEmptyCoo(g);
			ratedCoos.add(new RatedCoordinate(midOrFirstCoo, Double.POSITIVE_INFINITY));
			return ratedCoos;
		}
		
		return HeuristicCommon.getAllRatedCoosFromPotOfPotStreaks(potStreaks, cooEstimator);
	}

	@Override
	public Object clone() {
		return new SquareNaiveAttackHeuristic((AbstractCooValFromStreakEstimator) cooEstimator.clone());
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(cooEstimator = " + cooEstimator + ")";
	}
}
