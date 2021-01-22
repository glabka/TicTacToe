package grid_computations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import game_components.Grid;
import game_components.Square.SVal;

public class Computer {

	/**
	 * Function finds out full row streaks in grid of certain SVal. Substreaks are not included.
	 * @param g
	 * @param val
	 * @return
	 */
	public static Collection<FullStreak> getFullRowStreaks(Grid g, SVal val){
		Collection<FullStreak> allStreaks = new ArrayList<FullStreak>();
		for (int i = 0; i < g.size(); i++) {
			allStreaks = Stream.concat(allStreaks.stream(), getFullRowStreaksFromRow(g, val, i).stream())
					.collect(Collectors.toList());
        }
		return allStreaks;
	}
	
	public static Collection<FullStreak> getFullRowStreaksFromRow(Grid g, SVal val, int row){
		Collection<FullStreak> streaks = new ArrayList<FullStreak>();

		int start = -1;
		int counter = 0;
        for (int j = 0; j < g.size(); j++) {
            if (g.getVal(row, j) != val) {
                counter = 0;
                if(start != -1) {
                	streaks.add(new FullStreak(new Coordinate(row, start), new Coordinate(row, j - 1)));
                	start = -1;
                }
            } else {
            	if(counter == 0) {
            		start = j;
            	}
                counter++;
            }
        }
        
        // in case streak ends on border
        if (start != -1) {
        	streaks.add(new FullStreak(new Coordinate(row, start), new Coordinate(row, g.size() - 1)));
        }

        return streaks;
	}
	
	public static Collection<PotentialStreak> getPotentialRowStreaks(Grid g, SVal val, int streakLength) {
		Collection<PotentialStreak> allStreaks = new ArrayList<PotentialStreak>();
		for (int i = 0; i < g.size(); i++) {
			allStreaks = Stream.concat(allStreaks.stream(), getPotentialRowStreaksFromRow(g, val, streakLength ,i).stream())
					.collect(Collectors.toList());
        }
		return allStreaks;
	}
	
	public static Collection<PotentialStreak> getPotentialRowStreaksFromRow(Grid g, SVal val, int streakLength, int row) {
		Collection<PotentialStreak> streaks = new ArrayList<PotentialStreak>();
		
		for(int i = 0; i < g.size(); i++) {
			if(checkForPotentialStreak(g, val, streakLength, row, i)) {
				streaks.add(createPotRowStreak(g, val, streakLength, row, i));
			}
		}
		
		return streaks;
	}
	
	public static boolean checkForPotentialStreak(Grid g, SVal val, int streakLength, int row, int column) {
		if (column + streakLength > g.size()) {
			return false;
		}
		
		int counter = 0;
		for(int i = column; i < column + streakLength; i++) {
			if (g.getVal(row, i) == val) {
				counter++;
			} else if (g.getVal(row, i) == SVal.getOpposite(val)) {
				return false;
			}
		}
		return (counter > 0 ) && (counter < streakLength) ? true : false;
	}
	
	private static PotentialStreak createPotRowStreak(Grid g, SVal val, int streakLength, int row, int startColumn) {
		// coordinates filled with val
		ArrayList<Coordinate> filledCoordinates = new ArrayList<>();
		for(int i = startColumn; i < startColumn + streakLength; i++) {
			if(g.getVal(row, i) == val) {
				filledCoordinates.add(new Coordinate(row, i));
			}
		}
		
		return new PotentialStreak(new Coordinate(row, startColumn), new Coordinate(row, startColumn + streakLength -1), filledCoordinates.toArray(new Coordinate[0]));
	}
	
	
	/**
	 * Function finds out full column streaks in grid of certain SVal. Substreaks are not included.
	 * @param g
	 * @param val
	 * @return
	 */
	public static Collection<FullStreak> getFullColumnStreaks(Grid g, SVal val){
		Collection<FullStreak> allStreaks = new ArrayList<FullStreak>();
		for (int i = 0; i < g.size(); i++) {
			allStreaks = Stream.concat(allStreaks.stream(), getFullColumnStreaksFromRow(g, val, i).stream())
					.collect(Collectors.toList());
        }
		return allStreaks;
	}
	
	public static Collection<FullStreak> getFullColumnStreaksFromRow(Grid g, SVal val, int column){
		Collection<FullStreak> streaks = new ArrayList<FullStreak>();

		int start = -1;
		int counter = 0;
        for (int j = 0; j < g.size(); j++) {
            if (g.getVal(j, column) != val) {
                counter = 0;
                if(start != -1) {
                	streaks.add(new FullStreak(new Coordinate(start, column), new Coordinate(j - 1, column)));
                	start = -1;
                }
            } else {
            	if(counter == 0) {
            		start = j;
            	}
                counter++;
            }
        }
        
        // in case streak ends on border
        if (start != -1) {
        	streaks.add(new FullStreak(new Coordinate(start, column), new Coordinate(g.size() - 1, column)));
        }

        return streaks;
	}
	
	
}
