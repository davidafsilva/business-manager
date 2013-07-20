package pt.davidafsilva.bm.client.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.ToolTipManager;


/**
 * DSToolTip.java
 * 
 * The custom tool tip component
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 1:06:05 PM
 */
public class DSToolTip extends JToolTip {
	
	private static final long serialVersionUID = 5351700178917318964L;
	
	// background color
	private final Color bc = new Color(0, 0, 0, 0.8F);
	
	// font color
	private final Color fc = new Color(1, 1, 1, 0.95F);
	
	/**
	 * Creates an empty tool tip
	 */
	public DSToolTip(JComponent component) {
		super();
		setOpaque(false);
		setBackground(bc);
		setForeground(fc);
		setComponent(component);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g) {
		String text = getComponent().getToolTipText();
		if (text != null && text.trim().length() > 0) {
			Graphics2D g2 = (Graphics2D) g.create();
			
			// clear the background
			g2.fillRect(0, 0, getWidth(), getHeight());
			g2.setComposite(AlphaComposite.SrcOver);
			
			// create a round rectangle
			Shape round = new RoundRectangle2D.Float(4, 1, this.getWidth() - 4, this.getHeight() - 2, 8, 8);
			
			// draw the background
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(getBackground());
			g2.fill(round);
			
			// draw the left triangle
			Point p1 = new Point(4, 7);
			Point p2 = new Point(4, 17);
			Point p3 = new Point(0, 12);
			int[] xs = {p1.x, p2.x, p3.x};
			int[] ys = {p1.y, p2.y, p3.y};
			Polygon triangle = new Polygon(xs, ys, xs.length);
			g2.fillPolygon(triangle);
			
			// draw the text
			int cHeight = getComponent().getHeight();
			FontMetrics fm = g2.getFontMetrics();
			g2.setColor(getForeground());
			if (cHeight > getHeight()) {
				g2.drawString(text, 10, (getHeight() + fm.getAscent()) / 2);
			} else {
				g2.drawString(text, 10, (cHeight + fm.getAscent()) / 2);
			}
			
			g2.dispose();
			g.dispose();
		} else {
			super.paintComponent(g);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize() {
		Dimension dim = super.getPreferredSize();
		return new Dimension((int) dim.getWidth() + 11, (int) dim.getHeight() + 4);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#addNotify()
	 */
	@Override
	public void addNotify() {
		super.addNotify();
		setOpaque(false);
		Container parent = getParent();
		if (parent != null) {
			if (parent instanceof JComponent) {
				JComponent dsparent = (JComponent) parent;
				dsparent.setOpaque(false);
			}
		}
	}
	
	/**
	 * Shows the tool tip
	 */
	public void showTooltip() {
		ToolTipManager.sharedInstance().mouseMoved(new MouseEvent(getComponent(), 0, 0, 0, 0, 0, 0, false));
	}
}
