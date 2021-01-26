package players.ai_players.heuristics;

import java.util.List;

import game_components.Grid;
import game_components.Square.SVal;
import players.ai_players.support_classes.AbstractCooValFromStreakEstimator;
import players.ai_players.support_classes.RatedCoordinate;

public abstract class AbstractHeuristic {

	public abstract List<RatedCoordinate> getRatedCoos(SVal playersSVal, Grid g, int streakLength, AbstractCooValFromStreakEstimator cooEstimator);
	
}
