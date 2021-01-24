package players.ui_players.support_classes;

import grid_computations.Coordinate;
import grid_computations.PotentialStreak;

public class LengthCoordinateValueEstimator extends AbstractCoordinateValueFromStreakEstimator {

	@Override
	public double estimateValue(Coordinate coo, PotentialStreak potStreak) {
		return potStreak.filledCoosSize();
	}

}
