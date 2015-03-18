package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.sshtools.net.SocketTransport;
import com.sshtools.publickey.ConsoleKnownHostsKeyVerification;
import com.sshtools.ssh.PasswordAuthentication;
import com.sshtools.ssh.PseudoTerminalModes;
import com.sshtools.ssh.SshAuthentication;
import com.sshtools.ssh.SshClient;
import com.sshtools.ssh.SshConnector;
import com.sshtools.ssh.SshSession;

public class Tests {

	public static void main(String[] args) {
		String str = "-c Range=\"831 AND 1000\" Amount=\"169\"#Range[10,20,30] #Range[\"Fisch\",\"Fisch\",\"Fisch\",\"Fisch\",\"Maus\",\"hase\"] #-c Amount[0,100,10] Location \"Welcome=3  to india\"";
		str = "\"hallo hier bin ich\"";
		str = " a f 123 \n zimadmin";
		str = "--PATH hallo/palo/";
		str = "-c Range=\"831 AND 1000\" Amount=\"1000\"";
		List<String> list = new ArrayList<String>();
		// Matcher m =
		// Pattern.compile("(([\\w=]*\"(\\w+|\\s)+\")|([[^\"\\s]\\S]+))").matcher(str);
		// Matcher m = Pattern.compile("(-c)").matcher(str);
//		Matcher m = Pattern
//				.compile(
//						"(#[\\s]*(\\w+)\\x5B([\"\\w+\",]*)(\"\\w+\")\\x5D)|(#[\\s]*-.[\\s](\\w+)\\x5B(\\w+),(\\w+),(\\w+)\\x5D)|(#[\\s]*(\\w+)\\x5B(\\d+),(\\d+),(\\d+)\\x5D)")
//				.matcher(str);
		String patternOld = "(([\\w=]*\"(\\w+|\\s)+\")|([[^\"\\s]\\S]+))";
		String patternAll = "(-\\w) (\\w+)=(\"\\w+(\\W\\w+)*\")";
		String pattern0 = "(-\\w)? (\\w+)=((\"(\\w+(\\W\\w+)*)\")|(\\w+(\\W\\w+)*))";
		String pattern1 = "(\\w+)=\"(\\w+(\\W\\w+)*)\"";
		String pattern2 = "((\\w+)=(\\w+))";
		String pattern3 = "(\"[\\w+\\W]+\")";
		String pattern4 = "(--PATH\\s(\\\\)?((\\w+\\\\)+))|(--PATH\\s(/)?((\\w+/)+))";
		String pattern5 = "--PATH\\s(\\\\|/)?((\\w+\\\\)+)|((\\w+/)+)";
		System.out.println(str);
		/*
		 * In Java.util.regex.Matcher exist a bug. The find-Method send true back but the matches-Method send false.
		 */
		String pat = patternOld;
		Matcher m = Pattern
				.compile(
						pat)
				.matcher(str);
		
		// Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(str);
		System.out.println(m.groupCount());
		
		if (m.find()) {
			for(int i = 1; i <= m.groupCount();i++){
				if (m.group(i) != null) {
					System.out.println(i);
					list.add(m.group(i));
				}
			}
		}
		System.out.println(list);
		
		m = Pattern.compile(pat).matcher(str);
		while(m.find()){
			System.out.println(m.group(1));
		}

	}
	
	

//		public static void main(String[] args) {
//
//			final BufferedReader reader = new BufferedReader(new InputStreamReader(
//					System.in));
//
//			try {
//				
//
//				String username;
//				username = "zimadmin"; // reader.readLine();
//
//								/**
//				 * Create an SshConnector instance
//				 */
//				SshConnector con = SshConnector.createInstance();
//
//				// Verify server host keys using the users known_hosts file
//				con.getContext().setHostKeyVerification(
//						new ConsoleKnownHostsKeyVerification());
//
//				/**
//				 * Connect to the host
//				 */
//
//
//				SocketTransport transport = new SocketTransport("141.79.10.86", 22);
//
//				System.out.println("Creating SSH client");
//
//				final SshClient ssh = con.connect(transport, username);
//
//				/**
//				 * Authenticate the user using password authentication
//				 */
//				PasswordAuthentication pwd = new PasswordAuthentication();
//
//				do {
//					System.out.print("Password: ");
//					pwd.setPassword(reader.readLine());
//				} while (ssh.authenticate(pwd) != SshAuthentication.COMPLETE
//						&& ssh.isConnected());
//
//				/**
//				 * Start a session and do basic IO
//				 */
//				if (ssh.isAuthenticated()) {
//
//					// Some old SSH2 servers kill the connection after the first
//					// session has closed and there are no other sessions started;
//					// so to avoid this we create the first session and dont ever
//					// use it
//					final SshSession session = ssh.openSessionChannel();
//
//					// Use the newly added PseudoTerminalModes class to
//					// turn off echo on the remote shell
//					PseudoTerminalModes pty = new PseudoTerminalModes(ssh);
//					pty.setTerminalMode(PseudoTerminalModes.ECHOE, true);
//
//					session.requestPseudoTerminal("vt100", 80, 24, 0, 0, pty);
//
//					session.startShell();
//
//					Thread t = new Thread() {
//						public void run() {
//							try {
//								int read;
//								while ((read = session.getInputStream().read()) > -1) {
//									System.out.write(read);
//									System.out.flush();
//								}
//							} catch (IOException ex) {
//								ex.printStackTrace();
//							}
//						}
//					};
//
//					t.start();
//					int read;
//					// byte[] buf = new byte[4096];
//					while ((read = System.in.read()) > -1) {
//						StringBuffer s = new StringBuffer("whois -p wikitests -j CubeLoadParallel -c Range=\"831 AND 1000\" -o parallelLogs\\CubeLoad\\full\\1000\\6.log \n");
//						session.getOutputStream().write(read);
//						Thread.sleep(1000);
//						for(int i = 0; i < s.length(); i++){
//							session.getOutputStream().write(s.charAt(i));
//						}
//					}
//
//					session.close();
//				}
//
//				ssh.disconnect();
//			} catch (Throwable t) {
//				t.printStackTrace();
//			}
//		}
}
