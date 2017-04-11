package server;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utilities.AppearanceConstants;
import utilities.AppearanceSettings;
import utilities.MySQLDriver;
import utilities.TextFieldFocusListener;

public class GameServerGUI extends JFrame {

	private static final long serialVersionUID = 1;

	private MySQLDriver mySQLDriver;
	private ServerSocket mServerSocket;
	private Lock mSQLLock;
	private Condition mSQLCondition;
	private Lock mSSLock;
	private Condition mSSCondition;

	private JLabel masterAlertLabel;
	private JLabel sqlAlertLabel;
	private JTextField sqlHostnameTextField;
	private JTextField sqlPortTextField;
	private JTextField sqlUsernameTextField;
	private JTextField sqlPasswordTextField;
	private JButton sqlConnectButton;
	private JLabel portAlertLabel;
	private JTextField portNumberTextField;
	private JButton portListenButton;

	public GameServerGUI() {
		super("Chutes & Ladders: Server");
		this.initializeVariables();
		this.createGUI();
		this.addListeners();
	}

	private void addListeners() {

		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		// sqlHostnameTextField.addFocusListener(new
		// TextFieldFocusListener("MySQLWorkbench Hostname",
		// sqlHostnameTextField));
		// sqlPortTextField.addFocusListener(new
		// TextFieldFocusListener("MySQLWorkbench Port", sqlPortTextField));
		// sqlUsernameTextField.addFocusListener(new
		// TextFieldFocusListener("MySQLWorkbench Username",
		// sqlUsernameTextField));
		this.sqlPasswordTextField
				.addFocusListener(new TextFieldFocusListener("MySQLWorkbench Password", this.sqlPasswordTextField));

		this.sqlConnectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String sqlHostnameStirng = GameServerGUI.this.sqlHostnameTextField.getText();
				String sqlPortString = GameServerGUI.this.sqlPortTextField.getText();
				String sqlUsernameString = GameServerGUI.this.sqlUsernameTextField.getText();
				String sqlPasswordString = GameServerGUI.this.sqlPasswordTextField.getText();

				MySQLDriver tempSQLDriver = new MySQLDriver();
				boolean tryConnection = tempSQLDriver.connect(sqlHostnameStirng, Integer.parseInt(sqlPortString),
						sqlUsernameString, sqlPasswordString);

				if (tryConnection) {

					GameServerGUI.this.mSQLLock.lock();
					GameServerGUI.this.mySQLDriver = tempSQLDriver;
					GameServerGUI.this.mSQLCondition.signal();
					GameServerGUI.this.mSQLLock.unlock();

					AppearanceSettings.setForeground(AppearanceConstants.greenText, GameServerGUI.this.sqlAlertLabel);
					GameServerGUI.this.sqlAlertLabel.setText("Success: Connected to MySQL Database");
					GameServerGUI.this.sqlHostnameTextField.setEnabled(false);
					GameServerGUI.this.sqlPortTextField.setEnabled(false);
					GameServerGUI.this.sqlUsernameTextField.setEnabled(false);
					GameServerGUI.this.sqlPasswordTextField.setEnabled(false);
					GameServerGUI.this.sqlConnectButton.setEnabled(false);
					GameServerGUI.this.portNumberTextField.setEnabled(true);
					GameServerGUI.this.portListenButton.setEnabled(true);
				} else {
					AppearanceSettings.setForeground(AppearanceConstants.redText, GameServerGUI.this.sqlAlertLabel);
					GameServerGUI.this.sqlAlertLabel
							.setText("MySQL Alert: Failed Connection! Please check your setting.");
				}

			}

		});

		this.portListenButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int port = -1;

				try {
					port = Integer.parseInt(GameServerGUI.this.portNumberTextField.getText());
					if ((port <= 0) || (port >= 65535)) {
						AppearanceSettings.setForeground(AppearanceConstants.redText,
								GameServerGUI.this.portAlertLabel);
						GameServerGUI.this.portAlertLabel
								.setText("Port Alert: Invalid Port! Please enter a valid port.");
					} else {
						try {
							ServerSocket tempServerSocket = new ServerSocket(port);
							GameServerGUI.this.mSSLock.lock();
							GameServerGUI.this.mServerSocket = tempServerSocket;
							GameServerGUI.this.mSSCondition.signal();
							GameServerGUI.this.mSSLock.unlock();

							AppearanceSettings.setForeground(AppearanceConstants.greenText,
									GameServerGUI.this.masterAlertLabel, GameServerGUI.this.portAlertLabel);
							GameServerGUI.this.masterAlertLabel
									.setText("Success: Ready for Clients to Join at Port " + Integer.toString(port));
							GameServerGUI.this.portAlertLabel
									.setText("Success: Listening to Port " + Integer.toString(port));
							GameServerGUI.this.portNumberTextField.setEnabled(false);
							GameServerGUI.this.portListenButton.setEnabled(false);

						} catch (IOException ioe) {
							AppearanceSettings.setForeground(AppearanceConstants.redText,
									GameServerGUI.this.portAlertLabel);
							GameServerGUI.this.portAlertLabel
									.setText("Port Alert: Fail to Create ServerSocket! Please try another port.");
							ioe.printStackTrace();
						}

					}
				} catch (Exception pe) {
					AppearanceSettings.setForeground(AppearanceConstants.redText, GameServerGUI.this.portAlertLabel);
					GameServerGUI.this.portAlertLabel.setText("Port Alert: Invalid Port! Please enter an integer.");
					pe.printStackTrace();
					return;
				}

			}

		});

	}

	private JPanel createCenterPanel() {
		JPanel centerPanel = new JPanel(new BorderLayout());
		JPanel centerNorthPanel = new JPanel(new BorderLayout());
		JPanel centerCenterPanel = new JPanel(new GridLayout(2, 2));
		JPanel centerSouthPanel = new JPanel();
		JLabel sqlTitleLabel = new JLabel("Enter your MySQLWorkbench setting to connect");
		AppearanceSettings.setBackground(AppearanceConstants.veryLightGrey, centerPanel, centerNorthPanel,
				centerCenterPanel, centerSouthPanel, sqlTitleLabel, this.sqlAlertLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontMedium, sqlTitleLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, this.sqlAlertLabel, this.sqlHostnameTextField,
				this.sqlPortTextField, this.sqlUsernameTextField, this.sqlPasswordTextField, this.sqlConnectButton);
		AppearanceSettings.setForeground(AppearanceConstants.secondaryText, sqlTitleLabel);
		AppearanceSettings.setForeground(AppearanceConstants.secondaryText, this.sqlAlertLabel);
		AppearanceSettings.setSize(600, 250, centerPanel);
		AppearanceSettings.setSize(600, 100, centerNorthPanel);
		AppearanceSettings.setSize(600, 100, centerCenterPanel);
		AppearanceSettings.setSize(600, 50, centerSouthPanel);
		AppearanceSettings.setSize(200, 40, this.sqlHostnameTextField, this.sqlPortTextField, this.sqlUsernameTextField,
				this.sqlPasswordTextField);
		AppearanceSettings.setSize(300, 40, this.sqlConnectButton);
		AppearanceSettings.setTextAlignment(sqlTitleLabel, this.sqlAlertLabel);
		this.sqlConnectButton.setEnabled(true);
		centerNorthPanel.add(sqlTitleLabel, BorderLayout.CENTER);
		centerNorthPanel.add(this.sqlAlertLabel, BorderLayout.SOUTH);
		centerCenterPanel.add(this.sqlHostnameTextField);
		centerCenterPanel.add(this.sqlPortTextField);
		centerCenterPanel.add(this.sqlUsernameTextField);
		centerCenterPanel.add(this.sqlPasswordTextField);
		centerSouthPanel.add(this.sqlConnectButton);
		centerPanel.add(centerNorthPanel, BorderLayout.NORTH);
		centerPanel.add(centerCenterPanel, BorderLayout.CENTER);
		centerPanel.add(centerSouthPanel, BorderLayout.SOUTH);
		return centerPanel;
	}

	private void createGUI() {
		this.setSize(600, 600);
		this.add(this.createNorthPanel(), BorderLayout.NORTH);
		this.add(this.createCenterPanel(), BorderLayout.CENTER);
		this.add(this.createSouthPanel(), BorderLayout.SOUTH);
		this.setVisible(true);
	}

	private JPanel createNorthPanel() {
		JPanel northPanel = new JPanel(new BorderLayout());
		JLabel masterTitleLabel = new JLabel("Chutes & Ladders: Server");
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
		JPanel southCenterPanel = new JPanel();
		JPanel southSouthPanel = new JPanel();
		JLabel portTitleLabel = new JLabel("Enter a port integer between 0 and 65535");
		AppearanceSettings.setBackground(AppearanceConstants.veryLightGrey, southPanel, southNorthPanel,
				southCenterPanel, southSouthPanel, portTitleLabel, this.portAlertLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontMedium, portTitleLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, this.portAlertLabel, this.portNumberTextField,
				this.portListenButton);
		AppearanceSettings.setForeground(AppearanceConstants.secondaryText, portTitleLabel);
		AppearanceSettings.setForeground(AppearanceConstants.secondaryText, this.portAlertLabel);
		AppearanceSettings.setSize(600, 200, southPanel);
		AppearanceSettings.setSize(600, 100, southNorthPanel);
		AppearanceSettings.setSize(600, 50, southCenterPanel, southSouthPanel);
		AppearanceSettings.setSize(200, 40, this.portNumberTextField, this.portListenButton);
		AppearanceSettings.setTextAlignment(portTitleLabel, this.portAlertLabel);
		this.portNumberTextField.setEnabled(false);
		this.portListenButton.setEnabled(false);
		southNorthPanel.add(portTitleLabel, BorderLayout.CENTER);
		southNorthPanel.add(this.portAlertLabel, BorderLayout.SOUTH);
		southCenterPanel.add(this.portNumberTextField);
		southSouthPanel.add(this.portListenButton);
		southPanel.add(southNorthPanel, BorderLayout.NORTH);
		southPanel.add(southCenterPanel, BorderLayout.CENTER);
		southPanel.add(southSouthPanel, BorderLayout.SOUTH);
		return southPanel;
	}

	public MySQLDriver getMySQLDriver() {

		while (this.mySQLDriver == null) {
			this.mSQLLock.lock();
			try {
				this.mSQLCondition.await();
			} catch (InterruptedException ie) {
				System.out.println("InterruptedException: " + ie.getMessage());
			}
			this.mSQLLock.unlock();
		}

		return this.mySQLDriver;
	}

	public ServerSocket getServerSocket() {

		while (this.mServerSocket == null) {
			this.mSSLock.lock();
			try {
				this.mSSCondition.await();
			} catch (InterruptedException ie) {
				System.out.println("InterruptedException: " + ie.getMessage());
			}
			this.mSSLock.unlock();
		}

		return this.mServerSocket;
	}

	private void initializeVariables() {

		this.mySQLDriver = null;
		this.mServerSocket = null;
		this.mSQLLock = new ReentrantLock();
		this.mSQLCondition = this.mSQLLock.newCondition();
		this.mSSLock = new ReentrantLock();
		this.mSSCondition = this.mSSLock.newCondition();

		this.masterAlertLabel = new JLabel("Master Alert: N/A");
		this.sqlAlertLabel = new JLabel("MySQL Alert: Please execute the initial.sql file before connnecting.");
		this.sqlHostnameTextField = new JTextField("localhost");
		this.sqlPortTextField = new JTextField("3306");
		this.sqlUsernameTextField = new JTextField("root");
		this.sqlPasswordTextField = new JTextField();
		this.sqlConnectButton = new JButton("Connect to MySQL Database");
		ImageIcon water2 = new ImageIcon("images/connect.png");
		this.sqlConnectButton.setIcon(water2);
		this.portAlertLabel = new JLabel("Port Alert: N/A");
		this.portNumberTextField = new JTextField("6789");
		this.portListenButton = new JButton("Start Listening");
		ImageIcon water = new ImageIcon("images/listen.png");
		this.portListenButton.setIcon(water);
	}

}
