package game.communication;

import custom_exceptions.WebPlayerNotInitialized;
import game_components.Square.SVal;

public class InnerPlayerRepresentation {
	
	private String id;
	private SVal sVal;
	
	public InnerPlayerRepresentation(String id) {
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
		int hash = 7;
		for (int i = 0; i < id.length(); i++) {
		    hash = hash*31 + id.charAt(i);
		}
		return hash;
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
