package Fenetre;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class FenetreMsg extends JFrame implements ActionListener {

	private JTextArea textHist ;
	private JTextArea textSaisie ;
	private JButton bSend ;
	private JPanel panel ;

	public FenetreMsg () { 
		this.createComponents();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
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


	public static void main(String[] args) {
		//Fenetre 1
		FenetreMsg fenetre1 = new FenetreMsg();
		fenetre1.pack();
		fenetre1.setVisible(true);

	}


}