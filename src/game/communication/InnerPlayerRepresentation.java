package game.communication;

import custom_exceptions.WebPlayerNotInitialized;
import game_components.Square.SVal;

public class InnerPlayerRepresentation {
	
	private final int id;
	private SVal sVal;
	
	public InnerPlayerRepresentation(int id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		} else if (!(o instanceof InnerPlayerRepresentation)) {
			return false;
		}
		InnerPlayerRepresentation webPlayer = (InnerPlayerRepresentation) o;
		if (this.getId().equals(webPlayer.getId())) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	public void setSVal(SVal sVal) {
		this.sVal = sVal;
	}
	
	public SVal getSVal() {
		if(sVal == null) {
			throw new WebPlayerNotInitialized();
		}
		return sVal;
	}
	
}
