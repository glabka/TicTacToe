package strategies.support_classes;

import grid_computations.Coordinate;
import grid_computations.PotentialStreak;

public class PoweredLengthCooValEstimator extends AbstractCooValFromStreakEstimator {

	private double power;
	
	public PoweredLengthCooValEstimator(double power) {
		this.power = power;
	}
	
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (!(o instanceof PoweredLengthCooValEstimator)) {
			return false;
		}

		PoweredLengthCooValEstimator other = (PoweredLengthCooValEstimator) o;

		// checking all fields
		if (Math.abs(this.power - other.power) >= Double.MIN_VALUE) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return (Double.hashCode(power) + "1").hashCode();
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
