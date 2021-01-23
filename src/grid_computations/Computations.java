package grid_computations;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import custom_exceptions.IllegalSetOfArgumentsException;
import game_components.Grid;
import game_components.Square.SVal;

public class Computations {
	
	public static LinkedList<Stripe> getAllRows(Grid g){
		LinkedList<Stripe> allRows = new LinkedList<>();
		
		for(int rowIndex = 0; rowIndex < g.size(); rowIndex++) {
			allRows.add(createRow(g, rowIndex));
		}
		
		return allRows;
	}
	
	private static Stripe createRow(Grid g, int rowIndex) {
		Stripe row = new Stripe();
		
		for(int columnIndex = 0; columnIndex < g.size(); columnIndex++) {
			row.add(new ValuedCoordinate(rowIndex, columnIndex, g.getVal(rowIndex, columnIndex)));
		}
		
		return row;
	}
	
	public static LinkedList<Stripe> getAllColumns(Grid g) {
		LinkedList<Stripe> allColumns = new LinkedList<>();
		
		for(int columnIndex = 0; columnIndex < g.size(); columnIndex++) {
			allColumns.add(createColumn(g, columnIndex));
		}
		
		return allColumns;
	}
	
	private static Stripe createColumn(Grid g, int columnIndex) {
		Stripe column = new Stripe();
		
		for(int rowIndex = 0; rowIndex < g.size(); rowIndex++) {
			column.add(new ValuedCoordinate(rowIndex, columnIndex, g.getVal(rowIndex, columnIndex)));
		}
		
		return column;
	}
	
	public static LinkedList<Stripe> getAllLeftDiagonals(Grid g) {
		LinkedList<Stripe> leftDiagonals = new LinkedList<>();
		
		// checking diagonals starting on left side
		for (int row = 0; row < g.size(); row++) {
			leftDiagonals.add(createLeftDiagonal(g, row, 0));
        }
		
		// checking diagonals starting on upper side (starts at 1 because first row = 0 already checked column = 0 diagonal)
		for (int column = 1; column < g.size(); column++) {
			leftDiagonals.add(createLeftDiagonal(g, 0, column));
        }
		
		return leftDiagonals;
	}
	
	private static Stripe createLeftDiagonal(Grid g, int startingRow, int startingColumn) {
		if (startingRow != 0 && startingColumn != 0) {
            throw new IllegalSetOfArgumentsException();
        }

        int row = startingRow;
        int column = startingColumn;
        Stripe stripe = new Stripe();

        while (row < g.size() && column < g.size()) {
            stripe.add(new ValuedCoordinate(row, column, g.getVal(row, column)));
            row++;
            column++;
        }

        return stripe;
	}
	
	public static LinkedList<Stripe> getAllRightDiagonals(Grid g) {
		LinkedList<Stripe> rightDiagonals = new LinkedList<>();
		
		// checking diagonals starting on right side
		for (int row = 0; row < g.size(); row++) {
			rightDiagonals.add(createRightDiagonal(g, row, g.size() - 1));
        }
		
		// checking diagonals starting on upper side (ends at g.size() - 1 because diagonal starting on last column was already checked)
		for (int column = 0; column < g.size() - 1; column++) {
			rightDiagonals.add(createRightDiagonal(g, 0, column));
		}
		
		return rightDiagonals;
	}
	
	private static Stripe createRightDiagonal(Grid g, int startingRow, int startingColumn) {
		if (startingRow != 0 && startingColumn != g.size() - 1) {
            throw new IllegalSetOfArgumentsException();
        }
		
		int row = startingRow;
        int column = startingColumn;
        Stripe stripe = new Stripe();

        while (row < g.size() && column >= 0) {
            stripe.add(new ValuedCoordinate(row, column, g.getVal(row, column)));
            row++;
            column--;
        }

        return stripe;
	}
	
	//--------------------Full Streaks--------------------
	
	/**
	 * Function finds out full streaks in grid of certain SVal. Substreaks are not included.
	 * @param g
	 * @param val
	 * @return
	 */
	public static List<FullStreak> getAllFullStreaks(Grid g, SVal val){
		Stream<FullStreak> trivialStreaks = getTrivialFullStreaks(g, val).stream();
		List<FullStreak> streaksFromStripes = getFullStreaksFromStripes(getAllStripes(g), val);
		
		return Stream.concat(trivialStreaks, streaksFromStripes.stream()).collect(Collectors.toList());
	}
	
	public static List<Stripe> getAllStripes(Grid g) {
		Stream<Stripe> allStripesStream = Stream.concat(getAllRows(g).stream(), getAllColumns(g).stream());
		allStripesStream = Stream.concat(allStripesStream, getAllLeftDiagonals(g).stream());
		allStripesStream = Stream.concat(allStripesStream, getAllRightDiagonals(g).stream());
		return allStripesStream.collect(Collectors.toList());
	}
		

	public static List<FullStreak> getFullStreaksFromStripes(List<Stripe> stripes, SVal val){
		Stream<FullStreak> streaksFromStripesStream = getFullStreaksFromStripe(stripes.get(0), val).stream();
		stripes = stripes.subList(1, stripes.size());
		
		for (Stripe stripe : stripes) {			
			streaksFromStripesStream = Stream.concat(streaksFromStripesStream, getFullStreaksFromStripe(stripe, val).stream());
        }
		
		return streaksFromStripesStream.collect(Collectors.toList());
	}
	
	public static List<FullStreak> getFullStreaksFromStripe(Stripe stripe, SVal val){
		List<FullStreak> streaks = new ArrayList<FullStreak>();
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
	
	public static List<FullStreak> getTrivialFullStreaks(Grid g, SVal val){
		List<FullStreak> allStreaks = new ArrayList<FullStreak>();
		
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
	
	public static List<PotentialStreak> getAllPotentialStreaks(Grid g, SVal val, int streakLength) {
		return getPotentialStreaksFromStripes(getAllStripes(g), val, streakLength);
	}
	
	public static List<PotentialStreak> getPotentialStreaksFromStripes(List<Stripe> stripes, SVal val, int streakLength) {
		Stream<PotentialStreak> streaksFromStripesStream = getPotentialStreaksFromStripe(stripes.get(0), val, streakLength).stream();
		stripes = stripes.subList(1, stripes.size());
		
		for (Stripe stripe : stripes) {			
			streaksFromStripesStream = Stream.concat(streaksFromStripesStream, getPotentialStreaksFromStripe(stripe, val, streakLength).stream());
        }
		
		return streaksFromStripesStream.collect(Collectors.toList());
	}
	
	public static List<PotentialStreak> getPotentialStreaksFromStripe(Stripe stripe, SVal val, int streakLength) {
		List<PotentialStreak> streaks = new ArrayList<PotentialStreak>();
		
		for(int i = 0; i < stripe.size(); i++) {
			if(checkForPotentialStreak(stripe, val, streakLength, i)) {
				streaks.add(createPotStreak(stripe, val, streakLength, i));
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
	
	private static PotentialStreak createPotStreak(Stripe stripe, SVal val, int streakLength, int startIndex) {
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
