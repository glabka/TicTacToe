package web.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

import custom_exceptions.FullOpponentsException;
import custom_exceptions.SessionIsNotAmongThisOpponentsInstance;
import web.GameMessage;
import web.GameState;

public class WebServerLogic {

	private Map<String, WebPlayer> players = Collections.synchronizedMap(new HashMap<String, WebPlayer>());
	private Map<String, OnlineGame> games = Collections.synchronizedMap(new HashMap<String, OnlineGame>()); // game name -> Players
	
	public List<AddressedGameMessage> getResponse(AddressedGameMessage addressedGameMessage) {
		String senderID = addressedGameMessage.getWebPlayerID();
		WebPlayer senderWebPlayer = players.get(senderID);
		GameMessage gameMessage = addressedGameMessage.getGameMessage();
		
		// new player
		if (senderWebPlayer == null) {
			WebPlayer newWebPlayer = new WebPlayer(senderID);
			players.put(senderID, newWebPlayer);
			
			String gameName = gameMessage.getGameName();
			if(!games.containsKey(gameName)) {
				games.put(gameName, new OnlineGame());
			}
			OnlineGame opponents = games.get(gameName);
			if(!opponents.hasOpponents()) {
				opponents.addOpponent(newWebPlayer);
			} else {
				// send error - game with specified name already has two players
				GameMessage response = new GameMessage();
				
			}
		} else {
			
		}
		
		return null; // TODO
	}
	

	/**
	 * 
	 * @param playersID
	 * @return AddressedGameMessage related to end of game in case the end of game is premature
	 */
	public AddressedGameMessage endGame(String playersID) {
		WebPlayer player = players.get(playersID);
		for(String gameName : games.keySet()) {
			if(games.get(gameName).contains(player)) {
				OnlineGame game = games.get(gameName); 
				if(game.getGameState() == GameState.START && game.getGameState() == GameState.IN_GAME) {
					
					return new AddressedGameMessage(game.getOpponent(player), gMessage);
				}
			}
		}
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
	}
}
