package icc;

public class CategoryUtility extends ScoreCalculator {

	public CategoryUtility(int n, int[] m) {
		super(n, m);
	}

	@Override
	protected double getScoreWithDoubles(double x, double n, double y, double m) {
		return 0.5*(x/n*(Math.pow(y/x, 2) - Math.pow(m/n, 2) + Math.pow((x-y)/x, 2) - Math.pow((n-m)/n, 2)) +
			((n-x)/n)*(Math.pow((m-y)/(n-x), 2) - Math.pow(m/n, 2) + Math.pow((n-m-x+y)/(n-x), 2) - Math.pow((n-m)/n, 2)));
	}
}
