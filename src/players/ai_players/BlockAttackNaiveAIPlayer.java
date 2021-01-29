package players.ai_players;

import java.util.Collections;
import java.util.List;

import game_components.Grid;
import game_components.Move;
import game_components.Square.SVal;
import grid_computations.Computations;
import grid_computations.Coordinate;
import grid_computations.PotStreakFilledLengthComparator;
import grid_computations.PotentialStreak;
import players.Player;

public class BlockAttackNaiveAIPlayer extends AbstractAIPlayer {

	/**
	 * 
	 * @param playersSVal
	 * @param name
	 * @param streakLength winning streak length
	 */
	public BlockAttackNaiveAIPlayer(SVal playersSVal, String name, int streakLength) {
		super(playersSVal, name, streakLength);
	}

	@Override
	public Move nextMove(Grid g) {
		List<PotentialStreak> opponentsPotStreaks = Computations.getAllPotentialStreaks(g, SVal.getOpposite(this.getSVal()), streakLength);
		List<PotentialStreak> potStreaks = Computations.getAllPotentialStreaks(g, this.getSVal(), streakLength);
		
		Coordinate cooForDefending = defend(opponentsPotStreaks, streakLength - 2);
		if(cooForDefending != null) {
			return new Move(cooForDefending, this.getSVal());
		} else {
			return new Move(attack(potStreaks, g), this.getSVal());
		}
	}
	
	static Coordinate defend(List<PotentialStreak> opponentsPotStreaks, int dangerousStreakLength) {
		if(opponentsPotStreaks == null || opponentsPotStreaks.isEmpty()) {
			return null;
		}
		
		Collections.sort(opponentsPotStreaks, new PotStreakFilledLengthComparator());
		
		PotentialStreak fullestStreak = opponentsPotStreaks.get(opponentsPotStreaks.size() - 1);
		if(fullestStreak.filledCoosSize() >= dangerousStreakLength){
			return fullestStreak.potentialCoos()[0];
		} else {
			return null;
		}
	}

	static Coordinate attack(List<PotentialStreak> potStreaks, Grid g) {
		if(potStreaks == null || potStreaks.isEmpty()) {
			int row = g.size() / 2;
			int column = row;
			if(g.isSquareEmpty(row, column)) {
				return new Coordinate(row , column);
			} else if (g.size() > 2 && g.isSquareEmpty(row - 1, column - 1)) {
				return new Coordinate(row - 1, column - 1);
			} else {
				return firtEmptySquare(g);
			}
		}
		
		Collections.sort(potStreaks, new PotStreakFilledLengthComparator());
		
		PotentialStreak fullestStreak = potStreaks.get(potStreaks.size() - 1);
		return fullestStreak.potentialCoos()[0];
	}
	
	public static Coordinate firtEmptySquare(Grid g) {
		for (int i = 0; i < g.size(); i++) {
			for (int j = 0; j < g.size(); j++) {
				if(g.isSquareEmpty(i, j)) {
					return new Coordinate(i, j);
				}
			}
		}
		return null;
	}
	
	
}
