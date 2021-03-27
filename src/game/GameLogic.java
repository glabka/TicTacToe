package game;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import custom_exceptions.PlayerAlreadyExistsException;
import custom_exceptions.PlayerNotRegistredException;
import game.communication.IPlayerCallback;
import game.communication.InnerPlayerRepresentation;
import game.communication.Message;
import game.communication.MessageConsistencyChecker;
import game.communication.Move;
import game.communication.enums.CommunicationError;
import game.communication.enums.CommunicationProtocolValue;
import game.communication.enums.GameResult;
import game.communication.enums.MoveResult;

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
		
		InnerPlayerRepresentation sender = players.get(senderID);
		if (sender == null) {
			throw new PlayerNotRegistredException("callback for playerID: " + senderID + "not registered");
		}
		
		response = MessageConsistencyChecker.checkClientMessage(message);
		if (response != null) {
			return response;
		}
		
		String gameName = message.getGameName();
		if (message.getCommunicationProtocolValue() == CommunicationProtocolValue.CREATE_GAME) {
			return tryCreateGame(sender, message);
		} else if (message.getCommunicationProtocolValue() == CommunicationProtocolValue.DOES_GAME_EXIST) { 
			return doesGameExist(message);
		} else {
			// For all these communicationProtocolValues game must be created
			GameVer2 game = gameManager.getGame(gameName);
			if (game == null) {
				response = Message.createMessage(gameName, CommunicationProtocolValue.ERROR);
				response.setCommunicationError(CommunicationError.GAME_DOESNT_EXIST);
				return response;
			}
			
			synchronized (game) {
				if(message.getCommunicationProtocolValue() == CommunicationProtocolValue.REGISTER_PLAYER) {
					// registering second player for game
					return tryRegisterSecondPlayer(sender, game);
				}  else if (message.getCommunicationProtocolValue() == CommunicationProtocolValue.GET_GAME_METADATA) {
					return getGameMetaData(game);
				} else if (message.getCommunicationProtocolValue() == CommunicationProtocolValue.GET_GRID_REPRESENTATION) {
					return getGridRepresentation(sender, game);
				} else if (message.getCommunicationProtocolValue() == CommunicationProtocolValue.MY_MOVE) {
					return tryToPlayMove(sender, game, message.getMove());
				} else {
					throw new UnsupportedOperationException("message's CommunicationProtocolValue: " + message.getCommunicationProtocolValue());
				}
			}
		}
	}
	
	private Message tryCreateGame(InnerPlayerRepresentation sender, Message message) {
		String gameName = message.getGameName();
		synchronized (gameManager) {
			if (gameManager.doesGameExist(gameName)) {
				Message response = Message.createMessage(gameName, CommunicationProtocolValue.ERROR);
				response.setCommunicationError(CommunicationError.GAME_ALREADY_EXIST);
				return response;
			}
			
			GameMetaData gameMetaData = message.getGameMetaData();
			gameManager.registerGame(gameName, gameMetaData);
			// register player that initiated the game
			gameManager.getGame(gameName).registerPlayer(sender);
			return null;
		}
	}
	
	private Message doesGameExist(Message message) {
		Message response;
		String gameName = message.getGameName();
		if (gameManager.getGame(gameName) != null) {
			response = Message.createMessage(gameName, CommunicationProtocolValue.GAME_EXISTS);
			return response;
		} else {
			response = Message.createMessage(gameName, CommunicationProtocolValue.GAME_DOESNT_EXIST);
			return response;
		}
	}
	
	private Message tryRegisterSecondPlayer(InnerPlayerRepresentation sender, GameVer2 game) {
		Message response;
		String gameName = game.getGameName();
		if (!game.isInitialized()) {
			game.registerPlayer(sender);
			InnerPlayerRepresentation startingPlayer = game.getTurn();
			response = Message.createMessage(gameName, CommunicationProtocolValue.PLAY_FIRT_MOVE);
			
			if (sender.equals(startingPlayer)) {
				return response;
			} else {
				try {
					callbacks.get(startingPlayer).sendMessage(response);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;	
		} else {
			response = Message.createMessage(gameName, CommunicationProtocolValue.ERROR);
			response.setCommunicationError(CommunicationError.GAME_ALREADY_OCCUPIED);
			return response;
		}
	}
	
	private Message getGameMetaData(GameVer2 game) {
		Message response = Message.createMessage(game.getGameName(), CommunicationProtocolValue.GAME_METADATA);
		response.setGameMetaData(game.getGameMetaData());
		return response;
	}
	
	private Message getGridRepresentation(InnerPlayerRepresentation sender, GameVer2 game) {
		byte[][] grid = game.getGridRepresentation(sender);
		Message response = Message.createMessage(game.getGameName(), CommunicationProtocolValue.GRID_REPRESENTATION);
		response.setGrid(grid);
		return response;
	}
	
	private Message tryToPlayMove(InnerPlayerRepresentation sender, GameVer2 game, Move mv) {
		Message response;
		String gameName = game.getGameName();
		if (!game.getTurn().equals(sender)) {
			response = Message.createMessage(gameName, CommunicationProtocolValue.MOVE_RESULT);
			response.setMoveResult(MoveResult.NOT_YOUR_TURN);
			return response;
		} else if (!game.verifyCooInBoundaries(mv.getRow(), mv.getColumn())) {
			response = Message.createMessage(gameName, CommunicationProtocolValue.MOVE_RESULT);
			response.setMoveResult(MoveResult.MOVE_OUT_OF_RANGE);
			return response;
		} else if (!game.isSquareEmpty(mv.getRow(), mv.getColumn())) {
			response = Message.createMessage(gameName, CommunicationProtocolValue.MOVE_RESULT);
			response.setMoveResult(MoveResult.ALREADY_FILLED_UP_SQUARE);
			return response;
		} else {
			// everything about move checked
			game.insert(mv, sender);
			// check if game over
			GameState gameState = game.getGameState();
			if (gameState == GameState.GAME_OVER) {
				InnerPlayerRepresentation winner = game.getWinner();
				InnerPlayerRepresentation opponent = game.getOpponent(sender);
				// deleting finished game
				gameManager.deleteGame(gameName);
				
				response = Message.createMessage(gameName, CommunicationProtocolValue.GAME_OVER);
				response.setMoveResult(MoveResult.SUCCESS);
				Message messageForOpponent = Message.createMessage(gameName, CommunicationProtocolValue.GAME_OVER);
				messageForOpponent.setMove(mv); // sending last move to opponent
				
				if (winner != null) {
					response.setGameResult(GameResult.YOU_WIN);
					messageForOpponent.setGameResult(GameResult.YOU_LOSE);
				} else {
					response.setGameResult(GameResult.TIE);
					messageForOpponent.setGameResult(GameResult.TIE);
				}
				
				try {
					callbacks.get(opponent).sendMessage(messageForOpponent);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return response;
			} else {
				// game is not over
				InnerPlayerRepresentation opponent = game.getOpponent(sender);
				Message messageForOpponent = Message.createMessage(gameName, CommunicationProtocolValue.OPPONENTS_MOVE);
				messageForOpponent.setMove(mv);
				
				try {
					callbacks.get(opponent).sendMessage(messageForOpponent);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				response = Message.createMessage(gameName, CommunicationProtocolValue.MOVE_RESULT);
				response.setMoveResult(MoveResult.SUCCESS);
				return response;
				
			}
		}
	}
	
	public void registerPlayersCallback(int playersID, IPlayerCallback playersCallback) {
		if (players.containsKey(playersID)) {
			throw new PlayerAlreadyExistsException("playersID: " + playersID);
		}
		InnerPlayerRepresentation newPlayer = new InnerPlayerRepresentation(playersID);
		players.put(playersID, newPlayer);
		callbacks.put(newPlayer, playersCallback);
	}
	
	public void unregisterPlayer(int playersID) {
		// TODO has to delete player from players, delete his callback and end all game he is participating in
	}
	
}
