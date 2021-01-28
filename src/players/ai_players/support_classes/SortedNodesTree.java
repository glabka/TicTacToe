package players.ai_players.support_classes;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class SortedNodesTree<T> {

	private SortedTreeNode<T> root;
	private Comparator<T> comparator;
	
	/**
	 * 
	 * @param rootValue
	 * @param comparator
	 */
	public SortedNodesTree(T rootValue, Comparator<T> comparator) {
		this.root = new SortedTreeNode<T>(null, rootValue, comparator, 0);
		this.comparator = comparator;
	}
	
	/**
	 * Method returns the child
	 * @param parent
	 * @param childValue
	 * @return
	 */
	public SortedTreeNode<T> addChild(SortedTreeNode<T> parent, T childValue) {
		SortedTreeNode<T> child = new SortedTreeNode<T>(parent, childValue, comparator, parent.getDepth() + 1);
		parent.addChild(child);
		return child;
	}
	
	public SortedTreeNode<T> getRoot() {
		return root;
	}

	public List<SortedTreeNode<T>> getSortedChildren(SortedTreeNode<T> node) {
		return node.getChildren();
	}
	
	public SortedTreeNode<T> getParent(SortedTreeNode<T> node) {
		return node.getParent();
	}
	
	public int getDepth(SortedTreeNode<T> node) {
		return node.getDepth();
	}
	
	public void setNodeEvaluationNumber(SortedTreeNode<T> node, double evalNumber) {
		node.setNodeEvaluationNumber(evalNumber);
	}
	
	public String toString() {
//		breadth search 
		StringBuilder strB = new StringBuilder();
		
		List<SortedTreeNode> queue = new LinkedList<SortedTreeNode>();
		queue.add(root);
		
		int lastDepth = -1;
		while(!queue.isEmpty()) {
			SortedTreeNode treeNode = queue.get(0);
			queue.remove(0);
			strB.append(treeNode);
			
			if(lastDepth != treeNode.getDepth()) {
				strB.append("\n");
			}
			
			lastDepth = treeNode.getDepth();
			
			queue.addAll(treeNode.getChildren());
		}
		
		return strB.toString();
	}
	
}
