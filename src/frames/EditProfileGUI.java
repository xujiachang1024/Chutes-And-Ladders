package frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utilities.AppearanceConstants;
import utilities.AppearanceSettings;
import utilities.MySQLDriver;
import utilities.TextFieldFocusListener;
import utilities.User;

public class EditProfileGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {

		// Guest User
		new EditProfileGUI(new User("Miller"));

		// Registered User
		MySQLDriver mySQLDriver = new MySQLDriver();
		mySQLDriver.connect("localhost", 3306, "root", "Boeing7478");
		new EditProfileGUI(mySQLDriver.getOneUser("Ernest"));

	}

	private User mUser;
	private JLabel usernameAlertLabel;
	private JTextField usernameOldTextField;
	private JTextField usernameNewTextField;

	private JButton usernameChangeButton;
	private JLabel passwordAlertLabel;
	private JTextField passwordOldTextField;
	private JTextField passwordNewTextField;
	private JButton passwordChangeButton;

	private UserProfileGUI gui;

	public EditProfileGUI(User inUser) {
		super("Chutes & Ladders: Change Username/Password");
		this.mUser = inUser;
		this.initializeVariables();
		this.createGUI();
		this.addListeners();
	}

	public EditProfileGUI(User inUser, UserProfileGUI gui) {
		super("Chutes & Ladders: Change Username/Password");
		this.gui = gui;
		this.mUser = inUser;
		this.initializeVariables();
		this.createGUI();
		this.addListeners();
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				EditProfileGUI.this.exitProcedure();
			}
		});
	}

	private void addListeners() {

		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		this.usernameNewTextField
				.addFocusListener(new TextFieldFocusListener("Enter new username", this.usernameNewTextField));
		this.passwordOldTextField
				.addFocusListener(new TextFieldFocusListener("Enter old password", this.passwordOldTextField));
		this.passwordNewTextField
				.addFocusListener(new TextFieldFocusListener("Enter new password", this.passwordNewTextField));

		this.usernameChangeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String newUsername = EditProfileGUI.this.usernameNewTextField.getText();

				if (EditProfileGUI.this.mUser.isGuestUser()) {
					if (EditProfileGUI.this.mUser.changeUsername(newUsername)) {
						AppearanceSettings.setForeground(AppearanceConstants.greenText,
								EditProfileGUI.this.usernameAlertLabel);
						EditProfileGUI.this.usernameAlertLabel.setText("Success: Guest Username Changed");
						EditProfileGUI.this.usernameOldTextField.setText(EditProfileGUI.this.mUser.getUsername());
						EditProfileGUI.this.usernameNewTextField.setText("");
					} else {
						AppearanceSettings.setForeground(AppearanceConstants.redText,
								EditProfileGUI.this.usernameAlertLabel);
						EditProfileGUI.this.usernameAlertLabel.setText("Username Alert: Fail to change guest username");
					}

				}

				else if (EditProfileGUI.this.mUser.isRegisteredUser()) {

					if (newUsername.equals(EditProfileGUI.this.mUser.getUsername())) {
						AppearanceSettings.setForeground(AppearanceConstants.greenText,
								EditProfileGUI.this.usernameAlertLabel);
						EditProfileGUI.this.usernameAlertLabel.setText("Success: Registered Username Changed");
						EditProfileGUI.this.usernameOldTextField.setText(EditProfileGUI.this.mUser.getUsername());
						EditProfileGUI.this.usernameNewTextField.setText("");

					} else if (EditProfileGUI.this.mUser.changeUsername(newUsername)) {
						AppearanceSettings.setForeground(AppearanceConstants.greenText,
								EditProfileGUI.this.usernameAlertLabel);
						EditProfileGUI.this.usernameAlertLabel.setText("Success: Registered Username Changed");
						EditProfileGUI.this.usernameOldTextField.setText(EditProfileGUI.this.mUser.getUsername());
						EditProfileGUI.this.usernameNewTextField.setText("");

					} else {
						AppearanceSettings.setForeground(AppearanceConstants.redText,
								EditProfileGUI.this.usernameAlertLabel);
						EditProfileGUI.this.usernameAlertLabel.setText("Username Alert: New username already exists");
						EditProfileGUI.this.usernameNewTextField.setText("");
					}
				}
			}
		});

		this.passwordChangeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String oldPassword = EditProfileGUI.this.passwordOldTextField.getText();
				String newPassword = EditProfileGUI.this.passwordNewTextField.getText();

				if (EditProfileGUI.this.mUser.comparePassword(oldPassword)) {

					EditProfileGUI.this.mUser.changePassword(newPassword);

					AppearanceSettings.setForeground(AppearanceConstants.greenText,
							EditProfileGUI.this.passwordAlertLabel);
					EditProfileGUI.this.passwordAlertLabel.setText("Success: Registered Password Changed");
					EditProfileGUI.this.passwordOldTextField.setText("");
					EditProfileGUI.this.passwordNewTextField.setText("");
				} else {
					AppearanceSettings.setForeground(AppearanceConstants.redText,
							EditProfileGUI.this.passwordAlertLabel);
					EditProfileGUI.this.passwordAlertLabel.setText("Password Alert: The old password is incorrect");
					EditProfileGUI.this.passwordOldTextField.setText("");
					EditProfileGUI.this.passwordNewTextField.setText("");
				}
			}

		});

	}

	private void createGUI() {
		this.setSize(400, 230);
		this.setMinimumSize(new Dimension(400, 230));
		this.setMaximumSize(new Dimension(600, 230));
		this.setLocation(600, 300);
		this.add(this.createUsernamePanel(), BorderLayout.NORTH);
		// add(new JPanel(), BorderLayout.CENTER);
		this.add(this.createPasswordPanel(), BorderLayout.SOUTH);
		this.setVisible(true);
	}

	private JPanel createPasswordPanel() {
		JPanel passwordPanel = new JPanel(new BorderLayout());
		JPanel passwordNorthPanel = new JPanel(new BorderLayout());
		JPanel passwordCenterPanel = new JPanel(new GridLayout(1, 2));
		JPanel passwordSouthPanel = new JPanel();
		JLabel passwordTitleLabel = new JLabel("Change Your Password Below");
		AppearanceSettings.setFont(AppearanceConstants.fontSmallest, passwordTitleLabel, this.passwordAlertLabel,
				this.passwordOldTextField, this.passwordNewTextField, this.passwordChangeButton);
		AppearanceSettings.setSize(400, 100, passwordPanel);
		AppearanceSettings.setSize(400, 50, passwordNorthPanel);
		AppearanceSettings.setSize(400, 25, passwordCenterPanel, passwordSouthPanel, passwordTitleLabel,
				this.passwordAlertLabel);
		AppearanceSettings.setSize(180, 20, this.passwordOldTextField, this.passwordNewTextField,
				this.passwordChangeButton);
		AppearanceSettings.setTextAlignment(passwordTitleLabel, this.passwordAlertLabel);
		this.passwordOldTextField.setEnabled(this.mUser.isRegisteredUser());
		this.passwordNewTextField.setEnabled(this.mUser.isRegisteredUser());
		this.passwordChangeButton.setEnabled(this.mUser.isRegisteredUser());
		passwordNorthPanel.add(passwordTitleLabel, BorderLayout.CENTER);
		passwordNorthPanel.add(this.passwordAlertLabel, BorderLayout.SOUTH);
		passwordCenterPanel.add(this.passwordOldTextField);
		passwordCenterPanel.add(this.passwordNewTextField);
		passwordSouthPanel.add(this.passwordChangeButton);
		passwordPanel.add(passwordNorthPanel, BorderLayout.NORTH);
		passwordPanel.add(passwordCenterPanel, BorderLayout.CENTER);
		passwordPanel.add(passwordSouthPanel, BorderLayout.SOUTH);
		return passwordPanel;
	}

	private JPanel createUsernamePanel() {
		JPanel usernamePanel = new JPanel(new BorderLayout());
		JPanel usernameNorthPanel = new JPanel(new BorderLayout());
		JPanel usernameCenterPanel = new JPanel(new GridLayout(1, 2));
		JPanel usernameSouthPanel = new JPanel();
		JLabel usernameTitleLabel = new JLabel("Change Your Username Below");
		AppearanceSettings.setFont(AppearanceConstants.fontSmallest, usernameTitleLabel, this.usernameAlertLabel,
				this.usernameOldTextField, this.usernameNewTextField, this.usernameChangeButton);
		AppearanceSettings.setSize(400, 100, usernamePanel);
		AppearanceSettings.setSize(400, 50, usernameNorthPanel);
		AppearanceSettings.setSize(400, 25, usernameCenterPanel, usernameSouthPanel, usernameTitleLabel,
				this.usernameAlertLabel);
		AppearanceSettings.setSize(180, 20, this.usernameOldTextField, this.usernameNewTextField,
				this.usernameChangeButton);
		AppearanceSettings.setTextAlignment(usernameTitleLabel, this.usernameAlertLabel);
		this.usernameOldTextField.setEditable(false);
		this.usernameNewTextField.setEnabled(true);
		this.usernameChangeButton.setEnabled(true);
		usernameNorthPanel.add(usernameTitleLabel, BorderLayout.CENTER);
		usernameNorthPanel.add(this.usernameAlertLabel, BorderLayout.SOUTH);
		usernameCenterPanel.add(this.usernameOldTextField);
		usernameCenterPanel.add(this.usernameNewTextField);
		usernameSouthPanel.add(this.usernameChangeButton);
		usernamePanel.add(usernameNorthPanel, BorderLayout.NORTH);
		usernamePanel.add(usernameCenterPanel, BorderLayout.CENTER);
		usernamePanel.add(usernameSouthPanel, BorderLayout.SOUTH);
		return usernamePanel;
	}

	public void exitProcedure() {
		this.gui.relist();
		this.dispose();

	}

	private void initializeVariables() {
		this.usernameAlertLabel = new JLabel("Username Alert: N/A");
		this.usernameOldTextField = new JTextField(this.mUser.getUsername());
		this.usernameNewTextField = new JTextField();
		this.usernameChangeButton = new JButton("Change Username");
		this.passwordAlertLabel = new JLabel("Password Alert: N/A");
		this.passwordOldTextField = new JTextField();
		this.passwordNewTextField = new JTextField();
		this.passwordChangeButton = new JButton("Change Password");
	}
}
