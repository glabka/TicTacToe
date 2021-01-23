package players.ai_players;

import game_components.Square.SVal;
import players.Player;

public abstract class AbstractAIPlayer extends Player{

	protected int streakLength;

	/**
	 * 
	 * @param playersSVal
	 * @param name
	 * @param streakLength winning streak length
	 */
	public AbstractAIPlayer(SVal playersSVal, String name, int streakLength) {
		super(playersSVal, name);
		this.streakLength = streakLength;
	}

}
