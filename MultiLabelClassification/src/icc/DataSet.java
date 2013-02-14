package icc;

import org.apache.lucene.util.OpenBitSet;


public class DataSet {
	
	private final Data data;
	public Data getData() {
		return data;
	}

	private final OpenBitSet bitSet;
	public OpenBitSet getBitSet() {
		return bitSet;
	}
	
	private final int cardinality;
	public int getNumberOfTuples() {
		return cardinality;
	}

	public DataSet(Data data, OpenBitSet bitSet) {
		this.data = data;
		this.bitSet = bitSet;
		cardinality = (int) bitSet.cardinality();
	}
	
	public DataSet matching(ItemSet itemSet) {
		/*OpenBitSet matching = new OpenBitSet();
		for(int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1))
			if(itemSet.includes(data.getTuple(i)))
					matching.set(i);
		return new DataSet(data, matching);*/
		OpenBitSet matching = bitSet.clone();
		for(int i = itemSet.getBitSet().nextSetBit(0); i >= 0; i = itemSet.getBitSet().nextSetBit(i + 1))
			matching.and(getData().getMatching(i));
		return new DataSet(data, matching);
	}
	
	public int[] y_i_1(int i) {
		int[] array = new int[getNumberOfTuples()];
		int j = 0;
		for(int k = bitSet.nextSetBit(0); k >= 0; k = bitSet.nextSetBit(k + 1))
			array[j++] = data.getTuple(k).getClassValues()[i];
		return array;
	}
	
	public int y(int i) {
		int sum = 0;
		for(int k = bitSet.nextSetBit(0); k >= 0; k = bitSet.nextSetBit(k + 1))
			sum += data.getTuple(k).getClassValues()[i];
		return sum;
	}
	
	public int[] y() {
		if(getNumberOfTuples() < 1)
			return new int[]{};
		int m = data.getTuple(bitSet.nextSetBit(0)).getClassValues().length;
		int[] y = new int[m];
		for(int i = 0; i < m; i++)
			y[i] = y(i);
		return y;
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for(int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1))
			stringBuilder.append(data.getTuple(i).toString()+"\n");
		return stringBuilder.toString();
	}
}
