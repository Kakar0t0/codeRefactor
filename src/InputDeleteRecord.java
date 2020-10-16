import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class InputDeleteRecord extends Dialog implements ActionListener {
	private RandomAccessFile file;
	private JTextField recID;
	private JLabel recIDLabel;
	private JButton cancel, delete;
	private Record data;
	private int theRecID = -1, toCont;
	private JTable table;
	private String pData[][];
	private HardwareStore hwStore;
	private boolean found = false;

	/**
	 * @param hw_store
	 * @param f
	 * @param tab
	 * @param p_Data
	 */
	public InputDeleteRecord(HardwareStore hw_store, RandomAccessFile f, JTable tab, String p_Data[][]) {

		super(new Frame(), "Delete Record", true);
		setSize(400, 150);
		setLayout(new GridLayout(2, 2));
		file = f;
		table = tab;
		pData = p_Data;
		hwStore = hw_store;
		delSetup();
	}

	public void delSetup() {
		recIDLabel = new JLabel("Record ID");
		recID = new JTextField(10);
		delete = new JButton("Delete Record");
		cancel = new JButton("Cancel");

		cancel.addActionListener(this);
		delete.addActionListener(this);
		recID.addActionListener(this);

		add(recIDLabel);
		add(recID);
		add(delete);
		add(cancel);

		data = new Record();
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println("inputDeleteRecord(): 1a - In the actionPerformed() method. ");
		if (e.getSource() == recID) {
			theRecID = Integer.parseInt(recID.getText());

			if (theRecID < 0 || theRecID > 250) {
				recID.setText("Invalid part number");
			} else {

				try {
					file = new RandomAccessFile(hwStore.getImportFile(), "rw");

					file.seek(theRecID * data.getSize());
					data.ReadRec(file);
					System.out.println("inputDeleteRecord(): 1b - The record read is recid " + data.getRecID() + " "
							+ data.getToolType() + " " + data.getBrandName() + " " + data.getToolDesc() + " "
							+ data.getQuantity() + " " + data.getCost());
				} catch (IOException ex) {
					recID.setText("Error reading file");
				}
			}
		} else if (e.getSource() == delete) {
			theRecID = Integer.parseInt(recID.getText());

			for (int iii = 0; iii < pData.length; iii++) {
				if ((pData[iii][0]).equals("" + theRecID)) {
					theRecID = Integer.parseInt(pData[iii][0]);
					found = true;
					System.out.println("inputDeleteRecord(): 2 - The record id was found is  " + pData[iii][0]);
					break;
				}
			}

			try {

				System.out.println("inputDeleteRecord(): 3 - The data file is " + hwStore.getImportFile()
						+ "The record to be deleted is " + theRecID);
				file = new RandomAccessFile(hwStore.getImportFile(), "rw");
				data.setRecID(theRecID);

				hwStore.setEntries(hwStore.getEntries() - 1);
				System.out.println("inputDeleteRecord(): 4 - Go to the beginning of the file.");
				file.seek((0));
				file.seek((theRecID) * data.getSize());
				data.ReadRec(file);
				System.out.println("inputDeleteRecord(): 5 - Go to the record " + theRecID + " to be deleted.");
				data.setRecID(-1);
				System.out.println("inputDeleteRecord(): 6 - Write the deleted file to the file.");
				file.seek((0));
				file.seek((theRecID) * data.getSize());
				data.writeInteger(file, -1);

				System.out.println("inputDeleteRecord(): 7 - The number of entries is " + hwStore.getEntries());

				file.close();
			} catch (IOException ex) {
				recID.setText("Error writing file");
				return;
			}

			toCont = JOptionPane.showConfirmDialog(null, "Do you want to delete another record? \nChoose one",
					"Select Yes or No", JOptionPane.YES_NO_OPTION);

			if (toCont == JOptionPane.YES_OPTION) {
				recID.setText("");
			} else {
				delClear();
			}
		} else if (e.getSource() == cancel) {
			delClear();
		}
	}

	private void delClear() {
		try {
			System.out.println("inputDeleteRecord(): 3 - The data file is " + hwStore.getImportFile()
					+ "The record to be deleted is " + theRecID);
			file = new RandomAccessFile(hwStore.getImportFile(), "rw");

			hwStore.Redisplay(file, pData);
			file.close();
		} catch (IOException ex) {
			recID.setText("Error writing file");
			return;
		}
		setVisible(false);
		recID.setText("");
	}
}