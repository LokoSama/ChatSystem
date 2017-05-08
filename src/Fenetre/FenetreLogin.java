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


//Login window

@SuppressWarnings("serial")
public class FenetreLogin extends JFrame implements ActionListener {

	//Attributs
	private JButton bLogin ;
	private JLabel lbUsername;
	private JPanel panel ;
	private JTextField textPseudo ;

	private View view ;

	//Constructor
	public FenetreLogin (View view) { 
		this.view = view ;
		this.createComponents();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
	}

	private void createComponents () {
		//Buttons	
		bLogin= new JButton("Login");

		//TextArea 
		textPseudo = new JTextField(20);

		//Panel
		panel = new JPanel();

		//Labels
		lbUsername = new JLabel("Username",SwingConstants.CENTER);

		//Listeners
		bLogin.addActionListener(this);
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

	public void close() {
		this.setVisible(false);
	}
}
