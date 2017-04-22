package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Network.Packet.Packet;

/**
 * @author alex205
 */
abstract class NetworkListener extends Thread {
	private ServerSocket server;
	private Socket s;

	public NetworkListener(ServerSocket server) {
		this.server = server;
	}

	public void run() {
		try {
			while(true) {
				s = server.accept();
				ObjectInputStream is = new ObjectInputStream(s.getInputStream());
				Packet p = (Packet) is.readObject();
				managePacket(p);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected void closeConnection() {
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected abstract void managePacket(Packet p);
}
