package PCT;

import icc.*;
import main.Main;

public class PredictiveClusteringTree {
	
	private Node root;
	
	public Node getRoot(){
		return root;
	}
	
	public PredictiveClusteringTree(DataSet i) {
		root = pct(i, null);
	}
	
	public Node pct(DataSet data, Node parent){
		Node t = bestTest(data, parent);
		if (t != null){
			for(DataSet p : getPartition(t)){
				t.addChild(pct(p,t));
			}
			return t;
		}
		else return null;
	}
	
	/** 
	 * Zoeken naar de beste aanvaardbare attribuut-waarde test dat in een node kan zitten
	 */
	public Node bestTest(DataSet data, Node parent) {
		ItemSet pattern = Main.icc();
		Node t = new Node(pattern, data, parent);
		return t;
	}
	
	public DataSet[] getPartition(Node t){
		ItemSet pattern = t.getTest();
		DataSet data1 = t.getData().matching(pattern);
		return new DataSet[]{data1, t.getData().other(data1) };
	}
}
