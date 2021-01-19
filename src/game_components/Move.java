package game_components;

import game_components.Square.SVal;

public class Move {

	private final int row;
	private final int column;
	private final SVal sVal;
	
	public Move(int row, int column, SVal sVal) {
		this.row = row;
		this.column = column;
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
	
}
