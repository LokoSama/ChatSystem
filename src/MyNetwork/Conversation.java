package MyNetwork;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Controller.Controller;
import Controller.Debugger;
import Network.Packet.Packet;

public class Conversation {

	//Attributes
	private ConversationListener listener;
	private ObjectOutputStream output;
	private Socket sock;

	private ArrayList<Network.Packet.File> unopenedFiles;
	private ArrayList<String> unreadMessages;

	private final String workDir = System.getProperty("user.dir") + File.separator;

	
	public Conversation (Socket sock) {
		Debugger.log("Conversation : constructing");
		this.sock = sock;
		this.unreadMessages = new ArrayList<String>();
		this.unopenedFiles = new ArrayList<Network.Packet.File>();

		try {
			this.output = new ObjectOutputStream(sock.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
			this.listener = new ConversationListener(ois, this);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Debugger.log("Conversation : launching listener thread");
		listener.start();
	}

	public void addFile(Network.Packet.File pack) {
		this.unopenedFiles.add(pack);
	}

	public void addMessage(String message) {
		this.unreadMessages.add(message);
	}

	//main function : handles incoming packets
	public String getMessage() {
		FileOutputStream fos;
		String msg;
		String convDir;

		while(this.unreadMessages.isEmpty() && this.unopenedFiles.isEmpty()) {
			Controller.shortWait();
		} //wait for an incoming message or file

		//if there are no unreadMessages, it means we have a file to open : thus messages have higher priority than files (we only read files if there are no messages to be read)
		if (this.unreadMessages.isEmpty()) {
			Network.Packet.File incFile = this.unopenedFiles.get(0);
			this.unopenedFiles.remove(0);

			convDir = workDir + "from" + incFile.getPseudoSource() + File.separator; //received files are written to where the program is executed
			File f = new File(convDir + incFile.getFileName());
			
			long i = 0;
			while (f.exists() || f.isDirectory()) {
				i++;
				f = new File(convDir + "(" + i + ")" + incFile.getFileName());
			}
			
			Debugger.log("Final filepath: " + f.getAbsolutePath());
			//creates the necessary directories if needed
			(new File(convDir)).mkdirs();

			try {
				fos = new FileOutputStream(f.getAbsolutePath(), false);
				fos.write(incFile.getContent());
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			msg = "Sent file " + incFile.getFileName() + printSize(incFile.getSize()); //we print something either way
		} else { //handle a text message
			msg = this.unreadMessages.get(0);
			this.unreadMessages.remove(0);
		}
		return msg;
	}

	public void send(Packet pack) {
		try {
			output.writeObject(pack);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			Debugger.log("Closing conversation");
			listener.close();
			output.close();
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//this function returns a string containing the given size (in bytes) in a human readable format
	private String printSize(double byteSize) {
		String unit = "B";
		double factor = 1;

		if (byteSize >= 1000) {
			unit = "KB";
			factor *= 1000;

			if (byteSize >= 1000000) {
				unit = "MB";
				factor *= 1000;

				if (byteSize >= 1000000000) {
					unit = "GB";
					factor *= 1000;
				}
			}
		}
		double newSize = Math.round(byteSize * 100 / factor);
		newSize /= 100;

		return " (" + newSize + unit + ")";
	}

}
