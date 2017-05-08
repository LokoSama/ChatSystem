package Fenetre;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import Model.Status;
import Model.User;


@SuppressWarnings("serial")

public class FenetreMain extends JFrame implements ActionListener,WindowListener {

	//Interface
	private JButton bChat ;
	private JButton bStatus;
	
	//Model
	private JList<Model.Status> jlistStatus;
	private JList<User> jlistUsers ;
	private JScrollPane scrollPaneNotif ; 
	private JTextArea textCurrentStatus;
	private JTextArea textNotif;
	private DefaultListModel<User> jlistModel;
	private DefaultListModel<Model.Status> jlistMStatus ; 

	private View view;

	//Constructor
	public FenetreMain (View view) {
		this.view = view;
		this.createComponents() ;
		this.initUserList(view.getUserList()); //user list init
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == bChat){
			if (jlistUsers.getSelectedValue() != null) {
				JOptionPane.showMessageDialog(null, "Tentative de chat avec "+ jlistUsers.getSelectedValue());
				view.launchChatWith(jlistUsers.getSelectedValue());
			}
		}
		if (source == bStatus){
			Status select = jlistStatus.getSelectedValue();
			if (select != null) {
				JOptionPane.showMessageDialog(null, "Nouveau statut : "+ select);
				textCurrentStatus.setText("Statut actuel : " + select.name());
				this.view.setLocalStatus(select);
			}
		}
	}

	//populates the JList with the existing userArrayList
	public void initUserList(ArrayList<User> userArrayList) {
		for (int i = 0; i < userArrayList.size(); i++) {
			jlistModel.addElement(userArrayList.get(i));
		}
	}

	public void setNotif(String txt){
		this.textNotif.append(txt+"\n");
	}

	public void updateUserList() {
		jlistModel.clear();
		this.initUserList(view.getUserList());
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
		textNotif.setEditable(false);
		textCurrentStatus = new JTextArea("Statut actuel : " + view.getLocalUser().getStatus().name(),5,20);
		textCurrentStatus.setEditable(false);

		//Scroll
		scrollPaneNotif = new JScrollPane(textNotif);
		scrollPaneNotif.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneNotif.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

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

	//WindowListener functions to override : we only care about windowClosing
	@Override
	public void windowClosing(WindowEvent e) {
		this.view.closeWindow();
	}
	
	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowOpened(WindowEvent e) {}

}

