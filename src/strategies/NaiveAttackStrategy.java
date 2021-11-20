package strategies;

import java.util.List;

import game_components.Grid;
import game_components.Square.SVal;
import game_components.ValuedMove;
import grid_computations.Computations;
import grid_computations.PotentialStreak;

public class NaiveAttackStrategy extends AbstractStrategy {
	
	@Override
	public ValuedMove nextMove(SVal sVal, Grid g, int streakLength) {
		int minNumOfFilledCoos = 1;
		List<PotentialStreak> potStreaks = Computations.getAllPotentialStreaks(g, sVal, streakLength, minNumOfFilledCoos);
		
		return new ValuedMove(NaiveBlockAttackStrategy.attack(potStreaks, g), sVal);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (!(o instanceof NaiveAttackStrategy)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "()";
	}
}
