package game;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import custom_exceptions.PlayerAlreadyExistsException;
import custom_exceptions.PlayerNotRegistredException;
import game.communication.IPlayerCallback;
import game.communication.InnerPlayerRepresentation;
import game.communication.Message;
import game.communication.enums.CommunicationError;
import game.communication.enums.CommunicationProtocolValue;

public class GameLogic {

	private final static GameLogic gameLogic = new GameLogic();
	private GameManager gameManager = new GameManager();
	private Map<InnerPlayerRepresentation, IPlayerCallback> callbacks = Collections.synchronizedMap(new HashMap<InnerPlayerRepresentation, IPlayerCallback>());
	private Map<Integer, InnerPlayerRepresentation> players = Collections.synchronizedMap(new HashMap<Integer, InnerPlayerRepresentation>());
	
	private GameLogic() {
		super();
	}
	
	public static GameLogic getGameLogic() {
		return gameLogic;
	}
	
	public void quitGame(String gameName) {
		gameManager.deleteGame(gameName);
	}
	
	
	
	public void receiveMessage(int senderID, Message message) {
		Message response = null;
		
		InnerPlayerRepresentation senderPlayer = players.get(senderID);
		if (senderPlayer == null) {
			throw new PlayerNotRegistredException("playerID: " + senderID);
		}
		
		String gameName = message.getGameName();
		if (gameName == null || gameName.isEmpty()) {
			response = Message.createMessage(CommunicationProtocolValue.ERROR);
			response.setCommunicationError(CommunicationError.NULL_OR_EMPTY_REQUIRED_FIELD);
		}
		
		// TODO
//		gameManager.registerGame(, gameMetaData);
	}
	
	public void registerPlayersCallback(int playersID, IPlayerCallback playersCallback) {
		if (players.containsKey(playersID)) {
			throw new PlayerAlreadyExistsException("playersID: " + playersID);
		}
		InnerPlayerRepresentation newPlayer = new InnerPlayerRepresentation(playersID);
		players.put(playersID, newPlayer);
		callbacks.put(newPlayer, playersCallback);
	}
	
}
