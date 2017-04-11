package utilities;

import java.util.ArrayList;
import java.util.HashMap;

public class User {

	private MySQLDriver mySQLDriver;

	private String username;
	private String password;
	private int spaceForward;
	private int spaceBackward;
	private int laddersClimbed;
	private int slidesDescended;
	private boolean registeredUser;
	private boolean guestUser;

	// Robot Constructor: create a CPU player
	public User(int robotNumber) {
		this.username = "Bot #" + Integer.toString(robotNumber);
		this.password = null;
		this.spaceForward = 0;
		this.spaceBackward = 0;
		this.laddersClimbed = 0;
		this.slidesDescended = 0;
		this.registeredUser = false;
		this.guestUser = false;
		this.mySQLDriver = null;
	}

	// Guest Constructor: create a guest player (local gameplay)
	public User(String name) {
		this.username = name;
		this.password = null;
		this.spaceForward = 0;
		this.spaceBackward = 0;
		this.laddersClimbed = 0;
		this.slidesDescended = 0;
		this.registeredUser = false;
		this.guestUser = true;
		this.mySQLDriver = null;
	}

	// Old User Constructor: read from MySQLDriver
	public User(String inUsername, String inPassword, int inSpaceForward, int inSpaceBackward, int inLaddersClimbed,
			int inSlidesDescended, MySQLDriver inMySQLDriver) {
		this.username = inUsername;
		this.password = inPassword;
		this.spaceForward = inSpaceForward;
		this.spaceBackward = inSpaceBackward;
		this.laddersClimbed = inLaddersClimbed;
		this.slidesDescended = inSlidesDescended;
		this.registeredUser = true;
		this.guestUser = false;
		this.mySQLDriver = inMySQLDriver;
	}

	// New User Constructor: create a new account
	// automatically insert this User into MySQL
	public User(String inUsername, String inPassword, MySQLDriver inMySQLDriver) {
		this.username = inUsername;
		this.password = inPassword;
		this.spaceForward = 0;
		this.spaceBackward = 0;
		this.laddersClimbed = 0;
		this.slidesDescended = 0;
		this.registeredUser = true;
		this.guestUser = false;
		this.mySQLDriver = inMySQLDriver;
		this.mySQLDriver.addOneUser(this);
	}

	public void addLaddersClimbed(int addLaddersClimbed) {
		int newLaddersClimbed = this.laddersClimbed + addLaddersClimbed;
		this.setLaddersClimbed(newLaddersClimbed);
	}

	public void addSlidesDescended(int addSlidesDescended) {
		int newSlidesDescended = this.slidesDescended + addSlidesDescended;
		this.setSlidesDescended(newSlidesDescended);
	}

	public void addSpaceBackward(int addSpaceBackward) {
		int newSpaceBackward = this.spaceBackward + addSpaceBackward;
		this.setSpaceBackward(newSpaceBackward);
	}

	public void addSpaceForward(int addSpaceForward) {
		int newSpaceForward = this.spaceForward + addSpaceForward;
		this.setSpaceForward(newSpaceForward);
	}

	// changes the password
	// automatically updates the JDBC
	public void changePassword(String newPassword) {

		if (this.registeredUser) {
			this.password = newPassword;
			this.mySQLDriver.updatePassword(this.username, this.password);
		}
	}

	// changes the username
	// automatically updates the JDBC
	// returns true if a successful change
	// returns false if an unsuccessful change
	public boolean changeUsername(String newUsername) {

		if (this.registeredUser) {
			if (!this.mySQLDriver.checkOneUser(newUsername)) {
				String oldUsername = this.username;
				this.username = newUsername;
				this.mySQLDriver.updateUsername(oldUsername, this.username);
				return true;
			}
		}

		if (this.guestUser) {
			this.username = newUsername;
			return true;
		}

		return false;
	}

	public boolean comparePassword(String inPassword) {
		return (this.password.equals(inPassword));
	}

