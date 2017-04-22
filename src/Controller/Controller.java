package Controller;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import Fenetre.FenetreMsg;
import Fenetre.View;
import Model.Model;
import Model.Status;
import Model.User;
import MyNetwork.Conversation;
import Network.Packet.Control;
import Network.Packet.File;
import Network.Packet.Message;
import Network.Packet.Notification;
import Network.Packet.Packet;
import Network.Packet.Text;


public class Controller {
	//attributes
	//Modele
	Model model;
	View view ;
	//UDP networking
	private DatagramSocket UDPlisten; //UDP sockets for negotiating the TCP port to be used. UDPlisten always has port number LOCAL_LISTEN_PORT.
	private DatagramSocket UDPtalk;
	private final int LOCAL_LISTEN_PORT = 2042;
	private final int REMOTE_LISTEN_PORT = 2402;
	
	//TCP networking
	private ServerSocket serverSock;
	private HashMap<User,Conversation> convList; //contains all open Conversations
	
	
	//Constructeur
	public Controller() {
		this.model = new Model();
		this.view = new View(this.model,this);
		model.addObserver(view);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addUser("le nouveau", InetAddress.getLoopbackAddress(), Status.Busy); //permet de tester le pattern Observable Observer
		
		initNetwork();
	}
	
	
	private void initNetwork() {
		//We use 2 UDP sockets to accept incoming HELLO requests and answer with a port number
		//We use TCP Conversations to handle each conversation separately, stored in a HashMap
		try {
			UDPlisten = new DatagramSocket(LOCAL_LISTEN_PORT);
			UDPtalk = new DatagramSocket(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		convList = new HashMap<User,Conversation>();
	}
	
	public void sendText(User dest, String data) {
		Packet pack = new Text(this.getLocalUser().getUsername(), dest.getUsername(), model.getLocalIP(), dest.getIP(), data);
		Conversation conv = convList.get(dest);
		conv.send(pack);
	}
	
	public void send(User dest, Packet pack) {
		Conversation conv = convList.get(dest);
		
		if (pack instanceof Control) {
			//TODO
		} else if (pack instanceof File) {	
			//TODO
		} else if (pack instanceof Message) {
			//TODO
		} else if (pack instanceof Notification) {
			//TODO
		} else if (pack instanceof Text) {
			//done sendText
		} else {
			System.out.println("Error in Controller.send : packet type unclear");
		}
	}


	public void launchChatWith(User user) {
		if (convList.containsKey(user)) {
			//TODO : notifier l'utilisateur qu'il a déjà une conv en cours avec l'autre et/ou réafficher la fenêtre de conv
		} else {
			//TODO: determiner port distant et annoncer port local (ici 424242 en placeholder)
			int port = this.negotiatePort(user, 424242);

			Socket sock;
			try {
				sock = new Socket(user.getIP(), port);
				convList.put(user, new Conversation(sock, this));	//on crée une nouvelle conversation et on l'add à la liste
				(new Thread( new FenetreMsg(user) )).start(); 		//on ouvre une fenêtre pour la conversation
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private int negotiatePort(User dest, int port) {
		//TODO : finir ça, il envoie actuellement sont propre port mais ne récupère pas le port de l'autre
		InetAddress address = dest.getIP() ;//see InetAddress API
		
		Control control_packet = new Control(model.getLocalUser().getUsername(), dest.getUsername(), model.getLocalUser().getIP(), dest.getIP(), Control.Control_t.HELLO, port);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream os;
		try {
			os = new ObjectOutputStream(outputStream);
			os.writeObject(control_packet);
			byte[] buffer = outputStream.toByteArray();
			
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, REMOTE_LISTEN_PORT);
			UDPtalk.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0; //RETURN LE BON PORT ICI
	}
	
	public User getLocalUser() {
		return model.getLocalUser();
	}
	
	public InetAddress getLocalIP() {
		return model.getLocalIP();
	}
	
	public void setLocalUser(String name) {
		model.setLocalUser(name);
	}

	public View getView(){
		return view ;
	}

	public Model getModel() {	
		return model ;
	}

	public void showMessage(String message) {
		//TODO
	}


}
