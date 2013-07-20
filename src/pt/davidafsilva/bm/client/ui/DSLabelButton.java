package pt.davidafsilva.bm.client.ui;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.event.EventListenerList;
import pt.davidafsilva.bm.client.ui.event.MouseClickEvent;
import pt.davidafsilva.bm.client.ui.event.MouseClickEvent.MouseClickEventListener;


/**
 * DSLabelButton.java
 * 
 * The label button component
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 10:13:06 PM
 */
public class DSLabelButton extends JLabel {
	
	private static final long serialVersionUID = -3700921751678801395L;
	
	// the event subscribers
	private final EventListenerList eventSubscribers = new EventListenerList();
	
	/**
	 * Creates a label button instance with the specified
	 * text, image.
	 * The label is centered vertically in its display area.
	 * The text is on the trailing edge of the image.
	 * 
	 * @param text
	 *        The text to be displayed by the label.
	 * @param icon
	 *        The image to be displayed by the label.
	 */
	public DSLabelButton(String text, Icon icon) {
		setText(text);
		setIcon(icon);
		updateUI();
		setAlignmentX(LEFT_ALIGNMENT);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (isEnabled() && e.getButton() == MouseEvent.BUTTON1) {
					fireMouseClickEvent();
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
	}
	
	/**
	 * Creates a label button instance with the specified text.
	 * The label is aligned against the leading edge of its display area,
	 * and centered vertically.
	 * 
	 * @param text
	 *        The text to be displayed by the label.
	 */
	public DSLabelButton(String text) {
		this(text, null);
	}
	
	/**
	 * Creates a label button instance with the specified image.
	 * The label is centered vertically and horizontally
	 * in its display area.
	 * 
	 * @param image
	 *        The image to be displayed by the label.
	 */
	public DSLabelButton(Icon image) {
		this(null, image);
	}
	
	/**
	 * Creates a label button instance with
	 * no image and with an empty string for the title.
	 * The label is centered vertically
	 * in its display area.
	 * The label's contents, once set, will be displayed on the leading edge
	 * of the label's display area.
	 */
	public DSLabelButton() {
		this(null, null);
	}
	
	/**
	 * Subscribes the mouse click event
	 * 
	 * @param listener
	 *        The listener to register
	 */
	public void addMouseClickListener(MouseClickEventListener listener) {
		eventSubscribers.add(MouseClickEventListener.class, listener);
	}
	
	/**
	 * Unsubscribe the mouse click listener
	 * 
	 * @param listener
	 *        The listener to remove
	 */
	public void removeMouseClickListener(MouseClickEventListener listener) {
		eventSubscribers.remove(MouseClickEventListener.class, listener);
	}
	
	/**
	 * Fires the mouse click event
	 */
	private void fireMouseClickEvent() {
		MouseClickEvent evt = new MouseClickEvent(this);
		for (MouseClickEventListener listener : eventSubscribers.getListeners(MouseClickEventListener.class)) {
			listener.onMouseClick(evt);
		}
	}
}
