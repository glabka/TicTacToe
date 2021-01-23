package grid_computations;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import custom_exceptions.AlreadyAFullStreakException;
import custom_exceptions.MismatchValuesException;
import game_components.Square.SVal;

/**
 * Potential streak is streak containing of at least one filled square and with rest of them with value null.
 * @author glabk
 *
 */
public class PotentialStreak extends MetaStreak {

	private final ValuedCoordinate[] coos;
	private ValuedCoordinate[] nullCoos;
	private ValuedCoordinate[] filledCoos;
	
	/**
	 * 
	 * @param coos valued coordinates of potential streak
	 */
	public PotentialStreak(ValuedCoordinate[] coos) {
		super(sortCoordinatesAndReturnFirst(coos), coos[coos.length - 1]);
		
		Arrays.sort(coos);
		this.coos = coos;
		
		initiateFields(coos);
		
		if(filledCoos.length == this.getLength()) {
			throw new AlreadyAFullStreakException(this.toString());
		}

		areTheCoordinatesOfPotentialStreak();
		
	}
	
	private void initiateFields(ValuedCoordinate[] coos) {
		List<ValuedCoordinate> nullCoos = new LinkedList<ValuedCoordinate>();
		List<ValuedCoordinate> filledCoos = new LinkedList<ValuedCoordinate>();
		SVal streakVal = null;
		for(ValuedCoordinate coo : coos) {
			SVal cooVal = coo.getVal();
			if(cooVal == null) {
				nullCoos.add(coo);
			} else if (streakVal == null){
				streakVal = cooVal;
				filledCoos.add(coo);
			} else if (streakVal == cooVal) {
				filledCoos.add(coo);
			} else {
				// both circle and cross appeared in streak
				throw new MismatchValuesException();
			}
		}
		
		this.nullCoos = nullCoos.toArray(new ValuedCoordinate[0]);
		this.filledCoos = filledCoos.toArray(new ValuedCoordinate[0]);
	}
	
	public static Coordinate sortCoordinatesAndReturnFirst(Coordinate[] coos) {
		if(coos.length == 0) {
			throw new IllegalArgumentException();
		}
		Arrays.sort(coos);
		return coos[0];
	}
	
	private boolean areTheCoordinatesOfPotentialStreak() {
		return areTheseCoordinatesOfPotentialStreak(coos);
	}
	
	public ValuedCoordinate[] potentialCoos() {
		return Arrays.copyOf(nullCoos, nullCoos.length);
	}
	
	public ValuedCoordinate[] filledCoos() {
		return Arrays.copyOf(filledCoos, filledCoos.length);
	}
	
	public Coordinate[] getCoos() {
		return Arrays.copyOf(coos, coos.length);
	}
	
	public static boolean areTheseCoordinatesOfPotentialStreak(Coordinate[] c) {
		if (c.length == 1) {
			// Trivial Streak
			return true;
		}
		
		MetaStreak metaStreak = new MetaStreak(c[0], c[1]);
		StreakType type = metaStreak.getStreakType();
		for (int i = 1; i < c.length; i++) {
			metaStreak = new MetaStreak(c[0], c[i]);
			if (type != metaStreak.getStreakType()){
				return false;
			}
		}
		
		return true;
	}
	
	
	public int streakPointCount() {
		return coos.length;
	}
	
	
	public String toString() {
		StringBuilder strB = new StringBuilder();
		strB.append("<" + getStart().toString() + ">,");
		for(Coordinate c : filledCoos) {
			strB.append(c.toString() + ",");
		}
		strB.append("<" + getEnd().toString() + ">");
		return strB.toString();
	}
	
}
