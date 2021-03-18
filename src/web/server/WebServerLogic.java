package web.server;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import custom_exceptions.FullOpponentsException;
import custom_exceptions.IllegalPlayerException;
import custom_exceptions.IndexesOutOfRangeException;
import custom_exceptions.OnlineGameNotInitialized;
import custom_exceptions.PlayersWithSameSValException;
import custom_exceptions.SessionIsNotAmongThisOpponentsInstance;
import game.GameState;
import game.communication.Message;
import game.communication.Move;
import game.communication.InnerPlayerRepresentation;
import game_components.Grid;
import game_components.Square.SVal;
import game_mechanics.Rules;
import web.CommunicationSignal;

public class WebServerLogic {

	private Map<String, InnerPlayerRepresentation> players = Collections.synchronizedMap(new HashMap<String, InnerPlayerRepresentation>());
	private Map<String, OnlineGame> games = Collections.synchronizedMap(new HashMap<String, OnlineGame>());
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public List<AddressedMessage> getResponse(AddressedMessage addressedGameMessage) {
		List<AddressedMessage> responses = new LinkedList<>();
		CommunicationSignal error = null;
		
		String senderID = addressedGameMessage.getWebPlayerID();
		InnerPlayerRepresentation senderWebPlayer = players.get(senderID);
		Message receivedMessage = addressedGameMessage.getGameMessage();
		Message responseMessage;
		AddressedMessage response;
		
		// new player
		if (senderWebPlayer == null) {
			senderWebPlayer = new InnerPlayerRepresentation(senderID);
			players.put(senderID, senderWebPlayer);
		}
		
		String gameName = receivedMessage.getGameName();
		// new game
		if (!games.containsKey(gameName)) {
			if(receivedMessage.getGridSize() < 3) {
				error = CommunicationSignal.GRID_SIZE_TOO_SMALL;
			} else if (receivedMessage.getStreakLength() > receivedMessage.getGridSize()) {
				error = CommunicationSignal.STREAK_LENGTH_BIGGER_THAN_GRID;
			} else if (receivedMessage.getStreakLength() < 3) {
				error = CommunicationSignal.STREAK_LENGTH_TOO_SMALL;
			} else if (receivedMessage.getGridSize() > 100) {
				error = CommunicationSignal.GRID_SIZE_TOO_BIG;
			}
			
			if (error != null) {
				responseMessage = Message.createMessage(gameName, error);
				responses.add(new AddressedMessage(senderWebPlayer.getId(), responseMessage));
				return responses;
			}
			
			games.put(gameName, new OnlineGame(gameName, receivedMessage.getGameMataData()));
		}
		
		// adding players to game
		OnlineGame game = games.get(gameName);
		if (!game.hasOpponents()) {
			game.registerPlayer(senderWebPlayer);
		} else {
			// send error - game with specified name already has two players
			responseMessage = Message.createMessage(gameName, CommunicationSignal.GAME_ALREADY_FULL);
			response = new AddressedMessage(senderID, responseMessage);
			responses.add(response);
			return responses;
		}
		
		// beginning of game
		if (receivedMessage.getCommunicationSignal() == CommunicationSignal.START && game.hasOpponents()
				&& game.getGameState() == GameState.BEGINNING) {
			// verifying player has right grid size and streak length
			
			if (game.getGridSize() != receivedMessage.getGridSize()) {
				error = CommunicationSignal.WRONG_GRID_SIZE;
			} else if (game.getStreakLength() == receivedMessage.getStreakLength()) {
				error = CommunicationSignal.WRONG_STREAK_LENGTH;
			}
			
			if (error != null) {
				responseMessage = Message.createMessage(gameName, error);
				responses.add(new AddressedMessage(senderWebPlayer.getId(), responseMessage));
				return responses;
			}
			
			// choosing random player to start
			InnerPlayerRepresentation[] players = game.getPlayers();
			InnerPlayerRepresentation beginningPlayer;
			
			if (new Random().nextBoolean()) {
				beginningPlayer = players[0];
			} else {
				beginningPlayer = players[1];
			}
			beginningPlayer.setSVal(SVal.CROSS);
			game.getOpponent(beginningPlayer).setSVal(SVal.CIRCLE);
			game.setStartingPlayer(beginningPlayer);
			
			responseMessage = Message.createMessage(gameName, CommunicationSignal.START);
			responseMessage.setGameState(game.getGameState());
			response = new AddressedMessage(beginningPlayer.getId(), responseMessage);
			responses.add(response);
			return responses;
		}
		
		// process other CommunicationSignals from client then
		
		
		// process moves
		Move mv = receivedMessage.getMove();
		
		
		return null; // TODO
	}
	
	
	private AddressedMessage processMove(InnerPlayerRepresentation wPlayer, OnlineGame game, Move mv) {
		Message message = null;
		CommunicationSignal error = null;
		if (game.getTurn() == wPlayer && (game.getGameState() == GameState.IN_GAME ||
				game.getGameState() == GameState.START)) {
			if (game.areCoosInRange(mv.getRow(), mv.getColumn())) {
				if(game.isSquareEmpty(mv.getRow(), mv.getColumn())) {
					game.insert(mv.getRow(), mv.getColumn(), wPlayer.getSVal());
					message = Message.createMessage(game.getGameName(), CommunicationSignal.OPPONENTS_MOVE);
					message.setMove(mv);
				} else {
					error = CommunicationSignal.ALREADY_FILLED_UP_SQUARE;
				}
			} else {
				error = CommunicationSignal.MOVE_OUT_OF_RANGE;
			}
		} else if (game.getTurn() != wPlayer) {
			error = CommunicationSignal.NOT_YOUR_TURN;
		} 
		
		return null; // TODO
	}
	

	/**
	 * Ends games when player prematurely ends communication and returns responses for opponents. Also removes player from server.
	 * @param playersID
	 * @return List<AddressedGameMessage> related to prematurely ended games
	 */
	public List<AddressedMessage> quit(String playersID) {
		List<AddressedMessage> responses = new LinkedList<>();
		InnerPlayerRepresentation player = players.get(playersID);
		
		for(String gameName : games.keySet()) {
			if(games.get(gameName).contains(player)) {
				OnlineGame game = games.get(gameName);
				
				// response for opponent
				if(game.getGameState() == GameState.START && game.getGameState() == GameState.IN_GAME) {
					Message endGameMessage = Message.createMessage(gameName, CommunicationSignal.PREMATURE_END_ERROR);
					responses.add(new AddressedMessage(game.getOpponent(player).getId(), endGameMessage));
				}
			
				games.remove(gameName);
			}
		}
		
		players.remove(playersID);
		return responses;
	}
	
}
