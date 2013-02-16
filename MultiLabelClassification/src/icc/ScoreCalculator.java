package icc;

public abstract class ScoreCalculator {

	private final int n;
	private final int[] m;
	
	public ScoreCalculator(int n, int[] m) {
		this.n = n;
		this.m = m;
	}
	
	public double getScore(double x, double y, int classAttributeIndex) {
		return getScoreWithDoubles(x, n, y, m[classAttributeIndex]);
	}
	
	public double getCombinedScore(int x, int[] y) {
		double sum = 0.0;
		for(int i = 0; i < m.length; i++)
			sum += getScoreWithDoubles(x, n, y[i], m[i]);
		return sum;
	}
	
	protected abstract double getScoreWithDoubles(double x, double n, double y, double m);
}
