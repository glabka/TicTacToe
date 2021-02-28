package web.client_game.websocket;

import java.net.URI;
import java.util.concurrent.CountDownLatch;

import org.glassfish.tyrus.client.ClientManager;

public class ClientMain {
	
	public static void main(String[] args){
		String serverName = args[0];
		int portNumber = Integer.parseInt(args[1]);
		
		CountDownLatch latch = new CountDownLatch(1);
		TicTacToeClientEndpoint clientEndPoint = new TicTacToeClientEndpoint(latch);
		ClientManager clientManager = ClientManager.createClient();
				
		try {
			clientManager.connectToServer(clientEndPoint, new URI("ws://" + serverName + ":" + portNumber + "/websockets/tictactoe"));
			latch.await();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}
