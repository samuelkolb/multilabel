package main;
import java.util.HashMap;


public class StopWatch {
	
	private static HashMap<String, Long> startingTimes = new HashMap<String, Long>();
	
	public static void tic() {
		tic("DEFAULT");
	}
	
	public static void tic(String name) {
		startingTimes.put(name, System.currentTimeMillis());
	}
	
	public static long toc() {
		return toc("DEFAULT");
	}
	
	public static long toc(String name) {
		return System.currentTimeMillis() - startingTimes.get(name);
	}
	
	public static void printToc() {
		printToc("DEFAULT");
	}
	
	public static void printToc(String name) {
		System.out.println(name+": "+toc(name)+" ms");
	}
}
