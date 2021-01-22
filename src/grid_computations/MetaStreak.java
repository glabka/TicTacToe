package grid_computations;

import custom_exceptions.FatalErrorInCodeException;
import custom_exceptions.NotALegalStreakException;

public class MetaStreak {

	private final Coordinate start;
	private final Coordinate end;
	private final StreakType type;
	
	/**
	 * MetaStreak consist of at least one square and must be in row, column, left or right diagonal.
	 * MetaStreak can have holes in the streak.
	 * @param oneEnd
	 * @param secondEnd
	 */
	public MetaStreak(Coordinate oneEnd, Coordinate secondEnd) {
		if (oneEnd.getRow() == secondEnd.getRow()) {
			if(oneEnd.getColumn() < secondEnd.getColumn()) {
				start = oneEnd;
				end = secondEnd;
			} else {
				start = secondEnd;
				end = oneEnd;
			}
		} else if (oneEnd.getRow() < secondEnd.getRow()) {
			start = oneEnd;
			end = secondEnd;
		} else {
			start = secondEnd;
			end = oneEnd;
		}
		
		if(!isThisStreakLegal()) {
			throw new NotALegalStreakException("Describe streak doesn't fit into any of streak types.");
		}
		
		if (isThisTrivialStreak()) {
			this.type = StreakType.TRIVIAL;
		} else if(isThisRowStreak()) {
			this.type = StreakType.ROW;
		} else if (isThisColumnStreak()) {
			this.type = StreakType.COLUMN;
		} else if (isThisLeftDiagonalStreak()) {
			this.type = StreakType.LEFT_DIAGONAL;
		} else if (isThisRightDiagonalStreak()) {
			this.type = StreakType.RIGHT_DIAGONAL;
		} else {
			throw new FatalErrorInCodeException();
		}
	}
	
	public Coordinate getStart() {
		return start;
	}
	
	public Coordinate getEnd() {
		return end;
	}
	
	public int getLength() {
		return getLength(start, end);
	}
	
	public static int getLength(Coordinate oneEnd, Coordinate secondEnd) {
		return Math.max(Math.abs(oneEnd.getRow()- secondEnd.getRow()), Math.abs(oneEnd.getColumn()- secondEnd.getColumn())) + 1; // + 1 because both coordinates are inclusive
	}
	
	private boolean isThisStreakLegal() {
		return isThisTrivialStreak() || isThisRowStreak() || isThisColumnStreak() ||
				isThisLeftDiagonalStreak() ||isThisRightDiagonalStreak();
	}
	
	public static boolean isStreakLegal(Coordinate oneEnd, Coordinate secondEnd) {
		return isItTrivialStreak(oneEnd, secondEnd) || isItRowStreak(oneEnd, secondEnd) ||
				isItColumnStreak(oneEnd, secondEnd) ||isItLeftDiagonalStreak(oneEnd, secondEnd) ||
				isItRightDiagonalStreak(oneEnd, secondEnd);
	}
	
	public boolean isThisTrivialStreak() {
		return isItTrivialStreak(start, end);
	}
	
	public static boolean isItTrivialStreak(Coordinate oneEnd, Coordinate secondEnd) {
		return oneEnd.getRow() == secondEnd.getRow() && oneEnd.getColumn() == secondEnd.getColumn();
	}
	
	public boolean isThisRowStreak() {
		return isItRowStreak(start, end);
	}
	
	public static boolean isItRowStreak(Coordinate oneEnd, Coordinate secondEnd) {
		return oneEnd.getRow() == secondEnd.getRow() && oneEnd.getColumn() != secondEnd.getColumn();
	}
	
	public boolean isThisColumnStreak() {
		return isItColumnStreak(start, end);
	}
	
	public static boolean isItColumnStreak(Coordinate oneEnd, Coordinate secondEnd) {
		return oneEnd.getRow() != secondEnd.getRow() && oneEnd.getColumn() == secondEnd.getColumn();
	}
	
	public boolean isThisLeftDiagonalStreak() {
		return isItLeftDiagonalStreak(start, end);
	}
	
	public static boolean isItLeftDiagonalStreak(Coordinate oneEnd, Coordinate secondEnd) {
		return (oneEnd.getRow() - secondEnd.getRow()) == (oneEnd.getColumn() - secondEnd.getColumn());
	}
	
	public boolean isThisRightDiagonalStreak() {
		return isItRightDiagonalStreak(start, end);
	}
	
	public static boolean isItRightDiagonalStreak(Coordinate oneEnd, Coordinate secondEnd) {
		return (oneEnd.getRow() - secondEnd.getRow()) == -(oneEnd.getColumn() - secondEnd.getColumn());
	}
	
	public StreakType getStreakType() {
		return type;
	}
	
	public enum StreakType {
		TRIVIAL, ROW, COLUMN, LEFT_DIAGONAL, RIGHT_DIAGONAL
	}
}
