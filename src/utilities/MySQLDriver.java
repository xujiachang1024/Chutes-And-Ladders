package utilities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.mysql.jdbc.Driver;

public class MySQLDriver implements Serializable {

	public static final long serialVersionUID = 1;

	private static final String sqlSelectAllUsers = "SELECT * FROM ChutesAndLadders.User";
	private static final String sqlSelectOneUser = "SELECT * FROM ChutesAndLadders.User WHERE username=?";
	private static final String sqlAddOneUser = "INSERT INTO ChutesAndLadders.User (username, password, spaceForward, spaceBackward, laddersClimbed, slidesDescended, onlineStatus) VALUES(?, ?, 0, 0, 0, 0, 0)";
	private static final String sqlUpdateUsername = "UPDATE ChutesAndLadders.User SET username=? WHERE username=?";
	private static final String sqlUpdatePassword = "UPDATE ChutesAndLadders.User SET password=? WHERE username=?";
	private static final String sqlUpdateSpaceForward = "UPDATE ChutesAndLadders.User SET spaceForward=? WHERE username=?";

	private static final String sqlUpdateSpaceBackward = "UPDATE ChutesAndLadders.User SET spaceBackward=? WHERE username=?";
	private static final String sqlUpdateLaddersClimbed = "UPDATE ChutesAndLadders.User SET laddersClimbed=? WHERE username=?";
	private static final String sqlUpdateSlidesDescended = "UPDATE ChutesAndLadders.User SET slidesDescended=? WHERE username=?";
	private static final String sqlUpdateOnlineStatus = "UPDATE ChutesAndLadders.User SET onlineStatus=? WHERE username=?";
	private Connection sqlConnection;
	private String sqlHostname;
	private Integer sqlPort;
	private String sqlUsername;
	private String sqlPassword;
	private String sqlConnectionURL;

