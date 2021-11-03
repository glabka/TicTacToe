package strategies.support_classes;

import grid_computations.Coordinate;
import grid_computations.PotentialStreak;

public class OneValueEstimator extends AbstractCooValFromStreakEstimator{

	private double value;
	
	public OneValueEstimator(double value) {
		this.value = value;
	}
	
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (!(o instanceof OneValueEstimator)) {
			return false;
		}

		OneValueEstimator other = (OneValueEstimator) o;

		// checking all fields
		if (Math.abs(this.value - other.value) >= Double.MIN_VALUE) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return Double.hashCode(value);
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
