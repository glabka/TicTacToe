package grid_computations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import custom_exceptions.IllegalSetOfArgumentsException;
import game_components.Grid;
import game_components.Square.SVal;

public class Computations {
	
	//--------------------Full Streaks--------------------

	/**
	 * Function finds out full row streaks in grid of certain SVal. Substreaks are not included.
	 * @param g
	 * @param val
	 * @return
	 */
	public static Collection<FullStreak> getFullRowStreaks(Grid g, SVal val){
		Collection<FullStreak> allStreaks = new ArrayList<FullStreak>();
		
		for (int rowIndex = 0; rowIndex < g.size(); rowIndex++) {	
			Stripe row = new Stripe();
			for(int columnIndex = 0; columnIndex < g.size(); columnIndex++) {
				row.add(new ValueCoordinate(rowIndex, columnIndex, g.getVal(rowIndex, columnIndex)));
			}
		
			allStreaks = Stream.concat(allStreaks.stream(), getFullStreaksFromStripe(row, val).stream())
					.collect(Collectors.toList());
        }
		return allStreaks;
	}
	
	public static Collection<FullStreak> getFullColumnStreaks(Grid g, SVal val){
		Collection<FullStreak> allStreaks = new ArrayList<FullStreak>();
		
		for(int columnIndex = 0; columnIndex < g.size(); columnIndex++)  {
			Stripe column = new Stripe();
			for (int rowIndex = 0; rowIndex < g.size(); rowIndex++) {
				column.add(new ValueCoordinate(rowIndex, columnIndex, g.getVal(rowIndex, columnIndex)));
			}
		
			allStreaks = Stream.concat(allStreaks.stream(), getFullStreaksFromStripe(column, val).stream())
					.collect(Collectors.toList());
        }
		return allStreaks;
	}
	
	public static Collection<FullStreak> getFullLeftDiagonalStreaks(Grid g, SVal val){
		Collection<FullStreak> allStreaks = new ArrayList<FullStreak>();
		Stripe leftDiagonal;
		
		// checking diagonals starting on left side
		for (int row = 0; row < g.size(); row++) {
			leftDiagonal = createLeftDiagonal(g, row, 0);
        	allStreaks = Stream.concat(allStreaks.stream(), getFullStreaksFromStripe(leftDiagonal, val).stream())
					.collect(Collectors.toList());
        }
		
		// checking diagonals starting on upper side (starts at 1 because first row = 0 already checked column = 0 diagonal)
		for (int column = 1; column < g.size(); column++) {
			leftDiagonal = createLeftDiagonal(g, 0, column);
        	allStreaks = Stream.concat(allStreaks.stream(), getFullStreaksFromStripe(leftDiagonal, val).stream())
					.collect(Collectors.toList());
        }
		
		return allStreaks;
	}
	
	private static Stripe createLeftDiagonal(Grid g, int startingRow, int startingColumn) {
		if (startingRow != 0 && startingColumn != 0) {
            throw new IllegalSetOfArgumentsException();
        }

        int row = startingRow;
        int column = startingColumn;
        Stripe stripe = new Stripe();

        while (row < g.size() && column < g.size()) {
            stripe.add(new ValueCoordinate(row, column, g.getVal(row, column)));
            row++;
            column++;
        }

        return stripe;
	}
	
	public static Collection<FullStreak> getFullRightDiagonalStreaks(Grid g, SVal val){
		Collection<FullStreak> allStreaks = new ArrayList<FullStreak>();
		Stripe rightDiagonal;
		
		// checking diagonals starting on right side
		for (int row = 0; row < g.size(); row++) {
			rightDiagonal = createRightDiagonal(g, row, g.size() - 1);
        	allStreaks = Stream.concat(allStreaks.stream(), getFullStreaksFromStripe(rightDiagonal, val).stream())
					.collect(Collectors.toList());
        }
		
		// checking diagonals starting on upper side (ends at g.size() - 1 because diagonal starting on last column was already checked)
		for (int column = 0; column < g.size() - 1; column++) {
			rightDiagonal = createRightDiagonal(g, 0, column);
        	allStreaks = Stream.concat(allStreaks.stream(), getFullStreaksFromStripe(rightDiagonal, val).stream())
					.collect(Collectors.toList());
        }
		
		return allStreaks;
	}
	
	private static Stripe createRightDiagonal(Grid g, int startingRow, int startingColumn) {
		if (startingRow != 0 && startingColumn != g.size() - 1) {
            throw new IllegalSetOfArgumentsException();
        }
		
		int row = startingRow;
        int column = startingColumn;
        Stripe stripe = new Stripe();

        while (row < g.size() && column >= 0) {
            stripe.add(new ValueCoordinate(row, column, g.getVal(row, column)));
            row++;
            column--;
        }

        return stripe;
	}
	
