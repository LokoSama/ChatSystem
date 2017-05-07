package Model;

import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ModelTest {

	private Model model;
	
	@Before
	public void setUp() throws Exception {
		
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws UnknownHostException {
		Model model = new Model();
		int old = model.getUserList().size();
		User newUser = new User("Patrick", InetAddress.getLocalHost(), Status.Online);
		
		model.addUser(newUser.getUsername(),newUser.getIP(),newUser.getStatus());
		assertEquals(old+1,model.getUserList().size());
		assertEquals(newUser,model.getUser(newUser.getUsername(), newUser.getIP()));
	}

}
