package web.server;

import web.GameMessage;

public class AddressedGameMessage {
	
	private String webPlayerID;
	private GameMessage gMessage;
	
	public AddressedGameMessage(String webPlayerID, GameMessage gMessage) {
		this.webPlayerID = webPlayerID;
		this.gMessage = gMessage;
	}
	
	public String getWebPlayerID() {
		return webPlayerID;
	}
	
	public GameMessage getGameMessage() {
		return gMessage;
	}

}
