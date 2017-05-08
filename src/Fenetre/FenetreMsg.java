package Fenetre;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import Controller.Debugger;
import Model.User;

@SuppressWarnings("serial")
public class FenetreMsg extends JFrame implements ActionListener, Runnable, WindowListener {

	private JButton bSend ;
	private JButton bSendFile;
	private JTextArea filepath;
	private JPanel panel ;
	private User remoteUser;
	private JTextArea textHist ;
	private JTextArea textSaisi ;
	private View view;

	public FenetreMsg (User remoteUser, View view) { 
		this.remoteUser = remoteUser;
		this.view = view;

		this.setTitle("Chat with " + remoteUser.getUsername());
		this.createComponents();		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		addWindowListener(this);
		this.setLocationRelativeTo(null); //centers the window
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == bSend) {
			String newMsg= this.textSaisi.getText();
			this.textHist.append(view.getLocalUser().getUsername() + ": " + newMsg + "\n");
			this.textSaisi.setText("");
			view.sendText(remoteUser, newMsg);
		} else if (source == bSendFile) {
			view.sendFile(remoteUser, filepath.getText());
			this.filepath.setText("");
		}
	}

	@Override
	//Here goes the code executed in each thread
	public void run() {
		String msg;
		while(true) {
			msg = view.getMessageFrom(remoteUser); //blocking call to wait for a new message
			Debugger.log("FenetreMsg : réception");
			this.textHist.append(this.remoteUser.getUsername() + ": " + msg + "\n");
		}
	}



	private void createComponents() {
		//Buttons	
		bSend= new JButton("Send");
		bSendFile = new JButton("Send file from path :");
		//Listeners
		bSend.addActionListener(this);
		bSendFile.addActionListener(this);
		//TextArea
		textSaisi = new JTextArea(5,20);
		filepath = new JTextArea(1,20);
		textHist = new JTextArea(5,20); 
		textHist.setEditable(false);
		textSaisi.setLineWrap(true);
		textHist.setLineWrap(true);
		filepath.setLineWrap(true);
		//Scrollbar
		JScrollPane scrollHist = new JScrollPane(textHist);
        scrollHist.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollHist.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        JScrollPane scrollSaisi = new JScrollPane(textSaisi);
        scrollSaisi.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollSaisi.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
		//Panel
		panel = new JPanel();

		//Setup
		//set
		panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
		panel.setLayout(new GridLayout(5,1));

		//add
		panel.add(scrollHist);
		panel.add(scrollSaisi);
		panel.add(bSend);
		panel.add(bSendFile);
		panel.add(filepath);
		this.add(panel);
	}

	//WindowListener functions to override : we only care about windowClosing
	@Override
	public void windowClosing(WindowEvent e) {
		this.view.closeFenetreMsg(remoteUser);
	}
	
	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowOpened(WindowEvent e) {}
}
