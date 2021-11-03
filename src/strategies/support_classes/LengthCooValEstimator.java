package strategies.support_classes;

import grid_computations.Coordinate;
import grid_computations.PotentialStreak;

public class LengthCooValEstimator extends AbstractCooValFromStreakEstimator {

	@Override
	public double estimateValue(Coordinate coo, PotentialStreak potStreak) {
		return potStreak.filledCoosSize();
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (!(o instanceof LengthCooValEstimator)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return 10;
	}

	@Override
	public Object clone() {
		return new LengthCooValEstimator();
	}

}
