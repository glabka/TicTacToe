package players.ai_players.heuristics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import game_components.Grid;
import game_components.Square.SVal;
import grid_computations.Computations;
import grid_computations.PotentialStreak;
import players.ai_players.support_classes.AbstractCooValFromStreakEstimator;
import players.ai_players.support_classes.AbstractRatedCoosFilter;
import players.ai_players.support_classes.RatedCoordinate;
import players.ai_players.support_classes.RatedCoordinatesValueComparator;

public class SquareMixedHeuristic extends AbstractSquareHeuristic {

	private AbstractRatedCoosFilter filter;
	
	public SquareMixedHeuristic(AbstractCooValFromStreakEstimator cooEstimator) {
		super(cooEstimator);
	}
	
	public SquareMixedHeuristic(AbstractCooValFromStreakEstimator cooEstimator, AbstractRatedCoosFilter filter) {
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
			List<RatedCoordinate> pureAttackCoos = HeuristicCommon.getBestCoosFromStreaksRatedAccordingToAllStreaks(potStreaks, streaksForAttack, cooEstimator); 
			
			List<RatedCoordinate> mergeCoos = new SquareMergedHeuristic(cooEstimator).getRatedCoos(playersSVal, g, streakLength);
			List<RatedCoordinate> mixedList = mixListsAndAdjustCoos(pureAttackCoos, mergeCoos);
			ratedCoos = filter == null ? mixedList : filter.filterRatedCoos(mixedList);
		}
		
		if(ratedCoos == null || ratedCoos.isEmpty()) {
//			System.out.println("ratedCoos == null || ratedCoos.isEmpty()"); // debug
			return HeuristicCommon.getRatedCoosBasedOnNumOfPotStreaksTheyAreIn(g, playersSVal, streakLength);
		} else {		
			return ratedCoos;
		}
	}
	
	private static List<RatedCoordinate> mixListsAndAdjustCoos(List<RatedCoordinate> list1, List<RatedCoordinate> list2) {
		if(list1 == null || list1.isEmpty()) {
			return list2;
		} else if (list2 == null || list2.isEmpty()) {
			return null;
		}		
		
		RatedCoordinatesValueComparator c = new RatedCoordinatesValueComparator();
		Collections.sort(list1, c);
		Collections.reverse(list1);
		System.out.println("list1 = " + list1); // debug
		
		Collections.sort(list2, c);
		Collections.reverse(list2);
		System.out.println("list2 = " + list2); // debug
		
		List<RatedCoordinate> mixedList = mixLists(list1, list2);
		System.out.println("mixedList = " + mixedList); // debug
		
		if(mixedList.size() == 2) {
			if(!mixedList.get(0).equals(mixedList.get(1))) {
				// adjusting values when size is only 2
				mixedList.get(1).setValue(mixedList.get(0).getValue() / 2);
				return mixedList;
			} else {
				return mixedList.subList(0, 0);
			}
		}
		
		// can have duplicate coordinates
		List<RatedCoordinate> adjustedMixedList = adjustCoordinatesRatingAccordingToOddElements(mixedList);
		System.out.println("adjustedMixedList = " + adjustedMixedList); // debug
		
		// getting rid of duplicate coordinates
		List<RatedCoordinate> uniqueAdjustedMixedList = adjustedMixedList.stream().distinct().collect(Collectors.toList());
		System.out.println("uniqueAdjustedMixedList = " + uniqueAdjustedMixedList); // debug
		
		return uniqueAdjustedMixedList;
		
	}
	
	private static List<RatedCoordinate> adjustCoordinatesRatingAccordingToOddElements(List<RatedCoordinate> list) {
		double adjustedVal;
		for(int i = 0; i < list.size() - 2; i = i + 2) {
			adjustedVal = (list.get(i).getValue() + list.get(i + 2).getValue()) / 2;
			list.get(i + 1).setValue(adjustedVal);
		}
		
		// last coordinates value wasn't adjusted - is adjusted now to half of second to last value so it will be less or equal
		list.get(list.size() - 1).setValue(list.get(list.size() - 2).getValue() / 2);
		
		return list;
	}
	
	private static List<RatedCoordinate> mixLists(List<RatedCoordinate> list1, List<RatedCoordinate> list2) {
		List<RatedCoordinate> subList1;
		List<RatedCoordinate> subList2;
		int size;
		if(list1.size() < list2.size()) {
			size = list1.size();
		} else if (list2.size() < list1.size()) {
			size = list2.size();
		} else {
			size = list1.size();
		}
		
		subList1 = list1.subList(0, size);
		subList2 = list2.subList(0, size);
		
		
		List<RatedCoordinate> mixedList = new ArrayList<>(2 * subList1.size());
		for(int i = 0; i < subList1.size(); i++) {
			mixedList.add(subList1.get(i));
			mixedList.add(subList2.get(i));
		}
		
		return mixedList;
	}
	
	
//	public static void test() {
//		List<RatedCoordinate> list1 = new LinkedList<>();
//		list1.add(new RatedCoordinate(0, 1, 1));
////		list1.add(new RatedCoordinate(0, 2, 2));
////		list1.add(new RatedCoordinate(0, 3, 3));
////		list1.add(new RatedCoordinate(0, 4, 4));
////		
//		List<RatedCoordinate> list2 = new LinkedList<>();
//		list2.add(new RatedCoordinate(1, 1, 5));
////		list2.add(new RatedCoordinate(1, 2, 10));
////		list2.add(new RatedCoordinate(1, 3, 15));
////		list2.add(new RatedCoordinate(1, 4, 20));
////		
//		List<RatedCoordinate> mixedList = mixListsAndAdjustCoos(list1, list2);
//		System.out.println("adjustedMixedList = " + mixedList);
//	}
	
}
