package Network.Packet;


import java.io.Serializable;
import java.net.InetAddress;

/**
 * @author alex205
 */
@SuppressWarnings("serial")
public abstract class Packet implements Serializable {
	protected InetAddress addrDestination;
	protected InetAddress addrSource;
	protected String pseudoDestination;
	protected String pseudoSource;

	public Packet(String pseudoSource, String pseudoDestination, InetAddress addrSource, InetAddress addrDestination) {
		this.pseudoSource = pseudoSource;
		this.pseudoDestination = pseudoDestination;
		this.addrSource = addrSource;
		this.addrDestination = addrDestination;
	}

	public InetAddress getAddrDestination() {
		return addrDestination;
	}

	public InetAddress getAddrSource() {
		return addrSource;
	}

	public String getPseudoDestination() {
		return pseudoDestination;
	}

	public String getPseudoSource() {
		return pseudoSource;
	}

	public void setAddrDestination(InetAddress addrDestination) {
		this.addrDestination = addrDestination;
	}

	public void setAddrSource(InetAddress addrSource) {
		this.addrSource = addrSource;
	}

	public void setPseudoDestination(String pseudoDestination) {
		this.pseudoDestination = pseudoDestination;
	}

	public void setPseudoSource(String pseudoSource) {
		this.pseudoSource = pseudoSource;
	}
}

