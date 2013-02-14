package main;
import icc.Data;
import icc.DataSet;
import icc.ItemSet;
import icc.NominalItemSet;
import icc.NumericalItemSet;
import icc.Tuple;

import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.util.OpenBitSet;

import parsing.Parser;


public class Main {
	
	private static Data data;
	public static Data getData() {
		return data;
	}

	private static DataSet dataSet;
	public static DataSet getDataSet() {
		return dataSet;
	}

	private static int[] s;
	public static int[] getS() {
		return s;
	}

	private static int n;
	public static int getN() {
		return n;
	}

	private static int m;
	public static int getM() {
		return m;
	}

	/**
	 * TODO Vermijden dubbel berekenen u en var => miss omschrijven classes
	 * TODO BitVector ipv BitSet
	 * TODO Score berekenen i.p.v. score
	 * TODO DataSet matching?
	 * TODO don't use set?
	 */
	
	public static void main(String[] args) {
				
		/*
Profiling original home
293.0
2476.0
15474.0
56987.0
132744.0
319263.0

Original home
230.0
2011.0
11884.0
45655.0
101503.0
167562.0
210225.0

Include change home
152.0
1219.0
7513.0
28592.0
69773.0
120669.0
154299.0
155035.0
126816.0

Matching/Dataset change home
95.0
433.0
2306.0
8357.0
19768.0
32799.0
40459.0
40280.0
32100.0
21835.0
12226.0
5658.0
2060.0
573.0
122.0
19.0
2.0
0.0


		 */
		/*Tuple[] tuples = new Tuple[]{
			new Tuple("0 0 1 0 0", "1 3", ' '),
			new Tuple("1 0 0 1 1", "1 2", ' '),
			new Tuple("0 0 0 1 1", "1 1", ' '),
			new Tuple("1 1 0 0 1", "2 1", ' '),
			new Tuple("1 1 0 0 1", "3 1", ' '),
			new Tuple("1 1 0 0 1", "3 2", ' '),
			new Tuple("0 0 0 1 0", "3 3", ' '),
			new Tuple("0 1 0 1 1", "2 3", ' ')
		};*/
		/*Tuple[] tuples = new Tuple[] {
			new Tuple(new NumericalItemSet("0 0 1 0 0", ' '), new int[]{1, 3}),
			new Tuple(new NumericalItemSet("1 0 0 1 1", ' '), new int[]{1, 2}),
			new Tuple(new NumericalItemSet("0 0 0 1 1", ' '), new int[]{1, 1}),
			new Tuple(new NumericalItemSet("1 1 0 0 1", ' '), new int[]{2, 1}),
			new Tuple(new NumericalItemSet("1 1 0 0 1", ' '), new int[]{3, 1}),
			new Tuple(new NumericalItemSet("1 1 0 0 1", ' '), new int[]{3, 2}),
			new Tuple(new NumericalItemSet("0 0 0 1 0", ' '), new int[]{3, 3}),
			new Tuple(new NumericalItemSet("0 1 0 1 1", ' '), new int[]{2, 3})
		};
		data = new Data(tuples, new NumericalItemSet(new int[]{}));*/
		
		//data = Parser.parseAttributes("Corel5k-train.arff", 374);
		data = Parser.parseShortNotation("diabetes.txt", 1, new NumericalItemSet(new int[]{}));
		OpenBitSet bitSet = new OpenBitSet(data.getTuples().length);
		for(int i = 0; i < data.getTuples().length; i++)
			bitSet.set(i);
		dataSet = new DataSet(data, bitSet);
		s = dataSet.y();
		n = data.getTuples().length;
		m = data.getTuples()[0].getClassValues().length;
		
		/*double u = 0;
		long l = 0;
		for(int j = 0; j < 10; j++) {
			double t = System.currentTimeMillis();
			for(int i = 0; i < 100000; i++)
				u = data.getBluePrint().getOneItemSet(2, 1000).ub();
			l += (System.currentTimeMillis() - t);
		}
		System.out.println(((double)l/10));*/
	
		
		System.out.println("Data loaded, "+data.getTuples().length+" tuples, "+
				data.getTuples()[0].getItemSet().getLength()+" items, "+
				data.getTuples()[0].getClassValues().length+" class values");
				
		StopWatch.tic("Full time");
		System.out.println(icc());
		StopWatch.printToc("Full time");
	}
	
