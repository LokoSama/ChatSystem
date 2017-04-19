package Fenetre;


import java.io.IOException;
import java.net.UnknownHostException;

import Controller.Controller;
import Fenetre.*;
import Model.Model;

//le début est OK :
//TODO lorsque le listener des fenetres doit envoyer des choses, il appelle des fonctions du controller, le squelette de l algo se trouve ds le controller
public class View {

	//attributs
	public static String test; 
	private Controller controller;

	//Constructeur
	public View (Model model,Controller controller){
		this.controller = controller;
		FenetreLogin fenetre1 = new FenetreLogin(this);
		fenetre1.pack();
		fenetre1.setVisible(true);
	}

	
	public void initMain() { 
		FenetreMain fenetre2 = new FenetreMain();
		fenetre2.pack();
		fenetre2.setVisible(true);
	}

	public void Login(String username) {
		controller.setUser(username);
	}
	public static void main(String[] args) throws InterruptedException {


	}
}



