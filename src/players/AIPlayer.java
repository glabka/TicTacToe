package players.ai_players;

import game_components.Square.SVal;
import players.Player;
import players.ai_players.heuristics.AbstractGridHeuristic;
import players.ai_players.heuristics.AbstractSquareHeuristic;
import players.ai_players.support_classes.AbstractRatedCoosFilter;

public abstract class AbstractAIPlayer extends Player{

	protected int streakLength;
	protected AbstractSquareHeuristic squareHeuristic;
	protected AbstractGridHeuristic gridHeuristic;
	protected AbstractRatedCoosFilter ratedCoosFilter;

	/**
	 * 
	 * @param playersSVal
	 * @param name
	 * @param streakLength winning streak length
	 */
	public AbstractAIPlayer(SVal playersSVal, String name, int streakLength) {
		super(playersSVal, name);
		this.streakLength = streakLength;
		this.squareHeuristic = null;
	}
	
	/**
	 * 
	 * @param playersSVal
	 * @param name
	 * @param streakLength winning streak length
	 */
	public AbstractAIPlayer(SVal playersSVal, String name, int streakLength, AbstractSquareHeuristic squareHeuristic) {
		super(playersSVal, name);
		this.streakLength = streakLength;
		this.squareHeuristic = squareHeuristic;
	}
	
	/**
	 * 
	 * @param playersSVal
	 * @param name
	 * @param streakLength winning streak length
	 */
	public AbstractAIPlayer(SVal playersSVal, String name, int streakLength, AbstractSquareHeuristic squareHeuristic, AbstractGridHeuristic gridHeuristic, AbstractRatedCoosFilter ratedCoosFilter) {
		super(playersSVal, name);
		this.streakLength = streakLength;
		this.squareHeuristic = squareHeuristic;
		this.gridHeuristic = gridHeuristic;
		this.ratedCoosFilter = ratedCoosFilter;
	}

	public int getStreakLength() {
		return streakLength;
	}

	public AbstractSquareHeuristic getSquareHeuristic() {
		if (squareHeuristic == null) {
			return null;
		} else {
			return (AbstractSquareHeuristic) squareHeuristic.clone();
		}
	}

	public AbstractGridHeuristic getGridHeuristic() {
		if (gridHeuristic == null) {
			return null;
		} else {
			return (AbstractGridHeuristic) gridHeuristic.clone();
		}
	}

	public AbstractRatedCoosFilter getRatedCoosFilter() {
		if (ratedCoosFilter == null) {
			return null;
		} else {
			return (AbstractRatedCoosFilter) ratedCoosFilter.clone();
		}
	}
}
