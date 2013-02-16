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
	public double ub(ScoreCalculator scoreCalculator) {
		if(upperbound >= 0)
			return upperbound;
		
		OpenBitSet covered = Main.getDataSet().matchingBitSet(this);
		int m = Main.getDataSet().getData().getNumberOfClassAttributes();
		int x_I = (int) DataSet.numberTuples(covered);
		if(x_I == 0) {
			upperbound = 0;
			return upperbound;
		}
		double u = 0;
		int[] y = DataSet.calculateY(covered, Main.getData());

		// Following roughly paper Zimmermann and Raedt - p.9
		double y_plus_max, y_plus_min, y_minus_max, y_minus_min;
		for(int k = 1; k < x_I; k++) {
			double sum = 0;
			for(int i = 0; i < m; i++) {
				y_plus_max = Math.min(k, y[i]);
				y_plus_min = k - y_plus_max;
				y_minus_max = Math.min(k, x_I - y[i]);
				y_minus_min = k - y_minus_max;
				sum += Math.max(
					scoreCalculator.getScore(y_plus_max, y_minus_min, i),
					scoreCalculator.getScore(y_minus_max, y_plus_min, i)
				);
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
