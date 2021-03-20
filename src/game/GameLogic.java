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
			response = Message.createMessage(gameName, CommunicationProtocolValue.ERROR);
			response.setCommunicationError(CommunicationError.REQUIRED_FIELD_NULL_OR_EMPTY);
			response.setErrorInfo("Field communicationProtocolValue can not be null.");
			return response;
		} else if (message.getCommunicationProtocolValue() == CommunicationProtocolValue.CREATE_GAME) {
			synchronized (gameManager) {
				if (gameManager.doesGameExist(gameName)) {
					response = Message.createMessage(gameName, CommunicationProtocolValue.ERROR);
					response.setCommunicationError(CommunicationError.GAME_ALREADY_EXIST);
					return response;
				}
				
				GameMetaData gameMetaData = message.getGameMetaData();
				if (gameMetaData == null) {
					response = Message.createMessage(gameName, CommunicationProtocolValue.ERROR);
					response.setCommunicationError(CommunicationError.REQUIRED_FIELD_NULL_OR_EMPTY);
					response.setErrorInfo("gameMetadata null or empty");
					return response;
				} else {
					gameManager.registerGame(gameName, gameMetaData);
					// register player that initiated the game
					gameManager.getGame(gameName).registerPlayer(senderPlayer);
				}
			}
		} else if (message.getCommunicationProtocolValue() == CommunicationProtocolValue.DOES_GAME_EXIST) { 
			if (gameManager.getGame(gameName) != null) {
				response = Message.createMessage(gameName, CommunicationProtocolValue.GAME_EXISTS);
				return response;
			} else {
				response = Message.createMessage(gameName, CommunicationProtocolValue.GAME_DOESNT_EXIST);
				return response;
			}
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
					if (!game.isInitialized()) {
						game.registerPlayer(senderPlayer);
						InnerPlayerRepresentation startingPlayer = game.getTurn();
						response = Message.createMessage(gameName, CommunicationProtocolValue.PLAY_FIRT_MOVE);
						
						if (senderPlayer.equals(startingPlayer)) {
							return response;
						} else {
							try {
								callbacks.get(startingPlayer).sendMessage(response);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						
					} else {
						response = Message.createMessage(gameName, CommunicationProtocolValue.ERROR);
						response.setCommunicationError(CommunicationError.GAME_ALREADY_OCCUPIED);
						return response;
					}		
				}  else if (message.getCommunicationProtocolValue() == CommunicationProtocolValue.GET_GAME_METADATA) {
					response = Message.createMessage(gameName, CommunicationProtocolValue.GAME_METADATA);
					response.setGameMetaData(game.getGameMetaData());
					return response;
				} else if (message.getCommunicationProtocolValue() == CommunicationProtocolValue.GET_GRID_REPRESENTATION) {
					byte[][] grid = game.getGridRepresentation(senderPlayer);
					response = Message.createMessage(gameName, CommunicationProtocolValue.GRID_REPRESENTATION);
					response.setGrid(grid);
					return response;
				} else if (message.getCommunicationProtocolValue() == CommunicationProtocolValue.MY_MOVE) {
					Move mv = message.getMove();
					
					if (mv == null) {
						response = Message.createMessage(gameName, CommunicationProtocolValue.ERROR);
						response.setCommunicationError(CommunicationError.REQUIRED_FIELD_NULL_OR_EMPTY);
						return response;
					} else if (!game.getTurn().equals(senderPlayer)) {
						response = Message.createMessage(gameName, CommunicationProtocolValue.MOVE_RESULT);
						response.setMoveResult(MoveResult.NOT_YOUR_TURN);
						return response;
					} else if (!game.verifyCooInBoundaries(message.getMove().getRow(), message.getMove().getColumn())) {
						response = Message.createMessage(gameName, CommunicationProtocolValue.MOVE_RESULT);
						response.setMoveResult(MoveResult.MOVE_OUT_OF_RANGE);
						return response;
					} else if (!game.isSquareEmpty(message.getMove().getRow(), message.getMove().getColumn())) {
						response = Message.createMessage(gameName, CommunicationProtocolValue.MOVE_RESULT);
						response.setMoveResult(MoveResult.ALREADY_FILLED_UP_SQUARE);
						return response;
					} else {
						// everything about move checked
						game.insert(message.getMove(), senderPlayer);
						// check if game over
						GameState gameState = game.getGameState();
						if (gameState == GameState.GAME_OVER) {
							InnerPlayerRepresentation winner = game.getWinner();
							InnerPlayerRepresentation opponent = game.getOpponent(senderPlayer);
							// deleting finished game
							gameManager.deleteGame(gameName);
							
							response = Message.createMessage(gameName, CommunicationProtocolValue.GAME_OVER);
							Message messageForOpponent = Message.createMessage(gameName, CommunicationProtocolValue.GAME_OVER);
							messageForOpponent.setMove(message.getMove()); // sending last move to opponent
							
							if (winner != null) {
								response.setGameResult(GameResult.YOU_WIN);
								messageForOpponent.setGameResult(GameResult.YOU_LOSE);
							} else {
								response.setGameResult(GameResult.TIE);
								messageForOpponent.setGameResult(GameResult.TIE);
							}
							
							try {
								callbacks.get(opponent).sendMessage(message);
							} catch (IOException e) {
								e.printStackTrace();
							}
							
							return response;
						} else {
							// game is not over
							InnerPlayerRepresentation opponent = game.getOpponent(senderPlayer);
							Message messageForOpponent = Message.createMessage(gameName, CommunicationProtocolValue.OPPONENTS_MOVE);
							messageForOpponent.setMove(message.getMove());
							
							try {
								callbacks.get(opponent).sendMessage(message);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				} else {
					throw new UnsupportedOperationException("message's CommunicationProtocolValue: " + message.getCommunicationProtocolValue());
				}
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
	
	public void unregisterPlayer(int playersID) {
		// TODO has to delete player from players, delete his callback and end all game he is participating in
	}
	
}
