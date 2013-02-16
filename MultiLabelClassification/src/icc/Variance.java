package icc;

public class Variance extends ScoreCalculator {

	public Variance(int n, int[] m) {
		super(n, m);
	}

	@Override
	protected double getScoreWithDoubles(double x, double n, double y, double m) {
		double result = x*Math.pow(y/x-m/n, 2)+(n-x)*Math.pow((m-y)/(n-x)-m/n, 2);
		return result;
	}

}
