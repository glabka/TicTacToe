package game.communication.web.websocket;

import java.net.URI;
import java.util.concurrent.CountDownLatch;

import org.glassfish.tyrus.client.ClientManager;

import game.GameMetaData;

public class WebSocketClientMain {
	
	public static void main(String[] args){
		String serverName = args[0];
		int portNumber = Integer.parseInt(args[1]);
		
		CountDownLatch latch = new CountDownLatch(1);
		WebSocketClientEndpoint clientEndPoint = new WebSocketClientEndpoint(latch, "game5x3", new GameMetaData(5, 3));
		ClientManager clientManager = ClientManager.createClient();
				
		try {
			clientManager.connectToServer(clientEndPoint, new URI("ws://" + serverName + ":" + portNumber + "/websockets/tictactoe"));
			latch.await();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}
