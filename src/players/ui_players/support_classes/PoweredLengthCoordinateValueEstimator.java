package players.ui_players.support_classes;

import grid_computations.Coordinate;
import grid_computations.PotentialStreak;

public class PoweredLengthCoordinateValueEstimator extends AbstractCoordinateValueFromStreakEstimator {

	private int power;
	
	public PoweredLengthCoordinateValueEstimator(int power) {
		this.power = power;
	}
	
	@Override
	public double estimateValue(Coordinate coo, PotentialStreak potStreak) {
		return Math.pow(potStreak.filledCoosSize(), power);
	}

}
