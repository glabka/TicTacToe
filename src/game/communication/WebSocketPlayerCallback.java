package game.communication;

import java.io.IOException;

import javax.websocket.Session;

public class WebSocketPlayerCallback implements IPlayerCallback {

	private Session session;
	
	public WebSocketPlayerCallback(Session session) {
		this.session = session;
	}
	
	@Override
	public void sendMessage(Message message) throws IOException {
		
	}

}
