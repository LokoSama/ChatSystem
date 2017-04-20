package Controller;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import Fenetre.FenetreMsg;
import Fenetre.View;
import Model.Model;
import Model.User;


public class Controller {
	//attributes
	//Modele
	Model model;
	View view ;
	//Réseau TCP
	public ServerSocket serverSock;
	public Map<User,Socket> activeSockets; 
	private final static int SERVER_LISTENING_PORT = 2042;
	private final static int REMOTE_SERVER_PORT = 3461;
	

	//Constructeur
	public Controller() {
		this.model = new Model();
		this.view = new View(this.model,this);

		initNetwork();
	}

	public boolean launchChatWith(User user) {
		Socket sock = new Socket(user.getIP(), REMOTE_SERVER_PORT);
		activeSockets.put(user, sock);
		(new Thread( new FenetreMsg(user.getUsername(), sock) )).start();
		return true;
	}
	
	public User getLocalUser() {
		return model.getLocalUser();
	}
	
	public void setLocalUser(String name) {
		model.setLocalUser(name);
	}

	public View getView(){
		return view ;
	}

	public Model getModel() {	
		return model ;
	}
	
	private void initNetwork() {
		//TODO: socket handling
		// we need 1 server socket to accept incoming connections & 1 socket list to store the sockets used for individual conversations
		serverSock = new ServerSocket(SERVER_LISTENING_PORT);
		activeSockets = new HashMap<>();
	}



}
