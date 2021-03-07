package game_components;

import game_components.Square.SVal;
import grid_computations.Coordinate;

public class ValuedMove extends Coordinate {

	private final SVal sVal;
	
	public ValuedMove(int row, int column, SVal sVal) {
		super(row, column);
		this.sVal = sVal;
	}
	
	public ValuedMove(Coordinate coo, SVal sVal) {
		super(coo.getRow(), coo.getColumn());
		this.sVal = sVal;
	}
	
	public SVal getVal() {
		return this.sVal;
	}
	
	public String toString() {
		return "Move: " + this.getRow() + ", " + this.getColumn() + ", " + sVal;
	}
}
