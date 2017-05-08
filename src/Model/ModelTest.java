package Model;

import static org.junit.Assert.assertEquals;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class ModelTest {

	private Model model;


	private User createUser() throws UnknownHostException {
		return new User("Patrick",InetAddress.getLocalHost(),Status.Online);
	}

	private User createUseri(int itera) throws UnknownHostException {
		return new User("Patrick"+itera,InetAddress.getLocalHost(),Status.Online);
	}

	@Before
	public void setUp() throws Exception {
		model = new Model();
	}

	@AfterClass
	public static void tearDown() throws Exception {}

	@Test
	public void testAjoutUser() throws UnknownHostException {
		int oldSize = model.getUserList().size();
		User newUser = createUser();
		model.addUser(newUser.getUsername(),newUser.getIP(),newUser.getStatus());
		assertEquals("Taille liste",oldSize+1,model.getUserList().size());
		assertEquals("Pr�sence nouveau User",newUser,model.getUser(newUser.getUsername(), newUser.getIP()));
	}

	@Test
	public void testDeleteUser() throws UnknownHostException {
		User newUser = createUser();
		model.addUser(newUser.getUsername(),newUser.getIP(),newUser.getStatus()); //Ajout User
		int oldSize = model.getUserList().size();
		assertEquals("Pr�sence nouveau User",newUser,model.getUser(newUser.getUsername(), newUser.getIP()));
		model.deleteUser(newUser.getUsername(),newUser.getIP()); //DeleteUser
		assertEquals("Taille liste",oldSize-1,model.getUserList().size());
		assertEquals("Absence User supprim�",null,model.getUser(newUser.getUsername(), newUser.getIP()));
	}

	@Test
	public void testSetStatus() throws UnknownHostException {
		User newUser = createUser();
		model.addUser(newUser.getUsername(),newUser.getIP(),newUser.getStatus()); //Ajout User	
		assertEquals("Statut Online",Status.Online,model.getUser(newUser.getUsername(), newUser.getIP()).getStatus());	
		model.setStatus(newUser.getUsername(),newUser.getIP(),Status.Busy);
		assertEquals("Nouveau statut Busy",Status.Busy,model.getUser(newUser.getUsername(), newUser.getIP()).getStatus());	
	}

	@Test
	public void testBenchmark () throws UnknownHostException {
		int oldSize = model.getUserList().size();
		int iter;
		User newUser ;
		for (iter = 0; iter<1000;iter++) { //On ajoute Patrick[i] 1000 fois
			newUser = createUseri(iter);
			model.addUser(newUser.getUsername(),newUser.getIP(),newUser.getStatus());

		}
		assertEquals("Taille liste",oldSize+1000,model.getUserList().size());
		for (iter = 0; iter<1000;iter++) { //On ajoute Patrick[i] 1000 fois
			newUser = createUseri(iter);
			assertEquals("Présence nouveau User", newUser, model.getUser(newUser.getUsername(), newUser.getIP()));
		}
		for (iter = 0; iter<1000;iter=iter+2) { //On supprime les Patrick pairs (soit 500)
			newUser = createUseri(iter);
			model.deleteUser(newUser.getUsername(),newUser.getIP());
		}
		assertEquals("Taille liste",oldSize+500,model.getUserList().size());
		for (iter = 0; iter<1000;iter=iter+2) { //On supprime les Patrick pairs (soit 500)
			newUser = createUseri(iter);
			assertEquals("Absence User pairs",null,model.getUser(newUser.getUsername(),newUser.getIP()));
		}
		for (iter = 1; iter<1000;iter=iter+2) { //On supprime les Patrick pairs (soit 500)
			newUser = createUseri(iter);
			assertEquals("Présence User pairs",newUser,model.getUser(newUser.getUsername(), newUser.getIP()));
		}

	}

}
