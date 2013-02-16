package PCT;

import icc.DataSet;

import java.util.Random;

import org.apache.lucene.util.OpenBitSet;

public class PCTTest extends junit.framework.TestCase {
	
	OpenBitSet bitSet;

	protected void setUp() throws Exception {
		int length = 5;
		bitSet = new OpenBitSet(length);
		Random random = new Random();
		for(int i = 0; i < length; i++){
			int bit = random.nextInt(2);
			if(bit == 1)
				bitSet.fastSet(i);
		}
	}
	
	public void testNegate(){
		System.out.print("bitset: ");
		for(int i = 0; i < bitSet.length(); i++){
			System.out.print(bitSet.getBit(i));
		}
		System.out.println();
		DataSet dataSet = new DataSet(null, bitSet);
		OpenBitSet negation = dataSet.negate(bitSet);
		System.out.print("negation: ");
		for(int i = 0; i < negation.length(); i++){
			System.out.print(negation.getBit(i));
		}
	}

}
