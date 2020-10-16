import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class WindowHandler extends WindowAdapter {
	HardwareStore h;

	/**
	 * @param s
	 */
	public WindowHandler(HardwareStore s) {
		h = s;
	}

	public void windowClosing(WindowEvent e) {
		h.cleanup();
	}
}