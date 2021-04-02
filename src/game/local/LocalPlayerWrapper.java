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
			
			clientLogic.tryRegisterGameAndPlayer(metaData);
			
			outerloop:
			while(true) {
				while (true) {
					List<Message> messages = waitForResponse();
					for (Message message : messages) {
						if (clientLogic.processMessage(message)) {
							// Game Over
							clientLogic.getGrid().printGrid();
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
	
	private List<Message> waitForResponse() {
		List<Message> messages = myCallback.getMessages();
		if (messages != null && !messages.isEmpty()) {
			return messages;
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
		return myCallback.getMessages();
	}	
}
