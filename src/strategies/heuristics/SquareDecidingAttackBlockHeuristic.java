package strategies.heuristics;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import game_components.Grid;
import game_components.Square.SVal;
import grid_computations.Computations;
import grid_computations.PotentialStreak;
import strategies.support_classes.AbstractCooValFromStreakEstimator;
import strategies.support_classes.RatedCoordinate;

public class SquareDecidingAttackBlockHeuristic extends AbstractSquareHeuristic {

	public SquareDecidingAttackBlockHeuristic(AbstractCooValFromStreakEstimator cooEstimator) {
		super(cooEstimator);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (!(o instanceof SquareDecidingAttackBlockHeuristic)) {
			return false;
		}

		SquareDecidingAttackBlockHeuristic other = (SquareDecidingAttackBlockHeuristic) o;

		// checking all fields
		if (!this.cooEstimator.equals(other.cooEstimator)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		String str = "" + this.cooEstimator.hashCode() + 1;
		return str.hashCode();
	}


	@Override
	public List<RatedCoordinate> getRatedCoos(SVal playersSVal, Grid g, int streakLength) {
		int minNumOfFilledCoos = 1;
		List<PotentialStreak> opponentsPotStreaks = Computations.getAllPotentialStreaks(g, SVal.getOpposite(playersSVal), streakLength, minNumOfFilledCoos);
		List<PotentialStreak> potStreaks = Computations.getAllPotentialStreaks(g, playersSVal, streakLength, minNumOfFilledCoos);
		
		List<PotentialStreak> streaksForAtacking = HeuristicCommon.getAllMostFilledStreaks(potStreaks);
		List<PotentialStreak> threateningStreaks = opponentsPotStreaks.stream().filter(n -> n.filledCoosSize() >= streakLength - 2).collect(Collectors.toList());
		
		
		Comparator<PotentialStreak> mostFullStreakComparator = (PotentialStreak s1, PotentialStreak s2) -> Integer.compare(s1.filledCoosSize(), s2.filledCoosSize());
		Collections.sort(threateningStreaks, mostFullStreakComparator);
		int maxThreteningLenght = 0;
		if(threateningStreaks != null && !threateningStreaks.isEmpty()) {
			maxThreteningLenght = threateningStreaks.get(threateningStreaks.size() - 1).filledCoosSize();
		}
		int maxAttackingLength = 1; // it is preferable to attack when in case there is no blocking i.e. maxThreteningLenght = 0 
		if(streaksForAtacking != null && !streaksForAtacking.isEmpty()) {
			maxAttackingLength = streaksForAtacking.get(0).filledCoosSize();
		}
		
		if(maxAttackingLength > maxAttackingLength) {
			return HeuristicCommon.getBestCoosFromStreaksRatedAccordingToAllStreaks(opponentsPotStreaks, threateningStreaks, cooEstimator);
		} else {
			if(!potStreaks.isEmpty()) {
				return HeuristicCommon.getBestCoosFromStreaksRatedAccordingToAllStreaks(potStreaks, streaksForAtacking, cooEstimator);
			} else {
				return HeuristicCommon.getRatedCoosBasedOnNumOfPotStreaksTheyAreIn(g, playersSVal, streakLength);
			}
		}
	}

	@Override
	public Object clone() {
		return new SquareDecidingAttackBlockHeuristic((AbstractCooValFromStreakEstimator) cooEstimator.clone());
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(cooEstimator = " + cooEstimator + ")";
	}

}
