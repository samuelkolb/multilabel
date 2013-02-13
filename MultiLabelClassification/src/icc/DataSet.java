package icc;
import java.util.ArrayList;


public class DataSet {
	
	private final Tuple[] tuples;
	public Tuple[] getTuples() {
		return tuples;
	}

	private final ItemSet bluePrint;

	public ItemSet getBluePrint() {
		return bluePrint;
	}

	public DataSet(Tuple[] tuples, ItemSet bluePrint) {
		this.tuples = tuples;
		this.bluePrint = bluePrint;
	}
	
	public DataSet matching(ItemSet itemSet) {
		ArrayList<Tuple> tuples = new ArrayList<Tuple>();
		//double t = System.currentTimeMillis();
		DataSet matching = null;
		//for(int i = 0; i < 1000; i++) {
		for(Tuple tuple : getTuples())
			if(itemSet.includes(tuple))
				tuples.add(tuple);
		matching = new DataSet(tuples.toArray(new Tuple[tuples.size()]), getBluePrint());
		/*}
		System.out.println(System.currentTimeMillis() - t);*/
		return matching;
	}
	
	public int[] y_i_1(int i) {
		int[] array = new int[tuples.length];
		for(int j = 0; j < array.length; j++)
			array[j] = getTuples()[j].getClassValues()[i];
		return array;
	}
	
	public int y(int i) {
		int sum = 0;
		for(int j = 0; j < getTuples().length; j++)
			sum += getTuples()[j].getClassValues()[i];
		return sum;
	}
	
	public int[] y() {

		if(getTuples().length < 1)
			return new int[]{};
		int m = getTuples()[0].getClassValues().length;
		int[] y = new int[m];
		for(int i = 0; i < m; i++)
			y[i] = y(i);
		return y;
	}
	
	public int s(int i) {
		int sum = 0;
		for(Tuple tuple : getTuples())
			sum += tuple.getClassValues()[i];
		return sum;
	}
	
	public int[] s() {
		int m = getTuples()[0].getClassValues().length;
		int[] s = new int[m];
		for(int i = 0; i < m; i++)
			s[i] = s(i);
		return s;
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for(Tuple tuple : getTuples())
			stringBuilder.append(tuple.toString()+"\n");
		return stringBuilder.toString();
	}
}
