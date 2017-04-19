package Model;

import java.net.InetAddress;


public class RemoteUser {
	
	private String name;
	private InetAddress iPAddress;
	private Status status;
	
	public RemoteUser(String name, InetAddress iPAddress, Status status) {
		this.name = name;
		this.iPAddress = iPAddress;
		this.status = status;
	}
	
	public String toString () {
		return name + "@" + iPAddress;
	}
	
	//TODO : setters/getters
}
