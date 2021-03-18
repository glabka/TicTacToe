package game;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import custom_exceptions.FullOpponentsException;
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
	
	
	
	public Message receiveMessage(int senderID, Message message) {
		Message response = null;
		
		InnerPlayerRepresentation senderPlayer = players.get(senderID);
		if (senderPlayer == null) {
			throw new PlayerNotRegistredException("callback for playerID: " + senderID + "not registered");
		}
		
		String gameName = message.getGameName();
		if (gameName == null || gameName.isEmpty()) {
			response = Message.createMessage(CommunicationProtocolValue.ERROR);
			response.setCommunicationError(CommunicationError.REQUIRED_FIELD_NULL_OR_EMPTY);
			response.setErrorInfo("gameName null or empty");
			return response;
		}
		
		if (message.getCommunicationProtocolValue() == CommunicationProtocolValue.CREATE_GAME) {
			GameMetaData gameMetaData = message.getGameMataData();
			if (gameMetaData == null) {
				response.setCommunicationError(CommunicationError.REQUIRED_FIELD_NULL_OR_EMPTY);
				response.setErrorInfo("gamenull or empty");
				return response;
			} else {
				gameManager.registerGame(gameName, gameMetaData);
				// register player that initiated the game
				gameManager.getGame(gameName).registerPlayer(players.get(senderID));
			}
		} else if(message.getCommunicationProtocolValue() == CommunicationProtocolValue.REGISTER_FOR_GAME) {
			// registering second player for game
			try {
				gameManager.getGame(gameName).registerPlayer(players.get(senderID));
			} catch (FullOpponentsException e) {
				
			}
		}
		
		// TODO
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
