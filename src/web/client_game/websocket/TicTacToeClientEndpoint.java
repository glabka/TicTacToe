package web.client_game.websocket;

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

import web.CommunicationSignal;
import web.GameState;
import web.Message;

@ClientEndpoint
public class TicTacToeClientEndpoint {

	private CountDownLatch latch;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public TicTacToeClientEndpoint(CountDownLatch latch) {
		this.latch = latch;
	}

	@OnOpen
	public void onOpen(Session session) {
		logger.info("Connected:");

		Gson gson = new Gson();
		Message message = Message.createMessage("test game", CommunicationSignal.START);
		message.setGridSize(5);
		message.setStreakLength(3);
		String jsonMessage = gson.toJson(message);
		
		try {
			session.getBasicRemote().sendText(jsonMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@OnMessage
	public String onMessage(String message, Session session) {
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

		try {
			System.out.println(message);
			String userInput = bufferRead.readLine();
			return userInput;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info("Session " + session.getId() + " closed.");
		latch.countDown();
	}
}
