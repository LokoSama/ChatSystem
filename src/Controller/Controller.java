package Controller;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import Fenetre.FenetreMsg;
import Fenetre.View;
import Model.Model;
import Model.Status;
import Model.User;
import MyNetwork.Conversation;
import MyNetwork.UDPListener;
import Network.Packet.Control;
import Network.Packet.File;
import Network.Packet.Message;
import Network.Packet.Notification;
import Network.Packet.Packet;
import Network.Packet.Text;

/*
 * TODO :
 * - Découverte du réseau à la connexion
 * - Gestion notifications
 * - Gestion statut (affichage, changement, notification des autres)
 * - Déconnexion (cleanup + notif disconnect)
 * - Multicast
 * - Fichiers ?
 */

public class Controller {
	//attributes
	//Modele
	Model model;
	View view ;
	//UDP networking
	private DatagramSocket UDPlisten; //UDP sockets for negotiating the TCP port to be used. UDPlisten always has port number LOCAL_LISTEN_PORT.
	private DatagramSocket UDPtalk;
	private int LOCAL_LISTEN_PORT = 2042;
	private int REMOTE_LISTEN_PORT = 2042;
	private boolean newUDPPacket;
	private Control newPacket;

	//TCP networking
	private HashMap<User,Conversation> convList; //contains all open Conversations

	//Constructeur
	public Controller(String arg0) {
		//POUR LANCER 2 CLIENTS SUR UN PC, A ENLEVER APRES
		// + remettre LOCAL_LISTEN_PORT et REMOTE_LISTEN_PORT en "final" (constante)
		int userNumber = Integer.parseInt(arg0);
		if (userNumber == 1) {
			LOCAL_LISTEN_PORT = 15231;
			REMOTE_LISTEN_PORT = 17452;
		} else {
			LOCAL_LISTEN_PORT = 17452;
			REMOTE_LISTEN_PORT = 15231;
		}
		
		this.model = new Model();
		this.view = new View(this.model,this);
		model.addObserver(view);
		
		/*
		//FIXME: il faut se login vite, sinon le addUser decede parce qu'on est pas co
		Tempo(10000);
		model.addUser("le nouveau", InetAddress.getLoopbackAddress(), Status.Busy); //permet de tester le pattern Observable Observer
		Tempo(10000);
		model.setStatus("le nouveau", InetAddress.getLoopbackAddress(), Status.Online);
		Tempo(10000);
		model.deleteUser("le nouveau", InetAddress.getLoopbackAddress());
		Tempo(10000);*/
		
		initNetwork();
	}
	
	
	public static void Tempo(int time) { //pour les tests
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initNetwork() {
		this.newUDPPacket = false;
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
		
		UDPListener UDPL = new UDPListener(UDPlisten, this); //separate thread to listen to incoming UDP datagrams
		UDPL.start();
	}

	public void sendText(User dest, String data) {
		Debugger.log("Controller.sendText : sending");
		Text pack = new Text(this.getLocalUser().getUsername(), dest.getUsername(), this.getLocalIP(), dest.getIP(), data);
		Conversation conv = convList.get(dest);
		conv.send(pack);
	}

	public void sendControl(User dest, Control.Control_t type, int data) {
		Control controlPacket = new Control(this.getLocalUser().getUsername(), dest.getUsername(), this.getLocalUser().getIP(), dest.getIP(), type, data);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream os;
		try {
			os = new ObjectOutputStream(outputStream);
			os.writeObject(controlPacket);
			byte[] buffer = outputStream.toByteArray();

			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, dest.getIP(), REMOTE_LISTEN_PORT);
			UDPtalk.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void send(User dest, Packet pack) {

		if (pack instanceof Control) {
			//done sendControl
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


	public void launchChatWith(User remoteUser) {
		Debugger.log("Controller.launchChatWith: start");
		if (convList.containsKey(remoteUser)) {
			//TODO : notifier l'utilisateur qu'il a deja une conv en cours avec l'autre et/ou reafficher la fenetre de conv
		} else {

			Socket sock;
			ServerSocket servSock;

			try {
				servSock = new ServerSocket(0);
				int remotePort = this.negotiatePort(remoteUser, servSock.getLocalPort());
				Debugger.log("launchChatWith : socket server waiting");
				sock = servSock.accept();
				
				Debugger.log("Controller.launchChatWith: trying to negotiate port");

				//InetSocketAddress remoteSocketAddr = new InetSocketAddress(remoteUser.getIP(), remotePort);
				//sock.connect(remoteSocketAddr);

				addConversation(remoteUser, sock); //creates a new Conversation + fenetreMsg
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Debugger.log("Controller.launchChatWith: error binding the socket");
				e.printStackTrace();
			}
		}
	}

	public void addConversation(User remoteUser, Socket sock) {
		//if the ConvList already contains a conv with this user, it will be replaced/updated (which is probably better behavior than ignoring it)
		convList.put(remoteUser, new Conversation(sock, this));
		view.launchFenetreMsg(remoteUser);
	}

	private int negotiatePort(User dest, int localPort) {
		Debugger.log("Controller.negotiatePort: starting");

		//creates the packet to be sent with our local port number as data
		Control controlPacket = new Control(this.getLocalUser().getUsername(), dest.getUsername(), this.getLocalUser().getIP(), dest.getIP(), Control.Control_t.HELLO, localPort);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream os;
		try {
			os = new ObjectOutputStream(outputStream);
			os.writeObject(controlPacket);
			byte[] buffer = outputStream.toByteArray();

			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, dest.getIP(), REMOTE_LISTEN_PORT);
			UDPtalk.send(packet);
			Debugger.log("Controller.negotiatePort: HELLO sent, waiting for ACK");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//wait for ACK
		do {
			Debugger.log("BLOUBLOU");
			while (this.newUDPPacket == false) {
				Tempo(500);
			} //wait for a new packet
			Debugger.log("Controller.negotiatePort: UDP Control received, type is " + this.newPacket.getType());
		} while (this.newPacket.getType() != Control.Control_t.ACK); //loop back to waiting if it is not an ACK

		int remotePort = this.newPacket.getData();
		newUDPPacket = false;

		return remotePort;
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
	
	public void setLocalStatus(Status status){
		model.setLocalStatus(status);
	}

	public View getView(){
		return view ;
	}

	public Model getModel() {	
		return model ;
	}

	public void ACKPacketreceived(Control c) {
		while(this.newUDPPacket) {} //wait if there is an unhandled packet
		this.newPacket = c;
		this.newUDPPacket = true;
	}

	public String getMessageFrom(User remoteUser) {
		return this.convList.get(remoteUser).getMessage();
	}
}
