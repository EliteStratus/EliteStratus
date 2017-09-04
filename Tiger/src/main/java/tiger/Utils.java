package tiger;

public class Utils {
	
	public static String raplace(String in, String match, String replace) { // Not Replace
		while(in.contains(match)){
			in = in.replace(match, replace);
		}
		return in;
	}

	public static void sleep(int time){	
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {}
	}
}
