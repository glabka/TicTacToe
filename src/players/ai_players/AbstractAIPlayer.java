package players.ai_players;

import game_components.Square.SVal;
import players.Player;
import players.ai_players.heuristics.AbstractHeuristic;

public abstract class AbstractAIPlayer extends Player{

	protected int streakLength;
	protected AbstractHeuristic heuristic;

	/**
	 * 
	 * @param playersSVal
	 * @param name
	 * @param streakLength winning streak length
	 */
	public AbstractAIPlayer(SVal playersSVal, String name, int streakLength) {
		super(playersSVal, name);
		this.streakLength = streakLength;
		this.heuristic = null;
	}
	
	/**
	 * 
	 * @param playersSVal
	 * @param name
	 * @param streakLength winning streak length
	 */
	public AbstractAIPlayer(SVal playersSVal, String name, int streakLength, AbstractHeuristic heuristic) {
		super(playersSVal, name);
		this.streakLength = streakLength;
		this.heuristic = heuristic;
	}
	

}
