package grid_computations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Stripe {

	private ArrayList<ValueCoordinate> stripe = new ArrayList<>();
	
	public Stripe() {}
	
	public Stripe(ValueCoordinate start) {
		stripe.add(start);
	}
	
	public Stripe(ValueCoordinate[] coordinates) {
		Arrays.sort(coordinates);
		for(ValueCoordinate coo : coordinates) {
			stripe.add(coo);
		}
	}
	
	public void add(ValueCoordinate coordinate) {
		stripe.add(coordinate);
		Collections.sort(stripe);
	}
	
	public ValueCoordinate get(int index) {
		return stripe.get(index);
	}
	
	public int size() {
		return stripe.size();
	}
}
