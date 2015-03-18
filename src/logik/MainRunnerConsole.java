package logik;

import java.io.File;

import com.sshtools.ssh.SshException;

public class MainRunnerConsole {
	
	Controller controller;
	
	public MainRunnerConsole(Controller controller){
		this.controller = controller;
				
	}
	
	public void run(){
		System.out.println(controller.run());
	}
	
	public static void main(String[] args) {
		File privateKey = new File("publicKey/keys");
		File publicKey = new File("publicKey/keys.pub");
		String pwd = "zimadmin";
		Controller controller;
		IExecutionCase executionCase = new SerialCaseCSV("zimadmin", "141.79.10.86", "demonstration.csv");
		try {
			controller = new Controller(privateKey, publicKey, pwd);
			controller.connect(executionCase);
			MainRunnerConsole mrc = new MainRunnerConsole(controller);
			mrc.run();
			} catch (SshException e) {
			e.printStackTrace();
		}
	}

}
