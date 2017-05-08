package Model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;

//import Network.Packet;


public class Model extends Observable {

	private boolean connected ;
	//Attributes
	private User localUser;
	private ArrayList<User> userList;

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

	public boolean addUser(String name, InetAddress ip, Status status) {
		User u = new User(name, ip, status);
		boolean ret = false;
		if (!userList.contains(u))
			ret = userList.add(u);
		setChanged();
		notifyObservers();
		return ret;
	}

	public void connectUser() {
		connected = true;
	}

	public boolean deleteUser(String name, InetAddress ip) {
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
			userList.remove(index);
			setChanged();
			notifyObservers();
		}
		else {
			System.out.println("Fail delete User, index =" + index + "\n");
		}
		
		boolean ret = userList.remove(new User(name, ip, Status.Busy));
		setChanged();
		notifyObservers();
		return ret;
	}
	
	public void disconnectUser() {
		connected = false;
	}
	public InetAddress getLocalIP() {
		return localUser.getIP();
	}

	public User getLocalUser() {
		return localUser;
	}
	public User getUser(String name, InetAddress ip) {
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
			return userList.get(index);
	}
		else return null; //si User pas trouv� 
	}

	public ArrayList<User> getUserList() {
		return userList;
	}

	public void setLocalStatus(Status status){
		this.localUser.setStatus(status);
		setChanged();
		notifyObservers();
	}

	public void setLocalUser (String name)  {
		this.localUser.setName(name);
	}

	public void setStatus(String name,InetAddress ip, Status status){
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

	public boolean userIsConnected() {
		return connected;
	}

}


