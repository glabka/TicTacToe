package strategies.support_classes;

import grid_computations.Coordinate;
import grid_computations.PotentialStreak;

public class OneValueEstimator extends AbstractCooValFromStreakEstimator{

	private double value;
	
	public OneValueEstimator(double value) {
		this.value = value;
	}
	
	@Override
	public double estimateValue(Coordinate coo, PotentialStreak potStreak) {
		return value;
	}

	@Override
	public Object clone() {
		return new OneValueEstimator(value);
	}

}
