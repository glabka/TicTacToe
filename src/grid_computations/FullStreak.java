package grid_computations;

/**
 * Full streak means uninterrupted Streak - it can be smaller then streak required for win.
 * @author glabk
 *
 */
public class FullStreak extends MetaStreak {

	public FullStreak(Coordinate trivialCoordinate) {
		super(trivialCoordinate);
	}
	
	public FullStreak(Coordinate oneEnd, Coordinate secondEnd) {
		super(oneEnd, secondEnd);
	}
	
	public String toString() {
		return getStart().toString() + "," + getEnd().toString();
	}

}
