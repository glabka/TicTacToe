package strategies.support_classes;

import java.util.List;

/**
 *  Filters and sorts RatedCoordinates from best to worse
 * @author glabk
 *
 */
public abstract class AbstractRatedCoosFilter implements Cloneable {

	public abstract List<RatedCoordinate> filterRatedCoos(List<RatedCoordinate> coos);
	
	public abstract Object clone();

	public abstract boolean equals(Object o);

	public abstract int hashCode();
}
