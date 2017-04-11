package game_logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import utilities.MySQLDriver;
import utilities.User;

public class GameLobbyLogic {
	private User loggedInUser;

	public GameLobbyLogic(User loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	@SuppressWarnings("rawtypes")
	public String[][] getAllRelevantUserData(String term) {
		MySQLDriver sql = new MySQLDriver();
		sql.connect("localhost", 3306, "root", "root");
		HashMap<String, User> allUsers = sql.getAllUsers();
		ArrayList<String> allUsernames = new ArrayList<>();
		ArrayList<String> allSMF = new ArrayList<>();
		ArrayList<String> allSMB = new ArrayList<>();
		ArrayList<String> allLC = new ArrayList<>();
		ArrayList<String> allSD = new ArrayList<>();
		ArrayList<Boolean> allO = new ArrayList<>();
		Iterator it = allUsers.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			if (((User) pair.getValue()).getUsername().toLowerCase().contains(term.toLowerCase())) {
				allUsernames.add(((User) pair.getValue()).getUsername());
				allSMF.add("" + ((User) pair.getValue()).getSpaceForward());
				allSMB.add("" + ((User) pair.getValue()).getSpaceBackward());
				allLC.add("" + ((User) pair.getValue()).getLaddersClimbed());
				allSD.add("" + ((User) pair.getValue()).getSlidesDescended());
				allO.add(((User) pair.getValue()).isOnline());
			}
			it.remove(); // avoids a ConcurrentModificationException
		}

		String[][] userData = new String[allUsernames.size()][6];

		for (int i = 0; i < allUsernames.size(); i++) {
			userData[i][0] = allUsernames.get(i);
			userData[i][1] = allSMF.get(i);
			userData[i][2] = allSMB.get(i);
			userData[i][3] = allLC.get(i);
			userData[i][4] = allSD.get(i);
			if (allO.get(i).equals(new Boolean(false))) {
				userData[i][5] = "<html><font color='rgb(178,34,34)'><i>Offline</i></font></html>";
			} else {
				userData[i][5] = "<html><font color='rgb(0,100,0)'><b>Online<b></font></html>";
			}

		}

		return userData;
	}

	@SuppressWarnings("rawtypes")
	public String[][] getAllUserData() {
		MySQLDriver sql = new MySQLDriver();
		sql.connect("localhost", 3306, "root", "root");
		HashMap<String, User> allUsers = sql.getAllUsers();
		ArrayList<String> allUsernames = new ArrayList<>();
		ArrayList<String> allSMF = new ArrayList<>();
		ArrayList<String> allSMB = new ArrayList<>();
		ArrayList<String> allLC = new ArrayList<>();
		ArrayList<String> allSD = new ArrayList<>();
		Iterator it = allUsers.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			allUsernames.add(((User) pair.getValue()).getUsername());
			allSMF.add("" + ((User) pair.getValue()).getSpaceForward());
			allSMB.add("" + ((User) pair.getValue()).getSpaceBackward());
			allLC.add("" + ((User) pair.getValue()).getLaddersClimbed());
			allSD.add("" + ((User) pair.getValue()).getSlidesDescended());
			it.remove(); // avoids a ConcurrentModificationException
		}

		String[][] userData = new String[allUsernames.size()][6];

		for (int i = 0; i < allUsernames.size(); i++) {
			userData[i][0] = allUsernames.get(i);
			userData[i][1] = allSMF.get(i);
			userData[i][2] = allSMB.get(i);
			userData[i][3] = allLC.get(i);
			userData[i][4] = allSD.get(i);
			userData[i][5] = "Offline";
		}

		return userData;
	}

	public String getPassword() {
		return this.loggedInUser.getPassword();
	}

	public User getUser() {
		return this.loggedInUser;
	}

	public String[] getUserAch() {
		return this.loggedInUser.getAchievements();
	}

	public String[][] getUserData() {
		String[][] userData = { { "<html><b>Username</b></html>", this.loggedInUser.getUsername() },
				{ "<html><b>Spaces Moved Forwards</b></html>", "" + this.loggedInUser.getSpaceForward() },
				{ "<html><b>Spaces Moved Backwards</b></html>", "" + this.loggedInUser.getSpaceBackward() },
				{ "<html><b>Ladders Climbed</b></html>", "" + this.loggedInUser.getLaddersClimbed() },
				{ "<html><b>Slides Descended</b></html>", "" + this.loggedInUser.getSlidesDescended() } };

		return userData;
	}

	public String getUsername() {
		return this.loggedInUser.getUsername();
	}
}

/*
 *
 * Contains list of users and available games. Also populates the game lobby
 * with games so that each active user has a game to join.
 *
 *
 * GameLobbyLogic : Object - ArrayList<Game> : gameLists - User : user -
 * HashMap<String, User> : userList - int numGames - int usersActives - int
 * OpenPlayerSlots (+) GameLobbyLogic() (+) refreshLobby() : void (+)
 * search(String) : User (+) addGame : Game
 *
 *
 */
