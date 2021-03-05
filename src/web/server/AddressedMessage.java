package web.server;

import game.communication.Message;

public class AddressedMessage {
	
	private String webPlayerID;
	private Message gameMessage;
	
	public AddressedMessage(String webPlayerID, Message gMessage) {
		this.webPlayerID = webPlayerID;
		this.gameMessage = gMessage;
	}
	
	public String getWebPlayerID() {
		return webPlayerID;
	}
	
	public Message getGameMessage() {
		return gameMessage;
	}

}
