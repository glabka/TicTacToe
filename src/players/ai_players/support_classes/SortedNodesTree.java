package players.ai_players.support_classes;

import java.util.Comparator;
import java.util.List;

public class SortedNodesTree<T> {

	private SortedTreeNode<T> root;
	private Comparator<T> comparator;
	
	/**
	 * Just a container for SortedTreeNodes. Nodes are sorted only on node level, not on tree level.
	 * @param root
	 * @param copmarator
	 */
	public SortedNodesTree(SortedTreeNode<T> root, Comparator<T> comparator) {
		this.root = root;
	}
	
	public void addChild(SortedTreeNode<T> parent, T childValue) {
		parent.addChild(new SortedTreeNode<T>(parent, childValue, comparator));
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
	
}
