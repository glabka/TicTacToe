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
import game_components.Grid;
import game_components.Square.SVal;
import game_mechanics.Rules;
import web.CommunicationSignal;
import web.Message;
import web.Move;
import web.GameState;

public class WebServerLogic {

	private Map<String, WebPlayer> players = Collections.synchronizedMap(new HashMap<String, WebPlayer>());
	private Map<String, OnlineGame> games = Collections.synchronizedMap(new HashMap<String, OnlineGame>());
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public List<AddressedMessage> getResponse(AddressedMessage addressedGameMessage) {
		List<AddressedMessage> responses = new LinkedList<>();
		CommunicationSignal error = null;
		
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
			
			games.put(gameName, new OnlineGame(gameName, receivedMessage.getGridSize(), receivedMessage.getStreakLength()));
		}
		
		// adding players to game
		OnlineGame game = games.get(gameName);
		if (!game.hasOpponents()) {
			game.addOpponent(senderWebPlayer);
		} else {
			// send error - game with specified name already has two players
			responseMessage = Message.createMessage(gameName, CommunicationSignal.GAME_ALREADY_FULL);
			response = new AddressedMessage(senderID, responseMessage);
			responses.add(response);
			return responses;
		}
		
		// beginning of game
		if (receivedMessage.getCommunicationSignal() == CommunicationSignal.START && game.hasOpponents()
				&& game.getGameState() == GameState.START) {
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
			WebPlayer[] players = game.getPlayers();
			WebPlayer beginningPlayer;
			
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
	
	
	private AddressedMessage processMove(WebPlayer wPlayer, OnlineGame game, Move mv) {
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
		} else if ()
		
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
					Message endGameMessage = Message.createMessage(gameName, CommunicationSignal.PREMATURE_END_ERROR);
					responses.add(new AddressedMessage(game.getOpponent(player).getId(), endGameMessage));
				}
			
				games.remove(gameName);
			}
		}
		
		players.remove(playersID);
		return responses;
	}
	
	private class OnlineGame {
		
		private String gameName;
		private GameState gameState = GameState.START;
		private WebPlayer[] opponents = new WebPlayer[2];
		private WebPlayer turn;
		private Grid grid;
		private int streakLength;
		
		public OnlineGame(String gameName, int gridSize, int streakLength) {
			this.gameName = gameName;
			this.grid = new Grid(gridSize);
			this.streakLength = streakLength;
		}
		
		public void insert(int row, int column, SVal val) {
			grid.insert(row, column, val);
			if (gameState == GameState.START) {
				gameState = GameState.IN_GAME;
			} else if (gameState == GameState.IN_GAME && (Rules.endOfGame(grid, streakLength))) {
				gameState = GameState.GAME_OVER;
			}
		}
		
		public WebPlayer getWinner() {
			SVal winnerVal = Rules.findWinner(grid, streakLength);
			if(winnerVal == opponents[0].getSVal()) {
				return opponents[0];
			} else if (winnerVal == opponents[1].getSVal()){
				return opponents[1];
			} else {
				return null;
			}
		}
		
		public boolean areCoosInRange(int row, int column) {
			if(row >= 0 && row < grid.size() && column >= 0 && column < grid.size()) {
				return true;
			} else {
				return false;
			}
		}
		
		public boolean isSquareEmpty(int row, int column) {
			if(areCoosInRange(row, column)) {
				return grid.isSquareEmpty(row, column);
			} else {
				throw new IndexesOutOfRangeException("row: " + row + ", column: " + column + "but should be 0 <= and < grid.size(): " + grid.size());
			}
		}
		
		public GameState getGameState() {
			return gameState;
		}

		
		public void addOpponent(WebPlayer wPlayer) {
			if(opponents[0] == null) {
				opponents[0] = wPlayer;
			} else if (opponents[1] == null) {
				if (opponents[0].getSVal() == wPlayer.getSVal()) {
					throw new PlayersWithSameSValException();
				}
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
		
		public String getGameName() {
			return gameName;
		}

		public WebPlayer[] getPlayers() {
			return Arrays.copyOf(opponents, opponents.length);
		}
		
		public void nextTurn() {
			if(!isInitialized()) {
				throw new OnlineGameNotInitialized();
			}
			turn = getOpponent(this.turn);
		}
		
		public WebPlayer getTurn() {
			if(!isInitialized()) {
				throw new OnlineGameNotInitialized();
			}
			return turn;
		}
		
		public void setStartingPlayer(WebPlayer webPlayer) {
			if (opponents[0] != webPlayer && opponents[1] != webPlayer) {
				throw new IllegalPlayerException();
			}
		}
		
		private boolean isInitialized() {
			if(turn == null) {
				return false;
			} else if (opponents[0] == null) {
				return false;
			} else if (opponents[1] == null) {
				return false;
			}
			return true;
		}

		public int getStreakLength() {
			return streakLength;
		}
		
		public int getGridSize() {
			return grid.size();
		}
		
	}
}