	private static ItemSet icc() {
		ItemSet bestItemSet = null;
		int itemNumber = data.getTuples()[0].getItemSet().getLength();
		double bestItemScore = 0.0;
		Set<ItemSet> Q1 = new HashSet<ItemSet>();
		Set<ItemSet> B1 = new HashSet<ItemSet>(Q1);
		System.out.println("ICC init, with "+itemNumber+" items");
		for(int j = 0; j < itemNumber; j++) {
			ItemSet itemSet = getData().getBluePrint().getOneItemSet(j, itemNumber);
			double newScore = iccUpdate(itemSet, Q1, bestItemScore);
			if(newScore != bestItemScore) {
				bestItemSet = itemSet;
				bestItemScore = newScore;
			}
		}
		
		System.out.println("ICC init finished");
		
		Set<ItemSet> QVorige = new HashSet<ItemSet>();
		QVorige.addAll(Q1);

		Set<ItemSet> QVolgende = QVorige;
				
		while(!QVolgende.isEmpty()) {
			QVolgende = new HashSet<ItemSet>();
			double t = System.currentTimeMillis();
			for(ItemSet B : Q1) {
				for(ItemSet Q : QVorige) {
					int lastSetBit = 0;
					for(int i = Q.getBitSet().nextSetBit(0); i >= 0; i = Q.getBitSet().nextSetBit(i+1))
						lastSetBit = i;

					if(lastSetBit < B.getBitSet().nextSetBit(0) && Q.ub() >= bestItemScore) {
						if(B.ub() < bestItemScore)
							B1.remove(B);
						else {
							ItemSet itemSet = Q.union(B);
							double newScore = iccUpdate(itemSet, QVolgende, bestItemScore);
							if(newScore != bestItemScore) {
								bestItemSet = itemSet;
								bestItemScore = newScore;
							}
						}
					}
				}
			}
			QVorige = QVolgende;
			System.out.println((System.currentTimeMillis()-t));
		}
		return bestItemSet;
	}
	
	
	private static double iccUpdate(ItemSet itemSet, Set<ItemSet> itemSets, double bestItemScore) {
		if(itemSet.ub() >= bestItemScore) {
			itemSets.add(itemSet);
			DataSet covered = dataSet.matching(itemSet);
			double itemSetValue = var(covered.getNumberOfTuples(), covered.y());
			if(itemSetValue > bestItemScore) {
				return itemSetValue;
			}
		}
		return bestItemScore;
	}
	
	
	public static double var(double x, int[] y) {
		if(x == n || x == 0)
			return 0.0;

		double sum1 = 0.0;
		double sum2 = 0.0;
		
		double n = data.getTuples().length;
		for(int i = 0; i < y.length; i++) {
			double s_i_over_n = ((double)s[i])/n;
			double h1 = ((double)y[i]/x) - s_i_over_n;
			sum1 += h1*h1;
			double h2 = (((double)s[i]-(double)y[i])/(n - x)) - s_i_over_n;
			sum2 += h2*h2;
		}
				
		return x*sum1 + (n-x)*sum2;
	}
}

/*
 * Data loaded, 768 tuples, 48 items, 1 class values
ICC init, with 48 items
ICC init finished
Next k
Next k
Next k
Next k
Next k
Next k
Next k
Next k
Next k
Next k
Next k
Next k
Next k
Next k
Next k
Next k
Next k
Next k
{0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 1 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 1 0 0 0 0}
Runtime in ms: 434816.0

 * */
