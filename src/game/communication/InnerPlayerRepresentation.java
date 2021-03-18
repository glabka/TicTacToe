package game.communication;

public class InnerPlayerRepresentation {
	
	private final int id;
	
	public InnerPlayerRepresentation(int id) {
		this.id = id;
	}

	public int getId() {
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
		if (this.getId() == webPlayer.getId()) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return id;
	}
}
