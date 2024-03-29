package strategies.heuristics;

import java.util.List;
import java.util.stream.Collectors;

import game_components.Grid;
import game_components.Square.SVal;
import grid_computations.Computations;
import grid_computations.PotentialStreak;
import strategies.support_classes.AbstractCooValFromStreakEstimator;
import strategies.support_classes.RatedCoordinate;

public class SquareBlockAttackHeuristic extends AbstractSquareHeuristic {

	
	public SquareBlockAttackHeuristic(AbstractCooValFromStreakEstimator cooEstimator) {
		super(cooEstimator);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (!(o instanceof SquareBlockAttackHeuristic)) {
			return false;
		}

		SquareBlockAttackHeuristic other = (SquareBlockAttackHeuristic) o;

		// checking all fields
		if (!this.cooEstimator.equals(other.cooEstimator)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		String str = "" + this.cooEstimator.hashCode() + 0;
		return str.hashCode();
	}

	@Override
	public List<RatedCoordinate> getRatedCoos(SVal playersSVal, Grid g, int streakLength) {
		int minNumOfFilledCoos = 1;
		List<PotentialStreak> opponentsPotStreaks = Computations.getAllPotentialStreaks(g, SVal.getOpposite(playersSVal), streakLength, minNumOfFilledCoos);
		List<PotentialStreak> potStreaks = Computations.getAllPotentialStreaks(g, playersSVal, streakLength, minNumOfFilledCoos);
		
		List<PotentialStreak> threateningStreaks = opponentsPotStreaks.stream().filter(n -> n.filledCoosSize() >= streakLength - 2).collect(Collectors.toList());
		
		
		if(!threateningStreaks.isEmpty()) {
			return HeuristicCommon.getBestCoosFromStreaksRatedAccordingToAllStreaks(opponentsPotStreaks, threateningStreaks, cooEstimator);
		} else {
			if(!potStreaks.isEmpty()) {
				List<PotentialStreak> streaksForAtacking = HeuristicCommon.getAllMostFilledStreaks(potStreaks);
				return HeuristicCommon.getBestCoosFromStreaksRatedAccordingToAllStreaks(potStreaks, streaksForAtacking, cooEstimator);
			} else {
				return HeuristicCommon.getRatedCoosBasedOnNumOfPotStreaksTheyAreIn(g, playersSVal, streakLength);
			}
		}
	}

	@Override
	public Object clone() {
		return new SquareBlockAttackHeuristic((AbstractCooValFromStreakEstimator) cooEstimator.clone());
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(cooEstimator = " + cooEstimator + ")";
	}
}
