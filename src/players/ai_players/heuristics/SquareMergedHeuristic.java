package players.ai_players.heuristics;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import game_components.Grid;
import game_components.Square.SVal;
import grid_computations.Computations;
import grid_computations.PotentialStreak;
import players.ai_players.NaiveBlockAttackAIPlayer;
import players.ai_players.support_classes.AbstractCooValFromStreakEstimator;
import players.ai_players.support_classes.PoweredLengthCooValEstimator;
import players.ai_players.support_classes.RatedCoordinate;

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
		List<RatedCoordinate> coosForAttack = attack(g, potStreaks, cooEstimator);
		
		List<RatedCoordinate> combinedCoos = HeuristicCommon.combineTwoEqualRatedCoosFromTwoLists(coosForDefending, coosForAttack);
		return combinedCoos;
	}
	
	private List<RatedCoordinate> attack(Grid g, List<PotentialStreak> potStreaks, AbstractCooValFromStreakEstimator estimator) {
		if(potStreaks == null || potStreaks.isEmpty()) {
			return HeuristicCommon.getMiddleOrFirstEmptyCoo(g);
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
	

}
