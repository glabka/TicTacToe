package strategies.heuristics;

import java.util.LinkedList;
import java.util.List;

import game_components.Grid;
import game_components.ValuedMove;
import game_components.Square.SVal;
import grid_computations.Computations;
import grid_computations.Coordinate;
import grid_computations.PotentialStreak;
import srategies.NaiveBlockAttackStrategy;
import strategies.support_classes.AbstractCooValFromStreakEstimator;
import strategies.support_classes.RatedCoordinate;

public class SquareNaiveAttackHeuristic extends AbstractSquareHeuristic {

	public SquareNaiveAttackHeuristic(AbstractCooValFromStreakEstimator cooEstimator) {
		super(cooEstimator);
	}

	@Override
	public List<RatedCoordinate> getRatedCoos(SVal playersSVal, Grid g, int streakLength) {
		
		int minNumOfFilledCoos = 1;
		List<PotentialStreak> potStreaks = Computations.getAllPotentialStreaks(g, playersSVal, streakLength, minNumOfFilledCoos);
		
		return attack(potStreaks, g, cooEstimator);
	}
	
	static List<RatedCoordinate> attack(List<PotentialStreak> potStreaks, Grid g, AbstractCooValFromStreakEstimator cooEstimator) {
		List<RatedCoordinate> ratedCoos = new LinkedList<>();
		if(potStreaks == null || potStreaks.isEmpty()) {
			int row = g.size() / 2;
			int column = row;
			double val = Double.POSITIVE_INFINITY;
			if(g.isSquareEmpty(row, column)) {
				ratedCoos.add(new RatedCoordinate(row , column, val));
				return ratedCoos;
			} else if (g.size() > 2 && g.isSquareEmpty(row - 1, column - 1)) {
				ratedCoos.add(new RatedCoordinate(row - 1, column - 1, val));
				return ratedCoos;
			} else {
				ratedCoos.add(new RatedCoordinate(HeuristicCommon.firtEmptySquare(g), val));
				return ratedCoos;
			}
		}
		
		return HeuristicCommon.getAllRatedCoosFromPotOfPotStreaks(potStreaks, cooEstimator);
	}

	@Override
	public Object clone() {
		return new SquareNaiveAttackHeuristic((AbstractCooValFromStreakEstimator) cooEstimator.clone());
	}
}
