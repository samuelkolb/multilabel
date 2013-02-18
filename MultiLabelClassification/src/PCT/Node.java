package PCT;

import java.util.ArrayList;

import icc.*;

public class Node {
	
	private ItemSet test;
	
	private DataSet data;
	
	private Node parent;
	
	private ArrayList<Node> children;
	
	public Node(ItemSet test, DataSet data, Node parent){
		this.test = test;
		this.data = data;
		this.parent = parent;
		children = new ArrayList<Node>();
	}
	
	public ItemSet getTest(){
		return test;
	}
	
	public DataSet getData() {
		return data;
	}
	
	public void setParent(Node p){
		parent = p;
	}
	
	public Node getParent(){
		return parent;
	}
	
	public void addChild(Node child){
		children.add(child);
	}
	
	public ArrayList<Node> getChildren(){
		return children;
	}

}
