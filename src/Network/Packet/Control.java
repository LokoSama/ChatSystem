package Network.Packet;

import java.net.InetAddress;

/**
 * @author
 */
@SuppressWarnings("serial")
public class Control extends Packet {

	public enum Control_t {
		ACK,
		HELLO
	}

	private int data;
	private Control_t type;

	public Control(String pseudoSource, String pseudoDestination, InetAddress addrSource, InetAddress addrDestination, Control_t type, int data) {
		super(pseudoSource, pseudoDestination, addrSource, addrDestination);
		this.type = type;
		this.data = data;
	}

	public int getData() {
		return data;
	}

	public Control_t getType() {
		return type;
	}

	public String toString() {
		return "Control(" + this.type + ", from " + pseudoSource + " (" + this.getAddrSource() + "), to " + pseudoDestination + ", data = " + data + ")";
	}
}
