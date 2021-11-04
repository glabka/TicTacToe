package players;

import game_components.Grid;
import game_components.Square.SVal;
import strategies.AbstractStrategy;
import game_components.ValuedMove;

public class AIPlayer extends Player{
	
	protected int streakLength;
	protected AbstractStrategy strategy;
	
	public AIPlayer(SVal sVal, String name, int streakLength, AbstractStrategy strategy) {
		super(sVal, name);
		this.streakLength = streakLength;
		this.strategy = strategy;
	}
	
	public void setStreakLength(int streakLength) {
		this.streakLength = streakLength;
	}

	public int getStreakLength() {
		return streakLength;
	}

	@Override
	public ValuedMove nextMove(Grid g) {
		return strategy.nextMove(super.getSVal(), g, streakLength);
	}
}
