package game_components;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import game_logic.GameDataLogic;
import utilities.AppearanceConstants;

public class Gameboard extends JPanel {

	private Square[] grid;
	private Snake[] snakes;
	private Ladder[] ladders;
	private GameDataLogic gamePlay;

	private int dimension;

	public Gameboard(GameDataLogic g) {
		this.gamePlay = g;
		this.dimension = this.gamePlay.dimension();
		this.setLayout(new GridLayout(this.dimension, this.dimension, 5, 5));
		int total = this.dimension * this.dimension;
		this.grid = new Square[total + 1];

		// Filling arrays
		this.setSquares();
		this.setSnakes();
		this.setLadders();

		// Drawing snakes
		for (int i = 0; i < total; i++) {
			if (this.isSnakePresent(i)) {
				this.grid[i + 1].drawSnakeHead();
			}
			// if(isLadderPresent(i))
			// grid[i+1].drawLadder();
			if (this.isLadderTopPresent(i)) {
				this.grid[i + 1].drawLadderTop();
				/* if(isSnakeTailPresent(i)); */
			}
		}

	}

	public int getLadderTop(int index) {
		for (int i = 0; i < 5; i++) {
			if (this.ladders[i].getBottom().getIndex() == index) {
				return this.ladders[i].getTop().getIndex();
			}
		}

		return -1;
	}

	public int getSnakeTail(int index) {
		for (int i = 0; i < 5; i++) {
			if (this.snakes[i].getHead().getIndex() == index) {
				return this.snakes[i].getTail().getIndex();
			}
		}

		return -1;
	}

	public boolean isLadderPresent(int index) {
		for (int i = 0; i < this.gamePlay.numLadders(); i++) {
			if (this.ladders[i].getBottom().getIndex() == index) {
				return true;
			}
		}

		return false;
	}

	public boolean isLadderTopPresent(int index) {
		for (int i = 0; i < this.gamePlay.numLadders(); i++) {
			if (this.ladders[i].getTop().getIndex() == index) {
				return true;
			}
		}

		return false;
	}

	public boolean isSnakePresent(int index) {
		for (int i = 0; i < this.gamePlay.numLadders(); i++) {
			if (this.snakes[i].getHead().getIndex() == index) {
				return true;
			}
		}

		return false;
	}

	public boolean isSnakeTailPresent(int index) {
		for (int i = 0; i < this.gamePlay.numLadders(); i++) {
			if (this.snakes[i].getTail().getIndex() == index) {
				return true;
			}
		}

		return false;
	}

	private void setLadders() {
		this.ladders = new Ladder[this.gamePlay.numLadders()];

		int index = 0;
		for (int i = 0; i < (this.dimension * this.dimension); i++) {
			if (this.gamePlay.gameBoard()[i].getIndex() > 0) {
				this.ladders[index] = new Ladder(this.grid[i], this.grid[i + this.gamePlay.gameBoard()[i].getIndex()]);
				index++;
			}

		}

		/*
		 * ladders[0] = new Ladder(grid[20], grid[9]); ladders[1] = new
		 * Ladder(grid[43], grid[27]); ladders[2] = new Ladder(grid[61],
		 * grid[46]); ladders[3] = new Ladder(grid[82], grid[70]); ladders[4] =
		 * new Ladder(grid[80], grid[67]);
		 */

	}

	private void setSnakes() {
		this.snakes = new Snake[this.gamePlay.numLadders()];
		int index = 0;

		for (int i = 0; i < (this.dimension * this.dimension); i++) {
			if (this.gamePlay.gameBoard()[i].getIndex() < 0) {
				this.snakes[index] = new Snake(this.grid[i], this.grid[i + this.gamePlay.gameBoard()[i].getIndex()]);
				index++;
			}

		}

		/*
		 * snakes[0] = new Snake(grid[25], grid[8]); snakes[1] = new
		 * Snake(grid[67], grid[23]); snakes[2] = new Snake(grid[84], grid[48]);
		 * snakes[3] = new Snake(grid[98], grid[71]); snakes[4] = new
		 * Snake(grid[96], grid[69]);
		 */

	}

	private void setSquares() {

		/*
		 * for(int i = 0; i < (dimension*dimension); i++) { grid[i] = new
		 * Square(i); add(grid[i]); }
		 */

		int total = this.dimension * this.dimension;
		int number, direction;
		if ((this.dimension % 2) == 0) {
			number = total;
			direction = -1;
		} else {
			number = (total + 1) - this.dimension;
			direction = 1;
		}

		for (int y = 0; y < this.dimension; y++) {
			for (int x = 0; x < this.dimension; x++) {
				this.grid[number] = new Square(number);
				this.grid[number].setLayout(new BorderLayout());

				if (((x + y) % 2) == 0) {
					this.grid[number].setBackground(AppearanceConstants.lightGrey);
				} else {
					this.grid[number].setBackground(AppearanceConstants.veryLightGrey);
				}

				this.grid[number].add(new JLabel("" + (number - 1)), BorderLayout.EAST);
				this.add(this.grid[number]);
				number = number + direction;
			}

			if ((this.dimension % 2) == 0) {
				if (direction == -1) {
					number = (number + 1) - this.dimension;
				} else {
					number = number - 1 - this.dimension;
				}
			} else {
				if (direction == 1) {
					number = number - 1 - this.dimension;
				} else {
					number = (number + 1) - this.dimension;
				}
			}

			direction *= -1;

		}

	}

	public void updatePlayerPosition(int player, int prev, int index) {
		this.grid[prev + 1].removePlayer();
		this.grid[index + 1].drawPlayer(player);
	}

}
