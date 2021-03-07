package game;

import java.util.HashMap;
import java.util.Map;

import custom_exceptions.GameAlreadyExistExeption;
import custom_exceptions.GameDoesntExistExeption;

public class GameManager {

	private final Map<String, GameVer2> games = new HashMap<>();
	
	public void registerGame(String gameName, GameMetaData gameMetaData) {
		if(!games.containsKey(gameName)) {
			games.put(gameName, new GameVer2(gameName, gameMetaData));
		} else {
			throw new GameAlreadyExistExeption("gameName:" + gameName);
		}
	}
	
	public GameVer2 getGame(String gameName) {
		if(games.containsKey(gameName)) {
			return games.get(gameName);
		} else {
			throw new GameDoesntExistExeption("gameName:" + gameName);
		}
	}
	
	public boolean doesGameExist(String gameName) {
		return games.containsKey(gameName);
	}
	
	public void deleteGame(String gameName) {
		games.remove(gameName);
	}
	
}
