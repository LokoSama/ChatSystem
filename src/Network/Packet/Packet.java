package Network.Packet;


import java.io.Serializable;
import java.net.InetAddress;

/**
 * @author alex205
 */
@SuppressWarnings("serial")
public abstract class Packet implements Serializable {
	protected String pseudoSource;
	protected String pseudoDestination;
	protected InetAddress addrSource;
	protected InetAddress addrDestination;

	public Packet(String pseudoSource, String pseudoDestination, InetAddress addrSource, InetAddress addrDestination) {
		this.pseudoSource = pseudoSource;
		this.pseudoDestination = pseudoDestination;
		this.addrSource = addrSource;
		this.addrDestination = addrDestination;
	}

	public String getPseudoSource() {
		return pseudoSource;
	}

	public void setPseudoSource(String pseudoSource) {
		this.pseudoSource = pseudoSource;
	}

	public String getPseudoDestination() {
		return pseudoDestination;
	}

	public void setPseudoDestination(String pseudoDestination) {
		this.pseudoDestination = pseudoDestination;
	}

	public InetAddress getAddrSource() {
		return addrSource;
	}

	public void setAddrSource(InetAddress addrSource) {
		this.addrSource = addrSource;
	}

	public InetAddress getAddrDestination() {
		return addrDestination;
	}

	public void setAddrDestination(InetAddress addrDestination) {
		this.addrDestination = addrDestination;
	}
}

