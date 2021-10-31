package strategies.heuristics;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import game_components.Grid;
import game_components.Square.SVal;
import grid_computations.Computations;
import grid_computations.PotentialStreak;
import srategies.NaiveBlockAttackStrategy;
import strategies.support_classes.AbstractCooValFromStreakEstimator;
import strategies.support_classes.PoweredLengthCooValEstimator;
import strategies.support_classes.RatedCoordinate;

public class SquareMergedHeuristic extends AbstractSquareHeuristic{

	public SquareMergedHeuristic(AbstractCooValFromStreakEstimator cooEstimator) {
		super(cooEstimator);
	}

	@Override
	public List<RatedCoordinate> getRatedCoos(SVal playersSVal, Grid g, int streakLength) {
		int minNumOfFilledCoos = 1;
		List<PotentialStreak> opponentsPotStreaks = Computations.getAllPotentialStreaks(g, SVal.getOpposite(playersSVal), streakLength, minNumOfFilledCoos);
		List<PotentialStreak> potStreaks = Computations.getAllPotentialStreaks(g, playersSVal, streakLength, minNumOfFilledCoos);
		
		List<RatedCoordinate> coosForDefending = defend(opponentsPotStreaks, cooEstimator);
//		System.out.println("coosForDefending = " + coosForDefending); // debug
		List<RatedCoordinate> coosForAttack = attack(g, playersSVal, streakLength, potStreaks, cooEstimator);
//		System.out.println("coosForAttack = " + coosForAttack); // debug
		List<RatedCoordinate> combinedCoos = HeuristicCommon.combineTwoEqualRatedCoosFromTwoLists(coosForDefending, coosForAttack);
		return combinedCoos;
	}
	
	private List<RatedCoordinate> attack(Grid g, SVal playersSVal, int streakLength, List<PotentialStreak> potStreaks, AbstractCooValFromStreakEstimator estimator) {
		if(potStreaks == null || potStreaks.isEmpty()) {
			return HeuristicCommon.getRatedCoosBasedOnNumOfPotStreaksTheyAreIn(g, playersSVal, streakLength);
		}
		
		List<RatedCoordinate> allRatedCoos = HeuristicCommon.getAllRatedCoosFromPotOfPotStreaks(potStreaks, estimator);
		List<RatedCoordinate> combinedCoos = HeuristicCommon.combineAllEqualRatedCoos(allRatedCoos);
		return combinedCoos;
	}
	
	private List<RatedCoordinate> defend(List<PotentialStreak> opponentsPotStreaks, AbstractCooValFromStreakEstimator estimator){
		List<RatedCoordinate> allRatedCoos = HeuristicCommon.getAllRatedCoosFromPotOfPotStreaks(opponentsPotStreaks, estimator);
		List<RatedCoordinate> combinedCoos = HeuristicCommon.combineAllEqualRatedCoos(allRatedCoos);
		return combinedCoos;
	}

	@Override
	public Object clone() {
		return new SquareMergedHeuristic((AbstractCooValFromStreakEstimator) cooEstimator.clone());
	}
	

}
