package game.communication;

import game_components.Grid;
import game_components.Square.SVal;

public class GridTransferRepresentation {

	private static final byte NULL_VALUE = 0;
	private static final byte PLAYERS_VALUE = 1;
	private static final byte OPPONENTS_VALUE = -1;
	
	public static byte[][] getGridTransferRepresentation(Grid g, SVal playersVal) {
		byte[][] grid = new byte[g.size()][g.size()];

		for (int i = 0; i < g.size(); i++) {
			for (int j = 0; j < g.size(); j++) {
				SVal val = g.getVal(i, j);
				if (val == null) {
					grid[i][j] = NULL_VALUE;
				} else if (val == playersVal) {
					grid[i][j] = PLAYERS_VALUE;
				} else {
					grid[i][j] = OPPONENTS_VALUE;
				}
			}
		}

		return grid;
	}
	
	public static Grid getGrid(byte[][] transferGrid, SVal playersVal) {
		Grid grid = new Grid(transferGrid.length);
		
		for (int i = 0; i < grid.size(); i++) {
			for (int j = 0; j < grid.size(); j++) {
				byte val = transferGrid[i][j];
				if (val == NULL_VALUE) {
					// pass
				} else if (val == PLAYERS_VALUE) {
					grid.insert(i, j, playersVal);
				} else if (val == OPPONENTS_VALUE) {
					grid.insert(i, j, SVal.getOpposite(playersVal));
				} else {
					throw new IllegalArgumentException("tranferGrid[" + i + "][" + j + "]: " + val + "which is illegal");
				}
			}
		}

		return grid;
	}
}
