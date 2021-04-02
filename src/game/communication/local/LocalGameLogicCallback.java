package game.communication.local;

import java.io.IOException;

import game.GameLogic;
import game.communication.IGameLogicCallback;
import game.communication.Message;

public class LocalGameLogicCallback implements IGameLogicCallback {
	
	private int senderID;
	private GameLogic gameLogic;
	
	public LocalGameLogicCallback(int senderID, GameLogic gameLogic) {
		this.senderID = senderID;
		this.gameLogic = gameLogic;
	}

	@Override
	public void sendMessage(Message message) throws IOException {
		gameLogic.receiveMessage(senderID, message);
	}

}
