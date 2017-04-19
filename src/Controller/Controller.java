package Controller;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import Fenetre.View;
import Model.Model;


public class Controller {
	//attributes
	//Modele
	Model model;
	View view ;
	//Réseau TCP
	public ServerSocket serveur;
	public Socket sock; 
	//TODO : exemple de fonction appelable par view
		
		//Constructeur
		public Controller() {
			this.model = new Model();
			this.view = new View(this.model,this);
			
			initNetwork();
		}

		public void setUser(String name) {
			model.setUser(name);
		}
		
		public View getView(){
			return view ;
		}
		
		public Model getModel() {	
			return model ;
		}
		private void initNetwork() {
			
		}
		
		
		
}
