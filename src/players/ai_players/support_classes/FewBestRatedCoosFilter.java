package players.ai_players.support_classes;

import java.util.Collections;
import java.util.List;

public class FewBestRatedCoosFilter extends AbstractRatedCoosFilter {

	private int howMuch;
	
	public FewBestRatedCoosFilter(int howMuch) {
		this.howMuch = howMuch;
		System.out.println("debug howMuch in FewBestRatedCoosFilter: " + howMuch);
	}
	
	@Override
	public List<RatedCoordinate> filterRatedCoos(List<RatedCoordinate> coos) {
		System.out.println("howMuch : " + howMuch);
		Collections.sort(coos, new RatedCoordinatesValueComparator());
		Collections.reverse(coos);
		
		if(howMuch > coos.size()) {
			return coos.subList(0, coos.size());
		} else {
			return coos.subList(0, howMuch);
		}
	}

	
	
}
