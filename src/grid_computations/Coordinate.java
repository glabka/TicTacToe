package grid_computations;

public class Coordinate implements Comparable<Coordinate>{
	private final int row;
	private final int column;
	
	public Coordinate(Coordinate coo) {
		this.row = coo.getRow();
		this.column = coo.getColumn();
	}
	
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
	
	@Override
	public int compareTo(Coordinate c) {
		if (getRow() < c.getRow()) return -1;
        if (getRow() > c.getRow()) return +1;
        if (getColumn() < c.getColumn()) return -1;
        if (getColumn() > c.getColumn()) return +1;
        return 0;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		} else if (!(o instanceof Coordinate)) {
			return false;
		}
		Coordinate coo = (Coordinate) o;
		
		if(this.getRow() != coo.getRow()) {
			return false;
		} else if (this.getColumn() != coo.getColumn()) {
			return false;
		}
		
		return true; 		
	}
}
