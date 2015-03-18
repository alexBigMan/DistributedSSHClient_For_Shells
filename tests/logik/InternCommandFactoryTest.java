package logik;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.List;

import logik.internCMD.CheckExecutionReturnCode;
import logik.internCMD.PathCMD;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.generator.HamcrestFactoryWriter;
import org.junit.Test;

import com.sshtools.ssh.ChannelEventListener;
import com.sshtools.ssh.PseudoTerminalModes;
import com.sshtools.ssh.SshClient;
import com.sshtools.ssh.SshException;
import com.sshtools.ssh.SshIOException;
import com.sshtools.ssh.SshSession;
import com.sshtools.ssh.message.SshMessageRouter;

public class InternCommandFactoryTest {

	private DummyExCase dummyExCase;

	private class DummyExCase implements IExecutionCase {

		@Override
		public List<Connection> getConnectionInfo() {
			return null;
		}
		@Override
		public Reader getCommandFile() throws FileNotFoundException {
			return null;
		}
		@Override
		public SshSession getSession() {
			return new SshSession() {
				
				@Override
				public void setAutoConsumeInput(boolean autoConsumeInput) {
				}
				@Override
				public SshMessageRouter getMessageRouter() {
					return null;
				}
				@Override
				public int getChannelId() {
					return 0;
				}
				@Override
				public void addChannelEventListener(ChannelEventListener listener) {
				}
				
				@Override
				public boolean startShell() throws SshException {
					return false;
				}
				@Override
				public boolean requestPseudoTerminal(String term, int cols, int rows,
						int width, int height, PseudoTerminalModes terminalModes)
						throws SshException {
					return false;
				}
				@Override
				public boolean requestPseudoTerminal(String term, int cols, int rows,
						int width, int height, byte[] modes) throws SshException {
					return false;
				}
				@Override
				public boolean requestPseudoTerminal(String term, int cols, int rows,
						int width, int height) throws SshException {
					return false;
				}
				@Override
				public boolean isClosed() {
					return false;
				}
				@Override
				public InputStream getStderrInputStream() throws SshIOException {
					return null;
				}
				@Override
				public OutputStream getOutputStream() throws SshIOException {
					return new OutputStream() {
						@Override
						public void write(int b) throws IOException {
						}
					};
				}
				@Override
				public InputStream getInputStream() throws SshIOException {
					return null;
				}
				@Override
				public SshClient getClient() {
					return null;
				}
				@Override
				public int exitCode() {
					return 0;
				}
				@Override
				public boolean executeCommand(String cmd, String charset)
						throws SshException {
					return false;
				}
				@Override
				public boolean executeCommand(String cmd) throws SshException {
					return false;
				}
				@Override
				public void close() {
				}
				@Override
				public void changeTerminalDimensions(int cols, int rows, int width,
						int height) throws SshException {
				}
			};
		}
		@Override
		public void setSession(SshSession session) {
		}
		@Override
		public void closeSession() {
		}
		@Override
		public int run() {
			return 0;
		}
	}
	
	@Test
	public void testGetPathCMDRoot() {
		dummyExCase = new DummyExCase();
		
		ICommand ic = InternCommandFactory.getInternCommand("--PATH \\hallo\\palo\\", new DummyExCase());
		assertTrue(ic instanceof PathCMD);
		assertTrue(((PathCMD) ic).isRootPath());
	}
	
	@Test
	public void testGetPathCMDNotRoot() {
		ICommand ic = InternCommandFactory.getInternCommand("--PATH hallo\\palo\\", new DummyExCase());
		assertTrue(ic instanceof PathCMD);
		assertFalse(((PathCMD) ic).isRootPath());
		String str = ic.execCommand(new OutputStream() {
						@Override
						public void write(int b) throws IOException {
						}
					});
		MatcherAssert.assertThat(str, Matchers.is("mkdir -p \"hallo\"/\"palo\"/\n"));
	}
	
	@Test
	public void testGetPathCMDNotRootSlash() {
		ICommand ic = InternCommandFactory.getInternCommand("--PATH hallo/palo/", new DummyExCase());
		assertTrue(ic instanceof PathCMD);
		assertFalse(((PathCMD) ic).isRootPath());		
		String str = ic.execCommand(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
			}
		});
		MatcherAssert.assertThat(str, Matchers.is("mkdir -p \"hallo\"/\"palo\"/\n"));
	}

//	@Test
//	public void testCreateReturnCodeCMD() {
//		ICommand ic = InternCommandFactory.getInternCommand("--RETURN_CODE", new DummyExCase());
//		assertTrue(ic instanceof CheckExecutionReturnCode);
//	}
}
