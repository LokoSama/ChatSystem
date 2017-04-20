package Fenetre;


import java.util.ArrayList;

import Controller.Controller;
import Model.Model;
import Model.User;

//le début est OK :
//TODO lorsque le listener des fenetres doit envoyer des choses, il appelle des fonctions du controller, le squelette de l algo se trouve ds le controller
public class View {

	//attributs
	private Controller controller;
	private Model model;

	//Constructeur
	public View (Model model,Controller controller){
		this.model = model;
		this.controller = controller;
		FenetreLogin fenetreLog = new FenetreLogin(this);
		fenetreLog.pack();
		fenetreLog.setVisible(true);
	}

	public void initMain() { 
		initNetwork(); //first network functionality initialization
		FenetreMain fenetreMain = new FenetreMain(this);
		fenetreMain.pack();
		fenetreMain.setVisible(true);
	}

	public void initNetwork() {
		//TODO :
		// - initialize network itself (define ports, sockets)
		// - discover other users and make yourself known to them (populate model.userList)
		// - set model.connected to true
	}

	public boolean launchChatWith(User user) {
		return controller.launchChatWith(user);
	}
	
	public void login(String username) {
		controller.setLocalUser(username);
		initMain();
	}

	public ArrayList<User> getUserList() {
		return model.getUserList();
	}

	public User getLocalUser() {
		return model.getLocalUser();
	}
	
	public static void main(String[] args) throws InterruptedException {


	}
}



