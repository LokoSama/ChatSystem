package MyNetwork;

import java.io.IOException;
import java.io.ObjectInputStream;

import Controller.Controller;
import Model.Status;
import Model.User;
import Network.Packet.Control;
import Network.Packet.File;
import Network.Packet.Message;
import Network.Packet.Notification;
import Network.Packet.Packet;
import Network.Packet.Text;

public class ConversationListener extends Thread {

	//Attributes
	private ObjectInputStream input;
	private Controller controller;
	
	public ConversationListener(ObjectInputStream input, Controller controller) {
		this.input = input;
		this.controller = controller;
	}
	
	@Override
	public void run() {
		Packet pack;
		while (true) {
			try {
				pack = (Packet)input.readObject();
				handlePacket(pack);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void handlePacket(Packet pack) {
		if (pack instanceof Control) {
			//TODO
		} else if (pack instanceof File) {	
			//TODO
		} else if (pack instanceof Message) {
			//TODO
		} else if (pack instanceof Notification) {
			//TODO
		} else if (pack instanceof Text) {
			controller.showMessage(((Text)pack).getData(), new User(pack.getPseudoSource(), pack.getAddrSource(), Status.Online));
		} else {
			System.out.println("Error in ConversationListener.handlePacket : packet type unclear");
		}
	}
}
