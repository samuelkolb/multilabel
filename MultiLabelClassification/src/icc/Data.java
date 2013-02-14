package icc;

import org.apache.lucene.util.OpenBitSet;

public class Data {

	private final Tuple[] tuples;
	public Tuple[] getTuples() {
		return tuples;
	}
	public Tuple getTuple(int i) {
		return tuples[i];
	}
	
	private final ItemSet bluePrint;
	public ItemSet getBluePrint() {
		return bluePrint;
	}
	
	private final OpenBitSet[] matching;
	public OpenBitSet getMatching(int i) {
		return matching[i];
	}
	
	public Data(Tuple[] tuples, ItemSet bluePrint) {
		this.tuples = tuples;
		this.bluePrint = bluePrint;
		int itemNumber = getTuple(0).getItemSet().getLength();
		OpenBitSet[] matching = new OpenBitSet[itemNumber];
		ItemSet itemSet;
		for(int j = 0; j < itemNumber; j++) {
			itemSet = getBluePrint().getOneItemSet(j, itemNumber);
			OpenBitSet bitSet = new OpenBitSet();
			for(int i = 0; i < tuples.length; i++)
				if(itemSet.includes(tuples[i]))
					bitSet.set(i);
			matching[j] = bitSet;
		}
		this.matching = matching;
	}
}
