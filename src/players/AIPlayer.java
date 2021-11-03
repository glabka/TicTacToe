package players;

import game_components.Grid;
import game_components.Square.SVal;
import strategies.AbstractStrategy;
import game_components.ValuedMove;

public class AIPlayer extends Player{
	
	private SVal sVal;
	private int streakLength;
	private AbstractStrategy strategy;
	
	public AIPlayer(SVal sVal, String name, int streakLength, AbstractStrategy strategy) {
		super(sVal, name);
		this.sVal = sVal;
		this.streakLength = streakLength;
		this.strategy = strategy;
	}

	public int getStreakLength() {
		return streakLength;
	}

	@Override
	public ValuedMove nextMove(Grid g) {
		return strategy.nextMove(sVal, g, streakLength);
	}
}
