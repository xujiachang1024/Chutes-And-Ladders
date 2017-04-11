package frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

import game_components.Gameboard;
import game_logic.GameDataLogic;
import utilities.AppearanceConstants;
import utilities.AppearanceSettings;
import utilities.ExitWindowListener;
import utilities.User;

public class GameScreenGUI extends JFrame {

	private class DiePanel extends JPanel {

		private BufferedImage[] dice = new BufferedImage[6];
		private JLabel die;

		public DiePanel() {
			try {
				BufferedImage img = ImageIO.read(new File("images/dice/Die.png"));
				int width = 377 / 6;
				for (int index = 0; index < 6; index++) {
					this.dice[index] = img.getSubimage(width * index, 0, width, width);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			this.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			this.die = new JLabel(new ImageIcon(this.dice[0]));
			this.add(this.die, gbc);

			this.add(GameScreenGUI.this.diceButton, gbc);

			GameScreenGUI.this.diceButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					GameScreenGUI.this.diceButton.setEnabled(false);
					Timer timer = new Timer(20, new ActionListener() {
						private int counter;
						private int lastRoll;

						@Override
						public void actionPerformed(ActionEvent e) {
							if (this.counter < 20) {
								this.counter++;
								this.lastRoll = (int) (Math.random() * 6);
								System.out.println(this.counter + "/" + this.lastRoll);
								DiePanel.this.die.setIcon(new ImageIcon(DiePanel.this.dice[this.lastRoll]));
							} else {
								GameScreenGUI.this.gamePlay.playTurn(this.lastRoll + 1);
								GameScreenGUI.this.updateLabels();
								// update player images
								int turn = GameScreenGUI.this.gamePlay.whoseTurn() - 1;
								if (turn == -1) {
									turn = 3;
								}
								((Gameboard) GameScreenGUI.this.boardPanel).updatePlayerPosition(turn,
										GameScreenGUI.this.gamePlay.getOldPosition(turn),
										GameScreenGUI.this.gamePlay.getPosition(turn));
								((Timer) e.getSource()).stop();
								GameScreenGUI.this.diceButton.setEnabled(true);
							}
						}
					});
					timer.start();
				}
			});
		}
	}

	public static final long serialVersionUID = 1L;

	private static final int MAX_NUMBER_OF_TEAMS = 4;

	public static void main(String[] args) {
		List<User> playerList = new ArrayList<>();
		playerList.add(new User("Miles"));
		GameDataLogic networkedGame = new GameDataLogic(playerList);
		new StartScreenGUI(networkedGame).setVisible(true);
	}

	private JPanel mainPanel;
	private JPanel boardPanel;
	private JPanel sidePanel;
	private List<JTextArea> positionLabels;

	private JLabel whoseTurnLabel;

	private JLabel updateLabel;

	private JButton diceButton;

	private GameDataLogic gamePlay;

	public GameScreenGUI(GameDataLogic gamePlay) {

		super("Game Screen Menu");
		this.gamePlay = gamePlay;
		this.initializeComponents();
		this.createGUI();
		this.addListeners();
	}

	private void addListeners() {

		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		this.addWindowListener(new ExitWindowListener(this));
		/*
		 * this.diceButton.addActionListener(new ActionListener() {
		 *
		 * @Override public void actionPerformed(ActionEvent e) {
		 * GameScreenGUI.this.gamePlay.playTurn();
		 * GameScreenGUI.this.updateLabels(); // update player images int turn =
		 * GameScreenGUI.this.gamePlay.whoseTurn() - 1; if (turn == -1) { turn =
		 * 3; } ((Gameboard)
		 * GameScreenGUI.this.boardPanel).updatePlayerPosition(
		 * GameScreenGUI.this.gamePlay.getOldPosition(turn),
		 * GameScreenGUI.this.gamePlay.getPosition(turn));
		 * System.out.println(GameScreenGUI.this.gamePlay.getOldPosition(turn));
		 * System.out.println(GameScreenGUI.this.gamePlay.getPosition(turn));
		 *
		 * } });
		 */

	}

	private JPanel createChutesPanel() {
		JPanel chutesPanel = new JPanel();
		JLabel chutesLabel = new JLabel("O Chute!");

		AppearanceSettings.setBackground(AppearanceConstants.lightGrey, chutesPanel, chutesLabel);

		chutesLabel.setFont(AppearanceConstants.fontLarge);

		chutesPanel.add(chutesLabel);

		return chutesPanel;
	}

	private void createGUI() {
		this.createMainPanel();
		this.sidePanel = this.createSidePanel();

		this.add(this.mainPanel, BorderLayout.CENTER);
		this.add(this.sidePanel, BorderLayout.WEST);

		this.setSize(1200, 800);

	}

	private void createMainPanel() {
		this.mainPanel.setLayout(new BorderLayout());

		JPanel northPanel = new JPanel();
		JPanel chutesPanel = this.createChutesPanel();
		JPanel playerPanel = this.createPlayerPanel();

		northPanel.setLayout(new BorderLayout());
		northPanel.add(chutesPanel, BorderLayout.NORTH);
		northPanel.add(playerPanel, BorderLayout.SOUTH);

		this.boardPanel = new Gameboard(this.gamePlay);

		this.mainPanel.add(northPanel, BorderLayout.NORTH);
		this.mainPanel.add(this.boardPanel, BorderLayout.CENTER);
	}

	private JPanel createPlayerPanel() {
		JPanel playerPanel = new JPanel();
		AppearanceSettings.setBackground(AppearanceConstants.lightGrey, this.positionLabels.get(0),
				this.positionLabels.get(1), this.positionLabels.get(2), this.positionLabels.get(3));

		AppearanceSettings.setFont(AppearanceConstants.fontMedium, this.positionLabels.get(0),
				this.positionLabels.get(1), this.positionLabels.get(2), this.positionLabels.get(3));
		// AppearanceSettings.setForeground(AppearanceConstants.secondaryText,
		// this.positionLabels.get(0), this.positionLabels.get(1),
		// this.positionLabels.get(2), this.positionLabels.get(3));
		AppearanceSettings.setForeground(AppearanceConstants.blueText, this.positionLabels.get(0));
		AppearanceSettings.setForeground(AppearanceConstants.lightRedText, this.positionLabels.get(1));
		AppearanceSettings.setForeground(AppearanceConstants.purpleText, this.positionLabels.get(2));
		AppearanceSettings.setForeground(AppearanceConstants.lightGreenText, this.positionLabels.get(3));

		AppearanceSettings.setSize(1200, 100, playerPanel);
		AppearanceSettings.setSize(300, 200, this.positionLabels.get(0), this.positionLabels.get(1),
				this.positionLabels.get(2), this.positionLabels.get(3));

		AppearanceSettings.setBoxLayout(BoxLayout.LINE_AXIS, playerPanel);

		for (int i = 0; i < MAX_NUMBER_OF_TEAMS; i++) {
			playerPanel.add(this.positionLabels.get(i));
		}

		return playerPanel;
	}

	private JPanel createSidePanel() {
		JPanel sidePanel = new JPanel();
		sidePanel.setLayout(new BorderLayout());

		AppearanceSettings.setOpaque(this.whoseTurnLabel, this.updateLabel);
		AppearanceSettings.setForeground(AppearanceConstants.secondaryText, this.whoseTurnLabel, this.updateLabel);
		AppearanceSettings.setTextAlignment(this.whoseTurnLabel, this.updateLabel);

		this.whoseTurnLabel.setFont(AppearanceConstants.fontMedium);
		this.updateLabel.setFont(AppearanceConstants.fontMedium);

		sidePanel.add(this.whoseTurnLabel, BorderLayout.NORTH);

		JPanel holz = new JPanel();
		holz.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 0.15;
		c.gridy = 0;

		holz.setMaximumSize(new Dimension(280, 180));
		JScrollPane holderPanel = new JScrollPane(holz);
		holderPanel.setPreferredSize(new Dimension(300, 200));
		holz.add(this.updateLabel);

		sidePanel.add(holderPanel, BorderLayout.CENTER);

		this.diceButton = new JButton("Roll Die");
		this.diceButton.setEnabled(true);
		JPanel diePanel = new DiePanel();

		sidePanel.add(diePanel, BorderLayout.SOUTH);

		AppearanceSettings.setBackground(AppearanceConstants.brown, this.whoseTurnLabel, this.updateLabel, diePanel,
				sidePanel);
		AppearanceSettings.setBackground(AppearanceConstants.darkGrey, this.updateLabel);
		AppearanceSettings.setBackground(AppearanceConstants.darkGrey, holderPanel, holz);
		AppearanceSettings.setForeground(AppearanceConstants.veryLightGrey, this.updateLabel);
		AppearanceSettings.reDo(holderPanel, holz, sidePanel);

		return sidePanel;
	}

	private void initializeComponents() {
		this.mainPanel = new JPanel();

		this.positionLabels = new ArrayList<>(4);
		for (int i = 0; i < MAX_NUMBER_OF_TEAMS; i++) {

			String userName = this.gamePlay.getUsername(i);
			this.positionLabels.add(new JTextArea(userName + "\nPosition: 0"));
			this.positionLabels.get(i).setEditable(false);
		}

		String currentUser = this.gamePlay.getUsername(0);
		this.whoseTurnLabel = new JLabel("<html>It is <br>" + currentUser + "'s turn</html>");
		this.updateLabel = new JLabel("<html>Welcome to Oh Chute!<br>Click the dice to begin play.</html>");

	}

	private void updateLabels() {
		for (int i = 0; i < MAX_NUMBER_OF_TEAMS; i++) {
			this.positionLabels.get(i).setEditable(true);
			String userName = this.gamePlay.getUsername(i);
			int pos = this.gamePlay.getPosition(i);
			this.positionLabels.get(i).setText(userName + "\nPosition: " + pos);
			this.positionLabels.get(i).setEditable(false);
		}

		String currentUser = this.gamePlay.getUsername(this.gamePlay.whoseTurn());
		this.whoseTurnLabel.setText("<html>It is <br>" + currentUser + "'s turn</html>");

		this.updateLabel.setText("<html><br>" + this.gamePlay.update() + "</html>");
		AppearanceSettings.reDo(this.sidePanel);
		this.validate();
	}

}
