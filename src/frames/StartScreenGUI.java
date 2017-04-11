package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import game_logic.GameDataLogic;
import utilities.AppearanceConstants;
import utilities.AppearanceSettings;
import utilities.ExitWindowListener;
import utilities.User;

public class StartScreenGUI extends JFrame {

	public static final long serialVersionUID = 1L;
	private static final int MAX_NUMBER_OF_TEAMS = 4;

	private static String format(int i) {
		String result = String.valueOf(i);
		if (result.length() == 1) {
			result = "0" + result;
		}
		return result;
	}

	private GameDataLogic gamePlay;
	private JPanel mainPanel;
	private JLabel timerLabel;
	private List<JLabel> numberLabels;
	private List<JTextField> playerLabels;
	private JButton startGameButton, exitGameButton;

	private JOptionPane confirmExitOption;

	private List<User> userList;

	public StartScreenGUI(GameDataLogic gamePlay) {
		super("Start Screen Menu");
		this.gamePlay = gamePlay;
		// this is where you play around with the user list

		this.initializeComponents();
		this.createGUI();
		this.addListeners();
	}

	private void addListeners() {

		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		this.addWindowListener(new ExitWindowListener(this));

		Timer t = new Timer(1000, new ActionListener() {
			int time = 10;

			@Override
			public void actionPerformed(ActionEvent e) {
				this.time--;
				StartScreenGUI.this.timerLabel.setText(format(this.time / 60) + ":" + format(this.time % 60));
				if (this.time == 0) {
					Timer timer = (Timer) e.getSource();
					timer.stop();
					StartScreenGUI.this.startGameButton.setEnabled(true);
				}
			}
		});
		t.start();

		this.startGameButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				new GameScreenGUI(StartScreenGUI.this.gamePlay).setVisible(true);
				StartScreenGUI.this.dispose();
			}

		});

		this.exitGameButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// tentative
				StartScreenGUI.this.dispose();
				System.exit(0);
			}

		});

	}

	private void createGUI() {
		AppearanceSettings.setBackground(Color.darkGray, this.startGameButton, this.exitGameButton,
				this.numberLabels.get(0), this.numberLabels.get(1), this.numberLabels.get(2), this.numberLabels.get(3));
		AppearanceSettings.setBackground(AppearanceConstants.lightGrey, this.timerLabel, this.playerLabels.get(0),
				this.playerLabels.get(1), this.playerLabels.get(2), this.playerLabels.get(3));
		AppearanceSettings.setBackground(AppearanceConstants.veryLightGrey, this.mainPanel);

		AppearanceSettings.setFont(AppearanceConstants.fontLarge, this.timerLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontMedium, this.startGameButton, this.exitGameButton);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, this.numberLabels.get(0), this.numberLabels.get(1),
				this.numberLabels.get(2), this.numberLabels.get(3), this.playerLabels.get(0), this.playerLabels.get(1),
				this.playerLabels.get(2), this.playerLabels.get(3));

		AppearanceSettings.setForeground(Color.white, this.numberLabels.get(0), this.numberLabels.get(1),
				this.numberLabels.get(2), this.numberLabels.get(3), this.startGameButton, this.exitGameButton);

		AppearanceSettings.setOpaque(this.startGameButton, this.exitGameButton, this.numberLabels.get(0),
				this.numberLabels.get(1), this.numberLabels.get(2), this.numberLabels.get(3));

		AppearanceSettings.setSize(250, 100, this.startGameButton, this.exitGameButton);
		AppearanceSettings.setSize(200, 80, this.numberLabels.get(0), this.numberLabels.get(1),
				this.numberLabels.get(2), this.numberLabels.get(3));
		AppearanceSettings.setSize(200, 60, this.playerLabels.get(0), this.playerLabels.get(1),
				this.playerLabels.get(2), this.playerLabels.get(3));
		AppearanceSettings.setSize(300, 150, this.timerLabel);

		AppearanceSettings.unSetBorderOnButtons(this.startGameButton, this.exitGameButton);

		AppearanceSettings.setTextAlignment(this.timerLabel, this.numberLabels.get(0), this.numberLabels.get(1),
				this.numberLabels.get(2), this.numberLabels.get(3));

		for (int i = 0; i < MAX_NUMBER_OF_TEAMS; i++) {
			this.playerLabels.get(i).setEditable(false);
		}
		this.startGameButton.setEnabled(false);
		this.exitGameButton.setEnabled(true);

		this.createMainPanel();

		this.add(this.mainPanel, BorderLayout.CENTER);
		this.setSize(800, 700);
	}

	private void createMainPanel() {
		JPanel northPanel = new JPanel();
		JPanel titlePanel = new JPanel();
		JPanel welcomePanel = new JPanel();

		JPanel playersPanel = new JPanel();
		JPanel playerNumbersPanel1 = new JPanel();
		JPanel playerNumbersPanel2 = new JPanel();
		JPanel playerNamesPanel1 = new JPanel();
		JPanel playerNamesPanel2 = new JPanel();

		JPanel buttonPanel = new JPanel();

		JLabel welcomeLabel = new JLabel("O Chute!");

		AppearanceSettings.setBackground(AppearanceConstants.lightGrey, welcomeLabel, welcomePanel, titlePanel);
		AppearanceSettings.setTextAlignment(welcomeLabel);

		AppearanceSettings.setBackground(AppearanceConstants.veryLightGrey, buttonPanel, playersPanel,
				playerNumbersPanel1, playerNumbersPanel2, playerNamesPanel1, playerNamesPanel2);

		AppearanceSettings.setSize(800, 50, welcomePanel);
		AppearanceSettings.setSize(800, 400, buttonPanel);

		welcomeLabel.setFont(AppearanceConstants.fontLarge);

		AppearanceSettings.setBoxLayout(BoxLayout.LINE_AXIS, playerNumbersPanel1, playerNumbersPanel2,
				playerNamesPanel1, playerNamesPanel2, buttonPanel);
		AppearanceSettings.setBoxLayout(BoxLayout.PAGE_AXIS, northPanel, playersPanel);

		AppearanceSettings.addGlue(playerNumbersPanel1, BoxLayout.LINE_AXIS, true, this.numberLabels.get(0),
				this.numberLabels.get(1));
		AppearanceSettings.addGlue(playerNumbersPanel2, BoxLayout.LINE_AXIS, true, this.numberLabels.get(2),
				this.numberLabels.get(3));
		AppearanceSettings.addGlue(playerNamesPanel1, BoxLayout.LINE_AXIS, true, this.playerLabels.get(0),
				this.playerLabels.get(1));
		AppearanceSettings.addGlue(playerNamesPanel2, BoxLayout.LINE_AXIS, true, this.playerLabels.get(2),
				this.playerLabels.get(3));
		AppearanceSettings.addGlue(playersPanel, BoxLayout.PAGE_AXIS, true, playerNumbersPanel1, playerNamesPanel1,
				playerNumbersPanel2, playerNamesPanel2);

		AppearanceSettings.addGlue(buttonPanel, BoxLayout.LINE_AXIS, true, this.startGameButton, this.exitGameButton);

		welcomePanel.add(welcomeLabel, BorderLayout.CENTER);

		titlePanel.add(welcomePanel, BorderLayout.NORTH);
		titlePanel.add(this.timerLabel, BorderLayout.SOUTH);

		northPanel.add(titlePanel);

		this.mainPanel.add(northPanel);
		this.mainPanel.add(playersPanel);
		this.mainPanel.add(buttonPanel);

	}

	private void initializeComponents() {
		this.mainPanel = new JPanel(new GridLayout(3, 1));

		this.timerLabel = new JLabel("Countdown: 00:10");

		this.numberLabels = new ArrayList<>(4);
		this.playerLabels = new ArrayList<>(4);

		if (this.gamePlay.isNetworkedGame()) { // networked game
			this.numberLabels.add(new JLabel(this.gamePlay.getUsers().get(0).getUsername()));
			this.playerLabels.add(new JTextField(this.gamePlay.getUsers().get(0).getUsername()));

			for (int i = 0; i < (MAX_NUMBER_OF_TEAMS - 1); i++) {
				this.numberLabels.add(new JLabel("Bot #" + (i + 1)));
				this.playerLabels.add(new JTextField("Bot #" + (i + 1)));
			}
		} else { // local game
			this.numberLabels.add(new JLabel("Guest"));
			this.playerLabels.add(new JTextField("Guest"));

			for (int i = 0; i < (MAX_NUMBER_OF_TEAMS - 1); i++) {
				this.numberLabels.add(new JLabel("Bot #" + (i + 1)));
				this.playerLabels.add(new JTextField("Bot #" + (i + 1)));
			}
		}

		this.startGameButton = new JButton("Start Game");
		this.exitGameButton = new JButton("Leave Game");
	}

	/*
	 * public static void main(String[] args) { GameDataLogic localGame = new
	 * GameDataLogic(); // local game since default constructor
	 * 
	 * List<User> uList = new ArrayList<User>(); uList.add(new User("Derek"));
	 * GameDataLogic networkGame = new GameDataLogic(uList);
	 * 
	 * new StartScreenGUI(networkGame).setVisible(true); }
	 */

}
