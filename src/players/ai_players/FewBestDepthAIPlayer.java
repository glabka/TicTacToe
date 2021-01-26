package players.ai_players;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import game_components.Grid;
import game_components.Move;
import game_components.Square.SVal;
import players.ai_players.heuristics.AbstractHeuristic;
import players.ai_players.heuristics.Common;
import players.ai_players.support_classes.AbstractCooValFromStreakEstimator;
import players.ai_players.support_classes.PoweredLengthCooValEstimator;
import players.ai_players.support_classes.RatedCoordinate;
import players.ai_players.support_classes.RatedCoordinatesValueComparator;

public class FewBestDepthAIPlayer extends AbstractDepthAIPlayer {

	private int howMuch;
	
	/**
	 * 
	 * @param playersSVal
	 * @param name
	 * @param streakLength
	 * @param heuristic
	 * @param depth
	 * @param howMuch how much options of best options it should search 
	 */
	public FewBestDepthAIPlayer(SVal playersSVal, String name, int streakLength, AbstractHeuristic heuristic, int depth, int howMuch) {
		super(playersSVal, name, streakLength, heuristic, depth);
		this.howMuch = howMuch;
	}
	
	/**
	 * Method return sublist of size FewBestDepthAIPlayer.howMuch of sorted (from best to worse) RatedCoordinates 
	 * @param ratedCoos
	 * @return
	 */
	@Override
	public List<RatedCoordinate> filterRatedCoosForSearch(List<RatedCoordinate> ratedCoos) {
		Collections.sort(ratedCoos, new RatedCoordinatesValueComparator());
		Collections.reverse(ratedCoos);
		
		if(howMuch > ratedCoos.size()) {
			howMuch = ratedCoos.size();
		}
		
		return ratedCoos.subList(0, howMuch);
	}

	protected List<RatedCoordinate> filterRatedCoosForGridEvaluation(List<RatedCoordinate> ratedCoos) {
		return filterRatedCoosForSearch(ratedCoos);
	}

	@Override
	protected double getGridsHeuristicValue(Grid g) {
		AbstractCooValFromStreakEstimator cooEstimator = new PoweredLengthCooValEstimator(2);
		
		List<RatedCoordinate> potentialCoos = this.heuristic.getRatedCoos(this.playersSVal, g, this.streakLength, cooEstimator);
		List<RatedCoordinate> filtredPotCoos = filterRatedCoosForGridEvaluation(potentialCoos);
		
		List<RatedCoordinate> opponentsCoos = this.heuristic.getRatedCoos(SVal.getOpposite(playersSVal), g, this.streakLength, cooEstimator);
		List<RatedCoordinate> filtredOpponentsCoos = filterRatedCoosForGridEvaluation(opponentsCoos);
		
		return Common.addAllRatedCoos(filtredPotCoos) - Common.addAllRatedCoos(filtredOpponentsCoos);
	}

}
