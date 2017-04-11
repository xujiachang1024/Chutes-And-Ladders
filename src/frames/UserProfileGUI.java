package frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import client.GameClient;
import game_logic.GameLobbyLogic;
import utilities.AppearanceConstants;
import utilities.AppearanceSettings;

public class UserProfileGUI extends JPanel {
	private static final long serialVersionUID = 1L;
	private JPanel main;
	private GameLobbyLogic logic;
	private GameLobbyGUI gui;
	private JButton editButton;
	private JButton logoutButton;
	private UserProfileGUI thisholder;

	public UserProfileGUI(GameLobbyLogic inLogic, GameLobbyGUI gui) {
		this.logic = inLogic;
		this.gui = gui;
		this.thisholder = this;
		this.initializeVariables();
		this.addListeners();
	}

	private void addListeners() {
		this.editButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new EditProfileGUI(UserProfileGUI.this.logic.getUser(), UserProfileGUI.this.thisholder);
			}
		});

		this.logoutButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				UserProfileGUI.this.thisholder.gui.dispose();
				new GameClient();
			}
		});

	}

	private void initializeVariables() {
		this.main = new JPanel();
		this.main.setLayout(new BorderLayout());
		this.main.setPreferredSize(new Dimension(500, 500));
		String[] headers = { "A", "B" };
		String[][] userData = this.logic.getUserData();
		String[] achData = this.logic.getUserAch();
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("MyColumnHeader", achData);
		JTable table2 = new JTable(model) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		table2.setTableHeader(null);
		table2.setFillsViewportHeight(true);
		table2.setSize(new Dimension(300, 100));
		table2.setRowSelectionAllowed(false);
		table2.setFocusable(false);

		JTable table = new JTable(userData, headers) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		table.setTableHeader(null);
		table.setFillsViewportHeight(true);
		table.setSize(new Dimension(300, 100));
		table.setRowSelectionAllowed(false);
		table.setFocusable(false);

		JScrollPane scrollPane2 = new JScrollPane(table2);
		scrollPane2.setSize(new Dimension(300, 100));

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setSize(new Dimension(300, 100));

		// Panel holder = new JPanel(new BorderLayout());
		// holder.add(scrollPane, BorderLayout.CENTER);
		// holder.add(scrollPane2, BorderLayout.SOUTH);
		// holder.setSize(new Dimension(300,450));

		JPanel holder2 = new JPanel(new BorderLayout());
		this.editButton = new JButton("Edit Profile");
		this.logoutButton = new JButton("Logout");

		ImageIcon water = new ImageIcon("images/icon.png");
		this.editButton.setIcon(water);

		ImageIcon water33 = new ImageIcon("images/logout.png");
		this.logoutButton.setIcon(water33);

		holder2.add(this.editButton, BorderLayout.CENTER);
		holder2.add(this.logoutButton, BorderLayout.EAST);
		holder2.setBorder(BorderFactory.createMatteBorder(-1, -1, -4, -2, AppearanceConstants.black));

		this.main.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		// we want the layout to stretch the components in both directions
		c.fill = GridBagConstraints.BOTH;
		// if the total X weight is 0, then it won't stretch horizontally.
		// It doesn't matter what the weight actually is, as long as it's not 0,
		// because the grid is only one component wide
		c.weightx = 1;

		// Vertical space is divided in proportion to the Y weights of the
		// components
		c.weighty = 0.15;
		c.gridy = 0;
		this.main.add(scrollPane, c);
		// It's fine to reuse the constraints object; add makes a copy.
		c.weighty = 0.81;
		c.gridy = 1;
		this.main.add(scrollPane2, c);
		c.weighty = 0.04;
		c.gridy = 2;
		this.main.add(holder2, c);

		this.add(this.main);
		AppearanceSettings.setFont(AppearanceConstants.fontKindaSmallNoBold, table);
		AppearanceSettings.setFont(AppearanceConstants.fontSmallNoBold, table2);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, this.editButton, this.logoutButton);
		AppearanceSettings.setBackground(AppearanceConstants.black, this.editButton, this.logoutButton, holder2);
		AppearanceSettings.setForeground(AppearanceConstants.secondaryText, this.editButton, this.logoutButton);
		AppearanceSettings.setBackground(AppearanceConstants.black, scrollPane2, scrollPane);
		AppearanceSettings.reDo(table, scrollPane, this.main, this);
	}

	public void relist() {
		this.remove(this.main);
		this.initializeVariables();
		// this.editButton.setEnabled(false);
	}
}
