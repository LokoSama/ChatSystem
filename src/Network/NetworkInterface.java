package Network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

import Controller.Controller;
import Model.User;
import Network.Packet.Control;
import Network.Packet.Notification;


/**
 * @author alex205
 */
public class NetworkInterface {
	private static int helloPort = 20573;
	public static int basePort = 20574;

	private boolean upToDate = true;
	private Controller controller;

	//Les sockets de base pour négocier sur quel port se fera la communication
	private DatagramSocket anouk; //sert à parler
	private DatagramSocket hello; //sert à écouter

	//Listener pour hello
	private HelloListener helloListener;

	//Table de correspondance entre pseudo du contact et socket
	private HashMap<User, Socket> socketMap;

	// La network interface est un singleton parce qu'il faut en instancier qu'une seule !

	//Le holder
	private static class NetworkInterfaceHolder {
		private final static NetworkInterface instance = new NetworkInterface();
	}

	//pour récupérer l'instance
	public static NetworkInterface getInstance() {
		return NetworkInterfaceHolder.instance;
	}

	// Constructeur privé pour le singleton
	private NetworkInterface() {
		//Initialisation de la table vide
		socketMap = new HashMap<>();
		//On met déjà le socket hello en écoute
		try {
			hello = new DatagramSocket(helloPort);
			//Lancement du thread d'écoute pour hello
			helloListener = new HelloListener(hello);
			helloListener.start();
		} catch (IOException e) {
			System.out.println("Can't bind hello socket");
			e.printStackTrace();
		}

		//anouk est un datagram socket
		try {
			anouk = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	private Socket negotiatePort(User destUser) {
		System.out.println("Je vais négocier le port");
		try {

			System.out.println("anouk ok");
			ServerSocket com = new ServerSocket(basePort);
			CommunicationListener listener = new CommunicationListener(com);
			listener.start();
			sendControl(controller.getLocalUser(), destUser, Control.Control_t.HELLO, basePort);
			upToDate = false;
			basePort++;
			while(!upToDate) {}
			return socketMap.get(destUser.getUsername());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	protected void sendControl(User me, User dest, Control.Control_t type, int data) {
		try {
			Control control_packet = new Control(me.getUsername(), dest.getUsername(), me.getIP(), dest.getIP(), type, data);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(outputStream);
			os.writeObject(control_packet);
			byte[] buffer = outputStream.toByteArray();
			DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, dest.getIP(), helloPort);
			anouk.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Socket getSocket(User destUser) {
		Socket s = socketMap.get(destUser);
		if(s == null) {
			s = negotiatePort(destUser);
		}

		return s;
	}

	public void sendNotification(User dest, Notification.Notification_type type, String data) {
		Socket s = getSocket(dest);
		System.out.println("OKAY LOL");
		//Packet paquet = new Notification(src.getFullPseudo(), dest.getFullPseudo(), src.getIp(), dest.getIp(), type, data);

	}

	public void broadcastNotification(Notification.Notification_type type, String data) {
		Notification notification = new Notification(controller.getLocalUser().getUsername(), "bcast", controller.getLocalUser().getIP(), NetworkUtils.getBroadcastAddress(), type, data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream os = null;
		try {
			os = new ObjectOutputStream(outputStream);
			os.writeObject(notification);
			byte[] buffer = outputStream.toByteArray();
			DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, NetworkUtils.getBroadcastAddress(), helloPort);
			anouk.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	public void transmitMessage(String message, User dest) {

	}

	public void transmitFile(String filename, User dest) {

	}

	public void addMap(User user, Socket s) {
		socketMap.put(user, s);
		upToDate = true;
	}

}
