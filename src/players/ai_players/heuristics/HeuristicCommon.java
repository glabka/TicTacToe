package players.ai_players.heuristics;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import game_components.Grid;
import grid_computations.Coordinate;
import grid_computations.PotentialStreak;
import grid_computations.ValuedCoordinate;
import players.ai_players.support_classes.AbstractCooValFromStreakEstimator;
import players.ai_players.support_classes.RatedCoordinate;

public class HeuristicCommon {

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
		int val = 0;
		
		for(RatedCoordinate coo : ratedCoos) {
			val += coo.getValue();
		}
		
		return val;
	}
}
