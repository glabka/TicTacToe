package grid_computations;

public class Coordinate {
	private final int row;
	private final int column;
	
	public Coordinate(int row, int column) {
		if(row < 0) {
			throw new IllegalArgumentException("row must be bigger than 0");
		} else if (column < 0) {
			throw new IllegalArgumentException("column must be bigger than 0");
		}
		
		this.row = row;
		this.column = column;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	public String toString() {
		return "[" + row + "][" + column + "]";
	}
}
