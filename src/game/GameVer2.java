package game;

import java.util.Arrays;

import custom_exceptions.FullOpponentsException;
import custom_exceptions.IllegalPlayerException;
import custom_exceptions.IndexesOutOfRangeException;
import custom_exceptions.OnlineGameNotInitialized;
import custom_exceptions.PlayersWithSameSValException;
import custom_exceptions.SessionIsNotAmongThisOpponentsInstance;
import game.communication.InnerPlayerRepresentation;
import game_components.Grid;
import game_components.Square.SVal;
import game_mechanics.Rules;

public class GameVer2 {
	private final String gameName;
	private GameState gameState = GameState.BEGINNING;
	private final InnerPlayerRepresentation[] opponents = new InnerPlayerRepresentation[2];
	private InnerPlayerRepresentation turn;
	private final GameMetaData gameMetaData;
	private final Grid grid;
	
	public GameVer2(String gameName, GameMetaData gameMetaData) {
		this.gameName = gameName;
		this.gameMetaData = gameMetaData;
		this.grid = new Grid(gameMetaData.getGridSize());
	}
	
	public void insert(int row, int column, InnerPlayerRepresentation player) {
		grid.insert(row, column, player.getSVal());
		if (gameState == GameState.BEGINNING) {
			gameState = GameState.IN_GAME;
		} else if (gameState == GameState.IN_GAME && (Rules.endOfGame(grid, gameMetaData.getStreakLength()))) {
			gameState = GameState.GAME_OVER;
		}
	}
	
	public InnerPlayerRepresentation getWinner() {
		SVal winnerVal = Rules.findWinner(grid, gameMetaData.getStreakLength());
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

	
	public void registerPlayer(InnerPlayerRepresentation iPlayerRepresentation) {
		if(opponents[0] == null) {
			opponents[0] = iPlayerRepresentation;
		} else if (opponents[1] == null) {
			if (opponents[0].getSVal() == iPlayerRepresentation.getSVal()) {
				throw new PlayersWithSameSValException();
			}
			opponents[1] = iPlayerRepresentation;
		} else {
			throw new FullOpponentsException();
		}
	}
	
	public InnerPlayerRepresentation getOpponent(InnerPlayerRepresentation iPlayerRepresentation) {
		if(opponents[0].equals(iPlayerRepresentation)) {
			return opponents[1];
		} else if (opponents[1].equals(iPlayerRepresentation)) {
			return opponents[0];
		} else {
			throw new SessionIsNotAmongThisOpponentsInstance();
		}
	}
	
	public boolean hasOpponents() {
		return opponents[0] != null && opponents[1] != null;
	}
	
	public boolean contains(InnerPlayerRepresentation iPlayerRepresentation) {
		return opponents[0].equals(iPlayerRepresentation) || opponents[1].equals(iPlayerRepresentation);
	}
	
	public boolean removePlayer(InnerPlayerRepresentation iPlayerRepresentation) {
		if(opponents[0].equals(iPlayerRepresentation)) {
			opponents[0] = null;
			return true;
		} else if (opponents[1].equals(iPlayerRepresentation)){
			opponents[1] = null;
			return true;
		} else {
			return false;
		}
	}
	
	public String getGameName() {
		return gameName;
	}

	public InnerPlayerRepresentation[] getPlayers() {
		return Arrays.copyOf(opponents, opponents.length);
	}
	
	public void nextTurn() {
		if(!isInitialized()) {
			throw new OnlineGameNotInitialized();
		}
		turn = getOpponent(this.turn);
	}
	
	public InnerPlayerRepresentation getTurn() {
		if(!isInitialized()) {
			throw new OnlineGameNotInitialized();
		}
		return turn;
	}
	
	public void setStartingPlayer(InnerPlayerRepresentation iPlayerRepresentation) {
		if (opponents[0] != iPlayerRepresentation && opponents[1] != iPlayerRepresentation) {
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

	public GameMetaData getGameMetaData() {
		return gameMetaData;
	}
	

}
