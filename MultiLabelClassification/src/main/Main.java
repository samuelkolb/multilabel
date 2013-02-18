package main;
import icc.CategoryUtility;
import icc.Data;
import icc.DataSet;
import icc.ItemSet;
import icc.NominalItemSet;
import icc.NumericalItemSet;
import icc.ScoreCalculator;
import icc.Tuple;
import icc.Variance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.apache.lucene.util.OpenBitSet;

import parsing.Parser;

// TODO test trees

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

	private static ScoreCalculator scoreCalculator;
	public static ScoreCalculator getScoreCalculator() {
		return scoreCalculator;
	}
	
	public static void main(String[] args) {
		// Sese and morichita example dataset
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
		data = new Data(tuples, new NumericalItemSet(new int[]{}));//*/
		
		// Bigger dataset
		//data = Parser.parseAttributes("Corel5k-train.arff", 374, new NominalItemSet(new int[]{}));
		
		// Diabetes dataset
		// Found: {0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 1 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 1 0 0 0 0}
		data = Parser.parseShortNotation("diabetes.txt", 1, new NominalItemSet(new int[]{}));
		
		// Init dataset as including all tuples data
		OpenBitSet bitSet = new OpenBitSet(data.getTuples().length);
		for(int i = 0; i < data.getTuples().length; i++)
			bitSet.set(i);
		dataSet = new DataSet(data, bitSet);
		s = dataSet.y();
		n = data.getTuples().length;
		scoreCalculator = new CategoryUtility(n, s);
		//scoreCalculator = new Variance(n, s);
		
		System.out.println("Data loaded, "+data.getTuples().length+" tuples, "+
				data.getTuples()[0].getItemSet().getLength()+" items, "+
				data.getTuples()[0].getClassValues().length+" class values");
			
		StopWatch.tic("Full time");
		ItemSet bestItemSet = icc();
		System.out.println(bestItemSet);
		System.out.println("#Covered: "+getDataSet().matching(bestItemSet).getNumberOfTuples());
		StopWatch.printToc("Full time");
	}
	
	public static ItemSet icc() {
		ItemSet bestItemSet = null;
		int itemNumber = data.getTuples()[0].getItemSet().getLength();
		double bestItemScore = 0.0;
		List<ItemSet> oneItemSets = new ArrayList<ItemSet>();
		for(int j = 0; j < itemNumber; j++) {
			ItemSet itemSet = getData().getBluePrint().getOneItemSet(j, itemNumber);
			double newScore = iccUpdate(itemSet, oneItemSets, bestItemScore);
			if(newScore != bestItemScore) {
				bestItemSet = itemSet;
				bestItemScore = newScore;
			}
		}		
		List<ItemSet> QVorige = new ArrayList<ItemSet>(oneItemSets);

		List<ItemSet> QVolgende = QVorige;
		List<ItemSet> oneItemSetsNext;

		ItemSet itemSet;
		while(!QVolgende.isEmpty()) {
			QVolgende = new ArrayList<ItemSet>();
			oneItemSetsNext = new ArrayList<ItemSet>();
			double t = System.currentTimeMillis();
			for(ItemSet B : oneItemSets) {
				for(ItemSet Q : QVorige) {
					int lastSetBit = 0;
					for(int i = Q.getBitSet().nextSetBit(0); i >= 0; i = Q.getBitSet().nextSetBit(i+1))
						lastSetBit = i;
					
					if(lastSetBit < B.getBitSet().nextSetBit(0) && Q.ub(getScoreCalculator()) > bestItemScore) {
						itemSet = Q.union(B);
						double newScore = iccUpdate(itemSet, QVolgende, bestItemScore);
						if(newScore != bestItemScore) {
							bestItemSet = itemSet;
							bestItemScore = newScore;
						}
					}
				}
				if(B.ub(getScoreCalculator()) > bestItemScore)
					oneItemSetsNext.add(B);
			}
			oneItemSets = oneItemSetsNext;
			QVorige = QVolgende;
			double timeTaken = System.currentTimeMillis()-t;
			System.out.println("Iteration took: "+timeTaken+"ms");
		}
		return bestItemSet;
	}
	
	private static double iccUpdate(ItemSet itemSet, Collection<ItemSet> itemSets, double bestItemScore) {
		double ub = itemSet.ub(getScoreCalculator());
		if(ub >= bestItemScore) {
			itemSets.add(itemSet);
			OpenBitSet dataBitSet = dataSet.matchingBitSet(itemSet);
			double itemSetValue = getScoreCalculator().getCombinedScore(DataSet.numberTuples(dataBitSet),
				DataSet.calculateY(dataBitSet, getData()));
			if(DataSet.numberTuples(dataBitSet) == 0)
				System.out.println("added");
			if(itemSetValue > bestItemScore) {
				return itemSetValue;
			}
		}
		return bestItemScore;
	}
}
