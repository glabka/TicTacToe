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
		List<PotentialStreak> opponentsPotStreaks = Computations.getAllPotentialStreaks(g, SVal.getOpposite(playersSVal), streakLength);
		List<PotentialStreak> potStreaks = Computations.getAllPotentialStreaks(g, playersSVal, streakLength);
		AbstractCooValFromStreakEstimator estimator = new PoweredLengthCooValEstimator(2);
		
		List<RatedCoordinate> coosForDefending = defend(opponentsPotStreaks, estimator);
		List<RatedCoordinate> coosForAttack = attack(g, potStreaks, estimator);
		
		List<RatedCoordinate> combinedCoos = combineTwoEqualRatedCoosFromTwoLists(coosForDefending, coosForAttack);
		return combinedCoos;
	}
	
	private List<RatedCoordinate> attack(Grid g, List<PotentialStreak> potStreaks, AbstractCooValFromStreakEstimator estimator) {
		if(potStreaks == null || potStreaks.isEmpty()) {
			List<RatedCoordinate> list = new ArrayList<>();
			int row = g.size() / 2;
			int column = row;
			double value = Double.POSITIVE_INFINITY;
			if(g.isSquareEmpty(row, column)) {
				list.add(new RatedCoordinate(row, column, value));
				return list;
			} else if (g.size() > 2 && g.isSquareEmpty(row - 1, column - 1)) {
				list.add(new RatedCoordinate(row - 1, column - 1, value));
				return list;
			} else {
				
				list.add(new RatedCoordinate(NaiveBlockAttackAIPlayer.firtEmptySquare(g), value));
				return list;
			}
		}
		
		List<RatedCoordinate> allRatedCoos = HeuristicCommon.getAllRatedCoosFromPotOfPotStreaks(potStreaks, estimator);
		List<RatedCoordinate> combinedCoos = combineAllEqualRatedCoos(allRatedCoos);
		return combinedCoos;
	}
	
	private List<RatedCoordinate> defend(List<PotentialStreak> opponentsPotStreaks, AbstractCooValFromStreakEstimator estimator){
		List<RatedCoordinate> allRatedCoos = HeuristicCommon.getAllRatedCoosFromPotOfPotStreaks(opponentsPotStreaks, estimator);
//		// filtering out just rated coordinates of value bigger then steakLength - 2
//		List<RatedCoordinate> filteredRatedCoos = allRatedCoos.stream().filter(c -> c.getValue() > streakLength - 2).collect(Collectors.toList());
//		List<RatedCoordinate> combinedCoos = combineAllEqualRatedCoos(filteredRatedCoos);
		List<RatedCoordinate> combinedCoos = combineAllEqualRatedCoos(allRatedCoos);
		return combinedCoos;
	}
	
	/**
	 * Combines equal coordinates.
	 * It assumes that in either list there is only one RatedCoordinate of certain row and column.
	 * @param list1
	 * @param list2
	 * @return
	 */
	private List<RatedCoordinate> combineTwoEqualRatedCoosFromTwoLists(List<RatedCoordinate> list1, List<RatedCoordinate> list2) {
		if(list1 == null || list1.isEmpty()) {
			return list2;
		} else if (list2 == null || list2.isEmpty()) {
			return list1;
		}
		
		List<RatedCoordinate> combinedCoos = new LinkedList<>();
		
		for(RatedCoordinate coo : list1) {

			for(RatedCoordinate coo2 : list2) {
				if(coo.equals(coo2)) {
					coo = RatedCoordinate.addSameCoordinates(coo, coo2);
				}
			}
			
			combinedCoos.add(coo);
		}
		
		return combinedCoos;
	}
	
	/**
	 * Combines all RatedCoodrinates that are equal.
	 * @param ratedCoos
	 * @return
	 */
	private List<RatedCoordinate> combineAllEqualRatedCoos(List<RatedCoordinate> ratedCoos) {
		if(ratedCoos == null) {
			return null;
		}
		
		List<RatedCoordinate> combinedCoos = new LinkedList<>();
		
		for(RatedCoordinate coo : ratedCoos) {
			// unnecessary when removing of combined coordinates is implemented
			if(combinedCoos.contains(coo))
				continue;

			RatedCoordinate newCoo = coo;
			int cooIndex = ratedCoos.indexOf(coo);
//			List<RatedCoordinate> toBeRemoved = new LinkedList<>();
//			toBeRemoved.add(coo);
			for(int i = 0; i < ratedCoos.size(); i++) {
				RatedCoordinate cooi = ratedCoos.get(i);
				if(cooi.equals(coo) && i != cooIndex) {
					coo = RatedCoordinate.addSameCoordinates(coo, cooi);
//					toBeRemoved.add(cooi);
				}
			}
			
			combinedCoos.add(coo);
			
			// removing already summed coordinates
			// TODO - problematic thought - it might interfere with forloop
		}
		
		return combinedCoos;
	}

}
