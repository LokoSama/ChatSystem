package Network.Packet;

import java.net.InetAddress;

/**
 * @author alex205
 */
@SuppressWarnings({"serial", "unused"})
public class File extends Message {
	private byte[] content;
	private String fileName;
	private String mimeType;
	private double size;

	public File(String pseudoSource, String pseudoDestination, InetAddress addrSource,
			InetAddress addrDestination, String fileName, String mimeType, double size, byte[] content) {
		
		super(pseudoSource, pseudoDestination, addrSource, addrDestination);
		this.fileName = fileName;
		this.mimeType = mimeType;
		this.size = size;
		this.content = content;
	}
	
	public byte[] getContent() {
		return content;
	}

	public String getFileName() {
		return fileName;
	}

	public double getSize() {
		return size;
	}
}
