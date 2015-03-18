package logik;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.xml.transform.OutputKeys;

import org.apache.log4j.Logger;

import com.sshtools.net.SocketTransport;
import com.sshtools.publickey.InvalidPassphraseException;
import com.sshtools.publickey.SshPrivateKeyFile;
import com.sshtools.publickey.SshPrivateKeyFileFactory;
import com.sshtools.publickey.SshPublicKeyFile;
import com.sshtools.publickey.SshPublicKeyFileFactory;
import com.sshtools.ssh.ChannelOpenException;
import com.sshtools.ssh.PseudoTerminalModes;
import com.sshtools.ssh.SshConnector;
import com.sshtools.ssh.SshException;
import com.sshtools.ssh.SshSession;
import com.sshtools.ssh.SshTransport;
import com.sshtools.ssh.components.SshPrivateKey;
import com.sshtools.ssh2.Ssh2Client;
import com.sshtools.ssh2.Ssh2PublicKeyAuthentication;
import com.sshtools.ssh2.Ssh2Session;

public class Controller implements IController {

	private List<IExecutionCase> exCases;
	private List<SshSession> sessions;
	private KnownHostKeyVerificationOnFile hostKeys;
	private Logger logger = Logger.getLogger(Controller.class);
	private File privateKey;
	private File publicKey;
	private String pwd;

	public Controller() throws SshException {
		exCases = new ArrayList<IExecutionCase>();
		sessions = new ArrayList<SshSession>();
		hostKeys = new KnownHostKeyVerificationOnFile("keys");
		this.privateKey = new File("publicKey/keys");
		this.publicKey = new File("publicKey/keys.pub");
		this.pwd = "zimadmin";
	}

	public Controller(File privateKey, File publicKey, String pwd)
			throws SshException {
		this.privateKey = privateKey;
		this.publicKey = publicKey;
		this.pwd = pwd;
		exCases = new ArrayList<IExecutionCase>();
		sessions = new ArrayList<SshSession>();
		hostKeys = new KnownHostKeyVerificationOnFile("keys");
	}

	@Override
	public List<SshSession> connect(IExecutionCase exCase) {
		List<Connection> connections = exCase.getConnectionInfo();
		SshConnector sshConnection;
		try {
			sshConnection = SshConnector.createInstance();
			for (Connection c : connections) {
				SshTransport t = new SocketTransport(c.getHostname(),
						c.getPort());

				Ssh2Client client = sshConnection.connect(t, c.getUsername());
				SshPrivateKeyFile sshPrivateKey = SshPrivateKeyFileFactory
						.parse(new FileInputStream(privateKey.getAbsoluteFile()));
				SshPublicKeyFile sshPublicKey = SshPublicKeyFileFactory
						.parse(new FileInputStream(publicKey.getAbsoluteFile()));

				Ssh2PublicKeyAuthentication ssh2PublicKeyAuthentication = new Ssh2PublicKeyAuthentication();
				SshPrivateKey privateKey = sshPrivateKey.toKeyPair(pwd)
						.getPrivateKey();

				ssh2PublicKeyAuthentication.setPrivateKey(privateKey);
				ssh2PublicKeyAuthentication.setPublicKey(sshPublicKey
						.toPublicKey());
				if (client.isConnected()) {
					int ret = client.authenticate(ssh2PublicKeyAuthentication);
					System.out.println("auth : " + ret);
					if (client.isAuthenticated()) {

						SshSession session = client.openSessionChannel();
						PseudoTerminalModes pty = new PseudoTerminalModes(
								client);
						pty.setTerminalMode(PseudoTerminalModes.ECHOE, true);
						session.requestPseudoTerminal("vt100", 80, 24, 0, 0,
								pty);
						session.startShell();
						sessions.add(session);
						exCase.setSession(session);
						exCases.add(exCase);
					} else {
						logger.error("authentication error");
					}
				} else {
					logger.error("client is not connectable");
				}
			}
		} catch (SshException | ChannelOpenException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidPassphraseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sessions;
	}

	@Override
	public void disconnect(IExecutionCase exCase) {
		exCase.closeSession();
		logger.info("session USER:" + exCase.getSession().getClient().getUsername()
				+ " ExitCode:" + exCase.getSession().exitCode());
	}

	@Override
	public void disconnectAll() {
		for (SshSession c : sessions) {
			c.close();
			logger.info("session USER:" + c.getClient().getUsername()
					+ " ExitCode:" + c.exitCode());
		}
	}

	@Override
	public int run() {
		int ret = 0;
		for (IExecutionCase exCase : exCases) {
			ret = exCase.run();
			if (ret == -1)
				return -1;
		}
		return 0;
	}

	@Override
	public void mergeFiles(IExecutionCase exCase) {
		// TODO Auto-generated method stub

	}

	@Override
	public int run(IExecutionCase exCase) {
		return exCase.run();
	}

}
