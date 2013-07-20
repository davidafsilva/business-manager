package pt.davidafsilva.bm.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.EventListenerList;
import pt.davidafsilva.bm.client.ui.event.ContentChangedEvent;
import pt.davidafsilva.bm.client.ui.event.ContentChangedEvent.ContentChangedEventListener;
import pt.davidafsilva.bm.client.ui.event.SelectionChangeEvent;
import pt.davidafsilva.bm.client.ui.event.SelectionChangeEvent.SelectionChangeEventListener;


/**
 * DSItem.java
 * 
 * The item object
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 11:53:57 AM
 */
public class DSItem extends JPanel {
	
	private static final long serialVersionUID = -2566325426822383962L;
	
	// the label
	private final JLabel label;
	
	// the image
	private final JLabel imageLabel;
	
	// selected flag
	private boolean selected = false;
	
	// the default selection color
	private Color selectedColor = new Color(0x9FB6CD);
	
	// the default on hover color
	private final Color hoverColor = new Color(0xC9C9C9);
	
	// the events subscribers
	protected EventListenerList eventSubscribers = new EventListenerList();
	
	// control flags
	private boolean mouseHover = false;
	
	// item contents
	private Container contents;
	
	// the contents constraints
	private Object constraints;
	
	/**
	 * Creates an empty label item
	 */
	public DSItem() {
		this(null);
	}
	
	/**
	 * Creates a label item
	 * 
	 * @param title
	 *        The title of the item
	 */
	public DSItem(String title) {
		this(title, null);
	}
	
	/**
	 * Creates a label item
	 * 
	 * @param title
	 *        The title of the item
	 * @param image
	 *        The image of the item
	 */
	public DSItem(String title, Icon image) {
		super(new BorderLayout());
		
		// preferred size
		setPreferredSize(new Dimension(90, 120));
		
		// set the cursor
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		// initialize the icon label
		imageLabel = new JLabel(image);
		imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		imageLabel.setBorder(new EmptyBorder(5, 0, 2, 0));
		add(imageLabel, BorderLayout.NORTH);
		
		// initializes the text label
		label = new JLabel(title);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		label.setForeground(Color.DARK_GRAY);
		add(label, BorderLayout.CENTER);
		
		// handle the mouse events
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (!selected) {
						setSelected(!selected);
					}
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				if (mouseHover) {
					mouseHover = false;
					repaint();
				}
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				if (!mouseHover) {
					mouseHover = true;
					repaint();
				}
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		
		setOpaque(false);
	}
	
	/**
	 * Sets the selection color
	 * 
	 * @param c
	 *        The selection color to set
	 */
	public void setSelectionColor(Color c) {
		selectedColor = c;
	}
	
	/**
	 * Gets the selection color
	 * 
	 * @return
	 *         The selection color
	 */
	public Color getSelectionColor() {
		return selectedColor;
	}
	
	/**
	 * Checks if the item is selected
	 * 
	 * @return
	 *         <code>true</code> if it's selected, <code>false</code> otherwise.
	 */
	public boolean isSelected() {
		return selected;
	}
	
	/**
	 * Selects or deselects the item
	 * 
	 * @param selected
	 *        The selection status
	 */
	public void setSelected(boolean selected) {
		boolean oldSelection = this.selected;
		this.selected = selected;
		
		if (oldSelection != selected) {
			fireSelectionChangeEvent();
			repaint();
		}
	}
	
	/**
	 * Subscribes the selection change event
	 * 
	 * @param listener
	 *        The listener to register
	 */
	public void addSelectionChangeListener(SelectionChangeEventListener listener) {
		eventSubscribers.add(SelectionChangeEventListener.class, listener);
	}
	
	/**
	 * Unsubscribe the selection change listener
	 * 
	 * @param listener
	 *        The listener to remove
	 */
	public void removeSelectionChangeListener(SelectionChangeEventListener listener) {
		eventSubscribers.remove(SelectionChangeEventListener.class, listener);
	}
	
	/**
	 * Subscribes the content changed event
	 * 
	 * @param listener
	 *        The listener to register
	 */
	public void addContentChangedListener(ContentChangedEventListener listener) {
		eventSubscribers.add(ContentChangedEventListener.class, listener);
	}
	
	/**
	 * Unsubscribe the content changed listener
	 * 
	 * @param listener
	 *        The listener to remove
	 */
	public void removeContentChangedListener(ContentChangedEventListener listener) {
		eventSubscribers.remove(ContentChangedEventListener.class, listener);
	}
	
	/**
	 * Fires the selection change event
	 */
	private void fireSelectionChangeEvent() {
		SelectionChangeEvent evt = new SelectionChangeEvent(this);
		for (SelectionChangeEventListener listener : eventSubscribers.getListeners(SelectionChangeEventListener.class)) {
			listener.onSelectionChange(evt);
		}
	}
	
	/**
	 * Fires the content changed event
	 */
	private void fireContentChangedEvent() {
		ContentChangedEvent evt = new ContentChangedEvent(this);
		for (ContentChangedEventListener listener : eventSubscribers.getListeners(ContentChangedEventListener.class)) {
			listener.onContentChanged(evt);
		}
	}
	
	/**
	 * Sets the item contents
	 * 
	 * @param contents
	 *        The item contents
	 */
	public void setContents(Container contents) {
		setContents(contents, null);
	}
	
	/**
	 * Sets the item contents
	 * 
	 * @param contents
	 *        The item contents
	 * @param constraints
	 *        An object expressing layout contraints for the contents
	 */
	public void setContents(Container contents, Object constraints) {
		if (this.contents != contents) {
			this.contents = contents;
			this.constraints = constraints;
			fireContentChangedEvent();
		}
	}
	
	/**
	 * Gets the item contents
	 * 
	 * @return
	 *         The item contents
	 */
	public Container getContents() {
		return contents;
	}
	
	/**
	 * An object expressing layout contraints for the contents
	 * 
	 * @return
	 *         The constraints
	 */
	public Object getConstraints() {
		return constraints;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Container#paintComponents(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g) {
		// create a round rectangle
		Shape round = new RoundRectangle2D.Float(2, 2, this.getWidth() - 2, this.getHeight() - 8, 8, 8);
		
		// draw the background
		Graphics2D g2 = (Graphics2D) g.create();
		g2.clearRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
		
		// draw the selection/hover highlight
		if (selected || mouseHover) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(selected ? selectedColor : hoverColor);
			g2.fill(round);
			
			// draw the triangle
			if (selected) {
				Point p1 = new Point((this.getWidth() - 2) / 2 - 10 + 2, this.getHeight() - 6);
				Point p2 = new Point((this.getWidth() - 2) / 2 + 10 - 2, this.getHeight() - 6);
				Point p3 = new Point((this.getWidth() - 2) / 2, this.getHeight());
				int[] xs = {p1.x, p2.x, p3.x};
				int[] ys = {p1.y, p2.y, p3.y};
				Polygon triangle = new Polygon(xs, ys, xs.length);
				g2.fillPolygon(triangle);
			}
		}
		
		super.paintComponents(g2);
		
		g2.dispose();
		g.dispose();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize() {
		Dimension dim = super.getPreferredSize();
		return new Dimension((int) dim.getWidth() + 15, (int) dim.getHeight() + 18);
	}
}
