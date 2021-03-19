package game.communication;

import game.GameMetaData;
import game.GameState;
import game.communication.enums.CommunicationError;
import game.communication.enums.CommunicationProtocolValue;
import game.communication.enums.GameResult;
import game.communication.enums.MoveResult;

public class Message {

	private GameState gameState;
	private String gameName;
	private GameMetaData gameMetaData;
	private Move move;
	private CommunicationProtocolValue communicationProtocolValue;
	private MoveResult moveResult;
	private GameResult gameResult;
	private CommunicationError communicationError;
	private String errorInfo;
	private byte[][] grid;
	
	
	public static Message createMessage(String gameName, CommunicationProtocolValue communicationProtocolValue) {
		Message gameMessage = new Message();
		gameMessage.setGameName(gameName);
		gameMessage.setCommunicationProtocolValue(communicationProtocolValue);
		return gameMessage;
	}
	
	public static Message createMessage(CommunicationProtocolValue communicationProtocolValue) {
		Message gameMessage = new Message();
		gameMessage.setCommunicationProtocolValue(communicationProtocolValue);
		return gameMessage;
	}
	
	public CommunicationProtocolValue getCommunicationProtocolValue() {
		return communicationProtocolValue;
	}

	public void setCommunicationProtocolValue(CommunicationProtocolValue communicationProtocolValue) {
		this.communicationProtocolValue = communicationProtocolValue;
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

	public GameMetaData getGameMetaData() {
		return gameMetaData;
	}

	public void setGameMetaData(GameMetaData gameMetaData) {
		this.gameMetaData = gameMetaData;
	}

	public Move getMove() {
		return move;
	}

	public void setMove(Move move) {
		this.move = move;
	}

	public MoveResult getMoveResult() {
		return moveResult;
	}

	public void setMoveResult(MoveResult moveResult) {
		this.moveResult = moveResult;
	}

	public GameResult getGameResult() {
		return gameResult;
	}

	public void setGameResult(GameResult gameResult) {
		this.gameResult = gameResult;
	}

	public CommunicationError getCommunicationError() {
		return communicationError;
	}

	public void setCommunicationError(CommunicationError communicationError) {
		this.communicationError = communicationError;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public byte[][] getGrid() {
		return grid;
	}

	public void setGrid(byte[][] grid) {
		this.grid = grid;
	}
}
