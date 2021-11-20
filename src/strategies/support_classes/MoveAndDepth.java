package strategies.support_classes;

import java.lang.reflect.Field;

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

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(mv = " + mv + ", deopth = " + depth + ")";
	}
}