package Network.Packet;

import java.net.InetAddress;

/**
 * @author alex205
 */
@SuppressWarnings("serial")
public final class Notification extends Packet {
	public enum Notification_type {
		ACK,
		ACK_CONNECT,
		ALIVE,
		CONNECT,
		DISCONNECT,
		MISC,
		STATUS_CHANGE
	}

	private String data;
	private Notification_type type;

	public Notification(String pseudoSource, String pseudoDestination, InetAddress addrSource, InetAddress addrDestination, Notification_type type, String data) {
		super(pseudoSource, pseudoDestination, addrSource, addrDestination);
		this.type = type;
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public Notification_type getType() {
		return type;
	}
}
