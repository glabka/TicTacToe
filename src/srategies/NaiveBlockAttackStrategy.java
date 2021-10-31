package srategies;

import java.util.Collections;
import java.util.List;

import game_components.Grid;
import game_components.Square.SVal;
import game_components.ValuedMove;
import grid_computations.Computations;
import grid_computations.Coordinate;
import grid_computations.PotStreakFilledLengthComparator;
import grid_computations.PotentialStreak;
import strategies.heuristics.HeuristicCommon;

public class NaiveBlockAttackStrategy extends AbstractStrategy {

	@Override
	public ValuedMove nextMove(SVal sVal, Grid g, int streakLength) {
		int minNumOfFilledCoos = 1;
		List<PotentialStreak> opponentsPotStreaks = Computations.getAllPotentialStreaks(g, SVal.getOpposite(sVal), streakLength, minNumOfFilledCoos);
		List<PotentialStreak> potStreaks = Computations.getAllPotentialStreaks(g, sVal, streakLength, minNumOfFilledCoos);
		
		Coordinate cooForDefending = defend(opponentsPotStreaks, streakLength - 2);
		if(cooForDefending != null) {
			return new ValuedMove(cooForDefending, sVal);
		} else {
			return new ValuedMove(attack(potStreaks, g), sVal);
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
				return HeuristicCommon.firtEmptySquare(g);
			}
		}
		
		Collections.sort(potStreaks, new PotStreakFilledLengthComparator());
		
		PotentialStreak fullestStreak = potStreaks.get(potStreaks.size() - 1);
		return fullestStreak.potentialCoos()[0];
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (!(o instanceof NaiveBlockAttackStrategy)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return 2;
	}
	
	
}
