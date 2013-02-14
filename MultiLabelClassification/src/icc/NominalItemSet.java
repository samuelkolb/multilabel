package icc;

import main.Main;

import org.apache.lucene.util.OpenBitSet;


public class NominalItemSet extends ItemSet {

	public NominalItemSet(OpenBitSet itemSet, int length) {
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
		double u = 0;
		DataSet covered = Main.getDataSet().matching(this);
		int m = Main.getM();
		int x_I = covered.getNumberOfTuples();
		int n = Main.getN();
		int[] y = covered.y();
		for(int k = 1; k < x_I; k++) {
			double sum = 0;
			for(int i = 0; i < m; i++) {
				if(k <= x_I - y[i])
					sum += Math.max(Main.var(k,new int[]{0}), Main.var(k,new int[]{k}));
				else if(k <= y[i])
					sum += Math.max(Main.var(k, new int[]{k - x_I + y[i]}), Main.var(k,new int[]{k}));
				else
					sum += Math.max(Main.var(k, new int[]{k - x_I + y[i]}), Main.var(k,new int[]{y[i]}));
			}
			if(sum > u)
				u = sum;
		}		
		upperbound = u;
		return u;
	}

	@Override
	public ItemSet getInstance(OpenBitSet bitSet, int length) {
		return new NominalItemSet(bitSet, length);
	}

	@Override
	public ItemSet getInstance(int[] itemSet) {
		return new NominalItemSet(itemSet);
	}
}
