package Fenetre;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import Model.Model;
import Model.User;


@SuppressWarnings("serial")

public class FenetreMain extends JFrame implements ActionListener {

	//Attributs
	private JList<User> jlistUsers ;
	DefaultListModel<User> jlistModel;
	private JButton bChat ; 
	private JTextArea textNotif;
	private JScrollPane scrollPaneNotif ; 
	
	View view;
	
	//Constructor
	public FenetreMain (View view) {
		this.view = view;
		this.createComponents() ;
		this.initUserList(view.getUserList()); //init de la User List
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void createComponents () {

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
		//User[] data = {new User("name",InetAddress.getLocalHost(),Status.Online)};

		//JList model (the content, modify this to modify the JList)
		jlistModel = new DefaultListModel<User>();

		//JList
		jlistUsers = new JList<User>(jlistModel);
		jlistUsers.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		jlistUsers.setLayoutOrientation(JList.HORIZONTAL_WRAP);

		//Setup
		//set
		panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
		panel.setLayout(new GridLayout(3,2));

		//add
		panel.add(jlistUsers);
		panel.add(scrollPaneNotif);
		panel.add(bChat);
		this.add(panel);
		
	}

	//populates the JList with the existing userArrayList
	public void initUserList(ArrayList<User> userArrayList) {
		for (int i = 0; i < userArrayList.size(); i++) {
			jlistModel.addElement(userArrayList.get(i));
		}
	}
	
	public void updateUserList() {
		jlistModel.clear();
		this.initUserList(view.getUserList());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == bChat){
			JOptionPane.showMessageDialog(null, "Tentative de chat avec "+ jlistUsers.getSelectedValue());
			view.launchChatWith(jlistUsers.getSelectedValue());
		}

	}

	}

