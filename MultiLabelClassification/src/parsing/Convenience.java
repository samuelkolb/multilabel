package parsing;

public class Convenience {
	
	public static int[] arrayFromString(String string, char delimiter) {
		int[] array = new int[(string.length() + 1)/2];
		for(int i = 0; i < string.length(); i++)
			if(string.charAt(i) != delimiter)
					array[i/2] = Integer.parseInt(Character.toString(string.charAt(i)));
		return array;
	}
	
	public static int[] quickSplit(String string) {
		int[] array = new int[(string.length() + 1)/2];
		for(int i = 0; i < string.length(); i += 2)
			array[i/2] = string.charAt(i) - '0';
		return array;
	}
}
