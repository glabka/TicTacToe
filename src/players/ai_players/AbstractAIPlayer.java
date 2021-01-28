package players.ai_players;

import game_components.Square.SVal;
import players.Player;
import players.ai_players.heuristics.AbstractGridHeuristic;
import players.ai_players.heuristics.AbstractSquareHeuristic;
import players.ai_players.support_classes.AbstractRatedCoosFilter;

public abstract class AbstractAIPlayer extends Player{

	protected int streakLength;
	protected AbstractSquareHeuristic squareHeuristic;
	protected AbstractGridHeuristic gridHeurisric;
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
		this.gridHeurisric = gridHeuristic;
		this.ratedCoosFilter = ratedCoosFilter;
	}
	

}
