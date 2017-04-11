package frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import game_logic.GameLobbyLogic;
import utilities.AppearanceConstants;
import utilities.AppearanceSettings;

public class UserSearchGUI extends JPanel {
	private static final long serialVersionUID = 1L;
	private JPanel main;
	private GameLobbyLogic logic;
	private JButton searchButton;
	private JTextField searchBar;
	private String[][] userData;
	private JTable table;
	private JScrollPane scrollPane;
	private JButton refreshButton;

	public UserSearchGUI(GameLobbyLogic lo) {
		this.main = new JPanel();
		this.logic = lo;
		this.main.setLayout(new BorderLayout());
		this.main.setPreferredSize(new Dimension(500, 500));
		this.initializeVariables();
		this.addListeners();
	}

	private void addListeners() {

		this.searchBar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				UserSearchGUI.this.updateTable();
			}
		});

		this.refreshButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				UserSearchGUI.this.updateTable();
			}
		});

		this.searchBar.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				UserSearchGUI.this.updateTable();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				UserSearchGUI.this.updateTable();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				UserSearchGUI.this.updateTable();
			}

		});

		this.searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UserSearchGUI.this.updateTable();
			}
		});

	}

	private void initializeVariables() {
		String[] headers = { "Username", "SMF", "SMB", "LC", "SD", "Status" };

		this.userData = this.logic.getAllRelevantUserData("");

		this.table = new JTable(this.userData, headers) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};

		this.table.getColumnModel().getColumn(0).setPreferredWidth(180);
		this.table.getColumnModel().getColumn(1).setPreferredWidth(20);

		this.table.getColumnModel().getColumn(2).setPreferredWidth(20);
		this.table.getColumnModel().getColumn(3).setPreferredWidth(20);
		this.table.getColumnModel().getColumn(4).setPreferredWidth(20);
		this.table.getColumnModel().getColumn(5).setPreferredWidth(50);
		AppearanceSettings.setFont(AppearanceConstants.fontSmallest, this.table);
		AppearanceSettings.setBackground(AppearanceConstants.mediumLightGrey, this.table);
		this.refreshButton = new JButton("Refresh");
		JPanel holder2 = new JPanel(new BorderLayout());
		this.scrollPane = new JScrollPane(this.table);
		this.table.setFillsViewportHeight(true);
		this.table.setSize(new Dimension(300, 100));
		this.scrollPane.setSize(new Dimension(300, 100));
		this.table.setRowSelectionAllowed(false);
		this.table.setFocusable(false);

		JPanel holderz = new JPanel(new BorderLayout());
		this.searchBar = new JTextField("");
		this.searchButton = new JButton("Search");
		ImageIcon water = new ImageIcon("images/search.png");
		this.searchButton.setIcon(water);
		ImageIcon water2 = new ImageIcon("images/refresh.png");
		this.refreshButton.setIcon(water2);
		holderz.add(this.searchBar, BorderLayout.CENTER);
		holderz.add(this.searchButton, BorderLayout.EAST);

		AppearanceSettings.setFont(AppearanceConstants.fontSmall, this.searchBar, this.searchButton, holderz,
				this.refreshButton);
		AppearanceSettings.setBackground(AppearanceConstants.black, this.searchButton, holderz, holder2);
		AppearanceSettings.setForeground(AppearanceConstants.secondaryText, this.searchBar, this.searchButton, holderz);
		AppearanceSettings.setBackground(AppearanceConstants.black, this.main);

		holder2.add(this.refreshButton);

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
		c.weighty = 0.03;
		c.gridy = 0;
		this.main.add(holderz, c);
		// It's fine to reuse the constraints object; add makes a copy.
		c.weighty = 0.95;
		c.gridy = 1;
		this.main.add(this.scrollPane, c);
		c.weighty = 0.02;
		c.gridy = 2;
		this.main.add(holder2, c);

		this.add(this.main);
		AppearanceSettings.reDo(this.table, this.scrollPane, this.main, this);
		this.updateTable();
	}

	private void updateTable() {
		// hide irrelevant users
		if (this.searchBar.getText().equals("")) {
			this.userData = this.logic.getAllRelevantUserData("");
		} else {
			this.userData = this.logic.getAllRelevantUserData(this.searchBar.getText());
		}
		String[] headers = { "<html><b>Username</b></html>", "<html><b>SMF</b></html>", "<html><b>SMB</b></html>",
				"<html><b>LC</b></html>", "<html><b>SD</b></html>", "<html><b>Status</b></html>" };

		DefaultTableModel tableModel = new DefaultTableModel(this.userData, headers);
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(this.table.getModel());
		this.table.setRowSorter(sorter);

		List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
		sortKeys.add(new RowSorter.SortKey(5, SortOrder.DESCENDING));
		sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);

		this.table = new JTable(tableModel) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		this.table.setAutoCreateRowSorter(true);
		this.table.getColumnModel().getColumn(0).setPreferredWidth(180);
		this.table.getColumnModel().getColumn(1).setPreferredWidth(20);
		this.table.getColumnModel().getColumn(2).setPreferredWidth(20);
		this.table.getColumnModel().getColumn(3).setPreferredWidth(20);
		this.table.getColumnModel().getColumn(4).setPreferredWidth(20);
		this.table.getColumnModel().getColumn(5).setPreferredWidth(50);
		AppearanceSettings.setFont(AppearanceConstants.fontSmallest, this.table);
		AppearanceSettings.setBackground(AppearanceConstants.mediumLightGrey, this.table);
		// DefaultTableModel dm = (DefaultTableModel)table.getModel();
		// dm.fireTableDataChanged();
		this.main.remove(this.scrollPane);
		this.scrollPane = new JScrollPane(this.table);
		this.table.setFillsViewportHeight(true);
		this.table.setSize(new Dimension(300, 100));
		this.scrollPane.setSize(new Dimension(300, 100));
		this.table.setRowSelectionAllowed(false);
		this.table.setFocusable(false);
		GridBagConstraints c = new GridBagConstraints();
		// we want the layout to stretch the components in both directions
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0.85;
		c.gridy = 1;

		this.main.add(this.scrollPane, c);

		AppearanceSettings.reDo(this.table, this.main);
	}
}