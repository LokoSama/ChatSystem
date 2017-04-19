package Model;

import java.net.InetAddress;


public class User {
	//Attributes
	private String username;	
	private InetAddress IP;
	private Status status;

	//Constructor
	public User (String name, InetAddress IP, Status status) { 
		this.username = name ;
		this.IP = IP ;
		this.status = status;
	}
	
	//Methods
	
	//Getters
	public String getUsername(){
		return this.username ;
	}

	public InetAddress getIP() {
		return this.IP;		
	}
	
	public Status getStatus() {
		return status;
	}

	//Setters
	public void setStatus(Status status) {
		this.status = status;
	}

	//Override
	public boolean equals(User u) {
		return u != null
				&& u.getUsername() == this.getUsername()
				&& u.getIP() == this.getIP();
	}

	public String toString () {
		return username + "@" + IP ;
	}
}
