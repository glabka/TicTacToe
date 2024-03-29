package strategies.support_classes;

import grid_computations.Coordinate;
import grid_computations.PotentialStreak;

/**
 * Estimates value of coordinate according to PotentialStreak it is contained in
 */
public abstract class AbstractCooValFromStreakEstimator implements Cloneable {

	
	public abstract double estimateValue(Coordinate coo, PotentialStreak potStreak);
	
	public abstract Object clone();

	public abstract boolean equals(Object o);

	public abstract int hashCode();

	public abstract String toString();
}
