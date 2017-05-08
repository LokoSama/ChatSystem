package Main;
import Controller.Controller;

public class Chat {

	public static void main(String[] args) {

		if (args.length == 0)			//if no arguments are specified, the ChatSystem is launched on the default port
			new Controller(2042);
		else if (args.length == 1) {	//if one argument is specified
			int port = Integer.parseInt(args[0]);
			if (port > 1023)				//if the port is 1024 or more, use it to launch ChatSystem
				new Controller(port);
			if (port == 1 || port == 2)		//if the port is 1 or 2, launch a ChatSystem accordingly (specific ports are used to use 2 ChatSystems in local)
				new Controller(port);
		}
		else							//if more than 1 argument is specified or if the arguments are invalid, print usage
			printUsage();
	}

	private static void printUsage() {
		System.out.println("USAGE: PROGRAM [ 1 | 2 | PORT ]");
	}
}
