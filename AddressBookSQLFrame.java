import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import java.awt.Component;

public class AddressBookSQLFrame extends JFrame {

	private JPanel contentPane;
	private JTextField nameTFld;
	private JTextField emailTFld;
	private JTextField phoneTFld;
	private JScrollPane scrollPane;
	private JTextArea addressbook;
	private GroupLayout gl_panel;
	PrintWriter out;
	SwingValidator sv = new SwingValidator();
	Statement statement = null;
	Connection connection = null;
	int count = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddressBookSQLFrame frame = new AddressBookSQLFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AddressBookSQLFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 568, 393);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);

		JLabel lblNewLabel = new JLabel("Name:");

		JLabel lblNewLabel_1 = new JLabel("Email:");

		nameTFld = new JTextField();
		nameTFld.setColumns(10);
		nameTFld.requestFocusInWindow();

		emailTFld = new JTextField();
		emailTFld.setColumns(10);

		phoneTFld = new JTextField();
		phoneTFld.setColumns(10);

		JButton addBtn = new JButton("Add");
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (isValidData()) {

					try {
						if (checkForDuplicate(getConnection())) {
							getStatementConnection();

							String insertStatement = "INSERT INTO AddressBook (Name, Email, PhoneNumber) " + "VALUES ('"
									+ nameTFld.getText() + "', " + "'" + emailTFld.getText() + "', " + "'"
									+ phoneTFld.getText() + "')";

							try {
								count = statement.executeUpdate(insertStatement);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							clearTextField();

							fillAddressBook();

							if (count > 0) {
								String addMessage = "Entry sucessfully added to the address book.";
								sv.showPositiveMessage(addressbook, addMessage);
							}
						} else {
							sv.showNegativeMessage(addressbook,
									"Information was not added to the address book. Please try again.");
							// clearTextField();
						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		JButton exitBtn = new JButton("Exit");
		exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		JLabel lblNewLabel_2 = new JLabel("Phone:");

		JButton clearBtn = new JButton("Clear");
		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearTextField();
			}

		});

		JButton showListBtn = new JButton("Show List");
		showListBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fillAddressBook();
				nameTFld.requestFocusInWindow();
			}
		});

		JScrollPane scrollPane_1 = new JScrollPane();
		addressbook = new JTextArea();
		scrollPane_1.setViewportView(addressbook);
		// panel.setLayout(g1_panel);

		JButton updateListBtn = new JButton("Update Record");
		updateListBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (isValidData()) {
					String updateStatement = "UPDATE AddressBook SET Name = '" + nameTFld.getText() + "', "
							+ "Email = '" + emailTFld.getText() + "', " + "PhoneNumber = '" + phoneTFld.getText() + "' "
							+ "WHERE Name = '" + nameTFld.getText() + "'";

					try {
						count = statement.executeUpdate(updateStatement);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					clearTextField();
					fillAddressBook();

					if (count > 0) {
						String message = "An existing entry in the address book was updated sucessfully.";
						sv.showPositiveMessage(addressbook, message);
					} else if (count <= 0) {
						String noUpdateMessage = "There is no matching name in the address book. Please try again.";
						sv.showMessage(addressbook, noUpdateMessage);
					}
				}
			}

		});

		JButton deleteBtn = new JButton("Delete record");
		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getStatementConnection();
				String deleteStatement = null;

				if (!nameTFld.getText().isEmpty())
					deleteStatement = "DELETE FROM AddressBook WHERE Name = '" + nameTFld.getText() + "'";
				else if (!emailTFld.getText().isEmpty())
					deleteStatement = "DELETE FROM AddressBook WHERE Email = '" + emailTFld.getText() + "'";
				else if (!phoneTFld.getText().isEmpty())
					deleteStatement = "DELETE FROM AddressBook WHERE PhoneNumber = '" + phoneTFld.getText() + "'";

				try {
					count = statement.executeUpdate(deleteStatement);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				clearTextField();
				fillAddressBook();
				if (count > 0) {
					String message = "An existing entry in the address book was deleted sucessfully.";
					sv.showPositiveMessage(addressbook, message);
				} else if (count <= 0) {
					String noUpdateMessage = "There is no matching entry in the address book. Please try again.";
					sv.showMessage(addressbook, noUpdateMessage);
				}
			}
		});

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel
				.createSequentialGroup()
				.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel.createSequentialGroup()
						.addGap(43)
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addComponent(lblNewLabel_1)
								.addComponent(lblNewLabel).addComponent(lblNewLabel_2))
						.addGap(18)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(emailTFld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(nameTFld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(phoneTFld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 232, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup().addContainerGap(205, Short.MAX_VALUE)
								.addComponent(deleteBtn).addGap(18)
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
										.addGroup(gl_panel.createSequentialGroup().addComponent(clearBtn).addGap(18)
												.addComponent(addBtn).addGap(18).addComponent(exitBtn))
										.addGroup(gl_panel.createSequentialGroup().addGap(10).addComponent(showListBtn)
												.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(updateListBtn).addGap(12)))))
				.addGap(45)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel
						.createSequentialGroup().addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup().addGap(31).addGroup(gl_panel
										.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel).addGroup(
												gl_panel.createSequentialGroup()
														.addComponent(nameTFld, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addGap(28)
														.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
																.addComponent(emailTFld, GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE)
																.addComponent(lblNewLabel_1))
														.addGap(25)
														.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
																.addComponent(phoneTFld, GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE)
																.addComponent(lblNewLabel_2)))))
								.addGroup(gl_panel.createSequentialGroup().addGap(23).addComponent(scrollPane_1,
										GroupLayout.PREFERRED_SIZE, 210, GroupLayout.PREFERRED_SIZE)))
						.addPreferredGap(ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(addBtn)
								.addComponent(exitBtn).addComponent(clearBtn))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(showListBtn)
								.addComponent(updateListBtn).addComponent(deleteBtn))
						.addGap(24)));

		panel.setLayout(gl_panel);
	}

	private Connection getConnection() throws SQLException {
		Connection connection = null;

		// set up the home directory for Derby
		String dbDirectory = "c:/murach/java/db";
		System.setProperty("derby.system.home", dbDirectory);

		// create and return the connection
		String dbUrl = "jdbc:derby:AddressBookDB";
		connection = DriverManager.getConnection(dbUrl);

		return connection;
	}

	private void fillList(Connection connection) {
		try {
			statement = connection.createStatement();
			String query = "SELECT Name, Email, PhoneNumber FROM AddressBook";
			ResultSet rs = statement.executeQuery(query);
			String output = "";
			while (rs.next()) {
				String entry = rs.getString("Name") + "\t" + rs.getString("Email") + "\t" + rs.getString("PhoneNumber");
				output += entry + "\n";
			}
			addressbook.setText(output);
			rs.close();
		} catch (SQLException e) {
			for (Throwable t : e) {
				e.printStackTrace();
			}
		}
	}

	private void clearTextField() {
		nameTFld.setText("");
		emailTFld.setText("");
		phoneTFld.setText("");
	}

	public void getStatementConnection() {
		try {
			statement = getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean isValidData() {
		SwingValidator sv = new SwingValidator();
		return sv.isPresent(nameTFld, "Name") && sv.isValidName(nameTFld, "Name") && sv.isPresent(emailTFld, "Email")
				&& sv.isValidEmail(emailTFld, "Email") && sv.isPresent(phoneTFld, "Phone Number")
				&& sv.isValidPhone(phoneTFld, "Phone Number");
	}

	private void fillAddressBook() {
		try {
			fillList(getConnection());
			addressbook.setCaretPosition(0);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private boolean checkForDuplicate(Connection connection) {
		try {
			statement = connection.createStatement();
			String query = "SELECT * FROM AddressBook";
			ResultSet rs = statement.executeQuery(query);
			int dupeName = 0;
			int dupeEmail = 0;
			int dupePhone = 0;
			while (rs.next()) {
				if (nameTFld.getText().equals(rs.getString("Name")))
					dupeName++;
				if (emailTFld.getText().equals(rs.getString("Email")))
					dupeEmail++;
				if (phoneTFld.getText().equals(rs.getString("PhoneNumber")))
					dupePhone++;
			}

			if (dupeName > 0 || dupeEmail > 0 || dupePhone > 0) {
				showDuplicateErrorMsg(dupeName, dupeEmail, dupePhone);
				return false;
			}
			rs.close();
		} catch (SQLException e) {
			for (Throwable t : e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	private void showDuplicateErrorMsg(int dupeName, int dupeEmail, int dupePhone) {
		if (dupeName > 0 && dupeEmail > 0 && dupePhone > 0)
			sv.showNegativeMessage(addressbook, "Duplicate name, email, and phone entries");
		else if (dupeName > 0 && dupeEmail > 0)
			sv.showNegativeMessage(addressbook, "Duplicate name and email entries");
		else if (dupeName > 0 && dupePhone > 0)
			sv.showNegativeMessage(addressbook, "Duplicate name and phone entries");
		else if (dupeEmail > 0 && dupePhone > 0)
			sv.showNegativeMessage(addressbook, "Duplicate email and phone entries");
		else if (dupeName > 0)
			sv.showNegativeMessage(addressbook, "Duplicate name entry");
		else if (dupeEmail > 0)
			sv.showNegativeMessage(addressbook, "Duplicate email entry");
		else if (dupePhone > 0)
			sv.showNegativeMessage(addressbook, "Duplicate phone entry");
	}
}
