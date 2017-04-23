package Model;

import java.net.InetAddress;

import Controller.Controller;


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
	public void setName(String name) {
		this.username = name;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}

		@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (IP == null) {
			if (other.IP != null)
				return false;
		} else if (!IP.equals(other.IP))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	public String toString () {
		return this.username + "@" + this.IP + " (" + this.status + ")" ;
	}



}
