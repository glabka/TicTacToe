package players.ai_players;

import java.util.List;

import game_components.Grid;
import game_components.ValuedMove;
import game_components.Square.SVal;
import grid_computations.Computations;
import grid_computations.PotentialStreak;
import players.Player;

public class NaiveAttackAIPlayer extends AbstractAIPlayer {

	/**
	 * 
	 * @param playersSVal
	 * @param name
	 * @param streakLength winning streak length
	 */
	public NaiveAttackAIPlayer(SVal playersSVal, String name, int streakLength) {
		super(playersSVal, name, streakLength);
	}
	
	@Override
	public ValuedMove nextMove(Grid g) {
		int minNumOfFilledCoos = 1;
		List<PotentialStreak> potStreaks = Computations.getAllPotentialStreaks(g, this.getSVal(), streakLength, minNumOfFilledCoos);
		
		return new ValuedMove(NaiveBlockAttackAIPlayer.attack(potStreaks, g), this.getSVal());
	}
	
}
