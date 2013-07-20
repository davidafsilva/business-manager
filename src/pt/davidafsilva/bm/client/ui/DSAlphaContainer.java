package pt.davidafsilva.bm.client.ui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import javax.swing.JComponent;


/**
 *  DSAlphaContainer.java
 *
 *  A wrapper Container for holding components that use a background Color
 *  containing an alpha value with some transparency.
 *
 *  A Component that uses a transparent background should really have its
 *  opaque property set to false so that the area it occupies is first painted
 *  by its opaque ancestor (to make sure no painting artifacts exist). However,
 *  if the property is set to false, then most Swing components will not paint
 *  the background at all, so you lose the transparent background Color.
 *
 *  This components attempts to get around this problem by doing the
 *  background painting on behalf of its contained Component, using the
 *  background Color of the Component.
 *
 * @author David Silva <david@davidafsilva.pt>
 * @date 9:23:23 PM
 */
public class DSAlphaContainer extends JComponent {
	
	private static final long serialVersionUID = 8129020750910317493L;
	private JComponent component;
	
	/**
	 * Constructor
	 * @param component
	 * 		The child component
	 */
	public DSAlphaContainer(JComponent component) {
		setLayout(new BorderLayout());
		setOpaque(false);
		setComponent(component);
	}
	
	
	/**
	 * Sets the component
	 *
	 * @param component
	 * 		The component to set
	 */
	public void setComponent(JComponent component) {
		this.component = component;
		component.setOpaque(false);
		add(component);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(component.getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponents(g);
	}
}
