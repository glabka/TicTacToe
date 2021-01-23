package players.ai_players;

import java.util.List;

import game_components.Grid;
import game_components.Move;
import game_components.Square.SVal;
import grid_computations.Computations;
import grid_computations.Coordinate;
import grid_computations.PotentialStreak;
import players.Player;

public class DumbAIPlayer2 extends Player {

	private int streakLength;

	/**
	 * 
	 * @param playersSVal
	 * @param name
	 * @param streakLength winning streak length
	 */
	public DumbAIPlayer2(SVal playersSVal, String name, int streakLength) {
		super(playersSVal, name);
		this.streakLength = streakLength;
	}
	
	@Override
	public Move nextMove(Grid g) {
		List<PotentialStreak> potStreaks = Computations.getAllPotentialStreaks(g, this.getSVal(), streakLength);
		
		return new Move(DumbAIPlayer.attack(potStreaks, g), this.getSVal());
	}
	
}
