package game_logic;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import game_components.Square;
import utilities.User;

public class GameDataLogic {

	private static int[][] valuesSnakesLadders;
	/*
	 * public static void main(String[] args) { GameDataLogic g = new
	 * GameDataLogic();
	 * 
	 * while (g.gameOver == false) { g.playTurn(); }
	 * 
	 * }
	 */
	private Map<User, Integer> userMap;
	private Map<User, Integer> prevMap;
	private boolean isNetworkedGame;
	private boolean gameOver;
	private int whoseTurn;
	private BufferedReader br;
	private int boardDimension;
	private int totalSquares;
	private List<User> userList;

	private List<User> networkedUserList;

	private String update;

	private int numLadders;

	private Square[] gameBoard;

	// Local Gameplay Constructor
	public GameDataLogic() {

		// Initializing variables
		this.isNetworkedGame = false;
		this.userMap = new HashMap<>();
		this.prevMap = new HashMap<>();
		this.gameOver = false;
		this.boardDimension = 5;
		this.numLadders = 2;
		valuesSnakesLadders = new int[][] { { 9, 7, 5, 3, 1, 9, 7, 5, 3, 1 }, // Ladder
																				// Values
				{ 1, 3, 5, 7, 9, 1, 3, 5, 7, 9 } // Snake Values
		};
		this.totalSquares = this.boardDimension * this.boardDimension;

		this.initializePlayers();
		this.initializeBoard();

	}

	public GameDataLogic(List<User> playerList) {
		this.isNetworkedGame = true;
		this.userMap = new HashMap<>();
		this.prevMap = new HashMap<>();
		this.gameOver = false;
		this.boardDimension = 10;
		this.numLadders = 5;
		valuesSnakesLadders = new int[][] { { 19, 17, 15, 13, 11, 9, 7, 5, 3, 1, 19, 17, 15, 13, 11, 9, 7, 5, 3, 1 }, // Ladder
																														// Values
				{ 1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 1, 3, 5, 7, 9, 11, 13, 15, 17, 19 } // Snake
																							// Values
		};
		this.totalSquares = this.boardDimension * this.boardDimension;

		this.initializePlayers(playerList);
		this.initializeBoard();

	}

	public void addTeam(User u) {
		this.userList.add(u);
		this.userMap.put(u, 0);
		this.prevMap.put(u, 0);
	}

	public int dimension() {
		return this.boardDimension;
	}

	public Square[] gameBoard() {
		return this.gameBoard;
	}

	public int getOldPosition(int userIndex) {
		return this.prevMap.get(this.userList.get(userIndex));
	}

	public int getPosition(int userIndex) {
		return this.userMap.get(this.userList.get(userIndex));
	}

	public String getUsername(int userIndex) {
		return this.userList.get(userIndex).getUsername();
	}

	public List<User> getUsers() {
		return this.userList;
	}

	private void initializeBoard() {
		this.gameBoard = new Square[this.totalSquares];

		for (int i = 0; i < this.totalSquares; i++) {
			this.gameBoard[i] = new Square(0); // set all squares as default
												// squares (no step value)
		}

		// Setting Random Ladders
		for (int i = 0; i < this.numLadders; i++) {

			int bottom = ThreadLocalRandom.current().nextInt(1, this.totalSquares - this.boardDimension);
			int top = valuesSnakesLadders[0][bottom % this.boardDimension] + bottom;

			// checking to make sure top & bottom is not a ladder
			while ((this.gameBoard[top].getIndex() != 0) || (this.gameBoard[bottom].getIndex() != 0)) {
				bottom = ThreadLocalRandom.current().nextInt(1, this.totalSquares - this.boardDimension);
				top = valuesSnakesLadders[0][bottom % this.boardDimension] + bottom;
			}

			this.gameBoard[bottom].setIndex(top - bottom);
			System.out.println(
					"New ladder from " + bottom + " to " + top + " with value of " + this.gameBoard[bottom].getIndex());

		}

		// Setting Random Snakes
		for (int i = 0; i < this.numLadders; i++) {

			int top = ThreadLocalRandom.current().nextInt(this.boardDimension, this.totalSquares - 2);
			int bottom = top - valuesSnakesLadders[1][top % this.boardDimension];

			// checking to make sure top & bottom is not a ladder
			while ((this.gameBoard[top].getIndex() != 0) || (this.gameBoard[bottom].getIndex() != 0)) {
				top = ThreadLocalRandom.current().nextInt(this.boardDimension, this.totalSquares - 1);
				bottom = top - valuesSnakesLadders[1][top % this.boardDimension];
			}

			this.gameBoard[top].setIndex(bottom - top);
			System.out.println(
					"New snake from " + top + " to " + bottom + " with value of " + this.gameBoard[top].getIndex());
		}

	}

