package Fenetre;


import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Controller.Debugger;


//Cette fenêtre est la fenêtre de login. implémentation d'un popup de connexion qui pourra servir au fail de co

@SuppressWarnings("serial")
public class FenetreLogin extends JFrame implements ActionListener {
	//Attributs

	//View
	private View view ;
	//Graphique
	private JTextField textPseudo ;
	private JButton bLogin ;
	private JLabel lbUsername;
	private JPanel panel ;
	//Tests
	private String Pseudo;

	//Constructeur
	public FenetreLogin (View view) { 
		this.view = view ;
		this.createComponents();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		

	}

	public String getPseudo() {
		return Pseudo;	
	}

	private void createComponents () {

		//Buttons	
		bLogin= new JButton("Login");

		//Listeners
		bLogin.addActionListener(this);

		//Panel
		JPanel panel = new JPanel();

		//Labels
		lbUsername = new JLabel("Username",SwingConstants.CENTER);

		//TextArea 
		textPseudo = new JTextField(20);

		textPseudo.addActionListener(this);


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
		if (source == bLogin | source == textPseudo){
			String newUser = this.textPseudo.getText();
			if (!newUser.equals("")) {
				JOptionPane.showMessageDialog(null, "Login de "+ newUser);
				this.textPseudo.setText("");
				view.login(newUser);
				this.setVisible(false);
			}
		}
	}



}