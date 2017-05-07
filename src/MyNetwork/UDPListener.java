package MyNetwork;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

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
	DatagramSocket UDPlisten;
	Controller controller;

	public UDPListener(DatagramSocket UDPlisten, Controller controller) {
		this.UDPlisten = UDPlisten;
		this.controller = controller;
	}

	public void run() {
		byte[] buf = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);

		while (true) {
			try{
				Debugger.log("UDPListener : waiting for packet");
				UDPlisten.receive(packet);

				ByteArrayInputStream byteIS = new ByteArrayInputStream(packet.getData());
				ObjectInputStream objectIS = new ObjectInputStream(byteIS);
				Packet pack = (Packet)objectIS.readObject();
				
				if (pack instanceof Control) {
					Control c = (Control) pack;
					Debugger.log("UDPListener : Received " + c.getType() + " from " + c.getPseudoSource());
					
					if (c.getType() == Control.Control_t.HELLO) {
						User remoteUser = new User(c.getPseudoSource(), c.getAddrSource(), Status.Online); //TODO: incorrect, il faut récupérer le user dans Model.userList et pas en créer un autre
						controller.sendControl(remoteUser, Control.Control_t.ACK, 1891); //send ACK back with local port number (over UDP)
						Socket sock = new Socket(c.getAddrSource(), c.getData());
						controller.addConversation(remoteUser, sock); //add conversation
						Debugger.log("UDPListener : Conversation added");
						
					} else if (c.getType() == Control.Control_t.ACK) {
						controller.ACKPacketreceived(c);
					} else {
						System.out.println("ERROR in UDPListener.run(): received Control is neither HELLO nor ACK");
					}
				} else if (pack instanceof Notification) {
					Notification n = (Notification) pack;
					Debugger.log("UDPListener : Received " + n.getType() + " from " + n.getPseudoSource());

					if (n.getType() == Notification_type.CONNECT)
					{
						//On ajoute l'user à la liste
						controller.getView().printNotif(n.getPseudoSource() + " s'est connecté(e)");
						controller.getModel().addUser(n.getPseudoSource(),n.getAddrSource(), Status.Online);
						//On renvoie ACK_CONNECT pour se faire connaitre, et on envoie son propre statut
						User tmpDest = new User(n.getPseudoSource(),n.getAddrSource(), Status.Online);
						controller.sendNotif(tmpDest, Notification_type.ACK_CONNECT, "");
						controller.sendNotif(tmpDest, Notification_type.STATUS_CHANGE, controller.getModel().getLocalUser().getStatus().toString());
					}
					else if (n.getType() == Notification_type.ACK_CONNECT) {
						//cette ligne fait doublon si on considère que les autres users enverront forcément un ACK_CONNECT + un STATUS_CHANGE pour nous informer de leur statut
						//puisque ça affichera un message plus détaillé
						//controller.getView().printNotif(n.getPseudoSource() + " est en ligne");
						Debugger.log("About to add the following user : \n- NAME : " + n.getPseudoSource() + "\n- IP : " + n.getAddrSource());
						controller.getModel().addUser(n.getPseudoSource(), n.getAddrSource(), Status.Online);
					}
					else if(n.getType() == Notification_type.STATUS_CHANGE) {
						Status newStatus = controller.getModel().statusFromString(n.getData());
						controller.getView().printNotif(n.getPseudoSource() + " est " + newStatus);
						controller.getModel().setStatus(n.getPseudoSource(), n.getAddrSource(), newStatus);
					}
					else if (n.getType() == Notification_type.ACK) {
						//TODO
					}
					else if (n.getType() == Notification_type.MISC) {
						//TODO
					}
					else if (n.getType() == Notification_type.ALIVE) {
						//TODO
					}
					else if (n.getType() == Notification_type.DISCONNECT){
						controller.getView().printNotif(n.getPseudoSource() + " s'est d�connect�(e)");
						controller.getModel().deleteUser(n.getPseudoSource(),n.getAddrSource());
					}
					else {
						System.out.println("ERROR in UDPListener.run(): received packet expected Notification has no legit Notification type");
					}
					//TODO : gérer les messages CONNECT, ACK_CONNECT, STATUTS_CHANGE...
				} else {
					System.out.println("ERROR in UDPListener.run(): received packet is not Control");
				}
			}
			catch (IOException | ClassNotFoundException e) {
				//TODO
				e.printStackTrace();
			}
		}
	}

}
