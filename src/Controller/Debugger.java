package Controller;

public class Debugger {
	
	private static boolean enabled = true;
	
	public static void log(String debugMessage) {
		if (Debugger.enabled)
			System.out.println("***** " + debugMessage);
	}
}
