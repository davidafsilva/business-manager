package pt.davidafsilva.bm.client.ui;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;


/**
 * DSTextField.java
 * 
 * The text field component
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 1:54:28 PM
 */
public class DSTextField extends JTextField implements IDSToolTip {
	
	private static final long serialVersionUID = -8508068783568040714L;
	
	// the tooltip
	private final DSToolTip tooltip;
	
	// error tool tip
	private boolean isErrorTooltip = false;
	private final DSErrorToolTip errorTooltip;
	private String errorTip = null;
	
	// the animation
	private final DSAnimation animation;
	private String allowedChars;
	
	/**
	 * Constructs a new TextField.
	 */
	public DSTextField() {
		this(0);
	}
	
	/**
	 * Constructs a new TextField.
	 * 
	 * @param columns
	 *        the number of columns to use to calculate
	 *        the preferred width; if columns is set to zero, the
	 *        preferred width will be whatever naturally results from
	 *        the component implementation
	 */
	public DSTextField(int columns) {
		super(columns);
		tooltip = new DSToolTip(this);
		errorTooltip = new DSErrorToolTip(this);
		animation = new DSAnimation(this);
		addKeyListener(new KeyListener() {
			
			private boolean skip = false;
			
			@Override
			public void keyTyped(KeyEvent e) {
				if (skip) {
					Toolkit.getDefaultToolkit().beep();
					e.consume();
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				skip = false;
				if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
						e.getKeyCode() == KeyEvent.VK_ESCAPE ||
						e.getKeyCode() == KeyEvent.VK_TAB ||
						e.getKeyCode() == KeyEvent.VK_ENTER) {
					return;
				}
				
				if (DSTextField.this.allowedChars != null) {
					if (!String.valueOf(e.getKeyChar()).matches(DSTextField.this.allowedChars)) {
						skip = true;
					}
				}
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#createToolTip()
	 */
	@Override
	public JToolTip createToolTip() {
		if (isErrorTooltip) {
			return errorTooltip;
		}
		return tooltip;
	}
	
	/**
	 * Registers the text to display in a error tool tip.
	 * 
	 * @param text
	 *        The error tool tip text
	 */
	@Override
	public void setErrorToolTipText(String text) {
		errorTip = text;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#getToolTipLocation(java.awt.event.MouseEvent)
	 */
	@Override
	public Point getToolTipLocation(MouseEvent event) {
		if (isErrorToolTip()) {
			return new Point(0, 0 - getHeight());
		}
		return new Point(getWidth(), 0);
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.client.ui.IDSToolTip#getErrorToolTipText()
	 */
	@Override
	public String getErrorToolTipText() {
		return errorTip;
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.client.ui.IDSToolTip#isErrorToolTip()
	 */
	@Override
	public boolean isErrorToolTip() {
		return isErrorTooltip;
	}
	
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.client.ui.IDSToolTip#setIsErrorToolTip(boolean)
	 */
	@Override
	public void setIsErrorToolTip(boolean status) {
		isErrorTooltip = status;
	}
	
	/**
	 * Animates the component
	 * 
	 * @param type
	 *        The animation type to execute
	 */
	public void animate(final AnimationType type) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				switch (type) {
					case ERROR:
						animation.onError();
						if (errorTip != null) {
							setIsErrorToolTip(true);
							errorTooltip.showTooltip();
						}
						requestFocus();
						break;
					case RESIZE:
						break;
				}
			}
		});
	}
	
	
	/**
	 * Sets the allowed chars to be accepted as the correct input for this instance.
	 * For example, a valid <code>allowedChars</code> definition would be: [0-9] or [a-zA-Z]
	 * 
	 * @param allowedChars
	 *        the allowed chars regular expression to set.
	 */
	public void setAllowedChars(String allowedChars) {
		this.allowedChars = allowedChars;
	}
}
