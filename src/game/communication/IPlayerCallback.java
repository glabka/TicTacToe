package game.communication;

import java.io.IOException;

public interface IPlayerCallback {

	void sendMessage(Message message) throws IOException;
	
}
