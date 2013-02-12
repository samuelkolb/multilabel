package parsing;

import icc.DataSet;
import icc.ItemSet;
import icc.Tuple;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {
	
	public static DataSet parseAttributes(String filename, int cutoff, ItemSet bluePrint) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String string;
			ArrayList<Integer> valuesPerAttribute = new ArrayList<Integer>();
			while((string = reader.readLine()) != null && !string.startsWith("@data")) {
				if(string.startsWith("@attribute")) {
					String bounds = string.split("\\{")[1].split("\\}")[0];
					int numberValues = (bounds.length()+1)/2;
					valuesPerAttribute.add(numberValues);
				}
			}
			int numberOfItems = 0;
			int numberOfObjectiveValues = 0;
			for(int i = 0; i < valuesPerAttribute.size(); i++) {
				if(i == cutoff)
					numberOfObjectiveValues = numberOfItems;
				numberOfItems += valuesPerAttribute.get(i);
			}
			
			ArrayList<int[]> values = new ArrayList<int[]>();
			while((string = reader.readLine()) != null) {
				int[] ints = Convenience.quickSplit(string);
				int index = 0;
				int[] array = new int[numberOfItems];
				for(int i = 0; i < ints.length; i++) {
					for(int j = 0; j < valuesPerAttribute.get(i); j++, index++) {
						if(j == ints[i])
							array[index] = 1;
						else
							array[index] = 0;
					}
				}
				values.add(array);
			}
			
			Tuple[] tuples = new Tuple[values.size()];
			for(int i = 0; i < tuples.length; i++) {
				int[] items = new int[numberOfItems - numberOfObjectiveValues];
				int[] objectiveAttributes = new int[numberOfObjectiveValues];
				for(int j = 0; j < numberOfItems; j++) {
					if(j < numberOfItems - numberOfObjectiveValues)
						items[j] = values.get(i)[j];
					else
						objectiveAttributes[j+numberOfObjectiveValues-numberOfItems] = values.get(i)[j];
				}
				tuples[i] = new Tuple(bluePrint.getInstance(items), objectiveAttributes);
			}
			
			return new DataSet(tuples, bluePrint);
			
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("File not found");
		} catch (IOException e) {
			throw new IllegalArgumentException("IO exception");
		}
	}
	
	public static DataSet parseShortNotation(String filename, int numberClassValues, ItemSet bluePrint) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filename));
			String string;
			ArrayList<int[]> valuesPerAttribute = new ArrayList<int[]>();
			int max = 0;
			while((string = reader.readLine()) != null) {
				String[] array = string.split(" ");
				int localMax = Integer.parseInt(array[array.length-numberClassValues-1]);
				if(localMax > max)
					max = localMax;
				int[] numbers = new int[array.length];
				for(int i = 0; i < array.length; i++)
					numbers[i] = Integer.parseInt(array[i]);
				valuesPerAttribute.add(numbers);
			}
			Tuple[] tuples = new Tuple[valuesPerAttribute.size()];
			int tupleNumber = 0;
			for(int[] values : valuesPerAttribute) {
				int[] attributes = new int[max];
				int[] classValues = new int[numberClassValues];
				for(int i = 0; i < values.length; i++)
					if(i < values.length-numberClassValues)
						attributes[values[i]-1] = 1;
					else
						classValues[i-values.length+numberClassValues] = values[i];
				tuples[tupleNumber++] = new Tuple(bluePrint.getInstance(attributes), classValues);
			}
			return new DataSet(tuples, bluePrint);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("File not found");
		} catch (IOException e) {
			throw new IllegalArgumentException("IO exception");
		}
	}
}
