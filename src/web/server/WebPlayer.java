package web.server;

public class WebPlayer {
	
	private String id;
	
	public WebPlayer(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		} else if (!(o instanceof WebPlayer)) {
			return false;
		}
		WebPlayer webPlayer = (WebPlayer) o;
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
	
}
