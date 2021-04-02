package game.local;

import java.util.List;
import java.util.Random;

import javax.swing.plaf.synth.SynthToggleButtonUI;

import custom_exceptions.WrongMessageException;
import game.GameLogic;
import game.GameMetaData;
import game.communication.ClientLogic;
import game.communication.IGameLogicCallback;
import game.communication.InnerPlayerRepresentation;
import game.communication.Message;
import game.communication.MessageConsistencyChecker;
import game.communication.Move;
import game.communication.enums.CommunicationError;
import game.communication.enums.CommunicationProtocolValue;
import game.communication.enums.MoveResult;
import game.communication.local.LocalGameLogicCallback;
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
	private GameMetaData metaData;
	private ClientLogic clientLogic;
	private IGameLogicCallback gameLogicCallback;
	
	public LocalPlayerWrapper(GameMetaData metaData, Object lock, GameLogic gameLogic,
LocalPlayerCallback myCallBack, String gameName, int playersID) {
		grid = new Grid(metaData.getGridSize());
		streakLength = metaData.getStreakLength();
		this.lock = lock;
		this.gameLogic = gameLogic;
		this.myCallback = myCallBack;
		this.gameName = gameName;
		this.playersID = playersID;
		this.metaData = new GameMetaData(5, 3);
		gameLogicCallback = new LocalGameLogicCallback(playersID, gameLogic);
		this.clientLogic = new ClientLogic(metaData, gameLogicCallback, gameName, playersID);
	}
	
	@Override
	public void run() {
		synchronized (lock) {
			
			if (!registerGameAndPlayer()) {
				System.out.println("Game with name " + gameName + " was already occupied.");
				return;
			} else {
				System.out.println("playerID: " + playersID + " registered."); // debug
			}
			
			outerloop:
			while(true) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
					continue;
				}
				// this while loop is for case when new message is added during processing of older messages
				while (true) {
					List<Message> messages = myCallback.getMessages();
					if (messages.isEmpty()) {
						break;
					}
					for (Message message : messages) {
						if (clientLogic.processMessage(message)) {
							// Game Over
							break outerloop;
						} else {
							// TODO - maybe nothing
						}
					}
				}
			}
			System.out.println("Game Over");
		}
	}
	
	/**
	 * Method returns true if game was created/exists and player was successfully registered
	 * @return
	 */
	private boolean registerGameAndPlayer() {
		clientLogic.tryRegisterGameAndPlayer(metaData);
		
		Message message = waitForResponse();
		logMessageDebug("Q", message);
		
		if (message.getCommunicationProtocolValue() == CommunicationProtocolValue.GAME_CREATED) {
			return true;
		} else if (message.getCommunicationProtocolValue() == CommunicationProtocolValue.ERROR &&
				message.getCommunicationError() == CommunicationError.GAME_ALREADY_EXISTS) {
			// register as second player
			return registerAsSecondPlayer();
		} else {
			throw new UnsupportedOperationException("message's CommunicationProtocolValue: " + message);
		}
	}
	
	private boolean registerAsSecondPlayer() {
		clientLogic.tryRegisterAsSecondPlayer();
		
		Message message = waitForResponse();
		if (message.getCommunicationProtocolValue() == CommunicationProtocolValue.PLAYER_REGISTERED) {
			return true;
		} else if (message.getCommunicationProtocolValue() == CommunicationProtocolValue.ERROR &&
				message.getCommunicationError() == CommunicationError.GAME_ALREADY_OCCUPIED) {
			return false;
		} else {
			throw new UnsupportedOperationException("message's CommunicationProtocolValue: " + message);
		}
	}
	
	private Message waitForResponse() {
		Message message = myCallback.getMessage();
		if(message != null) {
			return message;
		}
		// wait for response from Game Logic
		while(true) {
			try {
				System.out.println("playersID:" + playersID + " waiting");
				lock.wait();
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
				continue;
			}
		}
		
		return myCallback.getMessage();
	}
	
	private void logMessageDebug(String identifier, Message message) {
		System.out.println(playersID + " " + identifier + " " + message);
	}
	
}
