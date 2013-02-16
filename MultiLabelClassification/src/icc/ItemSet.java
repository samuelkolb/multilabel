package icc;

import org.apache.lucene.util.OpenBitSet;

import parsing.Convenience;

public abstract class ItemSet {

	// **
	// * Variables and getters / setters
	// **
	private OpenBitSet bitSet;
	
	public OpenBitSet getBitSet() {
		return bitSet;
	}
	
	private final int length;
	
	public int getLength() {
		return length;
	}
	
	protected double upperbound = -1;

	// **
	// * Constructors
	// **
	public ItemSet(String itemSet, char delimiter) {
		this(Convenience.arrayFromString(itemSet, delimiter));
	}
	
	public ItemSet(int[] itemSet) {
		this(bitSetFromArray(itemSet), itemSet.length);
	}
	
	public ItemSet(OpenBitSet itemSet, int length) {
		this.bitSet = itemSet;
		this.length = length;
	}

	// **
	// * ItemSet methods
	// **
	public boolean includes(Tuple tuple) {
		OpenBitSet tupleSet = tuple.getItemSet().getBitSet();
		for(int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i+1))
			if(!tupleSet.get(i))
				return false;
		return true;
	}
	
	public ItemSet union(ItemSet other) {
		OpenBitSet copy = (OpenBitSet) getBitSet().clone();
		copy.or(other.getBitSet());
		return getInstance(copy, getLength());
	}
	
	public void unifyWith(ItemSet other) {
		bitSet.or(other.bitSet);
	}
	
	public static OpenBitSet bitSetFromArray(boolean[] array) {
		OpenBitSet bitSet = new OpenBitSet(array.length);
		for(int i = 0; i < array.length; i++)
			if(array[i])
				bitSet.set(i);
		return bitSet;
	}
	
	public static OpenBitSet bitSetFromArray(int[] array) {
		OpenBitSet bitSet = new OpenBitSet(array.length);
		for(int i = 0; i < array.length; i++)
			if(array[i] > 0)
				bitSet.set(i);
		return bitSet;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ItemSet))
			return false;
		ItemSet other = (ItemSet) obj;
		return getBitSet().equals(other.getBitSet()) && length == other.length;
	}
	
	@Override
	public int hashCode() {
		return getBitSet().hashCode();
	}
	
	@Override
	public String toString() {
		String string = "{";
		String buffer = "";
		for(int i = 0; i < getLength(); i++) {
			string += buffer + (getBitSet().get(i) ? 1 : 0);
			buffer = " ";
		}
		return string+"}";
	}
	
	public abstract double ub(ScoreCalculator scoreCalculator);
	public abstract ItemSet getInstance(OpenBitSet bitSet, int length);
	public abstract ItemSet getInstance(int[] itemSet);
	
	public ItemSet getOneItemSet(int j, int itemNumber) {
		int[] itemSet = new int[itemNumber];
		itemSet[j] = 1;
		return getInstance(itemSet);
	}
}
