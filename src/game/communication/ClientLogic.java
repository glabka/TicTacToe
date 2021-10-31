package game.communication;

import java.io.IOException;

import custom_exceptions.WrongMessageException;
import game.GameLogic;
import game.GameMetaData;
import game.communication.enums.CommunicationError;
import game.communication.enums.CommunicationProtocolValue;
import game.communication.enums.GameResult;
import game.communication.enums.MoveResult;
import game.communication.local.LocalPlayerCallback;
import game_components.Grid;
import game_components.ValuedMove;
import game_components.Square.SVal;
import players.Player;
import players.UIPlayer;

public class ClientLogic {


	private final Grid grid;
	private final int streakLength;
	private IGameLogicCallback gameLogicCallback;
	private Player player = null;
	private String gameName;
	private int playersID;
	
	public ClientLogic(GameMetaData metaData,  IGameLogicCallback gameLogicCallback, String gameName, int playersID) {
		grid = new Grid(metaData.getGridSize());
		streakLength = metaData.getStreakLength();
		this.gameLogicCallback = gameLogicCallback;
		this.gameName = gameName;
		this.playersID = playersID;
	}
	
	public void tryRegisterGameAndPlayer(GameMetaData metaData) {
		Message messageForGL = Message.createMessage(gameName, CommunicationProtocolValue.CREATE_GAME);
		messageForGL.setGameMetaData(metaData);
		try {
			gameLogicCallback.sendMessage(messageForGL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void tryRegisterAsSecondPlayer() {
		Message messageForGL = Message.createMessage(gameName, CommunicationProtocolValue.REGISTER_PLAYER);
		try {
			gameLogicCallback.sendMessage(messageForGL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param message
	 * @return true if game over or player even wasnt connected
	 */
	public boolean processMessage(Message message) {
		if(!MessageConsistencyChecker.checkServerMessage(message)) {
			throw new WrongMessageException();
		}
		
		logMessageDebug("A", message); // debug	
		
		CommunicationProtocolValue comVal = message.getCommunicationProtocolValue();
		// processing of attempt to register game and player
		if (comVal== CommunicationProtocolValue.GAME_CREATED) {
			return false;
		} else if (comVal== CommunicationProtocolValue.ERROR &&
				message.getCommunicationError() == CommunicationError.GAME_ALREADY_EXISTS) {
			// register as second player
			tryRegisterAsSecondPlayer();
		} else if (comVal == CommunicationProtocolValue.ERROR &&
				message.getCommunicationError() == CommunicationError.GAME_ALREADY_OCCUPIED) {
			return true;
		// processing of in game message
		} else if (comVal == CommunicationProtocolValue.PLAY_FIRT_MOVE || 
				comVal == CommunicationProtocolValue.OPPONENTS_MOVE) {
			
			if (comVal == CommunicationProtocolValue.PLAY_FIRT_MOVE) {
				player = new UIPlayer(SVal.CROSS, "someName" + playersID);
			} else if (comVal == CommunicationProtocolValue.OPPONENTS_MOVE) {
				if (player == null) {
					player = new UIPlayer(SVal.CIRCLE, "someName" + playersID);
				}
				
				Move opponentsMove = message.getMove();
				grid.insert(opponentsMove.getRow(), opponentsMove.getColumn(), SVal.getOpposite(player.getSVal()));
				grid.setCursonRow(opponentsMove.getRow());
				grid.setCursonColumn(opponentsMove.getColumn());
			}
			ValuedMove valMv = player.nextMove(grid);
			Move mv = new Move(valMv.getRow(), valMv.getColumn());
			
			Message messageForGL = Message.createMessage(gameName, CommunicationProtocolValue.MY_MOVE);
			messageForGL.setMove(mv);
			logMessageDebug("B", messageForGL); // debug
			try {
				gameLogicCallback.sendMessage(messageForGL);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (comVal == CommunicationProtocolValue.GAME_OVER) {
			Move lastMove = message.getMove();
			if (message.getMoveResult() == MoveResult.SUCCESS) {
				// it is your move that was the last
				grid.insert(lastMove.getRow(), lastMove.getColumn(), player.getSVal());
			} else {
				// message.getMoveResult() == null -> opponents move 
				grid.insert(lastMove.getRow(), lastMove.getColumn(), SVal.getOpposite(player.getSVal()));
			}
			return true;
		} else if (comVal == CommunicationProtocolValue.MOVE_RESULT &&
				message.getMoveResult() == MoveResult.SUCCESS) {
			Move mv = message.getMove();
			grid.insert(mv.getRow(), mv.getColumn(), player.getSVal());
		}  else {
			System.out.println("unprocessed CommunicationProtocolValue: " + comVal);
		}
		
		return false;
	}
	
	public Grid getGrid() {
		return grid.gridsCopy();
	}
	
	private void logMessageDebug(String identifier, Message message) {
		System.out.println(playersID + " " + identifier + " " + message);
	}
	
}
