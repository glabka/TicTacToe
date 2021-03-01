package web;

public class Message {

	private GameState gameState;
	private String gameName;
	private CommunicationError communicationError;
	
	public static Message createMessage(String gameName, GameState gameState) {
		Message gameMessage = new Message();
		gameMessage.setGameName(gameName);
		gameMessage.setGameState(gameState);
		return gameMessage;
	}
	
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
	
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	
	public GameState getGameState() {
		return gameState;
	}
	
	public String getGameName() {
		return gameName;
	}
	
	public void setCommunicationError(CommunicationError communicationError) {
		this.communicationError = communicationError;
	}
	
	public CommunicationError getCommunicationError() {
		return communicationError;
	}
	
}
