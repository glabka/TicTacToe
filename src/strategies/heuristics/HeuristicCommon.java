package players.ai_players.heuristics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import game_components.Grid;
import game_components.Square.SVal;
import game_mechanics.Rules;
import grid_computations.Computations;
import grid_computations.Coordinate;
import grid_computations.PotentialStreak;
import grid_computations.ValuedCoordinate;
import players.ai_players.NaiveBlockAttackAIPlayer;
import players.ai_players.support_classes.AbstractCooValFromStreakEstimator;
import players.ai_players.support_classes.AbstractRatedCoosFilter;
import players.ai_players.support_classes.OneValueEstimator;
import players.ai_players.support_classes.RatedCoordinate;
import players.ai_players.support_classes.RatedCoordinatesValueComparator;

public class HeuristicCommon {

	
	/**
	 * Method doesn't combine RatedCoordinates of same coordinates - they can occur with different values
	 * @param potStreaks
	 * @param estimator
	 * @return
	 */
	public static List<RatedCoordinate> getAllRatedCoosFromPotOfPotStreaks(List<PotentialStreak> potStreaks, AbstractCooValFromStreakEstimator estimator) {
		if(potStreaks == null || potStreaks.isEmpty()) {
			return null;
		}
		
		Stream<RatedCoordinate> allRatedCoos = getRatedCooFromPotOfPotStreak(potStreaks.get(0), estimator).stream();
		
		if(potStreaks.size() == 1) {
			return allRatedCoos.collect(Collectors.toList());
		}
		
		for (int i = 1; i < potStreaks.size(); i++) {
			PotentialStreak streak = potStreaks.get(i);
			allRatedCoos = Stream.concat(allRatedCoos, getRatedCooFromPotOfPotStreak(streak, estimator).stream());
		}
		
		return allRatedCoos.collect(Collectors.toList());
	}
	
	public static List<RatedCoordinate> getRatedCooFromPotOfPotStreak(PotentialStreak potStreak, AbstractCooValFromStreakEstimator estimator) {
		List<RatedCoordinate> ratedCoordinates = new LinkedList<>();
		
		for(ValuedCoordinate coo : potStreak.potentialCoos()) {
			double value = estimator.estimateValue(coo, potStreak);
			ratedCoordinates.add(new RatedCoordinate(coo, value));
		}
		
		return ratedCoordinates;
	}
	
	public static Coordinate firtEmptySquare(Grid g) {
		for (int i = 0; i < g.size(); i++) {
			for (int j = 0; j < g.size(); j++) {
				if(g.isSquareEmpty(i, j)) {
					return new Coordinate(i, j);
				}
			}
		}
		return null;
	}
	
	public static int addAllRatedCoos(List<RatedCoordinate> ratedCoos) {
		if(ratedCoos == null || ratedCoos.isEmpty()) {
			return 0;
		}
		
		int val = 0;
		
		for(RatedCoordinate coo : ratedCoos) {
			val += coo.getValue();
		}
		
		return val;
	}
	
	public static Double getInfinityIfWinner(Grid g, SVal currentPlayer, int streakLength) {
		SVal winner = Rules.findWinner(g, streakLength);
		if (winner == currentPlayer) {
			return Double.POSITIVE_INFINITY;
		} else if (winner == SVal.getOpposite(currentPlayer)) {
			return Double.NEGATIVE_INFINITY;
		} else if (Rules.endOfGame(g, streakLength)){
			// tie
			return Double.valueOf(0);
		} else {
			return null;
		}
	}
	
	/**
	 * Combines equal coordinates.
	 * It assumes that in either list there is only one RatedCoordinate of certain row and column.
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static List<RatedCoordinate> combineTwoEqualRatedCoosFromTwoLists(List<RatedCoordinate> list1, List<RatedCoordinate> list2) {
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
	public static List<RatedCoordinate> combineAllEqualRatedCoos(List<RatedCoordinate> ratedCoos) {
		if(ratedCoos == null) {
			return null;
		}
		
		List<RatedCoordinate> combinedCoos = new LinkedList<>();
		
		for(RatedCoordinate coo : ratedCoos) {
			// unnecessary when removing of combined coordinates is implemented
			if(combinedCoos.contains(coo))
				continue;

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
	
	public static List<RatedCoordinate> getRatedCoordinatesFromList(List<? extends Coordinate> whatCoos, List<RatedCoordinate> source){
		if(whatCoos == null || whatCoos.isEmpty() || source == null || source.isEmpty()) {
			return null;
		}
		
		List<RatedCoordinate> result = new LinkedList<>();
		for(Coordinate whatCoo: whatCoos) {
			for(RatedCoordinate rCoo : source) {
				if (whatCoo.equals(rCoo)) {
					result.add(rCoo);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Method that rates coordinates of potStreaksOfInterest according to filtered allStreaks coordinates
	 * @param allStreaks
	 * @param potStreaksOfInterest
	 * @param cooEstimator
	 * @param filter
	 * @return
	 */
	public static List<RatedCoordinate> getBestCoosFromStreaksRatedAccordingToAllStreaksFiltered(List<PotentialStreak> allStreaks, List<PotentialStreak> potStreaksOfInterest, AbstractCooValFromStreakEstimator cooEstimator, AbstractRatedCoosFilter filter){
		List<RatedCoordinate> coosOfInterest = HeuristicCommon.combineAllEqualRatedCoos(HeuristicCommon.getAllRatedCoosFromPotOfPotStreaks(potStreaksOfInterest, cooEstimator));
	
		List<RatedCoordinate> filtredRatedCoosFromEverything = filter.filterRatedCoos(HeuristicCommon.combineAllEqualRatedCoos(HeuristicCommon.getAllRatedCoosFromPotOfPotStreaks(allStreaks, cooEstimator)));
		
		return HeuristicCommon.getRatedCoordinatesFromList(coosOfInterest, filtredRatedCoosFromEverything);
	}
	
