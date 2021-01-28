package players.ai_players.support_classes;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class SortedTreeNode <T> implements Comparable<SortedTreeNode<T>>{

	private SortedTreeNode<T> parent;
	private List<SortedTreeNode<T>> children;
	private T containedValue;
	private Comparator<T> comparator;
	private int depth;
	private Double nodeEvaluationNumber = null; // number of node evaluated from outside
	
	public SortedTreeNode(SortedTreeNode<T> parent, T containedValue, Comparator<T> comparator, int depth) {
		this.parent = parent;
		this.children = new LinkedList<SortedTreeNode<T>>();
		this.containedValue = containedValue;
		this.comparator = comparator;
		this.depth = depth;
	}
	
	public void addChild(SortedTreeNode<T> child) {
		children.add(child);
//		Collections.sort(children, Collections.reverseOrder());
		
		Collections.sort(children);
		Collections.reverse(children);
	}
	
	public T getVal() {
		return containedValue;
	}

	@Override
	public int compareTo(SortedTreeNode<T> arg0) {
		return this.comparator.compare(this.getVal(), arg0.getVal());
	}
	
	public List<SortedTreeNode<T>> getChildren(){
		return children;
	}
	
	public SortedTreeNode<T> getParent() {
		return parent;
	}
	
	public void setNodeEvaluationNumber(double evalNum) {
		this.nodeEvaluationNumber = evalNum;
	}
	
	public Double getNodeEvaluationNumber() {
		return this.nodeEvaluationNumber;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public String superToString() {
		return "" + hashCode();
	}
	
	public String toString() {
		
		String str = "{ this: " + superToString() + " parent: " + (parent != null ? parent.superToString() : null) + " depth: " + depth;
		if (nodeEvaluationNumber == null) {
			str += " evalNum: " + nodeEvaluationNumber;
		}
		str += " cVal: " + containedValue + "}";
		
		return str; 
	}
}
