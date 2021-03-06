package players.ai_players;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import game_components.Grid;
import game_components.ValuedMove;
import game_components.Square.SVal;
import grid_computations.Computations;
import grid_computations.Coordinate;
import grid_computations.PotStreakFilledLengthComparator;
import grid_computations.PotentialStreak;
import grid_computations.ValuedCoordinate;
import players.ai_players.heuristics.HeuristicCommon;
import players.ai_players.support_classes.AbstractCooValFromStreakEstimator;
import players.ai_players.support_classes.LengthCooValEstimator;
import players.ai_players.support_classes.PoweredLengthCooValEstimator;
import players.ai_players.support_classes.RatedCoordinate;
import players.ai_players.support_classes.RatedCoordinatesValueComparator;

public class MergeAIPlayer extends AbstractAIPlayer {

	public MergeAIPlayer(SVal playersSVal, String name, int streakLength) {
		super(playersSVal, name, streakLength);
	}

	@Override
	public ValuedMove nextMove(Grid g) {
		int minNumOfFilledCoos = 1;
		List<PotentialStreak> opponentsPotStreaks = Computations.getAllPotentialStreaks(g, SVal.getOpposite(this.getSVal()), streakLength, minNumOfFilledCoos);
		List<PotentialStreak> potStreaks = Computations.getAllPotentialStreaks(g, this.getSVal(), streakLength, minNumOfFilledCoos);
		AbstractCooValFromStreakEstimator estimator = new PoweredLengthCooValEstimator(2);
		
		List<RatedCoordinate> coosForDefending = defend(opponentsPotStreaks, estimator);
//		if(coosForDefending != null) {
//			Collections.sort(coosForDefending, new RatedCoordinatesValueComparator()); // debug
//			Collections.reverse(coosForDefending); // debug	
//		}
//		System.out.println(coosForDefending); // debug
		List<RatedCoordinate> coosForAttack = attack(g, potStreaks, estimator);
//		if(coosForAttack != null) {
//			Collections.sort(coosForAttack, new RatedCoordinatesValueComparator()); // debug
//			Collections.reverse(coosForAttack); // debug
//		}
//		System.out.println(coosForAttack); // debug
		
		List<RatedCoordinate> combinedCoos = combineTwoEqualRatedCoosFromTwoLists(coosForDefending, coosForAttack);
//		Collections.sort(combinedCoos, new RatedCoordinatesValueComparator()); // debug
//		Collections.reverse(combinedCoos); // debug
//		System.out.println(combinedCoos); // debug
		RatedCoordinate coo = Collections.max(combinedCoos, new RatedCoordinatesValueComparator());
		
		return new ValuedMove(coo, playersSVal);
	}
	
	private List<RatedCoordinate> attack(Grid g, List<PotentialStreak> potStreaks, AbstractCooValFromStreakEstimator estimator) {
		if(potStreaks == null || potStreaks.isEmpty()) {
			List<RatedCoordinate> list = new ArrayList<>();
			int row = g.size() / 2;
			int column = row;
			int value = streakLength - 1;
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
	
	public void test() {
//		List<RatedCoordinate> ratedCoos0 = new LinkedList<>();
//		List<RatedCoordinate> combined0 = this.combineAllEqualRatedCoos(ratedCoos0);
//		System.out.println(combined0);
//		
//		
//		List<RatedCoordinate> ratedCoos1 = new LinkedList<>();
//		ratedCoos1.add(new RatedCoordinate(0, 0, 1));
//		ratedCoos1.add(new RatedCoordinate(0, 0, 2));
//		ratedCoos1.add(new RatedCoordinate(0, 0, 3));
//		ratedCoos1.add(new RatedCoordinate(0, 1, 1));
//		ratedCoos1.add(new RatedCoordinate(0, 1, 1));
//		ratedCoos1.add(new RatedCoordinate(2, 2, 1.4));
//		ratedCoos1.add(new RatedCoordinate(2, 2, 1));
//		ratedCoos1.add(new RatedCoordinate(2, 2, 1.3));
//		
//		List<RatedCoordinate> combined1 = this.combineAllEqualRatedCoos(ratedCoos1);
//		System.out.println(combined1);
//		
//		List<RatedCoordinate> ratedCoos2 = new LinkedList<>();
//		ratedCoos2.add(new RatedCoordinate(0, 0, -1));
//		ratedCoos2.add(new RatedCoordinate(0, 0, 2));
//		ratedCoos2.add(new RatedCoordinate(0, 0, 3));
//		ratedCoos2.add(new RatedCoordinate(0, 1, 0.5));
//		ratedCoos2.add(new RatedCoordinate(0, 1, 1));
//		ratedCoos2.add(new RatedCoordinate(2, 2, 1.4));
//		ratedCoos2.add(new RatedCoordinate(2, 2, 1));
//		ratedCoos2.add(new RatedCoordinate(2, 2, 1.3));
//		
//		List<RatedCoordinate> combined2 = this.combineAllEqualRatedCoos(ratedCoos2);
//		System.out.println(combined2);
//		
//		List<RatedCoordinate> combinedLists = this.combineTwoEqualRatedCoosFromTwoLists(combined1, combined2);
//		System.out.println(combinedLists);
//		
//		Collections.sort(combinedLists, new RatedCoordinatesValueComparator());
//		System.out.println(combinedLists);
		
	}

}
