package strategies.support_classes;

import java.util.Comparator;


public class RatedCoordinatesValueComparator  implements Comparator<RatedCoordinate>{

	@Override
	public int compare(RatedCoordinate arg0, RatedCoordinate arg1) {
		double val0 = arg0.getValue();
		double val1 = arg1.getValue();
		if(val0 < val1) {
			return -1;
		} else if (val0 > val1) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
	        return true;
	    if (!(o instanceof RatedCoordinatesValueComparator)) {
	        return false;
		}

	    return true;
	}
	
	@Override
	public int hashCode() {
		return 111;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "()";
	}

}
