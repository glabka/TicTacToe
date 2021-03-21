package game.local;

import game.GameLogic;
import game.GameMetaData;
import game.communication.Message;
import game.communication.PlayerIDAssigner;
import game.communication.enums.CommunicationProtocolValue;
import game.communication.local.LocalPlayerCallback;
import players.Player;

public class LocalGameForTwoManager {

	private String gameName;
	private GameLogic gameLogic;
	private int p1ID;
	private int p2ID;
	private Object lock1;
	private Object lock2;
	private LocalPlayerCallback callback1;
	private LocalPlayerCallback callback2;
	private GameMetaData metaData;
	
	public LocalGameForTwoManager() {
		gameLogic = GameLogic.getGameLogic();
		gameName = "game1";
	}
	
	private void registerPlayers() {
		p1ID = PlayerIDAssigner.getID();
		p2ID = PlayerIDAssigner.getID();
		lock1 = new Object();
		lock2 = new Object();
		callback1 = new LocalPlayerCallback(lock1);
		callback2 = new LocalPlayerCallback(lock2);
		gameLogic.registerPlayersCallback(p1ID, callback1);
		gameLogic.registerPlayersCallback(p2ID, callback2);
	}
	
	private void createGame() {
		Message message = Message.createMessage(gameName, CommunicationProtocolValue.CREATE_GAME);
		metaData = new GameMetaData(5, 3);
		message.setGameMetaData(metaData);
		message.setGameName(gameName);
		gameLogic.receiveMessage(p1ID, message);
	}
	
	private void registerSecondPlayerForGame() {
		Message message = Message.createMessage(gameName, CommunicationProtocolValue.REGISTER_PLAYER);
		message.setGameName(gameName);
		gameLogic.receiveMessage(p2ID, message);
	}
	
	public void play() {
		registerPlayers();
		createGame();
		registerSecondPlayerForGame();
		
		LocalPlayerWrapper lpw1 = new LocalPlayerWrapper(metaData, lock1, gameLogic, callback1, gameName, p1ID);
		LocalPlayerWrapper lpw2 = new LocalPlayerWrapper(metaData, lock2, gameLogic, callback1, gameName, p2ID);
		
		lpw1.start();
		lpw2.start();
		
		try {
			lpw1.join();
			lpw2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
}