	/**
	 * Method that rates coordinates of potStreaksOfInterest according to allStreaks coordinates
	 * @param allStreaks
	 * @param potStreaksOfInterest
	 * @param cooEstimator
	 * @return
	 */
	public static List<RatedCoordinate> getBestCoosFromStreaksRatedAccordingToAllStreaks(List<PotentialStreak> allStreaks, List<PotentialStreak> potStreaksOfInterest, AbstractCooValFromStreakEstimator cooEstimator){
		List<RatedCoordinate> coosOfInterest = HeuristicCommon.combineAllEqualRatedCoos(HeuristicCommon.getAllRatedCoosFromPotOfPotStreaks(potStreaksOfInterest, cooEstimator));
	
		List<RatedCoordinate> ratedCoosFromEverything = HeuristicCommon.combineAllEqualRatedCoos(HeuristicCommon.getAllRatedCoosFromPotOfPotStreaks(allStreaks, cooEstimator));
		
		return HeuristicCommon.getRatedCoordinatesFromList(coosOfInterest, ratedCoosFromEverything);
//		return combineAllEqualRatedCoos(HeuristicCommon.getRatedCoordinatesFromList(coosOfInterest, ratedCoosFromEverything));
	}
	
	/**
	 * Return all most filled streaks
	 * @param allPotStreaks
	 * @return
	 */
	public static List<PotentialStreak> getAllMostFilledStreaks(List<PotentialStreak> allPotStreaks) {
		if(allPotStreaks.isEmpty()) {
			return null;
		}
		
		allPotStreaks = new LinkedList<PotentialStreak>(allPotStreaks);
		Comparator<PotentialStreak> mostFullStreakComparator = (PotentialStreak s1, PotentialStreak s2) -> Integer.compare(s1.filledCoosSize(), s2.filledCoosSize());
		List<PotentialStreak> allStreaksForAttacking = new LinkedList<>();
		
		PotentialStreak streakForAttacking = allPotStreaks.stream().max(mostFullStreakComparator).orElse(null);
		allStreaksForAttacking.add(streakForAttacking);
		allPotStreaks.remove(streakForAttacking);
		
		PotentialStreak otherStreaksForAttacking = null;
		while ((otherStreaksForAttacking = allPotStreaks.stream().max(mostFullStreakComparator).orElse(null)) != null &&
				streakForAttacking.filledCoosSize() == otherStreaksForAttacking.filledCoosSize()) {
			allStreaksForAttacking.add(otherStreaksForAttacking);
			allPotStreaks.remove(otherStreaksForAttacking);
		}
		 
		return allStreaksForAttacking;
	}
	
	public static List<RatedCoordinate> getMiddleOrFirstEmptyCoo(Grid g) {
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
			Coordinate coo = NaiveBlockAttackAIPlayer.firtEmptySquare(g);
			if(coo != null) {
				list.add(new RatedCoordinate(coo, value));
			}
			return list;
		}
	}
	
	public static List<RatedCoordinate> getRatedCoosBasedOnNumOfPotStreaksTheyAreIn(Grid g,  SVal currentPlayer, int streakLength) {
		OneValueEstimator estimator = new OneValueEstimator(1);
		int minNumOfFilledCoos = 0;
		
		List<PotentialStreak> potStreaks = Computations.getAllPotentialStreaks(g, currentPlayer, streakLength, minNumOfFilledCoos);
		List<RatedCoordinate> allRatedCoos = HeuristicCommon.getAllRatedCoosFromPotOfPotStreaks(potStreaks, estimator);
		List<RatedCoordinate> combinedCoos = HeuristicCommon.combineAllEqualRatedCoos(allRatedCoos);
	
		if(combinedCoos == null || combinedCoos.isEmpty()) {
			List<RatedCoordinate> list = new ArrayList<>();
			int value = 5; // kind of random positive value
			Coordinate coo = NaiveBlockAttackAIPlayer.firtEmptySquare(g);
			
			if(coo != null) {
				list.add(new RatedCoordinate(coo, value));
			}
			
			return list;
		}
		Collections.sort(combinedCoos, new RatedCoordinatesValueComparator());
		Collections.reverse(combinedCoos);
		
		return combinedCoos;
	}
}
