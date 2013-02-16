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
	
	/**
	 * Returns a new Dataset, a subset of this dataset that matches the given itemset
	 */
	public DataSet matching(ItemSet itemSet) {
		return new DataSet(data, matchingBitSet(itemSet));
	}
	
	/**
	 * Returns a new bitset, representing a subset of this dataset that matches the given itemset
	 * For speed gain purposes - avoiding object creation if unnecesary
	 */
	public OpenBitSet matchingBitSet(ItemSet itemSet) {
		OpenBitSet matching = bitSet.clone();
		for(int i = itemSet.getBitSet().nextSetBit(0); i >= 0; i = itemSet.getBitSet().nextSetBit(i + 1))
			matching.and(getData().getMatching(i));
		return matching;
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
		return calculateY(bitSet, getData());
	}
	

	/**
	 * Method for calculating y
	 * Speed critical
	 * @param dataBitSet
	 * 			A bitset representing a subset of all tuples
	 * @param data
	 * 			A data object holding all the tuples
	 * @param m
	 * 			The number of the class attributes
	 * @return
	 * 			An array with the sums over all represented tuples for each class attribute
	 */
	public static int[] calculateY(OpenBitSet dataBitSet, Data data) {
		int[] y = new int[data.getNumberOfClassAttributes()];
		int sum;
		// Iterate over all class attributes
		for(int i = 0; i < data.getNumberOfClassAttributes(); i++) {
			sum = 0;
			for(int k = dataBitSet.nextSetBit(0); k >= 0; k = dataBitSet.nextSetBit(k + 1))
				sum += data.getTuple(k).getClassValues()[i];
			y[i] = sum;
		}
		return y;
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for(int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1))
			stringBuilder.append(data.getTuple(i).toString()+"\n");
		return stringBuilder.toString();
	}
	
	public static int numberTuples(OpenBitSet dataBitSet) {
		return (int) dataBitSet.cardinality();
	}
}
