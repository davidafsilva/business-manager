package pt.davidafsilva.bm.client.ui;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import pt.davidafsilva.bm.client.enums.ImagesEnum;
import pt.davidafsilva.bm.client.view.BaseView;


/**
 * DSDialog.java
 * 
 * The dialog functions
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 7:04:25 PM
 */
public class DSDialog {
	
	/**
	 * Shows an information message.
	 * 
	 * @param parent
	 *        The parents window
	 * @param msg
	 *        The message to display
	 * @param title
	 *        The window title
	 */
	public static void info(BaseView<?> parent, String msg, String title) {
		JOptionPane.showMessageDialog(parent, msg, title, JOptionPane.INFORMATION_MESSAGE, new ImageIcon(ImagesEnum.INFORMATION.getPath()));
	}
	
	/**
	 * Shows an error message.
	 * 
	 * @param parent
	 *        The parents window
	 * @param msg
	 *        The message to display
	 * @param title
	 *        The window title
	 */
	public static void error(BaseView<?> parent, String msg, String title) {
		JOptionPane.showMessageDialog(parent, msg, title, JOptionPane.ERROR_MESSAGE, new ImageIcon(ImagesEnum.ERROR.getPath()));
	}
	
	/**
	 * Asks a question.
	 * 
	 * @param parent
	 *        The parent window
	 * @param msg
	 *        The question to display
	 * @param title
	 *        The window title
	 * @return
	 *         <code>true</code> if the answer is positive, <code>false</code> otherwise.
	 */
	public static boolean question(BaseView<?> parent, String msg, String title) {
		return JOptionPane.showConfirmDialog(parent, msg, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(ImagesEnum.QUESTION.getPath()))
				== JOptionPane.YES_OPTION;
	}
}
