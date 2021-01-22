package grid_computations;

import java.util.Arrays;

import javax.management.remote.SubjectDelegationPermission;

import custom_exceptions.AlreadyAFullStreakException;

public class PotentialStreak extends MetaStreak {

	private final Coordinate[] psc;
	
	/**
	 * 
	 * @param psc potential streak coordinates - coordinates of every cross or circle in potential streak. The coordinates should be arranged in order
	 */
	public PotentialStreak(Coordinate oneEnd, Coordinate secondEnd, Coordinate[] psc) {
		super(oneEnd, secondEnd);
		if(psc.length == 0) {
			throw new IllegalArgumentException();
		}
		
		Arrays.sort(psc);
		this.psc = psc;
		if(psc.length == this.getLength()) {
			throw new AlreadyAFullStreakException(this.toString());
		}

		areTheCoordinatesOfPotentialStreak();
		
	}
	
	private boolean areTheCoordinatesOfPotentialStreak() {
		return areTheseCoordinatesOfPotentialStreak(psc);
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
		return psc.length;
	}
	
	public String toString() {
		StringBuilder strB = new StringBuilder();
		strB.append("<" + getStart().toString() + ">,");
		for(Coordinate c : psc) {
			strB.append(c.toString() + ",");
		}
		strB.append("<" + getEnd().toString() + ">");
		return strB.toString();
	}
	
}
