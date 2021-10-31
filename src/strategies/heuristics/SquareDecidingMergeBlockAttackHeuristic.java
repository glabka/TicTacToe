package strategies.heuristics;

import java.util.List;
import java.util.stream.Collectors;

import game_components.Grid;
import game_components.Square.SVal;
import grid_computations.Computations;
import grid_computations.PotentialStreak;
import strategies.support_classes.AbstractCooValFromStreakEstimator;
import strategies.support_classes.AbstractRatedCoosFilter;
import strategies.support_classes.RatedCoordinate;

public class SquareDecidingMergeBlockAttackHeuristic extends AbstractSquareHeuristic{

	private AbstractRatedCoosFilter filter;
	
	public SquareDecidingMergeBlockAttackHeuristic(AbstractCooValFromStreakEstimator cooEstimator) {
		super(cooEstimator);
	}
	
	public SquareDecidingMergeBlockAttackHeuristic(AbstractCooValFromStreakEstimator cooEstimator, AbstractRatedCoosFilter filter) {
		super(cooEstimator);
		this.filter = filter;
	}

	@Override
	public List<RatedCoordinate> getRatedCoos(SVal playersSVal, Grid g, int streakLength) {
		int minNumOfFilledCoos = 1;
		List<PotentialStreak> opponentsPotStreaks = Computations.getAllPotentialStreaks(g, SVal.getOpposite(playersSVal), streakLength, minNumOfFilledCoos);
		List<PotentialStreak> potStreaks = Computations.getAllPotentialStreaks(g, playersSVal, streakLength, minNumOfFilledCoos);
		
		List<PotentialStreak> threateningStreaks = opponentsPotStreaks.stream().filter(n -> n.filledCoosSize() >= streakLength - 2).collect(Collectors.toList());
		List<PotentialStreak> streaksForAttack = HeuristicCommon.getAllMostFilledStreaks(potStreaks);
		
		int maxThreteningLenght = 0;
		if(threateningStreaks != null && !threateningStreaks.isEmpty()) {
			maxThreteningLenght = threateningStreaks.get(threateningStreaks.size() - 1).filledCoosSize();
		}
		int maxAttackingLength = 1; // it is preferable to attack when in case there is no blocking i.e. maxThreteningLenght = 0 
		if(streaksForAttack != null && !streaksForAttack.isEmpty()) {
			maxAttackingLength = streaksForAttack.get(0).filledCoosSize();
		}

		
		List<RatedCoordinate> ratedCoos = null;
		
		if(maxThreteningLenght > maxAttackingLength) {
			if(threateningStreaks.size() > 1) {
				// best defense values form point of view of defending
//				System.out.println("Best defense according to defense"); // debug
				ratedCoos = filter == null ? HeuristicCommon.getBestCoosFromStreaksRatedAccordingToAllStreaks(opponentsPotStreaks, threateningStreaks, cooEstimator) :
					HeuristicCommon.getBestCoosFromStreaksRatedAccordingToAllStreaksFiltered(opponentsPotStreaks, threateningStreaks, cooEstimator, filter);
			} else if (threateningStreaks.size() == 1) {
				// best defense values from point of view of offense
//				System.out.println("Best defense according to offense"); // debug
				ratedCoos = filter == null ? HeuristicCommon.getBestCoosFromStreaksRatedAccordingToAllStreaks(potStreaks, threateningStreaks, cooEstimator) :
					HeuristicCommon.getBestCoosFromStreaksRatedAccordingToAllStreaksFiltered(potStreaks, threateningStreaks, cooEstimator, filter);
				
				if(ratedCoos == null || ratedCoos.isEmpty()) {
					// best values form point of view of defending
//					System.out.println("Best defense according to defense"); // debug
					ratedCoos = filter == null ? HeuristicCommon.getBestCoosFromStreaksRatedAccordingToAllStreaks(opponentsPotStreaks, threateningStreaks, cooEstimator) :
						HeuristicCommon.getBestCoosFromStreaksRatedAccordingToAllStreaksFiltered(opponentsPotStreaks, threateningStreaks, cooEstimator, filter);
				}
			}
		} else {
			// best values from point of view of attacking
//			System.out.println("attack"); // debug
			ratedCoos = filter == null ? HeuristicCommon.getBestCoosFromStreaksRatedAccordingToAllStreaks(potStreaks, streaksForAttack, cooEstimator) :
			HeuristicCommon.getBestCoosFromStreaksRatedAccordingToAllStreaksFiltered(potStreaks, streaksForAttack, cooEstimator, filter);
		}
		
		if(ratedCoos == null || ratedCoos.isEmpty()) {
//			System.out.println("ratedCoos == null || ratedCoos.isEmpty()"); // debug
			return HeuristicCommon.getRatedCoosBasedOnNumOfPotStreaksTheyAreIn(g, playersSVal, streakLength);
		} else {		
			return ratedCoos;
		}
		
	}

	@Override
	public Object clone() {
		if (filter == null) {
			return new SquareDecidingMergeBlockAttackHeuristic((AbstractCooValFromStreakEstimator) cooEstimator.clone());
		} else {
			return new SquareDecidingMergeBlockAttackHeuristic((AbstractCooValFromStreakEstimator) cooEstimator.clone(), (AbstractRatedCoosFilter) filter.clone());
		}
	}

}
