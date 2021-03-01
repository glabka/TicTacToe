package web.server;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import custom_exceptions.FullOpponentsException;
import custom_exceptions.SessionIsNotAmongThisOpponentsInstance;
import web.CommunicationError;
import web.Message;
import web.GameState;

public class WebServerLogic {

	private Map<String, WebPlayer> players = Collections.synchronizedMap(new HashMap<String, WebPlayer>());
	private Map<String, OnlineGame> games = Collections.synchronizedMap(new HashMap<String, OnlineGame>()); // game name -> Players
	
	public List<AddressedMessage> getResponse(AddressedMessage addressedGameMessage) {
		List<AddressedMessage> responses = new LinkedList<>();
		
		String senderID = addressedGameMessage.getWebPlayerID();
		WebPlayer senderWebPlayer = players.get(senderID);
		Message receivedMessage = addressedGameMessage.getGameMessage();
		Message responseMessage;
		AddressedMessage response;
		
		// new player
		if (senderWebPlayer == null) {
			senderWebPlayer = new WebPlayer(senderID);
			players.put(senderID, senderWebPlayer);
		}
		
		String gameName = receivedMessage.getGameName();
		// new game
		if (!games.containsKey(gameName)) {
			games.put(gameName, new OnlineGame());
		}
		
		// adding players to game
		OnlineGame game = games.get(gameName);
		if (!game.hasOpponents()) {
			game.addOpponent(senderWebPlayer);
		} else {
			// send error - game with specified name already has two players
			responseMessage = Message.createMessage(gameName, GameState.GENRAL_ERROR);
			responseMessage.setCommunicationError(CommunicationError.GAME_ALREADY_FULL);
			response = new AddressedMessage(senderID, responseMessage);
			responses.add(response);
			return responses;
		}
		
		// beginning of game
		if (receivedMessage.getGameState() == GameState.START && game.hasOpponents()) {
			// choosing random player to start
			WebPlayer[] players = game.getPlayers();
			WebPlayer beginningPlayer;
			
			if (new Random().nextBoolean()) {
				beginningPlayer = players[0];
			} else {
				beginningPlayer = players[1];
			}
			
			responseMessage = Message.createMessage(gameName, GameState.START);
			response = new AddressedMessage(beginningPlayer.getId(), responseMessage);
			responses.add(response);
			return responses;
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
		WebPlayer player = players.get(playersID);
		
		for(String gameName : games.keySet()) {
			if(games.get(gameName).contains(player)) {
				OnlineGame game = games.get(gameName);
				
				// response for opponent
				if(game.getGameState() == GameState.START && game.getGameState() == GameState.IN_GAME) {
					Message endGameMessage = Message.createMessage(gameName, GameState.PREMATURE_END_ERROR);
					responses.add(new AddressedMessage(game.getOpponent(player).getId(), endGameMessage));
				}
			
				games.remove(gameName);
			}
		}
		
		players.remove(playersID);
		return responses;
	}
	
	private class OnlineGame {
		
		private GameState gameState = GameState.START;
		private WebPlayer[] opponents = new WebPlayer[2];
		
		public GameState getGameState() {
			return gameState;
		}
		
		public void addOpponent(WebPlayer wPlayer) {
			if(opponents[0] == null) {
				opponents[0] = wPlayer;
			} else if (opponents[1] == null) {
				opponents[1] = wPlayer;
			} else {
				throw new FullOpponentsException();
			}
		}
		
		public WebPlayer getOpponent(WebPlayer wPlayer) {
			if(opponents[0].equals(wPlayer)) {
				return opponents[1];
			} else if (opponents[1].equals(wPlayer)) {
				return opponents[0];
			} else {
				throw new SessionIsNotAmongThisOpponentsInstance();
			}
		}
		
		public boolean hasOpponents() {
			return opponents[0] != null && opponents[1] != null;
		}
		
		public boolean contains(WebPlayer webPlayer) {
			return opponents[0].equals(webPlayer) || opponents[1].equals(webPlayer);
		}
		
		public boolean removePlayer(WebPlayer webPlayer) {
			if(opponents[0].equals(webPlayer)) {
				opponents[0] = null;
				return true;
			} else if (opponents[1].equals(webPlayer)){
				opponents[1] = null;
				return true;
			} else {
				return false;
			}
		}
		
		public WebPlayer[] getPlayers() {
			return Arrays.copyOf(opponents, opponents.length);
		}
	}
}
