package grid_computations;

import game_components.Square.SVal;

public class ValuedCoordinate extends Coordinate {
	
	private SVal val;

	public ValuedCoordinate(int row, int column, SVal val) {
		super(row, column);
		this.val = val;
	}
	
	public SVal getVal() {
		return val;
	}

}
