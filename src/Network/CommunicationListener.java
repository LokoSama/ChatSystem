package Network;

import java.net.ServerSocket;

import Network.Packet.Packet;

/**
 * @author alex205
 */
public class CommunicationListener extends NetworkListener{

	public CommunicationListener(ServerSocket server) {
		super(server);
	}

	@Override
	protected void managePacket(Packet p) {

	}
}
