package web;

public class Message {

	private GameState gameState;
	private String gameName;
	private CommunicationSignal communicationSignal;
	private int gridSize;
	private int streakLength;
	private Move move;
	
	public static Message createMessage(String gameName,  CommunicationSignal communicationSignal) {
		Message gameMessage = new Message();
		gameMessage.setGameName(gameName);
		gameMessage.setCommunicationSignal(communicationSignal);
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
	
	public void setCommunicationSignal(CommunicationSignal communicationSignal) {
		this.communicationSignal = communicationSignal;
	}
	
	public CommunicationSignal getCommunicationSignal() {
		return communicationSignal;
	}

	public int getGridSize() {
		return gridSize;
	}

	public void setGridSize(int gridSize) {
		this.gridSize = gridSize;
	}

	public int getStreakLength() {
		return streakLength;
	}

	public void setStreakLength(int streakLength) {
		this.streakLength = streakLength;
	}

	public Move getMove() {
		return move;
	}

	public void setMove(Move move) {
		this.move = move;
	}
}
