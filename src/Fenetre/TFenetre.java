package Fenetre;

import java.io.IOException;

import javax.imageio.IIOException;
import javax.swing.JFrame;

public class TFenetre extends Thread {
	
	//Attributes
	/*private BufferedReader reader;
	private String lastLine;
	*/
	
	public TFenetre() {
	}
	
	public void run (JFrame windows) throws IOException{
		while (true) {
			//windows.refreshReceive();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}