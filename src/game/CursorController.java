package game;

import custom_exceptions.NullCursorCoordinatesException;
import game_components.Grid;

public class CursorController {

	public static void moveRight(Grid g) {
		checkCursorIsNotNull(g);
		int cursorColumn = g.getCursorColumn();
		
		if (cursorColumn == g.size() - 1) {
            cursorColumn = 0;
        } else {
            cursorColumn += 1;
        }
		
		g.setCursonColumn(cursorColumn);
	}
	
	public static void moveLeft(Grid g) {
		checkCursorIsNotNull(g);
		int cursorColumn = g.getCursorColumn();
		
		if (cursorColumn == 0) {
            cursorColumn = g.size() - 1;
        } else {
            cursorColumn -= 1;
        }
		
		g.setCursonColumn(cursorColumn);
	}
	
	public static void moveUp(Grid g) {
		checkCursorIsNotNull(g);
		int cursorRow = g.getCursorRow();
				
		if (cursorRow == 0) {
            cursorRow = g.size() - 1;
        } else {
            cursorRow -= 1;
        }
		
		g.setCursonRow(cursorRow);
	}
	
	public static void moveDown(Grid g) {
		checkCursorIsNotNull(g);
		int cursorRow = g.getCursorRow();
		
		if (cursorRow == g.size() - 1) {
            cursorRow = 0;
        } else {
            cursorRow += 1;
        }
		
		g.setCursonRow(cursorRow);
	}
	
	private static void checkCursorIsNotNull(Grid g) {
		if (g.getCursorColumn() == null) {
			throw new NullCursorCoordinatesException();
		} else if (g.getCursorRow() == null) {
			throw new NullCursorCoordinatesException();
		}
	}
}
