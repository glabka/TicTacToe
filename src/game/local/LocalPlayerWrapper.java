package game.local;

import java.util.List;
import java.util.Random;

import custom_exceptions.WrongMessageException;
import game.GameLogic;
import game.GameMetaData;
import game.communication.Message;
import game.communication.MessageConsistencyChecker;
import game.communication.Move;
import game.communication.enums.CommunicationProtocolValue;
import game.communication.local.LocalPlayerCallback;
import game_components.Grid;
import game_components.Square.SVal;
import game_components.ValuedMove;
import players.Player;
import players.ui_players.UIPlayer;

public class LocalPlayerWrapper extends Thread {

	private final Grid grid;
	private final int streakLength;
	private Object lock; // lock for waiting
	private GameLogic gameLogic;
	private LocalPlayerCallback myCallback;
	private Player player = null;
	private String gameName;
	private int playersID;
	
	public LocalPlayerWrapper(GameMetaData metaData, Object lock, GameLogic gameLogic,
LocalPlayerCallback myCallBack, String gameName, int playersID) {
		grid = new Grid(metaData.getGridSize());
		streakLength = metaData.getStreakLength();
		this.lock = lock;
		this.gameLogic = gameLogic;
		this.myCallback = myCallBack;
		this.gameName = gameName;
		this.playersID = playersID;
	}
	
	@Override
	public void run() {
		synchronized (lock) {
			outerloop:
			while(true) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
					continue;
				}
				Move mv = null;
				List<Message> messages = myCallback.getMessages();
				for (Message message : messages) {
					if(!MessageConsistencyChecker.checkMessage(message)) {
						throw new WrongMessageException();
					}
					CommunicationProtocolValue comVal = message.getCommunicationProtocolValue();
					if (comVal == CommunicationProtocolValue.PLAY_FIRT_MOVE) {
						Random rand = new Random();
						player = new UIPlayer(SVal.CROSS, "someName" + rand.nextInt());
					} else if (comVal == CommunicationProtocolValue.OPPONENTS_MOVE) {
						Move opponentsMove = message.getMove();
						grid.insert(opponentsMove.getRow(), opponentsMove.getColumn(), SVal.getOpposite(player.getSVal()));
					} else if (comVal == CommunicationProtocolValue.GAME_OVER) {
						Move opponentsMove = message.getMove();
						grid.insert(opponentsMove.getRow(), opponentsMove.getColumn(), SVal.getOpposite(player.getSVal()));
						break outerloop;
					}
					ValuedMove valMv = player.nextMove(grid);
					mv = new Move(valMv.getRow(), valMv.getColumn());
				}
				
				Message message = Message.createMessage(gameName, CommunicationProtocolValue.MY_MOVE);
				message.setMove(mv);
				gameLogic.receiveMessage(playersID, message);
			}
			System.out.println("Game Over");
		}
	}
	
}
