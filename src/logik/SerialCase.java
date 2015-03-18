package logik;

/*
 * Target of SerialCase.csv
 * This class will execute all your commands in the file commandFile. 
 * The execution sequence is from top to down 
 */


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.sshtools.ssh.SshIOException;
import com.sshtools.ssh.SshSession;

public class SerialCase extends AbstractRunCase implements IExecutionCase {

	protected Connection connection;
	protected String commandFile;
	protected SshSession session;

	public SerialCase(String username, String host, String commandFile) {
		this.connection = new Connection(username, host, 22);
		this.commandFile = commandFile;
	}

	public void setSession(SshSession session) {
		this.session = session;
	}

	public SshSession getSession() {
		return this.session;
	}

	public void closeSession() {
		session.close();
	}

	@Override
	public List<Connection> getConnectionInfo() {
		List<Connection> l = new ArrayList<Connection>(1);
		l.add(connection);
		return l;
	}

	@Override
	public FileReader getCommandFile() throws FileNotFoundException {
		FileReader fileReader = new FileReader(commandFile);
		return fileReader;
	}

	public int run() {
		int errorCode = 0;
		try {
			FileWriter fileWriter = new FileWriter("ConnectionShell_"
					+ connection.getHostname() + "_"
					+ connection.getUsername() + ".log");
			OutputStream sendCmd = session.getOutputStream();
			InputStream hostResponses = session.getInputStream();
			BufferedReader cmdFile = new BufferedReader(getCommandFile());
			String line = "";
			ReaderThread readerThread = new ReaderThread(hostResponses, sendCmd, fileWriter, connection.getUsername());
			readerThread.start();
			Thread.sleep(1000);
			do {
				while(!readerThread.isReadyForCommand()){
					Thread.sleep(100);
				}
				sendCmd.write((line + "\n").getBytes());
				sendCmd.flush();
			} while ((line = cmdFile.readLine()) != null);
			

			cmdFile.close();
			Thread.sleep(1000);
			readerThread.interrupt();
			sendCmd.write("exit\n".getBytes());
			readerThread.join();
		} catch (SshIOException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return errorCode;
	}
}
