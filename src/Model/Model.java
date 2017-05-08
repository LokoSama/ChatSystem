package Model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;

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
			e.printStackTrace();
		}
		userList = new ArrayList<User>();
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

	public User getUser(String name, InetAddress ip) {
		int index=0;
		boolean find = false;
		User UserAux = new User(name,ip,Status.Online);
		while (index < userList.size() && !find){
			if (userList.get(index).equals(UserAux) ) {
				find = true;
			}
			index ++;
		}
		index --;
		if (find ) {
			return userList.get(index);
		}
		else return null; //si User pas trouvé 
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
		int index=0;
		boolean find = false;
		User UserAux = new User(name,ip,status);
		while (index < userList.size() && !find){
			if (userList.get(index).equals(UserAux)) {
				find = true;
			}
			index ++;
		}
		index --;
		if (find ) {
			userList.set(index,UserAux);
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

		User u = new User(name, ip, status);
		boolean ret = false;
		if (!userList.contains(u))
			ret = userList.add(u);
		setChanged();
		notifyObservers();
		return ret;
	}

	public boolean deleteUser(String name, InetAddress ip) {
		int index=0;
		boolean find = false;
		User UserAux = new User (name,ip,Status.Offline);
		while (index < userList.size() && !find){
			if (userList.get(index).equals(UserAux) ) {
				find = true;
			}
			index ++;
		}
		index --;
		if (find ) {
			userList.remove(index);
			setChanged();
			notifyObservers();
			return true;
		}
		else {
			System.out.println("Fail delete User, index =" + index + "\n");
			return false;
		}

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