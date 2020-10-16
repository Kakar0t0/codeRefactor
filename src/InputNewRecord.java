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

public class InputNewRecord extends Dialog implements ActionListener {

	private RandomAccessFile file;
	private JTextField recID, toolType, brandName, toolDesc, partNum, quantity, price;
	private JLabel recIDLabel, toolTypeLabel, brandNameLabel, toolDescLabel, partNumLabel, quantityLabel, priceLabel;
	private JButton cancel, save;
	private Record data;
	private int theRecID, toCont, fileLen;
	private JTable table;
	private String pData[][];
	private String columnNames[] = { "Record ID", "Type of tool", "Brand Name", "Tool Description", "partNum",
			"Quantity", "Price" };
	private HardwareStore hwStore;
	private boolean found = true;

	/**
	 * @param hw_store
	 * @param f
	 * @param tab
	 * @param p_Data
	 */
	public InputNewRecord(HardwareStore hw_store, RandomAccessFile f, JTable tab, String p_Data[][]) {

		super(new Frame(), "New Record", true);

		file = f;
		table = tab;
		pData = p_Data;
		hwStore = hw_store;

		newSetup();
	}

	public void newSetup() {
		setSize(400, 250);
		setLayout(new GridLayout(9, 2));

		recID = new JTextField(10);
		recID.setEnabled(false);
		try {
			file = new RandomAccessFile(hwStore.getImportFile(), "rw");
			file.seek(0);
			fileLen = (int) file.length() / data.getSize();
			recID.setText("" + fileLen);
		} catch (IOException ex) {
			partNum.setText("Error reading file");
		}
		toolType = new JTextField(10);
		brandName = new JTextField(10);
		toolDesc = new JTextField(10);
		partNum = new JTextField(10);
		quantity = new JTextField(10);
		price = new JTextField(10);
		recIDLabel = new JLabel("Record ID");
		toolTypeLabel = new JLabel("Type of Tool");
		brandNameLabel = new JLabel("Brand Name");
		toolDescLabel = new JLabel("Tool Description");
		partNumLabel = new JLabel("Part Number");
		quantityLabel = new JLabel("Quantity");
		priceLabel = new JLabel("Price");
		save = new JButton("Save Changes");
		cancel = new JButton("Cancel");

		recID.addActionListener(this);
		save.addActionListener(this);
		cancel.addActionListener(this);

		add(recIDLabel);
		add(recID);
		add(toolTypeLabel);
		add(toolType);
		add(brandNameLabel);
		add(brandName);
		add(toolDescLabel);
		add(toolDesc);
		add(partNumLabel);
		add(partNum);
		add(quantityLabel);
		add(quantity);
		add(priceLabel);
		add(price);
		add(save);
		add(cancel);

		data = new Record();
		JOptionPane.showMessageDialog(null, "The recID field is currently set to the next record ID.\n"
				+ "Please just fill in the " + "remaining fields.", "RecID To Be Entered",
				JOptionPane.INFORMATION_MESSAGE);

	}

	public void actionPerformed(ActionEvent e) {
		try {
			file = new RandomAccessFile(hwStore.getImportFile(), "rw");
			file.seek(0);
			fileLen = (int) file.length() / data.getSize();
			recID.setText("" + fileLen);
		} catch (IOException ex) {
			partNum.setText("Error reading file");
		}

		if (e.getSource() == recID) {
			recID.setEnabled(false);
		} else if (e.getSource() == save) {
			if (recID.getText().equals("")) {
			} else {
				try {
					data.setRecID(Integer.parseInt(recID.getText()));
					data.setToolType(toolType.getText().trim());
					data.setBrandName(brandName.getText().trim());
					data.setToolDesc(toolDesc.getText().trim());
					data.setPartNumber(partNum.getText().trim());
					data.setQuantity(Integer.parseInt(quantity.getText()));
					data.setCost(price.getText().trim());
					file.seek(0);
					file.seek((data.getRecID()) * data.getSize());
					data.write(file);

					theRecID = hwStore.getEntries();
					hwStore.sysPrint("inputNewRecord 1: The numbers of entries is " + (theRecID - 1));

					hwStore.sysPrint("inputNewRecord 2: A new record is being added at " + theRecID);
					pData[theRecID][0] = Integer.toString(data.getRecID());
					pData[theRecID][1] = data.getToolType().trim();
					pData[theRecID][2] = data.getBrandName().trim();
					pData[theRecID][3] = data.getToolDesc().trim();
					pData[theRecID][4] = data.getPartNumber().trim();
					pData[theRecID][5] = Integer.toString(data.getQuantity());
					pData[theRecID][6] = data.getCost().trim();
					table = new JTable(pData, columnNames);
					table.repaint();
					hwStore.setEntries(hwStore.getEntries() + 1);
				} catch (IOException ex) {
					partNum.setText("Error writing file");
					return;
				}
			}

			toCont = JOptionPane.showConfirmDialog(null, "Do you want to add another record? \nChoose one",
					"Choose one", JOptionPane.YES_NO_OPTION);

			if (toCont == JOptionPane.YES_OPTION) {
				recID.setText("");
				toolType.setText("");
				quantity.setText("");
				brandName.setText("");
				toolDesc.setText("");
				partNum.setText("");
				price.setText("");
			} else {
				newClear();
			}
		} else if (e.getSource() == cancel) {
			newClear();
		}
	}

	private void newClear() {
		partNum.setText("");
		toolType.setText("");
		quantity.setText("");
		price.setText("");
		setVisible(false);
	}
}