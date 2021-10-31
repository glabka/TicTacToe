package strategies.support_classes;

import game_components.ValuedMove;

public class MoveAndDepth {
	
	private int depth;
	private ValuedMove mv;
	
	public MoveAndDepth(ValuedMove mv, int depth) {
		this.mv = mv;
		this.depth = depth;
	}
	
	public ValuedMove getMove() {
		return mv;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public String toString() {
		return mv +" d:" + depth;
	}
	
}