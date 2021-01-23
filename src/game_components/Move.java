package game_components;

import game_components.Square.SVal;
import grid_computations.Coordinate;

public class Move {

	private final int row;
	private final int column;
	private final SVal sVal;
	
	public Move(int row, int column, SVal sVal) {
		this.row = row;
		this.column = column;
		this.sVal = sVal;
	}
	
	public Move(Coordinate coo, SVal sVal) {
		this.row =  coo.getRow();
		this.column = coo.getColumn();
		this.sVal = sVal;
	}
	
	public int getRow() {
		return this.row;
	}
	
	public int getColumn() {
		return this.column;
	}
	
	public SVal getVal() {
		return this.sVal;
	}
	
	public String toString() {
		return "Move: " + row + ", " + column + ", " + sVal;
	}
}
