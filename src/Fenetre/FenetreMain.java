package Fenetre;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

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

import Controller.Debugger;
import Model.Status;
import Model.User;


@SuppressWarnings("serial")

public class FenetreMain extends JFrame implements ActionListener,WindowListener {

	//Attributs
	private JList<User> jlistUsers ;
	private JList<Model.Status> jlistStatus;
	DefaultListModel<Model.Status> jlistMStatus ;
	DefaultListModel<User> jlistModel;
	private JButton bChat ; 
	private JButton bStatus;
	private JTextArea textNotif;
	private JTextArea textCurrentStatus;
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
		this.setTitle(view.getLocalUser().getUsername());
		
		//Buttons	
		bChat= new JButton("Chat with");
		bStatus = new JButton("Set new status");
		//Listeners
		bChat.addActionListener(this);
		bStatus.addActionListener(this);
		//Panel
		JPanel panel = new JPanel();

		//TextArea 
		textNotif  = new JTextArea(5,20);
		textCurrentStatus = new JTextArea("Statut actuel :" + view.getLocalUser().getStatus().name(),5,20);
		textCurrentStatus.setEditable(false);
			
		//Scroll
		scrollPaneNotif = new JScrollPane(textNotif);
		scrollPaneNotif.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneNotif.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		//Randoms gens
		//User[] data = {new User("name",InetAddress.getLocalHost(),Status.Online)};

		//JList model (the content, modify this to modify the JList)
		jlistModel = new DefaultListModel<User>();
		
		jlistMStatus = new DefaultListModel<Model.Status>();
		jlistMStatus.addElement(Status.Online); 
		jlistMStatus.addElement(Status.Busy); 
		jlistMStatus.addElement(Status.Away); 
		jlistMStatus.addElement(Status.Offline); 
		//JList
		jlistUsers = new JList<User>(jlistModel);
		jlistUsers.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		jlistUsers.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		jlistStatus = new JList<Model.Status>(jlistMStatus);
		jlistStatus.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		jlistStatus.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		//Setup
		//set
		panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
		panel.setLayout(new GridLayout(3,2));

		//add
		panel.add(jlistUsers);
		panel.add(bStatus);
		panel.add(bChat);
		panel.add(jlistStatus);
		panel.add(scrollPaneNotif);
		panel.add(textCurrentStatus);
		this.add(panel);
		addWindowListener(this);
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
	
	public void setNotif(String txt){
		this.textNotif.append(txt+"\n");
	}
	
	

	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == bChat){
			JOptionPane.showMessageDialog(null, "Tentative de chat avec "+ jlistUsers.getSelectedValue());
			view.launchChatWith(jlistUsers.getSelectedValue());
		}
		if (source == bStatus){
			Status select = jlistStatus.getSelectedValue();
			JOptionPane.showMessageDialog(null, "Nouveau statut : "+ select);
			textCurrentStatus.setText("Statut actuel : " + select.name());
			this.view.setLocalStatus(select);
		}

	}

	
	@Override
	public void windowClosed(WindowEvent arg0) {
		
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		this.view.closeWindow();
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}







}

