package grid_computations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Stripe {

	private ArrayList<ValuedCoordinate> stripe = new ArrayList<>();
	
	public Stripe() {}
	
	public Stripe(ValuedCoordinate start) {
		stripe.add(start);
	}
	
	public Stripe(ValuedCoordinate[] coordinates) {
		Arrays.sort(coordinates);
		for(ValuedCoordinate coo : coordinates) {
			stripe.add(coo);
		}
	}
	
	public void add(ValuedCoordinate coordinate) {
		stripe.add(coordinate);
		Collections.sort(stripe);
	}
	
	public ValuedCoordinate get(int index) {
		return stripe.get(index);
	}
	
	public int size() {
		return stripe.size();
	}
	
	public String toString() {
		StringBuilder strB = new StringBuilder();
		
		for(Coordinate c : stripe) {
			strB.append(c.toString() + ",");
		}
		strB.deleteCharAt(strB.length() - 1);
		
		return strB.toString();
	}
}
