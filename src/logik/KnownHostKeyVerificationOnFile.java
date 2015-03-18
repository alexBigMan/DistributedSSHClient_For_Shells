package logik;

import java.io.File;

import org.apache.log4j.Logger;

import com.sshtools.publickey.AbstractKnownHostsKeyVerification;
import com.sshtools.ssh.SshException;
import com.sshtools.ssh.components.SshPublicKey;

public class KnownHostKeyVerificationOnFile extends
		AbstractKnownHostsKeyVerification {

	File knownHosts;
	static Logger logger = Logger
			.getLogger(KnownHostKeyVerificationOnFile.class);

	public KnownHostKeyVerificationOnFile(String knownhostsFile)
			throws SshException {
		knownHosts = new File(knownhostsFile);
	}

	@Override
	public void onHostKeyMismatch(String host, SshPublicKey allowedHostKey,
			SshPublicKey actualHostKey) throws SshException {
		logger.info("onHostKeyMismatch host :" + host + " key :"
				+ actualHostKey.getFingerprint());
	}

	@Override
	public void onUnknownHost(String host, SshPublicKey key)
			throws SshException {
		logger.info("onUnknownHost host :" + host + " key :"
				+ key.getFingerprint());
	}

}
