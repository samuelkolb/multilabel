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
	public double ub() {
		//if(upperbound != -1) 
		//	return upperbound; 
		
		boolean printing = false;
		
		double time = System.currentTimeMillis();
		if(printing)
			System.out.println();
		
		double u = 0;
		
		DataSet covered = Main.getDataSet().matching(this);
		
		int m = Main.getM();

		double time3 = System.currentTimeMillis();
		int[][] sorted_y_i_1 = new int[m][covered.getNumberOfTuples()];
		for(int i = 0; i < m; i++) {
			sorted_y_i_1[i] = covered.y_i_1(i);
			Arrays.sort(sorted_y_i_1[i]);
		}
		if(printing)
			System.out.println("Building sorted y_i_1 took (ms): "+(System.currentTimeMillis() - time3));
		
		int x_I = covered.getNumberOfTuples();
		int[] min = new int[m]; // OPT
		int[] max = new int[m]; // OPT
		
		for(int i = 0; i < m; i++)
			if(x_I > 0)
				min[i] = sorted_y_i_1[i][0]; // OPT
		
		for(int i = 0; i < m; i++)
			if(x_I > 0)
				max[i] = sorted_y_i_1[i][sorted_y_i_1[i].length - 1]; // OPT
		
		int[] opt = new int[m];

		double time5 = System.currentTimeMillis();
		int s_k_n;
		int[] s = Main.getS();
		int n = Main.getN();
		for(int k = 1; k < x_I; k++) {
			
			for(int i = 0; i < m; i++) {
				min[i] = min[i] + sorted_y_i_1[i][k-1]; // OPT
				max[i] = max[i] + sorted_y_i_1[i][x_I-k]; // OPT
					
				s_k_n = s[i]*k/n;
				if(Math.abs(min[i] - s_k_n) > Math.abs(max[i] - s_k_n))
					opt[i] = min[i];
				else
					opt[i] = max[i];
			}
			double v = Main.var(k, opt);
			if(v > u)
				u = v;
		}
		if(printing)
			System.out.println("Building v took (ms): "+(System.currentTimeMillis() - time5));
		
		if(printing)
			System.out.println("Calculating u took (ms): "+(System.currentTimeMillis() - time));
		
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
