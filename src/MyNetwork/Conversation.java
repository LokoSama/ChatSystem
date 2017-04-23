package MyNetwork;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Controller.Controller;
import Network.Packet.Packet;

public class Conversation {

	//Attributes
	private Socket sock;
	private Controller controller;
	
	private ConversationListener listener;
	private ObjectOutputStream output;
	
	private ArrayList<String> unreadMessages;
	
	public Conversation (Socket sock, Controller controller) {
		this.sock = sock;
		this.controller = controller;
		this.unreadMessages = new ArrayList<String>();

		try {
			this.listener = new ConversationListener((ObjectInputStream)sock.getInputStream(), controller);
			this.output = (ObjectOutputStream)sock.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		while(this.unreadMessages.isEmpty()) {} //wait for a message
		String msg = this.unreadMessages.get(0);
		this.unreadMessages.remove(0);
		return msg;
	}
	
	public void close() {
		//TODO : cleanup des sockets, des streams et du thread ConversationListener associe
	}
	
}
