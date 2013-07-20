package pt.davidafsilva.bm.client.ui;

import java.awt.BasicStroke;
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
 * DSErrorToolTip.java
 * 
 * The custom error tooltip component
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 11:54:08 AM
 */
public class DSErrorToolTip extends JToolTip {
	
	private static final long serialVersionUID = -6856356841508073363L;
	
	// background color
	private final Color bc = new Color(0.75F, 0, 0, 0.8F);
	
	// font color
	private final Color fc = new Color(1, 1, 1, 0.95F);
	
	/**
	 * Creates an empty tool tip
	 */
	public DSErrorToolTip(JComponent component) {
		super();
		setBackground(bc);
		setForeground(fc);
		setComponent(component);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g) {
		String text;
		if (getComponent() instanceof IDSToolTip && ((IDSToolTip) getComponent()).isErrorToolTip())
			text = ((IDSToolTip) getComponent()).getErrorToolTipText();
		else
			text = getComponent().getToolTipText();
		
		if (text != null && text.trim().length() > 0) {
			// create a round rectangle
			Shape round = new RoundRectangle2D.Float(1, 1, this.getWidth() - 2, this.getHeight() - 7, 8, 8);
			
			// draw the background
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(getBackground());
			g2.fill(round);
			
			// draw the border
			g2.setColor(new Color(0, 0, 0, 0.9F));
			g2.setStroke(new BasicStroke(1));
			g2.draw(round);
			
			// draw the left triangle
			Point p1 = new Point(10, (int) getPreferredSize().getHeight() - 5);
			Point p2 = new Point(22, (int) getPreferredSize().getHeight() - 5);
			Point p3 = new Point(16, (int) getPreferredSize().getHeight());
			int[] xs = {p1.x, p2.x, p3.x};
			int[] ys = {p1.y, p2.y, p3.y};
			Polygon triangle = new Polygon(xs, ys, xs.length);
			g2.fillPolygon(triangle);
			
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
			// draw the text
			int cHeight = getComponent().getHeight();
			FontMetrics fm = g2.getFontMetrics();
			g2.setColor(getForeground());
			if (cHeight > getHeight())
				g2.drawString(text, 10, (getHeight() - 7 + fm.getAscent()) / 2);
			else
				g2.drawString(text, 10, (cHeight - 7 + fm.getAscent()) / 2);
			
			g2.dispose();
			g.dispose();
		}
		
		if (getComponent() instanceof IDSToolTip && ((IDSToolTip) getComponent()).isErrorToolTip())
			((IDSToolTip) getComponent()).setIsErrorToolTip(false);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize() {
		Dimension dim = super.getPreferredSize();
		return new Dimension((int) dim.getWidth(), (int) dim.getHeight() + 7);
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
