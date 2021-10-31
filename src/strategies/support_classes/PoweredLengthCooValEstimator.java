package strategies.support_classes;

import grid_computations.Coordinate;
import grid_computations.PotentialStreak;

public class PoweredLengthCooValEstimator extends AbstractCooValFromStreakEstimator {

	private double power;
	
	public PoweredLengthCooValEstimator(double power) {
		this.power = power;
	}
	
	@Override
	public double estimateValue(Coordinate coo, PotentialStreak potStreak) {
		return Math.pow(potStreak.filledCoosSize(), power);
	}

	@Override
	public Object clone() {
		return new PoweredLengthCooValEstimator(power);
	}

}
