package game.communication;

public class PlayerIDAssigner {

	private static int id = 0;
	
	public synchronized static int getID() {
		return id++;
	}
	
	
}
