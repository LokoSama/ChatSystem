package Controller;

public class Debugger {
	
	private static boolean enabled = false;
	
	public static void log(String debugMessage) {
		if (Debugger.enabled)
			System.out.println("*** " + debugMessage);
	}
}
