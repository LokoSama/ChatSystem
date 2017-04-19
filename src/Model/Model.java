package Model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

//import Network.Packet;


public class Model {

	//Attributes
	private User localUser;
	private ArrayList<User> userList;
	private boolean connected ;
			
	//Methods
	public Model () {
		
	}
	
	//ELEMENTS COMMUNS : PORT ECOUTE UDP = 
	//					 PORT ECOUTE TCP = 
	//TODO
	//public boolean send (Packet p) {
		//return true;
//	}

	public void setUser (String name)  {
		try {
			this.localUser = new User(name,InetAddress.getLocalHost(), Status.Online);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean deleteUser(String name, InetAddress ip) {
		return userList.remove(new User(name, ip, Status.Online));
	}
	
}


