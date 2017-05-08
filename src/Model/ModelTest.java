package Model;

import static org.junit.Assert.assertEquals;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ModelTest {

	private static Model model;
	
	@BeforeClass
	public static void setUp() throws Exception {
		model = new Model();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		
	}

	@Test
	public void test() throws UnknownHostException {
		int old = model.getUserList().size();
		User newUser = new User("Patrick", InetAddress.getLocalHost(), Status.Online);
		
		model.addUser(newUser.getUsername(),newUser.getIP(),newUser.getStatus());
		assertEquals(old+1,model.getUserList().size());
		assertEquals(newUser,model.getUser(newUser.getUsername(), newUser.getIP()));
	}

}
