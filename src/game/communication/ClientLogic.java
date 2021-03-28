package game.communication;

import custom_exceptions.WrongMessageException;
import game.GameLogic;
import game.GameMetaData;
import game.communication.enums.CommunicationProtocolValue;
import game.communication.enums.GameResult;
import game.communication.enums.MoveResult;
import game.communication.local.LocalPlayerCallback;
import game_components.Grid;
import game_components.ValuedMove;
import game_components.Square.SVal;
import players.Player;
import players.ui_players.UIPlayer;

public class ClientLogic {


	private final Grid grid;
	private final int streakLength;
	private GameLogic gameLogic;
	private Player player = null;
	private String gameName;
	private int playersID;
	
	public ClientLogic(GameMetaData metaData,  GameLogic gameLogic, String gameName, int playersID) {
		grid = new Grid(metaData.getGridSize());
		streakLength = metaData.getStreakLength();
		this.gameLogic = gameLogic;
		this.gameName = gameName;
		this.playersID = playersID;
	}
	
	/**
	 * 
	 * @param message
	 * @return true if game over
	 */
	public boolean processMessage(Message message) {
		if(!MessageConsistencyChecker.checkServerMessage(message)) {
			throw new WrongMessageException();
		}
		
		logMessageDebug("A", message); // debug	
		
		CommunicationProtocolValue comVal = message.getCommunicationProtocolValue();
		if (comVal == CommunicationProtocolValue.PLAY_FIRT_MOVE || 
				comVal == CommunicationProtocolValue.OPPONENTS_MOVE) {
			
			if (comVal == CommunicationProtocolValue.PLAY_FIRT_MOVE) {
				player = new UIPlayer(SVal.CROSS, "someName" + playersID);
			} else if (comVal == CommunicationProtocolValue.OPPONENTS_MOVE) {
				if (player == null) {
					player = new UIPlayer(SVal.CIRCLE, "someName" + playersID);
				}
				
				Move opponentsMove = message.getMove();
				grid.insert(opponentsMove.getRow(), opponentsMove.getColumn(), SVal.getOpposite(player.getSVal()));
			}
			ValuedMove valMv = player.nextMove(grid);
			Move mv = new Move(valMv.getRow(), valMv.getColumn());
			
			Message messageForGL = Message.createMessage(gameName, CommunicationProtocolValue.MY_MOVE);
			messageForGL.setMove(mv);
			logMessageDebug("B", messageForGL); // debug
			Message m = gameLogic.receiveMessage(playersID, messageForGL);
			logMessageDebug("C", m); // debug
			if (m.getCommunicationProtocolValue() == CommunicationProtocolValue.MOVE_RESULT &&
					m.getMoveResult() == MoveResult.SUCCESS) {
				grid.insert(mv.getRow(), mv.getColumn(), player.getSVal());
			} else if (m.getCommunicationProtocolValue() == CommunicationProtocolValue.GAME_OVER){
				printGameResult(m.getGameResult());
				return true;
			}
		} else if (comVal == CommunicationProtocolValue.GAME_OVER) {
			Move opponentsMove = message.getMove();
			grid.insert(opponentsMove.getRow(), opponentsMove.getColumn(), SVal.getOpposite(player.getSVal()));
			return true;
		} else {
			System.out.println("unprocessed CommunicationProtocolValue: " + comVal);
		}
		
		return false;
	}
	
	private void printGameResult(GameResult gameResult) {
		if(gameResult == GameResult.TIE) {
			System.out.println("Game ended by tie.");
		} else if (gameResult == GameResult.YOU_WIN) {
			System.out.println("Player " + player.getSVal() + " wins.");
		} else {
			System.out.println("Player " + SVal.getOpposite(player.getSVal()) + " wins.");
		}
	}
	
	private void logMessageDebug(String identifier, Message message) {
		System.out.println(playersID + " " + identifier + " " + message);
	}
	
}
