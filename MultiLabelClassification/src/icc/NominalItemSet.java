package icc;

import java.util.BitSet;

public class NominalItemSet extends ItemSet {

	public NominalItemSet(BitSet itemSet, int length) {
		super(itemSet, length);
	}
	
	public NominalItemSet(int[] itemSet) {
		super(itemSet);
	}
	
	public NominalItemSet(String itemSet, char delimiter) {
		super(itemSet, delimiter);
	}

	@Override
	public double ub() {
		if("1".equals("1"))
			throw new UnsupportedOperationException();
		
		if(upperbound != -1) 
			return upperbound; 
		
		double u = 0.0;
		
		upperbound = u;
		return u;
	}

	@Override
	public ItemSet getInstance(BitSet bitSet, int length) {
		return new NominalItemSet(bitSet, length);
	}

	@Override
	public ItemSet getInstance(int[] itemSet) {
		return new NominalItemSet(itemSet);
	}
}
