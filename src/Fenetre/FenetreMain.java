package Fenetre;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import Model.Status;
import Model.User;


public class FenetreMain extends JFrame implements ActionListener {

	//Attributs
	private JList<User> listUsers ;
	private JButton bChat ; 
	private JTextArea textNotif;
	private JScrollPane scrollPaneNotif ; 
	
	
	//Constructor
	public FenetreMain () throws UnknownHostException{
		this.createComponents() ;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void createComponents () throws UnknownHostException {

		//Buttons	
		bChat= new JButton("Chat with");

		//Listeners
		bChat.addActionListener(this);
		
		//Panel
		JPanel panel = new JPanel();
		
		//TextArea 
		textNotif  = new JTextArea(5,20);
		
		//Scroll
		scrollPaneNotif = new JScrollPane(textNotif);
		scrollPaneNotif.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneNotif.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		//Randoms gens
		User[] data = {new User("name",InetAddress.getLocalHost(),Status.Online)};
		//List
		listUsers = new JList<User>(data);
		listUsers.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		listUsers.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		
		//Setup
		//set
		panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
		panel.setLayout(new GridLayout(3,2));
		
		//add
		panel.add(listUsers);
		panel.add(scrollPaneNotif);
		panel.add(bChat);
		this.add(panel);

	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == bChat){
			JOptionPane.showMessageDialog(null, "Chat avec le monsieur n�"+ listUsers.getSelectedIndex());
		}
		
	}



public static void main(String[] args) throws UnknownHostException {
		
	//Fenetre 1
			FenetreMain fenetre1 = new FenetreMain();
			fenetre1.pack();
			fenetre1.setVisible(true);
			
}
}
