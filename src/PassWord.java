import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * *************************************************************************
 *
 * <p>
 * class PassWord
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c)
 * </p>
 * <p>
 * Company: TSSD
 * </p>
 * 
 * @author Ronald S. Holland
 * @version 1.0
 ****************************************************************************/
public class PassWord extends Dialog implements ActionListener {

	private JButton cancel, enter;
	private JTextField userID;
	private JLabel userIDLabel, passwordLabel;
	private JPasswordField password;
	private JPanel buttonPanel, mainPanel;
	private Container c;
	private HardwareStore hwStore;
	private String whichDialog;

	/**
	 * @param hw_Store
	 */
	public PassWord(HardwareStore hw_Store) {
		super(new Frame(), "Password Check", true);

		hwStore = hw_Store;

		enter = new JButton("Enter");
		cancel = new JButton("Cancel");

		buttonPanel = new JPanel();
		mainPanel = new JPanel();

		mainPanel.setLayout(new GridLayout(3, 2));
		add(mainPanel, BorderLayout.CENTER);

		userID = new JTextField(10);
		password = new JPasswordField(10);

		userIDLabel = new JLabel("Enter your user ID");
		passwordLabel = new JLabel("Enter your user password");

		mainPanel.add(userIDLabel);
		mainPanel.add(userID);
		mainPanel.add(passwordLabel);
		mainPanel.add(password);

		buttonPanel.add(enter);
		buttonPanel.add(cancel);
		add(buttonPanel, BorderLayout.SOUTH);

		enter.addActionListener(this);
		cancel.addActionListener(this);

		setSize(400, 300);

	}

	/**
	 * @param which_Dialog
	 */
	public void displayDialog(String which_Dialog) {
		whichDialog = which_Dialog;

		userID.setText("admin");
		password.setText("hwstore");

		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == enter) {

			String pwd = new String(password.getPassword());
			String uID = new String(userID.getText());

			if ((uID.equals("admin")) && (pwd.equals("hwstore"))) {
				if (whichDialog == "delete") {
					hwStore.displayDeleteDialog();
					whichDialog = "closed";
					userID.setText("");
					password.setText("");
					clear();
				} else if (whichDialog == "inputUpdateRecordRecord") {
					hwStore.displayinputUpdateRecordRecordDialog();
					whichDialog = "closed";
					userID.setText("");
					password.setText("");
					clear();
				} else if (whichDialog == "add") {
					hwStore.displayAddDialog();
					whichDialog = "closed";
					userID.setText("");
					password.setText("");
					clear();
				}
			} else {
				JOptionPane.showMessageDialog(null, "A userid or the password was incorrect.\n", "Invalid Password",
						JOptionPane.INFORMATION_MESSAGE);
				userID.setText("");
				password.setText("");
			}
		}

		clear();
	}

	private void clear() {
		setVisible(false);
		return;
	}

}