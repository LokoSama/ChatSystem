package Model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;

//import Network.Packet;


public class Model extends Observable {

	//Attributes
	private User localUser;
	private InetAddress localIP;
	private ArrayList<User> userList;
	private boolean connected ;

	//Methods
	public Model () {
		try {
			this.localUser = new User("",InetAddress.getLocalHost(), Status.Online);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		userList = new ArrayList<User>();
		userList.add(new User("Michel", InetAddress.getLoopbackAddress(), Status.Online));
		userList.add(new User("Emily", InetAddress.getLoopbackAddress(), Status.Away));
		userList.add(new User("Jean-Jacques", InetAddress.getLoopbackAddress(), Status.Busy));
		connected = false;
				
	}

	//ELEMENTS COMMUNS : PORT ECOUTE UDP = 
	//					 PORT ECOUTE TCP = 
	//TODO
	//public boolean send (Packet p) {
	//return true;
	//	}

	public ArrayList<User> getUserList() {
		return userList;
	}

	public InetAddress getLocalIP() {
		return localIP;
	}
	
	public User getLocalUser() {
		return localUser;
	}
	
	public void setLocalUser (String name)  {
		this.localUser.setName(name);
	}

	public boolean addUser(String name, InetAddress ip, Status status) {
		boolean ret = userList.add(new User(name, ip, status));
		setChanged();
		notifyObservers(this.userList);
		return ret;
	}

	public boolean deleteUser(String name, InetAddress ip) {
		return userList.remove(new User(name, ip, Status.Online));
	}

}


