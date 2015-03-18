package logik;

import static org.junit.Assert.*;

import static org.hamcrest.Matchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConnectionTest {
	Connection testee;
	
	@Before
	public void setUp() throws Exception {
		testee = new Connection("alex", "Rechner2", 5555);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAll() {
		assertThat(testee.getHostname(), equalTo("Rechner2"));
		assertThat(testee.getUsername(), equalTo("alex"));
		assertThat(testee.getPort(), equalTo(5555));
	}
	
	@Test
	public void testGetHostname(){
		testee.setHostname("testRechner");
		assertThat(testee.getHostname(), equalTo("testRechner"));
	}

	@Test
	public void testGetUserName(){
		testee.setUsername("testUser");
		assertThat(testee.getUsername(), equalTo("testUser"));
	}
	
	@Test
	public void testGetPort(){
		testee.setPort(88);
		assertThat(testee.getPort(), is(88));
	}


}
