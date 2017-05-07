package MyNetwork;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;

import Controller.Controller;
import Controller.Debugger;
import Network.Packet.Control;
import Network.Packet.File;
import Network.Packet.Notification;
import Network.Packet.Packet;
import Network.Packet.Text;

public class ConversationListener extends Thread {

	//Attributes
	private ObjectInputStream input;
	private Conversation conv;
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
				Controller.Tempo(30);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void handlePacket(Object pack) {
		Debugger.log("ConversationListener.handlePacket : receiving packet");
		if (pack instanceof Control) {
			Debugger.log("ConversationListener.handlePacket : received Control");
			//TODO
		} else if (pack instanceof File) {
			Debugger.log("ConversationListener.handlePacket : received File");
			//TODO
		} else if (pack instanceof Notification) {
			Debugger.log("ConversationListener.handlePacket : received Notification");
			//TODO
		} else if (pack instanceof Text) {
			Debugger.log("ConversationListener.handlePacket : received Text");
			conv.addMessage(((Text)pack).getData());
		} else {
			System.out.println("Error in ConversationListener.handlePacket : packet type unclear");
		}
	}

	public void close() {
		try {
			input.close();
			isRunning = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
