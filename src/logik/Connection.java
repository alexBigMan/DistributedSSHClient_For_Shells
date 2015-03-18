package logik;

public class Connection {
	private String hostname;
	private int port;
	private String username;

	public Connection(String username, String hostname, int port){
		this.username = username;
		this.hostname = hostname;
		this.port = port;
	}
	
	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
