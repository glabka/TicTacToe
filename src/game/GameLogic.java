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
		
		if (message.getCommunicationProtocolValue() == null) {
			response = Message.createMessage(CommunicationProtocolValue.ERROR);
			response.setCommunicationError(CommunicationError.REQUIRED_FIELD_NULL_OR_EMPTY);
			response.setErrorInfo("Field communicationProtocolValue can not be null.");
			return response;
		} else if (message.getCommunicationProtocolValue() == CommunicationProtocolValue.CREATE_GAME) {
			if (gameManager.getGame(gameName) != null) {
				response = Message.createMessage(CommunicationProtocolValue.ERROR);
				response.setCommunicationError(CommunicationError.GAME_ALREADY_EXIST);
				return response;
			}
			
			GameMetaData gameMetaData = message.getGameMetaData();
			if (gameMetaData == null) {
				response = Message.createMessage(CommunicationProtocolValue.ERROR);
				response.setCommunicationError(CommunicationError.REQUIRED_FIELD_NULL_OR_EMPTY);
				response.setErrorInfo("gameMetadata null or empty");
				return response;
			} else {
				gameManager.registerGame(gameName, gameMetaData);
				// register player that initiated the game
				gameManager.getGame(gameName).registerPlayer(players.get(senderID));
			}
		} else if (message.getCommunicationProtocolValue() == CommunicationProtocolValue.DOES_GAME_EXIST) { 
			if (gameManager.getGame(gameName) != null) {
				response = Message.createMessage(CommunicationProtocolValue.GAME_EXISTS);
				return response;
			} else {
				response = Message.createMessage(CommunicationProtocolValue.GAME_DOESNT_EXIST);
				return response;
			}
		} else {
			// For all these communicationProtocolValues game must be created
			GameVer2 game = gameManager.getGame(gameName);
			if (game == null) {
				response = Message.createMessage(CommunicationProtocolValue.GAME_DOESNT_EXIST);
				return response;
			}
			
			if(message.getCommunicationProtocolValue() == CommunicationProtocolValue.REGISTER_PLAYER) {
				// registering second player for game
				if (!gameManager.getGame(gameName).isInitialized()) {
					gameManager.getGame(gameName).registerPlayer(players.get(senderID));
				} else {
					response = Message.createMessage(CommunicationProtocolValue.ERROR);
					response.setCommunicationError(CommunicationError.GAME_ALREADY_OCCUPIED);
					return response;
				}		
			}  else if (message.getCommunicationProtocolValue() == CommunicationProtocolValue.GET_GAME_METADATA) {
				response = Message.createMessage(CommunicationProtocolValue.GAME_METADATA);
				response.setGameMetaData(game.getGameMetaData());
				return response;
			} else if (message.getCommunicationProtocolValue() == CommunicationProtocolValue.GET_GRID_REPRESENTATION) {
				
			}
		}
		
		// TODO
		return null;
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
