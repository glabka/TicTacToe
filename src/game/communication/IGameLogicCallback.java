package game.communication;

import java.io.IOException;

public interface IGameLogicCallback {

	void sendMessage(Message message) throws IOException;
	
}
