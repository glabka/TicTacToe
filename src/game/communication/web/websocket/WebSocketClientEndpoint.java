package game.communication.web.websocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import com.google.gson.Gson;

import game.communication.Message;

@ClientEndpoint
public class WebSocketClientEndpoint {

	private CountDownLatch latch;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private Gson gson = new Gson();
	
	public WebSocketClientEndpoint(CountDownLatch latch) {
		this.latch = latch;
	}

	@OnOpen
	public void onOpen(Session session) {
		logger.info("Connected:");
		//TODO
	}

	@OnMessage
	public String onMessage(String message, Session session) {
		// TODO
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info("Session " + session.getId() + " closed.");
		// TODO
		latch.countDown();
	}
}
