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
import game.communication.ClientLogic;
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

/**
 * Single game Web Socket Client Endpoint
 * @author glabka
 *
 */
@ClientEndpoint
public class WebSocketClientEndpoint {

	private CountDownLatch latch;
	private String gameName;
	private GameMetaData metaData;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private Gson gson = new Gson();
	private int playersID;
	private Object lock;
	private ClientLogic clientLogic;
	
	public WebSocketClientEndpoint(CountDownLatch latch, String gameName, GameMetaData metaData) {
		this.latch = latch;
		this.gameName = gameName;
		this.metaData = metaData;
		this.playersID = PlayerIDAssigner.getID();
		this.lock = new Object();
	}

	@OnOpen
	public void onOpen(Session session) {
		this.clientLogic = new ClientLogic(metaData, new WebSocketGameLogicCallback(session), gameName, playersID);
		clientLogic.tryRegisterGameAndPlayer(metaData);
	}

	@OnMessage
	public void onMessage(String messageStr, Session session) {
		Message message = gson.fromJson(messageStr, Message.class);
		
		synchronized (lock) {
			clientLogic.processMessage(message);
		}
	}
	
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info("Session " + session.getId() + " closed.");
		// TODO - maybe
		latch.countDown();
	}
}
