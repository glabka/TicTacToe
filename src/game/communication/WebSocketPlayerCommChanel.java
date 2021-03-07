package game.communication;

import java.io.IOException;

import javax.websocket.Session;

public class WebSocketPlayerCommChanel implements IPlayerCallback {

	private Session session;
	
	public WebSocketPlayerCommChanel(Session session) {
		this.session = session;
	}
	
	@Override
	public void sendMessage(Message message) throws IOException {
		
	}

}
