package logik;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.sshtools.ssh.SshSession;

public class SerialCaseCSVTest {

	SerialCaseCSV testee;

	@Before
	public void setUp() throws Exception {
		testee = new SerialCaseCSV("testUser", "127.0.0.1", "tests/testFile.csv");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetSession() {
		SshSession mSession = Mockito.mock(SshSession.class);
		testee.setSession(mSession);
		Assert.assertTrue(testee.getSession() == mSession);
	}

	@Test
	public void testCloseSession() {
		SshSession mSession = Mockito.mock(SshSession.class);
		testee.setSession(mSession);
		testee.closeSession();
		Mockito.verify(mSession, Mockito.times(1)).close();
	}

	@Test
	public void testGetConnectionInfo() {
		Assert.assertTrue(testee.getConnectionInfo().size() == 1);
		MatcherAssert.assertThat(testee.getConnectionInfo().get(0)
				.getHostname(), Matchers.is("127.0.0.1"));
	}

	@Test
	public void testGetCommandFile() {
		try {
			FileReader commandFile = testee.getCommandFile();
			char[] a = new char[1];
			commandFile.read(a);
			Assert.assertTrue(a[0] == ';');
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Ignore
	@Test
	public void testRun() {
		SshSession mSession = Mockito.mock(SshSession.class);
		ReadWriteMock readWriteMock = new ReadWriteMock(0, "testUser \n");
		OutputStream mO = readWriteMock.getOutputStream();
		InputStream mI = readWriteMock.getInputStream();
		try {
			Mockito.when(mSession.getInputStream()).thenReturn(mI);
			Mockito.when(mSession.getOutputStream()).thenReturn(mO);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		testee.setSession(mSession);
		testee.run();
	}
}
