package Fenetre;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import Controller.Controller;
import Model.Model;
import Model.Status;
import Model.User;
import Network.Packet.Notification.Notification_type;

public class View implements Observer {

	//Attributes
	private Controller controller;
	private Model model;
	private FenetreMain fenetreMain;
	private FenetreLogin fenetreLog;

	//Constructor
	public View (Model model,Controller controller) {
		this.model = model;
		this.controller = controller;
		fenetreLog = new FenetreLogin(this);
		fenetreLog.pack();
		fenetreLog.setVisible(true);
	}

	public void initMain() { 
		System.out.println("Envoi du paquet CONNECT en broadcast \n");
		this.controller.broadcastNotification(Notification_type.CONNECT, "lul"); 
		this.fenetreMain = new FenetreMain(this);
		this.fenetreMain.pack();
		this.fenetreMain.setVisible(true);
	}

	public void launchFenetreMsg(User remoteUser) {
		(new Thread( new FenetreMsg(remoteUser, this))).start();
	}

	public void printNotif (String txt){
		this.fenetreMain.setNotif(txt);
	}


	@Override
	public void update(Observable obs, Object arg) {
		if (obs instanceof Model){
			//JOptionPane.showMessageDialog(null, "nouveau user");
			this.fenetreMain.updateUserList();
		}
	}

	//ACCESS TO CONTROLLER/MODEL METHODS
	public void launchChatWith(User user) {
		controller.launchChatWith(user);
	}

	public void setLocalStatus (Status status){
		controller.setLocalStatus(status);
	}

	public void sendText(User remoteUser, String newMsg) {
		controller.sendText(remoteUser, newMsg);
	}

	public void login(String username) {
		controller.setLocalUser(username);
		initMain();
		model.connectUser();
	}

	public void sendFile(User dest, String strPath) {
		this.controller.sendFile(dest, strPath);
	}
	
	public User getLocalUser() {
		return model.getLocalUser();
	}

	public String getMessageFrom(User remoteUser) {
		return controller.getMessageFrom(remoteUser);
	}

	public ArrayList<User> getUserList() {
		return model.getUserList();
	}

	//CLOSING METHODS
	public void closeFenetreMsg(User u) {
		controller.closeFenetreMsg(u);
	}
	public void closeWindow() {
		controller.closeSoft();
	}

}
