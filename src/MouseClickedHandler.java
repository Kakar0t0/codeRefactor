
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.RandomAccessFile;

import javax.swing.JOptionPane;
import javax.swing.JTable;

public class MouseClickedHandler extends MouseAdapter {
	JTable table;
	String pData[][], columnNames[];
	RandomAccessFile f;
	HardwareStore hws = new HardwareStore();

	/**
	 * @param fPassed
	 * @param tablePassed
	 * @param p_Data
	 */
	MouseClickedHandler(RandomAccessFile fPassed, JTable tablePassed, String p_Data[][]) {
		table = tablePassed;
		pData = p_Data;
		f = fPassed;
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == table) {
			int ii = table.getSelectedRow();
			JOptionPane.showMessageDialog(null, "Enter the record ID to be inputUpdateRecordRecordd and press enter.",
					"inputUpdateRecordRecord Record", JOptionPane.INFORMATION_MESSAGE);
			InputUpdateRecordRecordRec inputUpdateRecordRecord = new InputUpdateRecordRecordRec(hws, f, pData, ii);
			if (ii < 250) {
				inputUpdateRecordRecord.setVisible(true);
				table.repaint();
			}
		}
	}
}