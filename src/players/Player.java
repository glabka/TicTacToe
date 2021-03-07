package players;

import game_components.Grid;
import game_components.ValuedMove;
import game_components.Square.SVal;

public abstract class Player {

	public SVal playersSVal;
	public String name;
	
	public Player(SVal playersSVal, String name) {
		this.playersSVal = playersSVal;
		this.name = name;
	}
	
	public abstract ValuedMove nextMove(Grid g);
	
	public String getName() {
		return name;
	}
	
	public SVal getSVal() {
		return playersSVal;
	}
}