	public static Collection<FullStreak> getFullStreaksFromStripe(Stripe stripe, SVal val){
		Collection<FullStreak> streaks = new ArrayList<FullStreak>();
		if(stripe.size() <= 1) {
			// trivial FullStreaks don't count - return empty streak
			return streaks;
		}

		Coordinate start = null;
		int counter = 0;
        for (int i = 0; i < stripe.size(); i++) {
            if (stripe.get(i).getVal() != val) {
                if(start != null && counter > 1) {
                	streaks.add(new FullStreak(start, stripe.get(i - 1)));
                	start = null;
                }
                counter = 0;
            } else {
            	if(counter == 0) {
            		start = stripe.get(i);
            	}
                counter++;
            }
        }
        
        // in case streak ends on border
        if (start != null && counter > 1) {
        	streaks.add(new FullStreak(start, stripe.get(stripe.size() - 1)));
        }

        return streaks;
	}
	
	public static Collection<FullStreak> getTrivialFullSteraksFromGrid(Grid g, SVal val){
		Collection<FullStreak> allStreaks = new ArrayList<FullStreak>();
		
		for (int i = 0; i < g.size(); i++) {
			for (int j = 0; j < g.size(); j++) {
				if (g.getVal(i, j) == val && checkBorders(g, i, j, val)) {
					allStreaks.add(new FullStreak(new Coordinate(i,j), new Coordinate(i,j)));
				}
			}
		}
		
		return allStreaks;
	}
	
	// checks there are no squares with same value on surrounding borders
	private static boolean checkBorders(Grid g, int row, int column, SVal val) {
		int startRow = row > 0 ? row - 1 : row;
		int startColumn = column > 0 ? column - 1 : column;
		int endRow = g.size() > row + 1 ? row + 1 : row;
		int endColumn = g.size() > column + 1 ? column + 1: column;
		
		for (int rowIndex = startRow; rowIndex <= endRow; rowIndex++) {
			for(int columnIndex = startColumn; columnIndex <= endColumn; columnIndex++) {
				if(rowIndex == row && columnIndex == column) {
					continue;
				}
				if(g.getVal(rowIndex, columnIndex) == val) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	//--------------------Potential Streaks--------------------
	
	public static Collection<PotentialStreak> getPotentialRowStreaks(Grid g, SVal val, int streakLength) {
		Collection<PotentialStreak> allStreaks = new ArrayList<PotentialStreak>();
		for (int i = 0; i < g.size(); i++) {
			
			Stripe row = new Stripe();
			for(int j = 0; j < g.size(); j++) {
				row.add(new ValueCoordinate(i, j, g.getVal(i, j)));
			}
			
			allStreaks = Stream.concat(allStreaks.stream(), getPotentialStreaksFromStripe(row, val, streakLength).stream())
					.collect(Collectors.toList());
        }
		return allStreaks;
	}
	
	public static Collection<PotentialStreak> getPotentialStreaksFromStripe(Stripe stripe, SVal val, int streakLength) {
		Collection<PotentialStreak> streaks = new ArrayList<PotentialStreak>();
		
		for(int i = 0; i < stripe.size(); i++) {
			if(checkForPotentialStreak(stripe, val, streakLength, i)) {
				streaks.add(createPotRowStreak(stripe, val, streakLength, i));
			}
		}
		
		return streaks;
	}
	
	public static boolean checkForPotentialStreak(Stripe stripe, SVal val, int streakLength, int startCooIndex) {
		if (startCooIndex + streakLength > stripe.size()) {
			return false;
		}
		
		int counter = 0;
		for(int i = startCooIndex; i < startCooIndex + streakLength; i++) {
			if (stripe.get(i).getVal() == val) {
				counter++;
			} else if (stripe.get(i).getVal() == SVal.getOpposite(val)) {
				return false;
			}
		}
		
		// counter < streakLength because there can be already a FullStreak
		return (counter > 0 ) && (counter < streakLength) ? true : false;
	}
	
	private static PotentialStreak createPotRowStreak(Stripe stripe, SVal val, int streakLength, int startIndex) {
		// coordinates filled with val
		ArrayList<Coordinate> filledCoordinates = new ArrayList<>();
		for(int i = startIndex; i < startIndex + streakLength; i++) {
			if(stripe.get(i).getVal() == val) {
				filledCoordinates.add(stripe.get(i));
			}
		}
		
		return new PotentialStreak(stripe.get(startIndex), stripe.get(startIndex + streakLength - 1), filledCoordinates.toArray(new Coordinate[0]));
	}
	
	
	
}