	public String[] getAchievements() {
		ArrayList<String> aList = new ArrayList<>();

		aList.add("<html><b>Spaces Moved Forward Achievements: </b></html>");
		aList.add("");
		if (this.getSpaceForward() < 100) {
			aList.add("                None");
		}
		if (this.getSpaceForward() >= 100) {
			aList.add("                Board Crawler");
		}
		if (this.getSpaceForward() >= 1000) {
			aList.add("                Board Stroller");
		}
		if (this.getSpaceForward() >= 10000) {
			aList.add("                Board Walker");
		}
		if (this.getSpaceForward() >= 100000) {
			aList.add("                Board Sprinter");
		}
		if (this.getSpaceForward() >= 1000000) {
			aList.add("                Get A Life");
		}
		aList.add("");
		aList.add("");

		aList.add("<html><b>Spaces Moved Backwards Achievements: </b></html>");
		aList.add("");
		if (this.getSpaceBackward() < 100) {
			aList.add("                None");
		}
		if (this.getSpaceBackward() >= 100) {
			aList.add("                Wrong Way Willie");
		}
		if (this.getSpaceBackward() >= 1000) {
			aList.add("                Reverse, Reverse");
		}
		if (this.getSpaceBackward() >= 10000) {
			aList.add("                Moon Walker");
		}
		if (this.getSpaceBackward() >= 100000) {
			aList.add("                Time Warper");
		}
		if (this.getSpaceBackward() >= 1000000) {
			aList.add("                efiL A teG");
		}
		aList.add("");
		aList.add("");

		aList.add("<html><b>Ladders Climbed Achievements: </b></html>");
		aList.add("");
		if (this.getLaddersClimbed() < 100) {
			aList.add("                None");
		}
		if (this.getLaddersClimbed() >= 100) {
			aList.add("                Step Ladder Stepper");
		}
		if (this.getLaddersClimbed() >= 1000) {
			aList.add("                Helping Mom Hang A Portrait");
		}
		if (this.getLaddersClimbed() >= 10000) {
			aList.add("                Saving A Cat In A Tree");
		}
		if (this.getLaddersClimbed() >= 100000) {
			aList.add("                Michaelangelo in the Sistine Chapel");
		}
		if (this.getLaddersClimbed() >= 1000000) {
			aList.add("                World's Least Likely Astronaut");
		}
		aList.add("");
		aList.add("");

		aList.add("<html><b>Slides Descended Achievements: </b></html>");
		aList.add("");
		if (this.getSlidesDescended() < 100) {
			aList.add("                None");
		}
		if (this.getSlidesDescended() >= 100) {
			aList.add("                Slip 'N' Slider");
		}
		if (this.getSlidesDescended() >= 1000) {
			aList.add("                Oh Chute");
		}
		if (this.getSlidesDescended() >= 10000) {
			aList.add("                Chute Me");
		}
		if (this.getSlidesDescended() >= 100000) {
			aList.add("                Super Duper Chuter Cutie");
		}
		if (this.getSlidesDescended() >= 1000000) {
			aList.add("                Falling Like My GPA");
		}

		return aList.stream().toArray(String[]::new);
	}

	public HashMap<String, User> getAllRegisteredUser() {
		return this.mySQLDriver.getAllUsers();
	}

	public int getLaddersClimbed() {
		return this.laddersClimbed;
	}

	public MySQLDriver getMySQLDriver() {
		return this.mySQLDriver;
	}

	public String getPassword() {
		return this.password;
	}

	public int getSlidesDescended() {
		return this.slidesDescended;
	}

	public int getSpaceBackward() {
		return this.spaceBackward;
	}

	public int getSpaceForward() {
		return this.spaceForward;
	}

	public String getUsername() {
		return this.username;
	}

	public boolean isGuestUser() {
		return this.guestUser;
	}

	public boolean isHuman() {
		return (this.registeredUser || this.guestUser);
	}

	public boolean isOnline() {
		if (this.registeredUser) {
			int onlineStatus = this.mySQLDriver.getOnlineStatus(this.username);
			return (onlineStatus == 1);
		}
		return true;
	}

	public boolean isRegisteredUser() {
		return this.registeredUser;
	}

	public boolean isRobot() {
		return !(this.registeredUser || this.guestUser);
	}

	private void setLaddersClimbed(int inLaddersClimbed) {
		this.laddersClimbed = inLaddersClimbed;
		if (this.registeredUser) {
			this.mySQLDriver.updateLaddersClimbed(this.username, this.laddersClimbed);
		}
	}

	private void setSlidesDescended(int inSlidesDescended) {
		this.slidesDescended = inSlidesDescended;
		if (this.registeredUser) {
			this.mySQLDriver.updateSlidesDescended(this.username, this.slidesDescended);
		}
	}

	private void setSpaceBackward(int inSpaceBackward) {
		this.spaceBackward = inSpaceBackward;
		if (this.registeredUser) {
			this.mySQLDriver.updateSpaceBackward(this.username, this.spaceBackward);
		}
	}

	private void setSpaceForward(int inSpaceForward) {
		this.spaceForward = inSpaceForward;
		if (this.registeredUser) {
			this.mySQLDriver.updateSpaceForward(this.username, this.spaceForward);
		}
	}

	public void toggleOffline() {
		if (this.registeredUser) {
			this.mySQLDriver.toggleOffline(this.username);
		}
	}

	public void toggleOnline() {
		if (this.registeredUser) {
			this.mySQLDriver.toggleOnline(this.username);
		}
	}

	// public static void main(String args[]) {
	// MySQLDriver driver = new MySQLDriver();
	// driver.connect("localhost", 3306, "root", "Boeing7478");
	// driver.checkOneUser("Ernest");
	// User ernest = driver.getOneUser("Ernest");
	// ernest.changeUsername("Ernest");
	// ernest.changePassword("airbus350");
	// ernest.addSpaceForward(1);
	// ernest.addSpaceBackward(2);
	// ernest.addLaddersClimbed(3);
	// ernest.addSlidesDescended(4);
	// }

}
