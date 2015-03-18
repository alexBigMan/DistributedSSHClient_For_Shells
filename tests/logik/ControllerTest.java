package logik;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.generator.HamcrestFactoryWriter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.sshtools.ssh.SshException;
import com.sshtools.ssh.SshSession;

public class ControllerTest {
	IController control;

	@Before
	public void setUp() throws Exception {
		control = new Controller();
	}

	@After
	public void tearDown() throws Exception {
		control.disconnectAll();
	}

	// @Test
	// public void testRunSerialCase() {
	// IExecutionCase exCase = new SerialCase("zimadmin", "141.79.10.87",
	// "testFile.txt");
	// control.connect(exCase);
	// control.run();
	// }
	//
	// @Test
	// public void testRunSerialCaseCSV() {
	// IExecutionCase exCase = new SerialCaseCSV("zimadmin", "141.79.10.87",
	// "testFile.csv");
	// control.connect(exCase);
	// control.run();
	// }

	@Test
	public void testConnectionSuccess() {
		IExecutionCase m = Mockito.mock(IExecutionCase.class);
		Mockito.when(m.getConnectionInfo()).thenReturn(Arrays.asList(new Connection("zimadmin",
				"141.79.10.87", 22)));
		
		List<SshSession> sessions = control.connect(m);
		org.junit.Assert.assertTrue(sessions.get(0).getClient().isAuthenticated());
	}
	
	@Test
	public void testConnectionFailureOnAuth() {
		IExecutionCase m = Mockito.mock(IExecutionCase.class);
		Mockito.when(m.getConnectionInfo()).thenReturn(Arrays.asList(new Connection("zimadmin",
				"141.79.10.85", 22)));
		
		List<SshSession> sessions = control.connect(m);
		org.junit.Assert.assertTrue(sessions.size() == 0);
	}
	
	@Test
	public void testRunOK(){
		IExecutionCase m = Mockito.mock(IExecutionCase.class);
		Mockito.when(m.getConnectionInfo()).thenReturn(Arrays.asList(new Connection("zimadmin",
				"141.79.10.87", 22)));
		Mockito.when(m.run()).thenReturn(0);
		control.connect(m);
		MatcherAssert.assertThat(control.run(), Matchers.is(0));
	}
	
	@Test
	public void testRunFalse(){
		IExecutionCase m = Mockito.mock(IExecutionCase.class);
		Mockito.when(m.getConnectionInfo()).thenReturn(Arrays.asList(new Connection("zimadmin",
				"141.79.10.87", 22)));
		Mockito.when(m.run()).thenReturn(-1);
		control.connect(m);
		MatcherAssert.assertThat(control.run(), Matchers.is(-1));
	}

}
