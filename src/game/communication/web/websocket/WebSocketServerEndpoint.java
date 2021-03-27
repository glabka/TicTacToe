package game.communication.web.websocket;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import game.GameLogic;
import game.communication.Message;
import game.communication.PlayerIDAssigner;
import game.communication.enums.CommunicationError;
import game.communication.enums.CommunicationProtocolValue;

@ServerEndpoint(value = "/tictactoe")
public class WebSocketServerEndpoint {
	
    private static GameLogic gameLogic = GameLogic.getGameLogic();
    private static Map<Session, Integer> playersIDs = Collections.synchronizedMap(new HashMap<Session, Integer>());
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private Gson gson = new Gson();
	
	@OnOpen
	public void onOpen(Session session) {
		playersIDs.put(session, PlayerIDAssigner.getID());
		gameLogic.registerPlayersCallback(playersIDs.get(session), new WebSocketPlayerCallback(session));
	}
	
	@OnMessage
	public String onMessage(String message, Session session) {
		
		// decoding message
		logger.info("message from " + session.getId() + " message: " + message);
		Message decodedMessage = null;
		try {
			decodedMessage = gson.fromJson(message, Message.class);
		} catch (JsonSyntaxException e) { 
			Message response = Message.createMessage(CommunicationProtocolValue.ERROR);
			response.setCommunicationError(CommunicationError.WRONG_MESSAGE_FORMAT);
			
			return gson.toJson(response);
		}
		// sending to GameLogic
		Message gameLogicResponse = gameLogic.receiveMessage(playersIDs.get(session), decodedMessage);
		// returning back response
		return gson.toJson(gameLogicResponse);
	}
	
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		playersIDs.remove(session);
		logger.info("Session " + session.getId() + " closed");
//		gameLogic.quit(); // TODO - quitting all games player was involved in
	}
	
}
