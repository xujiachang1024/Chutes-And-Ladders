package utilities;

import java.io.Serializable;

public class MySQLSettings implements Serializable {

	public static final long serialVersionUID = 1;

	private String sqlHostname;
	private Integer sqlPort;
	private String sqlUsername;
	private String sqlPassword;

	public MySQLSettings(MySQLSettings inMySQLSettings) {

	}

	public MySQLSettings(String inHostname, int inPort, String inUsername, String inPassword) {
		this.sqlHostname = inHostname;
		this.sqlPort = inPort;
		this.sqlUsername = inUsername;
		this.sqlPassword = inPassword;
	}

	public String getSQLHostname() {
		return this.sqlHostname;
	}

	public String getSQLPassword() {
		return this.sqlPassword;
	}

	public int getSQLPort() {
		return this.sqlPort;
	}

	public String getSQLUsername() {
		return this.sqlUsername;
	}

	public void setSQLHostname(String inHostname) {
		this.sqlHostname = inHostname;
	}

	public void setSQLPassword(String inPassword) {
		this.sqlPassword = inPassword;
	}

	public void setSQLPort(int inPort) {
		this.sqlPort = inPort;
	}

	public void setSQLUsername(String inUsername) {
		this.sqlUsername = inUsername;
	}

}
