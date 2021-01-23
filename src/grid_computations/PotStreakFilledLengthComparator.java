package grid_computations;

import java.util.Comparator;

public class PotStreakFilledLengthComparator implements Comparator<PotentialStreak> {

	@Override
	public int compare(PotentialStreak arg0, PotentialStreak arg1) {
		int arg0Size = arg0.filledCoosSize();
		int arg1Size = arg1.filledCoosSize(); 
		if(arg0Size< arg1Size) {
			return -1;
		} else if (arg0Size > arg1Size) {
			return 1;
		} else {
			return 0;
		}
	}

}
