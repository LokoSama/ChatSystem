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
		Debugger.log("UDPListener : starting");
		byte[] buf = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);

		while (true) {
			try{
				UDPlisten.receive(packet);
				Debugger.log("UDPListener : packet received");

				ByteArrayInputStream byteIS = new ByteArrayInputStream(packet.getData());
				ObjectInputStream objectIS = new ObjectInputStream(byteIS);
				Packet pack = (Packet)objectIS.readObject();

				//TODO : des tests, pour comprendre ce qui merde... le listener se bloque entre "UDPListener : 2" et "UDPListener : 3"
				//possiblement parce que je fais tourner les deux clients en local ?
				
				
				if (pack instanceof Control) {
					Control c = (Control) pack;
					Debugger.log("UDPListener : received " + c);
					if (c.getType() == Control.Control_t.HELLO) {
						Debugger.log("UDPListener : 1");
						User remoteUser = new User(c.getPseudoSource(), c.getAddrSource(), Status.Online);
						Debugger.log("UDPListener : 2");
						Socket sock = new Socket(c.getAddrSource(), c.getData());
						Debugger.log("UDPListener : 3");
						controller.addConversation(remoteUser, sock); //add conversation
						Debugger.log("UDPListener : 4");
						controller.sendControl(remoteUser, Control.Control_t.ACK, sock.getLocalPort()); //send ACK back with local port number (over UDP)
						Debugger.log("UDPListener : sent ACK back");
					} else if (c.getType() == Control.Control_t.ACK) {
						controller.ACKPacketreceived(c);
					} else {
						System.out.println("Error in UDPListener.run(): received Control is neither HELLO nor ACK");
					}
				} else {
					System.out.println("Error in UDPListener.run(): received packet is not Control");
				}
			}
			catch (IOException | ClassNotFoundException e) {
				//TODO
			}
		}
	}

}
