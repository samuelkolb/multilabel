package icc;

import org.apache.lucene.util.OpenBitSet;

public class Data {

	private final Tuple[] tuples;
	public Tuple[] getTuples() {
		return tuples;
	}
	
	private final int m;
	public int getNumberOfClassAttributes() {
		return m;
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
		if(tuples.length == 0)
			throw new IllegalArgumentException("Empty data");
		this.tuples = tuples;
		this.m = tuples[0].getClassValues().length;
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
	
	public int getNumberOfTuples() {
		return getTuples().length;
	}
	
	public DataSet getFullDataSet() {
		OpenBitSet bitSet = new OpenBitSet(getNumberOfTuples());
		for(int i = 0; i < getNumberOfTuples(); i++)
			bitSet.set(i);
		return new DataSet(this, bitSet);
	}
}
