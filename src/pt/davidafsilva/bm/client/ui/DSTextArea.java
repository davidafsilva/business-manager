package pt.davidafsilva.bm.client.ui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JTextArea;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;


/**
 * DSTextArea.java
 * 
 * The text area component
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 11:07:09 AM
 */
public class DSTextArea extends JTextArea implements IDSToolTip {
	
	private static final long serialVersionUID = 2345948396835190454L;
	
	// the tooltip
	private final DSToolTip tooltip;
	
	// error tool tip
	private boolean isErrorTooltip = false;
	private final DSErrorToolTip errorTooltip;
	private String errorTip = null;
	
	// the animation
	private final DSAnimation animation;
	
	/**
	 * Constructs a new Text area.
	 */
	public DSTextArea() {
		tooltip = new DSToolTip(this);
		errorTooltip = new DSErrorToolTip(this);
		animation = new DSAnimation(this);
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
}
