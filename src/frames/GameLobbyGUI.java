package frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import game_logic.GameDataLogic;
import game_logic.GameLobbyLogic;
import utilities.AppearanceConstants;
import utilities.AppearanceSettings;
import utilities.User;

public class GameLobbyGUI extends JFrame {

	public static final long serialVersionUID = 1;
	private JTabbedPane lobbyPane;
	private JComponent userProfile;
	private JComponent gamesAvailable;
	private JComponent userSearch;
	private GameLobbyLogic logic;
	private User loggedInUser;

	public GameLobbyGUI() {
		this(new User("Miller"));
	}

	public GameLobbyGUI(int x) {
		this.setVisible(false);
		GameDataLogic networkedGame = new GameDataLogic();
		new StartScreenGUI(networkedGame).setVisible(true);
	}

	public GameLobbyGUI(User userIn) {
		super("Game Lobby");
		this.logic = new GameLobbyLogic(userIn);
		this.initializeVariables();
		this.loggedInUser = userIn;
		this.logic = new GameLobbyLogic(this.loggedInUser);
		this.setLayout(new BorderLayout());
		this.add(this.lobbyPane, BorderLayout.CENTER);
		this.setSize(600, 600);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((dim.width / 2) - (this.getSize().width / 2), (dim.height / 2) - (this.getSize().height / 2));
		this.toFront();
		this.setVisible(true);
		AppearanceSettings.setBackground(AppearanceConstants.black, this.userProfile, this.gamesAvailable,
				this.userSearch);
		AppearanceSettings.setBackground(AppearanceConstants.black, this.lobbyPane);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, this.lobbyPane);
		this.setBackground(AppearanceConstants.mediumGrey);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.toFront();
	}

	private void initializeVariables() {
		this.lobbyPane = new JTabbedPane();
		this.userProfile = new UserProfileGUI(this.logic, this);
		this.gamesAvailable = new GamesAvailableGUI(this.logic, this);
		this.userSearch = new UserSearchGUI(this.logic);
		this.lobbyPane.addTab("User Profile", this.userProfile);
		this.lobbyPane.addTab("Games Available", this.gamesAvailable);
		this.lobbyPane.addTab("User Search", this.userSearch);
		this.lobbyPane.setSize(600, 550);
		AppearanceSettings.reDo(this.lobbyPane);
	}

}
