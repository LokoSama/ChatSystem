package Fenetre;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class FenetreMsg extends JFrame implements ActionListener, Runnable {

	private JTextArea textHist ;
	private JTextArea textSaisie ;
	private JButton bSend ;
	private JPanel panel ;
	private Socket socket;

	public FenetreMsg (String title, Socket socket) { 
		this.setTitle(title);
		this.socket = socket;
		this.createComponents();		
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}

	@Override
	//Here goes the code executed in each thread
	public void run() {
		boolean running = true;
		while(running) {
			//TODO : send/receive messages
			if (true)
				running = false;
		}
	}

	private void createComponents() {
		//Buttons	
		bSend= new JButton("Send");
		//Listeners
		bSend.addActionListener(this);
		//TextArea
		textSaisie = new JTextArea(5,20);
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
		panel.add(textSaisie);
		panel.add(bSend);
		this.add(panel);

	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == bSend){
			String newMsg= this.textSaisie.getText();
			this.textHist.append(newMsg+"\n");
			this.textSaisie.setText("");
		}
	}



}