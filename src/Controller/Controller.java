package Controller;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Fenetre.View;
import Model.Model;
import Model.Status;
import Model.User;
import MyNetwork.Conversation;
import MyNetwork.Tools;
import MyNetwork.UDPListener;
import Network.Packet.Control;
import Network.Packet.Notification;
import Network.Packet.Notification.Notification_type;
import Network.Packet.Packet;
import Network.Packet.Text;


public class Controller {

	//Attributes
	//General
	private Model model;
	private View view ;
	//UDP networking
	private DatagramSocket UDPlisten; //UDP sockets for negotiating the TCP port to be used. UDPlisten always has port number LOCAL_LISTEN_PORT.
	private DatagramSocket UDPtalk;
	private boolean newACKPacket;
	private Control newPacket;
	private int LOCAL_LISTEN_PORT; //these ports have to be identical for every ChatSystem user as soon as there are more than 2
	private int REMOTE_LISTEN_PORT;	
	//TCP networking
	private HashMap<User,Conversation> convList; //stores all open Conversations


	public static void shortWait() {
		try {
			Thread.sleep(15);
		} catch (InterruptedException e) {
		}
	}

	public static void mediumWait() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}
	}

	//Constructor
	public Controller(int port) {

		//checks if he should launch as user 1, user 2 or single user (which keeps the default ports)
		if (port == 1) {
			LOCAL_LISTEN_PORT = 15231;
			REMOTE_LISTEN_PORT = 17452;
		}
		else if (port == 2) {
			LOCAL_LISTEN_PORT = 17452;
			REMOTE_LISTEN_PORT = 15231;
		} else if (port > 1023) {
			LOCAL_LISTEN_PORT = port;
			REMOTE_LISTEN_PORT = port;
		} else {
			throw new IllegalArgumentException();
		}

		this.model = new Model();
		this.view = new View(this.model,this);
		model.addObserver(view);

		initNetwork();
	}



	public void addConversation(User remoteUser, Socket sock) {
		//if the ConvList already contains a conv with this user, it will be replaced/updated (which is probably better behavior than ignoring it)
		convList.put(remoteUser, new Conversation(sock));
		view.launchFenetreMsg(remoteUser);
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
				this.negotiatePort(remoteUser, servSock.getLocalPort()); //returns the port the other user will create a socket on ; not used, we can get the socket directly
				Debugger.log("Controller.launchChatWith : establishing connection to " + remoteUser.getUsername());
				sock = servSock.accept();

				addConversation(remoteUser, sock); //creates a new Conversation + fenetreMsg
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Debugger.log("Controller.launchChatWith: error binding the socket");
				e.printStackTrace();
			}
		}
	}

	//NETWORKING MEHODS (reception is handled in the Conversations)
	private void initNetwork() {
		this.newACKPacket = false;
		//We use 2 UDP sockets to accept incoming HELLO requests and answer with a port number
		//We use TCP Conversations to handle each conversation separately, stored in a HashMap
		try {
			UDPlisten = new DatagramSocket(LOCAL_LISTEN_PORT);
			UDPtalk = new DatagramSocket(0);
		} catch (IOException e) {
			e.printStackTrace();
		}

		convList = new HashMap<User,Conversation>();

		UDPListener UDPL = new UDPListener(UDPlisten, this); //separate thread to listen to incoming UDP datagrams
		while (! model.userIsConnected())
			Controller.mediumWait();
		UDPL.start();
	}

	public void ACKPacketreceived(Control c) {
		while(this.newACKPacket) //wait if there is an unhandled packet
			Controller.shortWait();
		this.newPacket = c;
		this.newACKPacket = true;
	}
	
	//TCP sending
	public void sendText(User dest, String data) {
		Debugger.log("Controller.sendText : sending");
		Text pack = new Text(this.getLocalUser().getUsername(), dest.getUsername(), model.getLocalIP(), dest.getIP(), data);
		Conversation conv = convList.get(dest);
		conv.send(pack);
	}

	public void sendFile(User dest, String strPath) {
		Debugger.log("Controller.sendFile : sending");
		File f = new File(strPath);
		Path filePath = Paths.get(strPath);
		byte[] content;
		Network.Packet.File pack = null;

		if (f.exists() && Files.isReadable(filePath)) {
			try {
				content = Files.readAllBytes(filePath);

				pack = new Network.Packet.File(this.getLocalUser().getUsername(), dest.getUsername(), model.getLocalIP(),
						dest.getIP(), f.getName(), Files.probeContentType(filePath), Files.size(filePath), content);
			} catch (IOException e) {
				Debugger.log("ERROR in Controller.sendFile : could not read file content");
				e.printStackTrace();
			}
		}
		Conversation conv = convList.get(dest);
		conv.send(pack);
	}

	//(slightly) generic function to send a Packet (either Notification, or Control) via UDP
	private void sendUDP(InetAddress destIP, Packet genericPack) {
		if (genericPack instanceof Notification || genericPack instanceof Control) {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutputStream os;
			try {
				os = new ObjectOutputStream(outputStream);
				os.writeObject(genericPack);
				byte[] buffer = outputStream.toByteArray();

				DatagramPacket UDPpacket = new DatagramPacket(buffer, buffer.length, destIP, REMOTE_LISTEN_PORT);
				UDPtalk.send(UDPpacket);
				Debugger.log("sendUDP : " + (genericPack instanceof Notification ? "Notification" : "Control") + " sent");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("ERROR in Controller.sendUDP : could not create/send datagram");
			}
		} else {
			Debugger.log("ERROR in Controller.sendUDP : expected Control or Notification object as argument");
		}
	}

	public void sendControl(User dest, Control.Control_t type, int data) {
		Control pack = new Control(this.getLocalUser().getUsername(), dest.getUsername(), this.getLocalUser().getIP(), dest.getIP(), type, data);
		this.sendUDP(dest.getIP(), pack);
	}

	public void sendNotif(User dest, Notification_type type, String data) {
		Notification pack = new Notification(this.getLocalUser().getUsername(), dest.getUsername(), model.getLocalIP(), dest.getIP(), type, data);
		this.sendUDP(dest.getIP(), pack);
	}

	//port negotiation
	private int negotiatePort(User dest, int localPort) {
		Debugger.log("Controller.negotiatePort: starting");

		//creates the packet to be sent with our local port number as data
		Control controlPacket = new Control(this.getLocalUser().getUsername(), dest.getUsername(), this.getLocalUser().getIP(), dest.getIP(), Control.Control_t.HELLO, localPort);

		this.sendUDP(dest.getIP(), controlPacket);
		Debugger.log("Controller.negotiatePort: HELLO sent, waiting for ACK");

		//wait for ACK
		do {
			while (this.newACKPacket == false) {
				Controller.shortWait();
			} //wait for a new packet
			Debugger.log("Controller.negotiatePort: UDP Control received, type is " + this.newPacket.getType());
		} while (this.newPacket.getType() != Control.Control_t.ACK); //loop back to waiting if it is not an ACK

		int remotePort = this.newPacket.getData();
		newACKPacket = false;

		return remotePort;
	}
	
	//sends a Notification of a type to every user on the local network
	public void broadcastNotification(Notification.Notification_type type, String data) {
		Notification notification = new Notification(this.getLocalUser().getUsername(), "bcast", this.getLocalUser().getIP(), Tools.getBroadcastAddress(), type, data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream os = null;
		try {
			os = new ObjectOutputStream(outputStream);
			os.writeObject(notification);
			byte[] buffer = outputStream.toByteArray();
			DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, Tools.getBroadcastAddress(), REMOTE_LISTEN_PORT);
			UDPtalk.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//ACCESS TO VIEW AND MODEL : GETTERS
	public User getLocalUser() {
		return this.model.getLocalUser();
	}

	public String getMessageFrom(User remoteUser) {
		return this.convList.get(remoteUser).getMessage();
	}

	public User getUser(String pseudoSource, InetAddress addrSource) {
		return this.model.getUser(pseudoSource, addrSource);
	}

	public void printNotif(String string) {
		this.view.printNotif(string);
	}

	public void addUser(String pseudoSource, InetAddress addrSource, Status status) {
		this.model.addUser(pseudoSource, addrSource, status);
	}

	public Status statusFromString(String data) {
		return this.model.statusFromString(data);
	}

	public void setStatus(String pseudoSource, InetAddress addrSource, Status newStatus) {
		this.model.setStatus(pseudoSource, addrSource, newStatus);
	}

	public void deleteUser(String pseudoSource, InetAddress addrSource) {
		this.model.deleteUser(pseudoSource, addrSource);
	}
	
	//ACCESS TO VIEW AND MODEL : SETTERS
	public void setLocalStatus(Status status){
		model.setLocalStatus(status);

		String msg = status.name();
		this.broadcastNotification(Notification_type.STATUS_CHANGE, msg);
	}

	public void setLocalUser(String name) {
		model.setLocalUser(name);
	}
	

	//CLOSING/CLEANUP METHODS
	public void closeSoft() {
		Debugger.log("Closing ChatSystem");
		this.broadcastNotification(Notification_type.DISCONNECT, null);
		UDPListener.close();
		this.closeAllConversations();
		this.UDPlisten.close();
		this.UDPtalk.close();
	}

	private void closeAllConversations() {
		Iterator<Map.Entry<User,Conversation>> it = convList.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<User, Conversation> pair = (Map.Entry<User, Conversation>)it.next();
			pair.getValue().close();
			it.remove(); //avoids a ConcurrentModificationException
		}
	}

	public void closeFenetreMsg(User u) {
		convList.get(u).close();
		this.convList.remove(u);
	}
}
