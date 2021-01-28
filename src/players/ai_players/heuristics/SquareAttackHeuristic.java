package players.ai_players.heuristics;

import java.util.LinkedList;
import java.util.List;

import game_components.Grid;
import game_components.Move;
import game_components.Square.SVal;
import grid_computations.Computations;
import grid_computations.Coordinate;
import grid_computations.PotentialStreak;
import players.ai_players.DumbAIPlayer;
import players.ai_players.support_classes.AbstractCooValFromStreakEstimator;
import players.ai_players.support_classes.RatedCoordinate;

public class SquareAttackHeuristic extends AbstractSquareHeuristic {

	@Override
	public List<RatedCoordinate> getRatedCoos(SVal playersSVal, Grid g, int streakLength,
			AbstractCooValFromStreakEstimator cooEstimator) {
		
		List<PotentialStreak> potStreaks = Computations.getAllPotentialStreaks(g, playersSVal, streakLength);
		
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
				ratedCoos.add(new RatedCoordinate(Common.firtEmptySquare(g), val));
				return ratedCoos;
			}
		}
		
		return Common.getAllRatedCoosFromPotOfPotStreaks(potStreaks, cooEstimator);
	}
}
