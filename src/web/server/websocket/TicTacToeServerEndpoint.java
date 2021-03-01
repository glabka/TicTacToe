package web.server.websocket;


import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;

import custom_exceptions.FullOpponentsException;
import custom_exceptions.SessionIsNotAmongThisOpponentsInstance;
import web.Message;
import web.GameState;
import web.server.AddressedMessage;
import web.server.WebServerLogic;

@ServerEndpoint(value = "/tictactoe")
public class TicTacToeServerEndpoint {
	
    private static WebServerLogic wsl = new WebServerLogic();
    private static Map<String, Session> sessions = Collections.synchronizedMap(new HashMap<String, Session>());
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private Gson gson = new Gson();
	
	@OnOpen
	public void onOpen(Session session) throws IOException {
		sessions.put(session.getId(), session);
	}
	
	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		synchronized(wsl) {
			
			// decoding message
			System.out.println("message from " + session.getId() + " message: " + message);
			Message decodedMessage = gson.fromJson(message, Message.class);
			// getting server response
			AddressedMessage addressedMessage = new AddressedMessage(session.getId(), decodedMessage);
			List<AddressedMessage> responses = wsl.getResponse(addressedMessage);
			
			// sending responses
			for(AddressedMessage response : responses) {
				Session sessionForResponse = sessions.get(response.getWebPlayerID());
				
				Message responsMessage = response.getGameMessage();
				String strResponse = gson.toJson(responsMessage);
				
				System.out.println("message to " + response.getWebPlayerID() + " message:" + strResponse);
				sessionForResponse.getBasicRemote().sendText(strResponse);
			}
		}
	}
	
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		sessions.remove(session.getId());
		logger.info("Session " + session.getId() + " closed");
		wsl.quit(session.getId());
	}
	
}
