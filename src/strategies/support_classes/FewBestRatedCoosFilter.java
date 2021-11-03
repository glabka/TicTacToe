package strategies.support_classes;

import java.util.Collections;
import java.util.List;

public class FewBestRatedCoosFilter extends AbstractRatedCoosFilter {

	private int howMuch;
	
	public FewBestRatedCoosFilter(int howMuch) {
		this.howMuch = howMuch;
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (!(o instanceof FewBestRatedCoosFilter)) {
			return false;
		}

		FewBestRatedCoosFilter other = (FewBestRatedCoosFilter) o;

		// checking all fields
		if (this.howMuch != other.howMuch) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return howMuch;
	}

	
	@Override
	public List<RatedCoordinate> filterRatedCoos(List<RatedCoordinate> coos) {
		if(coos == null) {
			return null;
		}
		
		Collections.sort(coos, new RatedCoordinatesValueComparator());
		Collections.reverse(coos);
		
		if(howMuch > coos.size()) {
			return coos.subList(0, coos.size());
		} else {
			return coos.subList(0, howMuch);
		}
	}

	@Override
	public Object clone() {
		return new FewBestRatedCoosFilter(howMuch);
	}

}
