package MyNetwork;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import Controller.Controller;
import Model.User;
import Network.Packet.Control;
import Network.Packet.File;
import Network.Packet.Message;
import Network.Packet.Notification;
import Network.Packet.Packet;
import Network.Packet.Text;

public class Conversation {

	//Attributes
	private Socket sock;
	private Controller controller;
	
	private ConversationListener listener;
	private ObjectOutputStream output;
	
	public Conversation (Socket sock, Controller controller) {
		this.sock = sock;
		this.controller = controller;

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
	
	public void close() {
		//TODO : cleanup des sockets, des streams et du thread ConversationListener associé
	}
	
}
