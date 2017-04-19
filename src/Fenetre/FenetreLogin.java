package Fenetre;


import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.UnknownHostException;

import javax.imageio.IIOException;
import javax.swing.*;


//Cette fen�tre est la fen�tre de login. impl�mentation d'un popup de connexion qui pourra servir au fail de co

public class FenetreLogin extends JFrame implements ActionListener {
	//Attributs

	//View
	private View view ;
	//Graphique
	private JTextArea textPseudo ;
	private JButton bLogin ;
	private JLabel lbUsername;
	private JPanel panel ;
	//Tests
	private boolean okPseudo;
	private String Pseudo;

	//Constructeur
	public FenetreLogin (View view) { 
		this.view = view ;
		this.createComponents();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		

	}


	public boolean getOk() {
		return okPseudo;
	}

	public String getPseudo() {
		return Pseudo;	
	}


	private void createComponents () {
		//Tests
		okPseudo = false;


		//Buttons	
		bLogin= new JButton("Login");

		//Listeners
		bLogin.addActionListener(this);

		//Panel
		JPanel panel = new JPanel();

		//Labels
		lbUsername = new JLabel("Username",SwingConstants.CENTER);

		//TextArea 
		textPseudo = new JTextArea(5,20);


		//Setup
		//set
		panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
		panel.setLayout(new GridLayout(3,1));

		//add
		panel.add(lbUsername);
		panel.add(textPseudo);
		panel.add(bLogin);
		this.add(panel);
	}

	public void close() {
		this.setVisible(false);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == bLogin){
			String newUser= this.textPseudo.getText();
			JOptionPane.showMessageDialog(null, "Login de "+ newUser);
			this.textPseudo.setText("");
			System.out.println("dans le listener");
			view.Login(newUser);
			this.setVisible(false);
		}
	}
	


}