package game_components;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Square extends JPanel {

	private int squareIndex;
	private JLabel imgPlayer;

	public Square(int index) {
		this.squareIndex = index;
		this.imgPlayer = new JLabel("");
	}

	public void drawLadder() {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("images/ladders/bottom_1.gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Image dimg = img.getScaledInstance(90, 90, Image.SCALE_SMOOTH);
		ImageIcon imageIcon = new ImageIcon(dimg);
		JLabel imgLadder = new JLabel(imageIcon);
		imgLadder.setHorizontalAlignment(SwingConstants.LEFT);
		imgLadder.setVerticalAlignment(SwingConstants.BOTTOM);
		this.add(imgLadder, BorderLayout.WEST);
	}

	public void drawLadderTop() {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("images/ladders/top_1.gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Image dimg = img.getScaledInstance(90, 90, Image.SCALE_SMOOTH);
		ImageIcon imageIcon = new ImageIcon(dimg);
		JLabel imgLadderTop = new JLabel(imageIcon);
		imgLadderTop.setHorizontalAlignment(SwingConstants.LEFT);
		imgLadderTop.setVerticalAlignment(SwingConstants.BOTTOM);
		this.add(imgLadderTop, BorderLayout.WEST);
	}

	public void drawPlayer(int i) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("images/players/player" + (i + 1) + ".gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Image dimg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		ImageIcon imageIcon = new ImageIcon(dimg);
		this.imgPlayer.setIcon(imageIcon);
		this.add(this.imgPlayer, BorderLayout.NORTH);

	}

	public void drawSnakeHead() {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("images/snakes/1.gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Image dimg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		ImageIcon imageIcon = new ImageIcon(dimg);
		JLabel imgSnake = new JLabel(imageIcon);
		imgSnake.setHorizontalAlignment(SwingConstants.LEFT);
		imgSnake.setVerticalAlignment(SwingConstants.TOP);
		this.add(imgSnake, BorderLayout.WEST);
	}

	public void drawSnakeTail() {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("images/snakes/back_1.gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Image dimg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		ImageIcon imageIcon = new ImageIcon(dimg);
		JLabel imgSnake = new JLabel(imageIcon);
		imgSnake.setHorizontalAlignment(SwingConstants.LEFT);
		imgSnake.setVerticalAlignment(SwingConstants.BOTTOM);
		this.add(imgSnake, BorderLayout.WEST);
	}

	public int getIndex() {
		return this.squareIndex;
	}

	public void removePlayer() {
		this.imgPlayer.setIcon(null);
	}

	public void setIndex(int i) {
		this.squareIndex = i;
	}

}
