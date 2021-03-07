package game;

import game.communication.Message;
import game.communication.PlayerIDAssigner;
import game.communication.enums.CommunicationProtocolValue;
import players.Player;

public class LocalGameForTwoManager {

	private String gameName;
	private GameLogic gameLogic;
	private Player p1;
	private Player p2;
	
	public LocalGameForTwoManager(Player p1, Player p2) {
		this.p1 = p1;
		this.p2 = p2;
		gameLogic = GameLogic.getGameLogic();
		gameName = "game1";
	}
	
	private void registerPlayers() {
		// TODO
		gameLogic.registerPlayersCallback(PlayerIDAssigner.getID(), playersCallback);
		gameLogic.registerPlayersCallback(PlayerIDAssigner.getID(), playersCallback);
	}
	
	private void createGame() {
		Message message = Message.createMessage(gameName, CommunicationProtocolValue.CREATE_GAME);
		message.setGameMataData(new GameMetaData(5, 3));
		gameLogic.receiveMessage(1, message);
	}
	
	public void play() {
		ValuedMove vMv = p1.nextMove(g);
	}
	
}
