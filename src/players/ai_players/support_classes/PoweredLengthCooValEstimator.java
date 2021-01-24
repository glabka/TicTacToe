package players.ai_players.support_classes;

import grid_computations.Coordinate;
import grid_computations.PotentialStreak;

public class PoweredLengthCooValEstimator extends AbstractCooValFromStreakEstimator {

	private int power;
	
	public PoweredLengthCooValEstimator(int power) {
		this.power = power;
	}
	
	@Override
	public double estimateValue(Coordinate coo, PotentialStreak potStreak) {
		return Math.pow(potStreak.filledCoosSize(), power);
	}

}
