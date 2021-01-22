package grid_computations;

import game_components.Square.SVal;

public class ValueCoordinate extends Coordinate {
	
	private SVal val;

	public ValueCoordinate(int row, int column, SVal val) {
		super(row, column);
		this.val = val;
	}
	
	public SVal getVal() {
		return val;
	}

}
