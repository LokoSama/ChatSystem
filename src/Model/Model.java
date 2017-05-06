package Model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;

//import Network.Packet;


public class Model extends Observable {

	//Attributes
	private User localUser;
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
		//userList.add(new User("Michel", InetAddress.getLoopbackAddress(), Status.Online));
		//userList.add(new User("Emily", InetAddress.getLoopbackAddress(), Status.Away));
		//userList.add(new User("Jean-Jacques", InetAddress.getLoopbackAddress(), Status.Busy));
		connected = false;
	}

	public ArrayList<User> getUserList() {
		return userList;
	}

	public InetAddress getLocalIP() {
		return localUser.getIP();
	}
	
	public User getLocalUser() {
		return localUser;
	}
	
	public void setLocalUser (String name)  {
		this.localUser.setName(name);
	}

	public void setLocalStatus(Status status){
		this.localUser.setStatus(status);
		setChanged();
		notifyObservers();
	}
	public void setStatus(String name,InetAddress ip, Status status){
		System.out.println("appel set status");
		int index=0;
		boolean find = false;
		while (index < userList.size() && !find){
		if (userList.get(index).getUsername() == name && userList.get(index).getIP() == ip ) {
			find = true;
		}
		index ++;
		}
		index --;
		if (index > -1  && index < userList.size() ) {
		userList.set(index,new User(name,ip,status));
		setChanged();
		notifyObservers();
		}
		else {
			System.out.println("Fail set status, index =" + index + "\n");
		}
		}
	
	public Status statusFromString(String s) {
		
		if (s.equals("Online"))
			return Status.Online;
		else if (s.equals("Busy"))
			return Status.Busy;
		else if (s.equals("Away"))
			return Status.Away;
		else
			return Status.Offline;
	}
	
	public boolean addUser(String name, InetAddress ip, Status status) {
		boolean ret = userList.add(new User(name, ip, status));
		setChanged();
		notifyObservers();
		return ret;
	}

	public boolean deleteUser(String name, InetAddress ip) {
		
		boolean ret = userList.remove(new User(name, ip, Status.Busy));
		setChanged();
		notifyObservers();
		System.out.println(ret);
		return ret;
	}
	
	public boolean userIsConnected() {
		return connected;
	}

	public void connectUser() {
		connected = true;
	}
	
	public void disconnectUser() {
		connected = false;
	}

}


