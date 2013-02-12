package icc;
import java.util.Arrays;

public class Tuple {
	
	private final ItemSet itemSet;

	public ItemSet getItemSet() {
		return itemSet;
	}


	private final int[] classValues;
	
	public int[] getClassValues() {
		return classValues;
	}
	
	public Tuple(ItemSet itemSet, int[] classValues) {
		this.itemSet = itemSet;
		this.classValues = classValues;
	}
	
	@Override
	public String toString() {
		return getItemSet().toString()+Arrays.toString(getClassValues());
	}
}
