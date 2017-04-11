package frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.GameClientListener;
import utilities.AppearanceConstants;
import utilities.AppearanceSettings;
import utilities.MySQLDriver;
import utilities.TextFieldFocusListener;
import utilities.User;

public class LoginScreenGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private MySQLDriver mySQLDriver;
	private Socket mClientSocket;
	private GameClientListener mClientListener;
	private User loggedUser;

	private JLabel masterAlertLabel;
	private JLabel clientAlertLabel;
	private JTextField clientHostnameTextField;
	private JTextField clientPortTextField;
	private JButton clientConnectButton;
	private JButton clientLocalButton;
	private JLabel accountAlertLabel;
	private JTextField accountUsernameTextField;
	private JTextField accountPasswordTextField;
	private JButton accountLoginButton;
	private JButton accountCreateButton;

	public LoginScreenGUI() {
		super("Chutes & Ladders: Client Login");
		this.initializeComponents();
		this.createGUI();
		this.addListeners();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((dim.width / 2) - (this.getSize().width / 2), (dim.height / 2) - (this.getSize().height / 2));
		this.toFront();
	}

	private void addListeners() {

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.accountUsernameTextField
				.addFocusListener(new TextFieldFocusListener("Username", this.accountUsernameTextField));
		this.accountPasswordTextField
				.addFocusListener(new TextFieldFocusListener("Password", this.accountPasswordTextField));

		this.clientConnectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int port = -1;

				try {
					port = Integer.parseInt(LoginScreenGUI.this.clientPortTextField.getText());
					if ((port <= 0) || (port >= 65535)) {
						AppearanceSettings.setForeground(AppearanceConstants.redText,
								LoginScreenGUI.this.clientAlertLabel);
						LoginScreenGUI.this.clientAlertLabel
								.setText("Client Alert: Invalid Port! Please enter a valid port.");
					} else {
						String hostname = LoginScreenGUI.this.clientHostnameTextField.getText();
						try {
							LoginScreenGUI.this.mClientSocket = new Socket(hostname, port);
							LoginScreenGUI.this.clientAlertLabel.setText("Client Alert: Socket Successfully Created");
							LoginScreenGUI.this.mClientListener = new GameClientListener(LoginScreenGUI.this,
									LoginScreenGUI.this.mClientSocket);
							LoginScreenGUI.this.clientAlertLabel
									.setText("Client Alert: Socket & ClientListener Successfully Created");
							// while (mySQLDriver == null) {
							// if (mySQLDriver != null) break;
							// }
							System.out.println("Success: MySQLDriver received by Client "
									+ LoginScreenGUI.this.mClientSocket.getInetAddress() + ":"
									+ LoginScreenGUI.this.mClientSocket.getPort());
							AppearanceSettings.setForeground(AppearanceConstants.greenText,
									LoginScreenGUI.this.clientAlertLabel);
							LoginScreenGUI.this.clientAlertLabel.setText(
									"Success: Connected to Server at " + hostname + ":" + Integer.toString(port));
							LoginScreenGUI.this.clientHostnameTextField.setEnabled(false);
							LoginScreenGUI.this.clientPortTextField.setEnabled(false);
							LoginScreenGUI.this.clientConnectButton.setEnabled(false);
							LoginScreenGUI.this.clientLocalButton.setEnabled(false);
							LoginScreenGUI.this.accountUsernameTextField.setEnabled(true);
							LoginScreenGUI.this.accountPasswordTextField.setEnabled(true);
							LoginScreenGUI.this.accountLoginButton.setEnabled(true);
							LoginScreenGUI.this.accountCreateButton.setEnabled(true);

						} catch (IOException ioe) {
							AppearanceSettings.setForeground(AppearanceConstants.redText,
									LoginScreenGUI.this.clientAlertLabel);
							LoginScreenGUI.this.clientAlertLabel.setText("Client Alert: Fail to connect to Server at "
									+ hostname + ":" + Integer.toString(port));
							ioe.printStackTrace();
						}
					}
				} catch (Exception pe) {
					AppearanceSettings.setForeground(AppearanceConstants.redText, LoginScreenGUI.this.clientAlertLabel);
					LoginScreenGUI.this.clientAlertLabel
							.setText("Client Alert: Invalid Port! Please enter an integer.");
					pe.printStackTrace();
					return;
				}
			}

		});

		this.clientLocalButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				AppearanceSettings.setForeground(AppearanceConstants.greenText, LoginScreenGUI.this.masterAlertLabel,
						LoginScreenGUI.this.clientAlertLabel);
				LoginScreenGUI.this.masterAlertLabel.setText("Success: Playing Local Game Next");
				LoginScreenGUI.this.clientAlertLabel.setText("Success: Logged In as Guest");
				LoginScreenGUI.this.clientHostnameTextField.setEnabled(false);
				LoginScreenGUI.this.clientPortTextField.setEnabled(false);
				LoginScreenGUI.this.clientConnectButton.setEnabled(false);
				LoginScreenGUI.this.clientLocalButton.setEnabled(false);

				new GameLobbyGUI(-1).setVisible(true);
				LoginScreenGUI.this.dispose();
			}
		});

		this.accountLoginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (LoginScreenGUI.this.mySQLDriver != null) {

					String username = LoginScreenGUI.this.accountUsernameTextField.getText();

					if (LoginScreenGUI.this.mySQLDriver.checkOneUser(username)) {

						User tempUser = LoginScreenGUI.this.mySQLDriver.getOneUser(username);
						String password = LoginScreenGUI.this.accountPasswordTextField.getText();

						if (tempUser.comparePassword(password)) {

							AppearanceSettings.setForeground(AppearanceConstants.greenText,
									LoginScreenGUI.this.masterAlertLabel, LoginScreenGUI.this.accountAlertLabel);
							LoginScreenGUI.this.loggedUser = tempUser;
							LoginScreenGUI.this.loggedUser.toggleOnline();
							LoginScreenGUI.this.masterAlertLabel.setText("Success: Going to Game Lobby Next");
							LoginScreenGUI.this.accountAlertLabel.setText(
									"Success: User " + LoginScreenGUI.this.loggedUser.getUsername() + " Logged In");

							new GameLobbyGUI(LoginScreenGUI.this.loggedUser);
							LoginScreenGUI.this.dispose();

						} else {
							AppearanceSettings.setForeground(AppearanceConstants.redText,
									LoginScreenGUI.this.accountAlertLabel);
							LoginScreenGUI.this.accountAlertLabel.setText("Account Alert: This password is incorrect.");
						}
					} else {
						AppearanceSettings.setForeground(AppearanceConstants.redText,
								LoginScreenGUI.this.accountAlertLabel);
						LoginScreenGUI.this.accountAlertLabel.setText("Account Alert: This username doesn't exit.");
					}
				} else {
					AppearanceSettings.setForeground(AppearanceConstants.redText,
							LoginScreenGUI.this.accountAlertLabel);
					LoginScreenGUI.this.accountAlertLabel
							.setText("Account ALert: Client is NOT connected to MySQL Database.");
				}
			}

		});

		this.accountCreateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (LoginScreenGUI.this.mySQLDriver != null) {

					String username = LoginScreenGUI.this.accountUsernameTextField.getText();

					if (LoginScreenGUI.this.mySQLDriver.checkOneUser(username)) {
						AppearanceSettings.setForeground(AppearanceConstants.redText,
								LoginScreenGUI.this.accountAlertLabel);
						LoginScreenGUI.this.accountAlertLabel.setText("Account Alert: This username already exits.");
					} else {
						String password = LoginScreenGUI.this.accountPasswordTextField.getText();
						AppearanceSettings.setForeground(AppearanceConstants.greenText,
								LoginScreenGUI.this.masterAlertLabel, LoginScreenGUI.this.accountAlertLabel);
						LoginScreenGUI.this.loggedUser = new User(username, password, LoginScreenGUI.this.mySQLDriver);
						LoginScreenGUI.this.loggedUser.toggleOnline();
						LoginScreenGUI.this.masterAlertLabel.setText("Success: Going to Game Lobby Next");
						LoginScreenGUI.this.accountAlertLabel
								.setText("Success: User " + LoginScreenGUI.this.loggedUser.getUsername() + " Created");

						new GameLobbyGUI(LoginScreenGUI.this.loggedUser);
						LoginScreenGUI.this.dispose();
					}
				} else {
					AppearanceSettings.setForeground(AppearanceConstants.redText,
							LoginScreenGUI.this.accountAlertLabel);
					LoginScreenGUI.this.accountAlertLabel
							.setText("Account ALert: Client is NOT connected to MySQL Database.");
				}
			}

		});
	}

	private JPanel createCenterPanel() {
		JPanel centerPanel = new JPanel(new BorderLayout());
		JPanel centerNorthPanel = new JPanel(new BorderLayout());
		JPanel centerCenterPanel = new JPanel(new GridLayout(1, 2));
		JPanel centerSouthPanel = new JPanel();
		JLabel clientTitleLabel = new JLabel("Enter hostname and port to connect");
		AppearanceSettings.setBackground(AppearanceConstants.veryLightGrey, centerPanel, centerNorthPanel,
				centerCenterPanel, centerSouthPanel, clientTitleLabel, this.clientAlertLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontMedium, clientTitleLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, this.clientAlertLabel, this.clientHostnameTextField,
				this.clientPortTextField, this.clientConnectButton, this.clientLocalButton);
		AppearanceSettings.setForeground(AppearanceConstants.secondaryText, clientTitleLabel, this.clientAlertLabel);
		AppearanceSettings.setSize(600, 200, centerPanel);
		AppearanceSettings.setSize(600, 100, centerNorthPanel);
		AppearanceSettings.setSize(600, 50, centerCenterPanel, centerSouthPanel);
		AppearanceSettings.setSize(200, 40, this.clientHostnameTextField, this.clientPortTextField,
				this.clientConnectButton, this.clientLocalButton);
		AppearanceSettings.setTextAlignment(clientTitleLabel, this.clientAlertLabel);
		this.clientHostnameTextField.setEnabled(true);
		this.clientPortTextField.setEnabled(true);
		this.clientConnectButton.setEnabled(true);
		centerNorthPanel.add(clientTitleLabel, BorderLayout.CENTER);
		centerNorthPanel.add(this.clientAlertLabel, BorderLayout.SOUTH);
		centerCenterPanel.add(this.clientHostnameTextField);
		centerCenterPanel.add(this.clientPortTextField);
		centerSouthPanel.add(this.clientConnectButton);
		centerSouthPanel.add(this.clientLocalButton);
		centerPanel.add(centerNorthPanel, BorderLayout.NORTH);
		centerPanel.add(centerCenterPanel, BorderLayout.CENTER);
		centerPanel.add(centerSouthPanel, BorderLayout.SOUTH);
		return centerPanel;
	}

	private void createGUI() {
		this.setSize(600, 550);
		this.setLocation(0, 180);
		this.add(this.createNorthPanel(), BorderLayout.NORTH);
		this.add(this.createCenterPanel(), BorderLayout.CENTER);
		this.add(this.createSouthPanel(), BorderLayout.SOUTH);
		this.setVisible(true);
	}

	private JPanel createNorthPanel() {
		JPanel northPanel = new JPanel(new BorderLayout());
		JLabel masterTitleLabel = new JLabel("Chutes & Ladders: Client Login");
		AppearanceSettings.setBackground(AppearanceConstants.lightGrey, northPanel, masterTitleLabel,
				this.masterAlertLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontLarge, masterTitleLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, this.masterAlertLabel);
		AppearanceSettings.setSize(600, 150, northPanel);
		AppearanceSettings.setSize(600, 100, masterTitleLabel);
		AppearanceSettings.setSize(600, 50, this.masterAlertLabel);
		AppearanceSettings.setTextAlignment(masterTitleLabel, this.masterAlertLabel);
		northPanel.add(masterTitleLabel, BorderLayout.CENTER);
		northPanel.add(this.masterAlertLabel, BorderLayout.SOUTH);
		return northPanel;
	}

	private JPanel createSouthPanel() {
		JPanel southPanel = new JPanel(new BorderLayout());
		JPanel southNorthPanel = new JPanel(new BorderLayout());
		JPanel southCenterPanel = new JPanel(new GridLayout(1, 2));
		JPanel southSouthPanel = new JPanel();
		JLabel accountTitleLabel = new JLabel("Please enter your username and password");
		AppearanceSettings.setBackground(AppearanceConstants.veryLightGrey, southPanel, southNorthPanel,
				southCenterPanel, southSouthPanel, accountTitleLabel, this.accountAlertLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontMedium, accountTitleLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, this.accountAlertLabel, this.accountUsernameTextField,
				this.accountPasswordTextField, this.accountLoginButton, this.accountCreateButton);
		AppearanceSettings.setForeground(AppearanceConstants.secondaryText, accountTitleLabel, this.accountAlertLabel);
		AppearanceSettings.setSize(600, 200, southPanel);
		AppearanceSettings.setSize(600, 100, southNorthPanel);
		AppearanceSettings.setSize(600, 50, southCenterPanel, southSouthPanel);
		AppearanceSettings.setSize(200, 40, this.accountUsernameTextField, this.accountPasswordTextField,
				this.accountLoginButton, this.accountCreateButton);
		AppearanceSettings.setTextAlignment(accountTitleLabel, this.accountAlertLabel);
		this.accountUsernameTextField.setEnabled(false);
		this.accountPasswordTextField.setEnabled(false);
		this.accountLoginButton.setEnabled(false);
		this.accountCreateButton.setEnabled(false);
		southNorthPanel.add(accountTitleLabel, BorderLayout.CENTER);
		southNorthPanel.add(this.accountAlertLabel, BorderLayout.SOUTH);
		southCenterPanel.add(this.accountUsernameTextField);
		southCenterPanel.add(this.accountPasswordTextField);
		southSouthPanel.add(this.accountLoginButton);
		southSouthPanel.add(this.accountCreateButton);
		southPanel.add(southNorthPanel, BorderLayout.NORTH);
		southPanel.add(southCenterPanel, BorderLayout.CENTER);
		southPanel.add(southSouthPanel, BorderLayout.SOUTH);
		return southPanel;
	}

	private void initializeComponents() {

		this.mySQLDriver = null;
		this.mClientSocket = null;
		this.loggedUser = null;

		// mySQLDriver = new MySQLDriver();
		// mySQLDriver.connect("localhost", 3306, "root", "Boeing7478");

		this.masterAlertLabel = new JLabel("Master Alert: N/A");
		this.clientAlertLabel = new JLabel("Client Alert: Please make sure a Server is open first");
		this.clientHostnameTextField = new JTextField("localhost");
		this.clientPortTextField = new JTextField("6789");
		this.clientConnectButton = new JButton("Connect");
		this.clientLocalButton = new JButton("Local Game");
		ImageIcon water = new ImageIcon("images/connect.png");
		this.clientConnectButton.setIcon(water);
		ImageIcon water2 = new ImageIcon("images/local.png");
		this.clientLocalButton.setIcon(water2);

		this.accountAlertLabel = new JLabel("Account Alert: N/A");
		this.accountUsernameTextField = new JTextField();
		this.accountPasswordTextField = new JTextField();
		this.accountLoginButton = new JButton("Login");
		this.accountCreateButton = new JButton("Create Account");
		ImageIcon water3 = new ImageIcon("images/login.png");
		this.accountLoginButton.setIcon(water3);
		ImageIcon water4 = new ImageIcon("images/create.png");
		this.accountCreateButton.setIcon(water4);
	}

	public void setMySQLDriver(MySQLDriver inMySQLDriver) {
		this.mySQLDriver = inMySQLDriver;
	}

}
