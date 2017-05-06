package MyNetwork;

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
	private Socket sock;
	private Controller controller;

	private ConversationListener listener;
	private ObjectOutputStream output;

	private ArrayList<String> unreadMessages;

	public Conversation (Socket sock, Controller controller) {
		Debugger.log("Conversation : constructing");
		this.sock = sock;
		this.controller = controller;
		this.unreadMessages = new ArrayList<String>();

		try {
			this.output = new ObjectOutputStream(sock.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
			this.listener = new ConversationListener(ois, this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Debugger.log("Conversation : launching listener thread");
		listener.start();
	}

	public void send(Packet pack) {
		try {
			output.writeObject(pack);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addMessage(String message) {
		this.unreadMessages.add(message);
	}

	public String getMessage() {
		while(this.unreadMessages.isEmpty()) {
			Controller.Tempo(500);
		} //wait for a message
		String msg = this.unreadMessages.get(0);
		this.unreadMessages.remove(0);
		return msg;
	}

	public void close() {
		//TODO : cleanup des sockets, des streams et du thread ConversationListener associe
		try {
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
