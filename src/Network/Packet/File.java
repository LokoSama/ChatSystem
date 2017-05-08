package Network.Packet;

import java.net.InetAddress;

/**
 * @author alex205
 */
@SuppressWarnings({"serial", "unused"})
public class File extends Message {
	private String fileName;
	private String mimeType;
	private double size;
	private byte[] content;

	public File(String pseudoSource, String pseudoDestination, InetAddress addrSource,
			InetAddress addrDestination, String fileName, String mimeType, double size, byte[] content) {
		
		super(pseudoSource, pseudoDestination, addrSource, addrDestination);
		this.fileName = fileName;
		this.mimeType = mimeType;
		this.size = size;
		this.content = content;
	}
	
	public String getFileName() {
		return fileName;
	}

	public String getMimeType() {
		return mimeType;
	}

	public double getSize() {
		return size;
	}

	public byte[] getContent() {
		return content;
	}
}
