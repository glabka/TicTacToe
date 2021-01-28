package players.ai_players.support_classes;

import game_components.Move;

public class MoveAndDepth {
	
	private int depth;
	private Move mv;
	
	public MoveAndDepth(Move mv, int depth) {
		this.mv = mv;
		this.depth = depth;
	}
	
	public Move getMove() {
		return mv;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public String toString() {
		return mv +" d:" + depth;
	}
	
}