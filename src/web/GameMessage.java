package web;

public class GameMessage {

	private GameState gameState;
	private String gameName;
	
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
	
}
