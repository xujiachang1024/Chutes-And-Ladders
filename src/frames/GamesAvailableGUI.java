package frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game_logic.GameDataLogic;
import game_logic.GameLobbyLogic;
import utilities.AppearanceConstants;
import utilities.AppearanceSettings;
import utilities.User;

public class GamesAvailableGUI extends JPanel {
	private static final long serialVersionUID = 1L;
	private JPanel main;
	private GameLobbyLogic logic;
	private GameLobbyGUI guiParent;
	private ArrayList<JButton> joinGameButtons;

	public GamesAvailableGUI(GameLobbyLogic logic, GameLobbyGUI guiParent) {
		this.guiParent = guiParent;
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(500, 500));
		this.logic = logic;
		this.initializeVariables();
		this.addListeners();
	}

	private void addListeners() {
		this.joinGameButtons.get(0).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				GamesAvailableGUI.this.guiParent.setVisible(false);
				List<User> playerList = new ArrayList<>();
				playerList.add(GamesAvailableGUI.this.logic.getUser());
				GameDataLogic networkedGame = new GameDataLogic(playerList);
				new StartScreenGUI(networkedGame).setVisible(true);
			}
		});

		this.joinGameButtons.get(1).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				GamesAvailableGUI.this.guiParent.setVisible(false);
				List<User> playerList = new ArrayList<>();
				playerList.add(GamesAvailableGUI.this.logic.getUser());
				GameDataLogic networkedGame = new GameDataLogic(playerList);
				new StartScreenGUI(networkedGame).setVisible(true);
			}
		});
	}

	private void initializeVariables() {
		this.joinGameButtons = new ArrayList<>();
		this.main = new JPanel(new BorderLayout());
		this.main.setPreferredSize(new Dimension(500, 500));
		JPanel insideMain = new JPanel();
		insideMain.setSize(new Dimension(300, 300));

		JPanel gameOne = new JPanel(new BorderLayout());
		JLabel gameOneName = new JLabel("<html><center><b>Networked Game</b>: <i>4 Users</i></center></html>");
		JButton gameOneButton = new JButton("Join Game");
		ImageIcon water = new ImageIcon("images/arrow.png");
		gameOneButton.setIcon(water);
		this.joinGameButtons.add(gameOneButton);
		gameOne.add(gameOneName, BorderLayout.CENTER);
		gameOne.add(gameOneButton, BorderLayout.EAST);

		JPanel gameTwo = new JPanel(new BorderLayout());
		JLabel gameTwoName = new JLabel("<html><center><b>Networked Game</b>: <i>4 Users</i></center></html>");
		JButton gameTwoButton = new JButton("Join Game");
		ImageIcon water20 = new ImageIcon("images/arrow.png");
		gameTwoButton.setIcon(water20);
		this.joinGameButtons.add(gameTwoButton);
		gameTwo.add(gameTwoName, BorderLayout.CENTER);
		gameTwo.add(gameTwoButton, BorderLayout.EAST);

		gameOne.setBorder(BorderFactory.createMatteBorder(2, 2, 1, 2, AppearanceConstants.black));
		gameTwo.setBorder(BorderFactory.createMatteBorder(1, 2, 2, 2, AppearanceConstants.black));

		insideMain.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		// we want the layout to stretch the components in both directions
		c.fill = GridBagConstraints.BOTH;
		// if the total X weight is 0, then it won't stretch horizontally.
		// It doesn't matter what the weight actually is, as long as it's not 0,
		// because the grid is only one component wide
		c.weightx = 1;

		// Vertical space is divided in proportion to the Y weights of the
		// components
		c.weighty = 0.20;
		c.gridy = 0;
		insideMain.add(gameOne, c);
		c.weighty = 0.20;
		c.gridy = 1;
		insideMain.add(gameTwo, c);

		JPanel twotemp = new JPanel();
		AppearanceSettings.setBackground(AppearanceConstants.black, twotemp);
		// It's fine to reuse the constraints object; add makes a copy.
		c.weighty = 0.60;
		c.gridy = 2;
		insideMain.add(twotemp, c);
		this.main.add(insideMain);
		this.add(this.main);

		AppearanceSettings.setFont(AppearanceConstants.fontSmall, gameOneName, gameTwoName, gameOneButton,
				gameTwoButton);
		AppearanceSettings.setBackground(AppearanceConstants.lightGrey, gameOne, gameOneName, gameOneButton, gameTwo,
				gameTwoName, gameTwoButton, insideMain);
		AppearanceSettings.setBackground(AppearanceConstants.black, this.main);
		AppearanceSettings.setForeground(AppearanceConstants.secondaryText, gameOneName, gameOneButton, gameTwoName,
				gameTwoButton);

		AppearanceSettings.reDo(this.main, gameOne, gameTwo);
	}
}