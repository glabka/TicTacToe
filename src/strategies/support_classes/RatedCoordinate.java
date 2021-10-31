package players.ai_players.support_classes;

import grid_computations.Coordinate;

public class RatedCoordinate extends Coordinate{

	private double value;
	
	public RatedCoordinate(Coordinate coo, double value) {
		super(coo);
		this.value = value;
	}
	
	public RatedCoordinate(int row, int column, double value) {
		super(row, column);
		this.value = value;
	}
	
	public double getValue() {
		return this.value;
	}
	
	public void setValue(double val) {
		this.value = val;
	}
	
	public static RatedCoordinate addSameCoordinates(RatedCoordinate coo1, RatedCoordinate coo2) {
		if(coo1.getRow() != coo2.getRow() || coo1.getColumn() != coo2.getColumn()) {
			throw new IllegalArgumentException("coordinates have to have same row and column for this operation. That wasn't fullfilled by " + coo1 + ", " + coo2);
		}
		
		return new RatedCoordinate(coo1, coo1.getValue() + coo2.getValue());
	}
	
	public static RatedCoordinate weightAddSameCoordinates(double coefficient1, RatedCoordinate coo1, double coefficient2, RatedCoordinate coo2) {
		if(coo1.getRow() != coo2.getRow() || coo1.getColumn() != coo2.getColumn()) {
			throw new IllegalArgumentException("coordinates have to have same row and column for this operation. That wasn't fullfilled by " + coo1 + ", " + coo2);
		}
		
		return new RatedCoordinate(coo2, coefficient1 * coo1.getValue() + coefficient2 * coo2.getValue());
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this)
	        return true;
	    if (!(o instanceof RatedCoordinate))
	        return false;
	    RatedCoordinate coo = (RatedCoordinate) o;
	    if(getRow() != coo.getRow()) {
	    	return false;
	    } else if (getColumn() != coo.getColumn()) {
	    	return false;
	    } else {
	    	return true;
	    }
	}
	
	@Override
	public int hashCode() {
		return -3 * getRow() + 100 * getColumn();
	}
	
	@Override
	public String toString() {
		return "[" + this.getRow() + "][" + this.getColumn() + "] |" + value + "|";

	}
}
