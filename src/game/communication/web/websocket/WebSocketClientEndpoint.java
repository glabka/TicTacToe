package game.communication.web.websocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import custom_exceptions.WrongMessageException;
import game.GameLogic;
import game.GameMetaData;
import game.communication.Message;
import game.communication.MessageConsistencyChecker;
import game.communication.Move;
import game.communication.PlayerIDAssigner;
import game.communication.enums.CommunicationError;
import game.communication.enums.CommunicationProtocolValue;
import game.communication.enums.MoveResult;
import game_components.ValuedMove;
import game_components.Square.SVal;
import players.ui_players.UIPlayer;

@ClientEndpoint
public class WebSocketClientEndpoint {

	private CountDownLatch latch;
	private String gameName;
	private GameMetaData metaData;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private Gson gson = new Gson();
	private int playersID;
	private Object lock;
	
	public WebSocketClientEndpoint(CountDownLatch latch, String gameName, GameMetaData metaData) {
		this.latch = latch;
		this.gameName = gameName;
		this.metaData = metaData;
		this.playersID = PlayerIDAssigner.getID();
		this.lock = new Object();
	}

	@OnOpen
	public void onOpen(Session session) {
		logger.info("Session " + session.getId() + " opened.");
		Message message = Message.createMessage(gameName, CommunicationProtocolValue.CREATE_GAME);
		message.setGameMetaData(metaData);
		Message m = gameLogic.receiveMessage(playersID, message);
		if (m != null && m.getCommunicationProtocolValue() == CommunicationProtocolValue.ERROR
				&& m.getCommunicationError() == CommunicationError.GAME_ALREADY_EXISTS) {
			message = Message.createMessage(gameName, CommunicationProtocolValue.REGISTER_PLAYER);
			m = gameLogic.receiveMessage(playersID, message);
			if (m != null && m.getCommunicationProtocolValue() == CommunicationProtocolValue.ERROR
					&& m.getCommunicationError() == CommunicationError.GAME_ALREADY_OCCUPIED) {
				System.out.println("Game already exist and is fully occupied by players.");
				try {
					session.close();
				} catch (IOException e) {
					e.printStackTrace();
					latch.countDown();
				}
			}
		}
	}

	@OnMessage
	public String onMessage(String messageStr, Session session) {
		synchronized (lock) {
			Message message = gson.fromJson(messageStr, Message.class);
			// TODO
			return null;
		}
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info("Session " + session.getId() + " closed.");
		// TODO
		latch.countDown();
	}
}
