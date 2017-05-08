package MyNetwork;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import Controller.Controller;
import Controller.Debugger;
import Model.Status;
import Model.User;
import Network.Packet.Control;
import Network.Packet.Notification;
import Network.Packet.Notification.Notification_type;
import Network.Packet.Packet;

public class UDPListener extends Thread {

	//Attributes
	Controller controller;
	DatagramSocket UDPlisten;
	static boolean running = true;

	public UDPListener(DatagramSocket UDPlisten, Controller controller) {
		this.UDPlisten = UDPlisten;
		this.controller = controller;
	}

	public static void close() {
		running = false;
	}

	public void run() {
		byte[] buf = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		Packet pack;

		while (running) {
			try{
				Debugger.log("UDPListener : waiting for packet");
				pack = getIncomingPacket(packet); //blocking

				if (pack == null) {
					//We ran into a SocketException at getIncomingPacket
				}
				else if (pack instanceof Control) {
					handleControl(pack);
				} else if (pack instanceof Notification) {
					Notification n = (Notification) pack;
					Debugger.log("UDPListener : Received " + n.getType() + " from " + n.getPseudoSource());

					if (n.getType() == Notification_type.CONNECT)
					{
						//add the user to the list
						controller.printNotif(n.getPseudoSource() + " s'est connecté(e)");
						controller.addUser(n.getPseudoSource(),n.getAddrSource(), Status.Online);
						//Send ACK_CONNECT back so the remote user knows us and send STATUS_CHANGE so he can set our status
						User tmpDest = new User(n.getPseudoSource(),n.getAddrSource(), Status.Online);
						controller.sendNotif(tmpDest, Notification_type.ACK_CONNECT, "");
						controller.sendNotif(tmpDest, Notification_type.STATUS_CHANGE, controller.getLocalUser().getStatus().toString());
					}
					else if (n.getType() == Notification_type.ACK_CONNECT) {
						controller.addUser(n.getPseudoSource(), n.getAddrSource(), Status.Online);
					}
					else if(n.getType() == Notification_type.STATUS_CHANGE) {
						Status newStatus = controller.statusFromString(n.getData());
						controller.printNotif(n.getPseudoSource() + " est " + newStatus);
						controller.setStatus(n.getPseudoSource(), n.getAddrSource(), newStatus);
					}
					else if (n.getType() == Notification_type.MISC) {
						//Supposedly used for any kind of notification, like poking; not implemented and thus ignored
					}
					else if (n.getType() == Notification_type.ACK) {
						//N/A : we only need ACK_CONNECT
					}
					else if (n.getType() == Notification_type.ALIVE) {
						//Not implemented
					}
					else if (n.getType() == Notification_type.DISCONNECT){
						controller.printNotif(n.getPseudoSource() + " s'est déconnecté(e)");
						controller.deleteUser(n.getPseudoSource(),n.getAddrSource());
					}
					else {
						System.out.println("ERROR in UDPListener.run(): received packet has no legitimate Notification type");
					}
				} else {
					System.out.println("ERROR in UDPListener.run(): received packet is neither Control nor Notification");
				}
			}
			catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private Packet getIncomingPacket(DatagramPacket packet) throws IOException, ClassNotFoundException {
		Packet pack;
		try {
			UDPlisten.receive(packet);
			ByteArrayInputStream byteIS = new ByteArrayInputStream(packet.getData());
			ObjectInputStream objectIS = new ObjectInputStream(byteIS);
			pack = (Packet)objectIS.readObject();
		} catch (SocketException e) {
			pack = null;
		}
		return pack;
	}

	private void handleControl(Packet pack) throws IOException {
		Control c = (Control) pack;
		Debugger.log("UDPListener : Received " + c.getType() + " from " + c.getPseudoSource());

		if (c.getType() == Control.Control_t.HELLO) {
			User remoteUser = controller.getUser(c.getPseudoSource(), c.getAddrSource());

			Socket sock = new Socket(); //we create an empty socket and then bind it to port 0 (so a port is assigned dynamically)
			sock.bind(new InetSocketAddress(controller.getLocalUser().getIP(), 0));
			controller.sendControl(remoteUser, Control.Control_t.ACK, sock.getLocalPort()); //send ACK back with local port number (over UDP)
			sock.connect(new InetSocketAddress(c.getAddrSource(), c.getData())); //connects to the remote ServerSocket

			controller.addConversation(remoteUser, sock); //add conversation
			Debugger.log("UDPListener : Conversation added");

		} else if (c.getType() == Control.Control_t.ACK) {
			controller.ACKPacketreceived(c);
		} else {
			System.out.println("ERROR in UDPListener.run(): received Control is neither HELLO nor ACK");
		}
	}
}
