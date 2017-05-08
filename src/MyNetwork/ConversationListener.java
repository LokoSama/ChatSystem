package MyNetwork;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;

import Controller.Controller;
import Controller.Debugger;
import Network.Packet.Packet;
import Network.Packet.Text;

public class ConversationListener extends Thread {

	//Attributes
	private Conversation conv; //parent conversation
	private ObjectInputStream input;
	private boolean isRunning;

	public ConversationListener(ObjectInputStream input, Conversation conv) {
		this.input = input;
		this.conv = conv;
	}

	@Override
	public void run() {
		Packet pack;
		isRunning = true;
		
		while (isRunning) {
			try {
				Debugger.log("ConversationListener : waiting for packet");
				try {
					pack = (Packet) input.readObject();
				} catch (SocketException | EOFException e) {
					pack = null;
					isRunning = false;
				}
				if (pack != null)
					handlePacket(pack);
				Controller.shortWait();
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void handlePacket(Object pack) {
		Debugger.log("ConversationListener.handlePacket : receiving packet");
		if (pack instanceof Text) {
			Debugger.log("ConversationListener.handlePacket : received Text");
			conv.addMessage(((Text)pack).getData());
		} else if (pack instanceof Network.Packet.File) {
			Debugger.log("ConversationListener.handlePacket : received File");
			conv.addFile((Network.Packet.File)pack);
		} else {
			Debugger.log("Error in ConversationListener.handlePacket : packet type unclear");
		}
	}
	
	public void close() {
		try {
			input.close();
			isRunning = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