	public MySQLDriver() {

		this.sqlConnection = null;
		// PLEASE DON'T change the default initialization here!
		// This default initialization is the same as the JDBC lab instruction.
		// The only difference here should your MySQLWorkbench password.
		// See the setSQLVariables() method below to change it.
		this.sqlHostname = "localhost";
		this.sqlPort = 3306;
		this.sqlUsername = "root";
		this.sqlPassword = null;

		try {
			new Driver();
			System.out.println("Success: Initialize MySQLDriver");
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	public void addOneUser(User inUser) {

		if (!this.checkOneUser(inUser.getUsername())) {
			try {
				PreparedStatement ps = this.sqlConnection.prepareStatement(sqlAddOneUser);
				ps.setString(1, inUser.getUsername());
				ps.setString(2, inUser.getPassword());
				ps.executeUpdate();
				if (this.checkOneUser(inUser.getUsername())) {
					System.out.println("Success: User " + inUser.getUsername() + " added");
				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	public boolean checkOneUser(String inUsername) {

		try {
			PreparedStatement ps = this.sqlConnection.prepareStatement(sqlSelectOneUser);
			ps.setString(1, inUsername);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				String usernameInDatabase = result.getString(1);
				if (inUsername.equals(usernameInDatabase)) {
					System.out.println("Success: User " + inUsername + " exists");
					return true;
				}
			}
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
			sqle.printStackTrace();
		}

		System.out.println("Fail to find User " + inUsername);
		return false;
	}

	public boolean connect(MySQLSettings inMySQLSettings) {

		String inHostname = inMySQLSettings.getSQLHostname();
		Integer inPort = inMySQLSettings.getSQLPort();
		String inUsername = inMySQLSettings.getSQLUsername();
		String inPassword = inMySQLSettings.getSQLPassword();

		this.setSQLVariables(inHostname, inPort, inUsername, inPassword);

		if ((this.sqlHostname == null) || (this.sqlPort == null) || (this.sqlUsername == null)
				|| (this.sqlPassword == null)) {
			System.out.println("Fail to connect MySLQDriver: 1 of the 4 SQL variables is null");
			return false;
		}

		try {
			this.sqlConnection = DriverManager.getConnection(this.sqlConnectionURL);
			System.out.println("Success: Connect MySQLDriver");
			return true;
		} catch (SQLException sqle) {
			System.out.println("Fail to connect MySLQDriver: " + sqle.getMessage());
			sqle.printStackTrace();
			return false;
		}
	}

	// You need enter your MySQLWorkbench setting first to be able to connect.
	// returns true if a successful connection
	// returns false if an unsuccessful connection
	public boolean connect(String inHostname, int inPort, String inUsername, String inPassword) {

		this.setSQLVariables(inHostname, inPort, inUsername, inPassword);

		if ((this.sqlHostname == null) || (this.sqlPort == null) || (this.sqlUsername == null)
				|| (this.sqlPassword == null)) {
			System.out.println("Fail to connect MySLQDriver: 1 of the 4 SQL variables is null");
			return false;
		}

		try {
			this.sqlConnection = DriverManager.getConnection(this.sqlConnectionURL);
			System.out.println("Success: Connect MySQLDriver");
			return true;
		} catch (SQLException sqle) {
			System.out.println("Fail to connect MySLQDriver: " + sqle.getMessage());
			sqle.printStackTrace();
			return false;
		}
	}

	public HashMap<String, User> getAllUsers() {
		HashMap<String, User> existingUsers = new HashMap<>();

		try {
			PreparedStatement ps = this.sqlConnection.prepareStatement(sqlSelectAllUsers);
			ResultSet result = ps.executeQuery();
			result.beforeFirst();
			while (result.next()) {
				String inUsername = result.getString(1);
				String inPassword = result.getString(2);
				int inSpaceForward = result.getInt(3);
				int inSpaceBackward = result.getInt(4);
				int inLaddersClimbed = result.getInt(5);
				int inSlidesDescended = result.getInt(6);
				existingUsers.put(inUsername, new User(inUsername, inPassword, inSpaceForward, inSpaceBackward,
						inLaddersClimbed, inSlidesDescended, this));
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return existingUsers;
	}

	public MySQLSettings getMySQLSettings() {
		if ((this.sqlHostname == null) || (this.sqlPort == null) || (this.sqlUsername == null)
				|| (this.sqlPassword == null)) {
			System.out.println("Fail to retrive MySQLSettings");
			return null;
		} else {
			System.out.println("Success: MySQLSettings retrieved");
			return new MySQLSettings(this.sqlHostname, this.sqlPort, this.sqlUsername, this.sqlPassword);
		}
	}

	public User getOneUser(String inUsername) {

		try {
			PreparedStatement ps = this.sqlConnection.prepareStatement(sqlSelectOneUser);
			ps.setString(1, inUsername);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				String inPassword = result.getString(2);
				int inSpaceForward = result.getInt(3);
				int inSpaceBackward = result.getInt(4);
				int inLaddersClimbed = result.getInt(5);
				int inSlidesDescended = result.getInt(6);
				System.out.println("Success: User " + inUsername + " retrieved");
				return new User(inUsername, inPassword, inSpaceForward, inSpaceBackward, inLaddersClimbed,
						inSlidesDescended, this);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return null;
	}

	public int getOnlineStatus(String inUsername) {

		try {
			PreparedStatement ps = this.sqlConnection.prepareStatement(sqlSelectOneUser);
			ps.setString(1, inUsername);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				return result.getInt(7);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return -1;
	}

	// PRIVATE method!!!
	// allows the MySQLDriver object to connect
	// according to your MySQLWorkbench setting.
	private void setSQLVariables(String inHostname, int inPort, String inUsername, String inPassword) {
		this.sqlHostname = inHostname;
		this.sqlPort = inPort;
		this.sqlUsername = inUsername;
		this.sqlPassword = inPassword;
		this.sqlConnectionURL = "jdbc:mysql://" + this.sqlHostname + ":" + Integer.toString(this.sqlPort) + "/ChutesAndLadders?user=" + this.sqlUsername + "&password=" + this.sqlPassword;
		System.out.println("Success: setSQLVariables() method");
	}

	public void stop() {
		try {
			this.sqlConnection.close();
			System.out.println("Success: MySQLDriver Disconnected");
		} catch (SQLException sqle) {
			System.out.println("Fail to disconnect MySLQDriver: " + sqle.getMessage());
			sqle.printStackTrace();
		}
	}

	public void toggleOffline(String inUsername) {

		if (this.checkOneUser(inUsername)) {

			try {
				PreparedStatement ps = this.sqlConnection.prepareStatement(sqlUpdateOnlineStatus);
				ps.setInt(1, 0);
				ps.setString(2, inUsername);
				ps.executeUpdate();
				System.out.println("Success: User " + inUsername + "is now offline");
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	public void toggleOnline(String inUsername) {

		if (this.checkOneUser(inUsername)) {

			try {
				PreparedStatement ps = this.sqlConnection.prepareStatement(sqlUpdateOnlineStatus);
				ps.setInt(1, 1);
				ps.setString(2, inUsername);
				ps.executeUpdate();
				System.out.println("Success: User " + inUsername + "is now online");
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	public void updateLaddersClimbed(String oldUsername, int newLaddersClimbed) {

		if (this.checkOneUser(oldUsername)) {

			try {
				PreparedStatement ps = this.sqlConnection.prepareStatement(sqlUpdateLaddersClimbed);
				ps.setInt(1, newLaddersClimbed);
				ps.setString(2, oldUsername);
				ps.executeUpdate();
				System.out.println("Success: User " + oldUsername + " updated his laddersClimbed to "
						+ Integer.toString(newLaddersClimbed));
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	public void updatePassword(String oldUsername, String newPassword) {

		if (this.checkOneUser(oldUsername)) {

			try {
				PreparedStatement ps = this.sqlConnection.prepareStatement(sqlUpdatePassword);
				ps.setString(1, newPassword);
				ps.setString(2, oldUsername);
				ps.executeUpdate();
				System.out.println("Success: User " + oldUsername + " updated his password to " + newPassword);
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	public void updateSlidesDescended(String oldUsername, int newSlidesDescended) {

		if (this.checkOneUser(oldUsername)) {

			try {
				PreparedStatement ps = this.sqlConnection.prepareStatement(sqlUpdateSlidesDescended);
				ps.setInt(1, newSlidesDescended);
				ps.setString(2, oldUsername);
				ps.executeUpdate();
				System.out.println("Success: User " + oldUsername + " updated his slidesDescended to "
						+ Integer.toString(newSlidesDescended));
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	public void updateSpaceBackward(String oldUsername, int newSpaceBackward) {

		if (this.checkOneUser(oldUsername)) {

			try {
				PreparedStatement ps = this.sqlConnection.prepareStatement(sqlUpdateSpaceBackward);
				ps.setInt(1, newSpaceBackward);
				ps.setString(2, oldUsername);
				ps.executeUpdate();
				System.out.println("Success: User " + oldUsername + " updated his spaceBackward to "
						+ Integer.toString(newSpaceBackward));
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	public void updateSpaceForward(String oldUsername, int newSpaceForward) {

		if (this.checkOneUser(oldUsername)) {

			try {
				PreparedStatement ps = this.sqlConnection.prepareStatement(sqlUpdateSpaceForward);
				ps.setInt(1, newSpaceForward);
				ps.setString(2, oldUsername);
				ps.executeUpdate();
				System.out.println("Success: User " + oldUsername + " updated his spaceForward to "
						+ Integer.toString(newSpaceForward));
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	public void updateUsername(String oldUsername, String newUsername) {

		if (this.checkOneUser(oldUsername)) {

			try {
				PreparedStatement ps = this.sqlConnection.prepareStatement(sqlUpdateUsername);
				ps.setString(1, newUsername);
				ps.setString(2, oldUsername);
				ps.executeUpdate();
				System.out.println("Success: User " + oldUsername + " updated his username to " + newUsername);
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	// public static void main(String args[]) {
	// MySQLDriver driver = new MySQLDriver();
	// driver.connect("localhost", 3306, "root", ""); // TODO change your
	// password here
	// boolean bool1 = driver.checkOneUser("Ernest");
	// boolean bool2 = driver.checkOneUser("Phil");
	// boolean bool3 = driver.checkOneUser("Benjamin");
	// driver.updatePassword("Benjamin", "bucknell");
	// driver.updateSpaceForward("Benjamin", 1);
	// driver.updateSpaceBackward("Benjamin", 1);
	// driver.updateLaddersClimbed("Benjamin", 1);
	// driver.updateSlidesDescended("Benjamin", 1);
	// }

}
