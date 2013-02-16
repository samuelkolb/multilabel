package icc;

import java.util.Arrays;

import org.apache.lucene.util.OpenBitSet;

import main.Main;

public class NumericalItemSet extends ItemSet {

	public NumericalItemSet(String itemSet, char delimiter) {
		super(itemSet, delimiter);
	}

	public NumericalItemSet(int[] itemSet) {
		super(itemSet);
	}

	public NumericalItemSet(OpenBitSet itemSet, int length) {
		super(itemSet, length);
	}
	
	@Override
	/**
	 * Calculates the upperbound, if not already calculated using the given scoreCalculator
	 */
	public double ub(ScoreCalculator scoreCalculator) {
		if(upperbound >= 0)
			return upperbound;
		
		DataSet covered = Main.getDataSet().matching(this);
		int x = covered.getNumberOfTuples();
		double u = scoreCalculator.getCombinedScore(x, covered.y());
		int m = Main.getDataSet().getData().getNumberOfClassAttributes();

		int[][] sorted_y_i_1 = new int[m][covered.getNumberOfTuples()];
		for(int i = 0; i < m; i++) {
			sorted_y_i_1[i] = covered.y_i_1(i);
			Arrays.sort(sorted_y_i_1[i]);
		}
		
		int[] min = new int[m];
		int[] max = new int[m];
		int[] opt = new int[m];

		int s_k_n;
		int[] s = Main.getS();
		int n = Main.getN();
		double v;
		for(int k = 1; k < x; k++) {
			for(int i = 0; i < m; i++) {
				min[i] = min[i] + sorted_y_i_1[i][k-1];
				max[i] = max[i] + sorted_y_i_1[i][x-k];
					
				s_k_n = s[i]*k/n;
				if(Math.abs(min[i] - s_k_n) > Math.abs(max[i] - s_k_n))
					opt[i] = min[i];
				else
					opt[i] = max[i];
			}
			v = scoreCalculator.getCombinedScore(k, opt);
			if(v > u)
				u = v;
		}		
		upperbound = u;
		return u;
	}

	@Override
	public ItemSet getInstance(OpenBitSet bitSet, int length) {
		return new NumericalItemSet(bitSet, length);
	}

	@Override
	public ItemSet getInstance(int[] itemSet) {
		return new NumericalItemSet(itemSet);
	}
}
