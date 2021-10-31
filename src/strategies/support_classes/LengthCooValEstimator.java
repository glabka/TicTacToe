package strategies.support_classes;

import grid_computations.Coordinate;
import grid_computations.PotentialStreak;

public class LengthCooValEstimator extends AbstractCooValFromStreakEstimator {

	@Override
	public double estimateValue(Coordinate coo, PotentialStreak potStreak) {
		return potStreak.filledCoosSize();
	}

	@Override
	public Object clone() {
		return new LengthCooValEstimator();
	}

}
