package players.ai_players;

import java.util.Collections;
import java.util.List;

import game_components.Grid;
import game_components.ValuedMove;
import game_components.Square.SVal;
import grid_computations.Coordinate;
import players.ai_players.heuristics.AbstractSquareHeuristic;
import players.ai_players.support_classes.AbstractCooValFromStreakEstimator;
import players.ai_players.support_classes.LengthCooValEstimator;
import players.ai_players.support_classes.PoweredLengthCooValEstimator;
import players.ai_players.support_classes.RatedCoordinate;
import players.ai_players.support_classes.RatedCoordinatesValueComparator;

public class OneStepAIPlayer extends AbstractAIPlayer {

	public OneStepAIPlayer(SVal playersSVal, String name, int streakLength, AbstractSquareHeuristic squareHeuristic) {
		super(playersSVal, name, streakLength, squareHeuristic);
	}

	@Override
	public ValuedMove nextMove(Grid g) {
		List<RatedCoordinate> suggestedCoos = this.squareHeuristic.getRatedCoos(playersSVal, g, streakLength);
//		if(suggestedCoos != null) { // debug
//			Collections.sort(suggestedCoos, new RatedCoordinatesValueComparator()); // debug
//			Collections.reverse(suggestedCoos); // debug
//			System.out.println(suggestedCoos);// debug
//		} // debug
		Coordinate bestCoo = Collections.max(suggestedCoos, new RatedCoordinatesValueComparator());
		return new ValuedMove(bestCoo, this.getSVal());
	}

}
