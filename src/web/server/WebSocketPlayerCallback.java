package web.server;

import java.io.IOException;

import javax.websocket.Session;

import com.google.gson.Gson;

import game.communication.IPlayerCallback;
import game.communication.Message;

public class WebSocketPlayerCallback implements IPlayerCallback{
	
	private Gson gson = new Gson();
	private Session session;

	public WebSocketPlayerCallback(Session session) {
		this.session = session;
	}

	@Override
	public void sendMessage(Message message) throws IOException {
		String strMessage = gson.toJson(message);
		session.getBasicRemote().sendText(strMessage);
	}
	
}
