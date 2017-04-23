package Fenetre;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import Controller.Controller;
import Model.User;

@SuppressWarnings("serial")
public class FenetreMsg extends JFrame implements ActionListener, Runnable {

	private JTextArea textHist ;
	private JTextArea textSaisi ;
	private JButton bSend ;
	private JPanel panel ;
	private Controller controller;
	private User remoteUser;

	public FenetreMsg (User remoteUser) { 
		this.remoteUser = remoteUser;
		this.setTitle(remoteUser.getUsername());
		this.createComponents();		
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}

	@Override
	//Here goes the code executed in each thread
	public void run() {
		String msg;
		while(true) {
			msg = controller.getMessageFrom(remoteUser); //blocking call to wait for a new message
			this.textHist.append(this.remoteUser.getUsername() + ": " + msg + "\n");

		}
	}

	private void createComponents() {
		//Buttons	
		bSend= new JButton("Send");
		//Listeners
		bSend.addActionListener(this);
		//TextArea
		textSaisi = new JTextArea(5,20);
		textHist = new JTextArea(5,20); 
		textHist.setEditable(false);
		//Panel
		JPanel panel = new JPanel();

		//Setup
		//set
		panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
		panel.setLayout(new GridLayout(3,1));

		//add
		panel.add(textHist);
		panel.add(textSaisi);
		panel.add(bSend);
		this.add(panel);

	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == bSend){
			String newMsg= this.textSaisi.getText();
			this.textHist.append(this.controller.getLocalUser().getUsername() + ": " + newMsg + "\n");
			this.textSaisi.setText("");
			controller.sendText(remoteUser, newMsg);
		}
	}



}