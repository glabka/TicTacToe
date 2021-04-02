package game.communication.web.websocket;

import java.io.IOException;

import javax.websocket.Session;

import com.google.gson.Gson;

import game.communication.IGameLogicCallback;
import game.communication.Message;

public class WebSocketGameLogicCallback implements IGameLogicCallback {
	
	private Session session;
	private Gson gson;
	
	public WebSocketGameLogicCallback(Session session) {
		this.session = session;
		gson = new Gson();
	}

	@Override
	public void sendMessage(Message message) throws IOException {
		session.getBasicRemote().sendText(gson.toJson(message));
	}

}
