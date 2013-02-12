package icc;
import java.util.BitSet;

import parsing.Convenience;



public abstract class ItemSet {

	// **
	// * Variables and getters / setters
	// **
	private final BitSet bitSet;
	
	public BitSet getBitSet() {
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
	
	public ItemSet(BitSet itemSet, int length) {
		this.bitSet = itemSet;
		this.length = length;
	}

	// **
	// * ItemSet methods
	// **
	public boolean includes(Tuple tuple) {
		BitSet copy = (BitSet) this.bitSet.clone();
		copy.and(tuple.getItemSet().getBitSet());
		return copy.equals(this.bitSet);
	}
	
	public ItemSet union(ItemSet other) {
		BitSet copy = (BitSet) getBitSet().clone();
		copy.or(other.getBitSet());
		return getInstance(copy, getLength());
	}
	
	public static BitSet bitSetFromArray(boolean[] array) {
		BitSet bitSet = new BitSet(array.length);
		for(int i = 0; i < array.length; i++)
			bitSet.set(i, array[i]);
		return bitSet;
	}
	
	public static BitSet bitSetFromArray(int[] array) {
		BitSet bitSet = new BitSet(array.length);
		for(int i = 0; i < array.length; i++)
			bitSet.set(i, array[i] > 0);
		return bitSet;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ItemSet))
			return false;
		ItemSet other = (ItemSet) obj;
		return getBitSet().equals(other.getBitSet());
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
	
	public abstract double ub();
	public abstract ItemSet getInstance(BitSet bitSet, int length);
	public abstract ItemSet getInstance(int[] itemSet);
	
	public ItemSet getOneItemSet(int j, int itemNumber) {
		int[] itemSet = new int[itemNumber];
		itemSet[j] = 1;
		return getInstance(itemSet);
	}
}