	// Start a local game
	private void initializePlayers() {
		System.out.println("C1");
		this.userList = new ArrayList<>();
		User guest = new User("Guest");
		this.addTeam(guest);
		System.out.println("Added " + guest.getUsername());

		for (int i = 0; i < 3; i++) {
			User u = new User(i);
			this.addTeam(u);
			System.out.println("Added " + u.getUsername());
		}

		// Setting whoseTurn
		this.whoseTurn = 0;
	}

	// Start a networked game
	private void initializePlayers(List<User> playerList) {
		System.out.println("C2");
		this.userList = new ArrayList<>();

		for (int i = 0; i < playerList.size(); i++) {
			User u = playerList.get(i);
			this.addTeam(u);
			System.out.println("Added " + u.getUsername());
		}

		for (int i = 0; i < (4 - playerList.size()); i++) {
			User u = new User(i + 1);
			this.addTeam(u);
			System.out.println("Added " + u.getUsername());
		}

		// Setting whoseTurn
		this.whoseTurn = 0;

	}

	public boolean isNetworkedGame() {
		return this.isNetworkedGame;
	}

	public int numLadders() {
		return this.numLadders;
	}

	public void playTurn(int rollNum) {

		// Dice
		// int rollNum = ThreadLocalRandom.current().nextInt(1, 7);

		// Update their position
		this.update = "<html>" + this.userList.get(this.whoseTurn).getUsername() + " rolled a " + rollNum + ".<br>";
		int oldPosition = this.userMap.get(this.userList.get(this.whoseTurn));
		this.prevMap.put(this.userList.get(this.whoseTurn), oldPosition);
		int newPosition = this.userMap.get(this.userList.get(this.whoseTurn)) + rollNum;

		// Check winner, if winner then set new position to winning positio
		if (newPosition > (this.totalSquares - 1)) {
			newPosition = this.totalSquares - 1;
		}

		int stepVal = 0;
		// Check snake/ladder
		if (this.gameBoard[newPosition].getIndex() != 0) {
			stepVal = this.gameBoard[newPosition].getIndex(); // stepVall will
																// be positive
																// or negative
																// to indicate
																// ladder/snake
			String temp = "";
			if (this.gameBoard[newPosition].getIndex() > 0) {
				temp = "ladder";
			} else {
				temp = "chute";
			}
			this.update += "Hit a " + temp + " on " + newPosition + "!<br>You move "
					+ this.gameBoard[newPosition].getIndex() + " spaces.<br>";
			newPosition += this.gameBoard[newPosition].getIndex();
		}

		// Update stats for ladder/slides, and spaces forward on this turn
		// (newPosition - oldPosition)
		if (this.isNetworkedGame) {
			this.updateUser(this.userList.get(this.whoseTurn), stepVal,
					newPosition - this.userMap.get(this.userList.get(this.whoseTurn)));
		}

		// Update user position in the user map
		this.userMap.put(this.userList.get(this.whoseTurn), newPosition);
		this.update += this.userList.get(this.whoseTurn).getUsername() + " is now on space "
				+ this.userMap.get(this.userList.get(this.whoseTurn)) + ".<br></html>";
		System.out.println(this.update);

		// Check winner
		if (this.userMap.get(this.userList.get(this.whoseTurn)) == (this.totalSquares - 1)) {
			System.out.println("Game Over! <br>" + this.userList.get(this.whoseTurn).getUsername() + " wins!");
			this.gameOver = true;
		}

		// If we've reached end of map, reset counter to start
		this.whoseTurn++;
		if (this.whoseTurn == 4) {
			this.whoseTurn = 0;
		}

		if (this.gameOver) {
			System.exit(0);
		}

	}

	public void removeTeam(User u) {
		this.userMap.remove(u);
	}

	public String update() {
		return this.update;
	}

	private void updateUser(User u, int stepVal, int spacesForward) {

		if (stepVal > 0) {
			u.addLaddersClimbed(1);
		} else {
			u.addSlidesDescended(1);
		}

		u.addSpaceForward(spacesForward);
	}

	public int whoseTurn() {
		return this.whoseTurn;
	}
}
