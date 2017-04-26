package Fenetre;


import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;

import Controller.Controller;
import Model.Model;
import Model.Status;
import Model.User;

//le début est OK :
//TODO lorsque le listener des fenetres doit envoyer des choses, il appelle des fonctions du controller, le squelette de l algo se trouve ds le controller
public class View implements Observer {

	//attributs
	private Controller controller;
	private Model model;
	
	//attribut fen�tre
	private FenetreMain fenetreMain; //n�cessaire pour propager l'information depuis Model
	//Constructeur
	public View (Model model,Controller controller) {
		this.model = model;
		this.controller = controller;
		FenetreLogin fenetreLog = new FenetreLogin(this);
		fenetreLog.pack();
		fenetreLog.setVisible(true);
	}

	public void initMain() { 
		//initNetwork(); //first network functionality initialization
		this.fenetreMain = new FenetreMain(this);
		this.fenetreMain.pack();
		this.fenetreMain.setVisible(true);
	}
	
	public void setLocalStatus (Status status){
		controller.setLocalStatus(status);
	}
	public void launchChatWith(User user) {
		controller.launchChatWith(user);
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
	
	public String getMessageFrom(User remoteUser) {
		return controller.getMessageFrom(remoteUser);
	}	
	public static void main(String[] args) throws InterruptedException {

	}

	@Override
	public void update(Observable obs, Object arg) {
		if (obs instanceof Model){
			System.out.println("MAJ user");
			//JOptionPane.showMessageDialog(null, "nouveau user");
			this.fenetreMain.updateUserList();
		}
	}

	public void launchFenetreMsg(User remoteUser) {
		(new Thread( new FenetreMsg(remoteUser, this))).start();
	}

	public void sendText(User remoteUser, String newMsg) {
		controller.sendText(remoteUser, newMsg);
	}
}



