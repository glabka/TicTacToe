package game.communication.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.glassfish.tyrus.server.Server;

import game.communication.web.websocket.WebSocketServerEndpoint;

public class WebServer {
	
//	private GameLogic gameLogic = GameLogic.getGameLogic(); // for use from different technologies than is websocket
	
	public static void main(String[] args) {
		String serverName =  args[0]; // an ip address or localhost
    	int portNumber = Integer.parseInt(args[1]);
    	
    	Server server = new Server(serverName, portNumber, "/websockets", WebSocketServerEndpoint.class);
    	
    	try {
    		server.start();
    		System.out.println("Please press a key to stop the server.");
    		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            reader.readLine();
    	} catch (Exception e) {
    		throw new RuntimeException(e);
    	} finally {
    		server.stop();
    	}

	}
	
}
