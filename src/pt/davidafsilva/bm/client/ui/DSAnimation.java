package pt.davidafsilva.bm.client.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;


/**
 * DSAnimation.java
 * 
 * Implements the UI animations functions
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 9:34:58 AM
 */
public class DSAnimation {
	
	// animation going on
	private boolean[] animationRunning;
	
	// component
	private Component component;
	
	// lock
	private ReentrantLock[] lock;
	
	/**
	 * Initializes the animation for the specific component
	 * 
	 * @param component
	 * 		The component where to apply the animation
	 */
	public DSAnimation(Component component) {
		this.component = component;
		this.animationRunning = new boolean[2];
		this.lock = new ReentrantLock[2];
		for (int i = 0; i < lock.length; i++)
			this.lock[i] = new ReentrantLock();
	}
	
	/**
	 * Starts the error animation
	 */
	public void onError() {
		if (animationRunning[0])
			animationRunning[0] = false;
		
		lock[0].lock();
		animationRunning[0] = true;
		final Condition syncCondition = lock[0].newCondition();
		
		Border border = null;
		if (component instanceof JComponent) {
			border = ((JComponent) component).getBorder();
			
			// create a thin red border around the component
			((JComponent) component).setBorder(BorderFactory.createLineBorder(Color.red, 1));
		}
		
		int x = component.getBounds().x, y = component.getBounds().y;
		// shake the component
		for (int i = 0; i < 10 && animationRunning[0]; i++) {
			if (i % 2 == 0)
				component.setLocation(x - 2, y);
			else
				component.setLocation(x + 2, y);
			
			component.update(component.getGraphics());
			
			try {
				syncCondition.await(75, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
			}
		}
		
		// restores the location
		component.setLocation(x, y);
		
		// restores the border
		if (component instanceof JComponent) {
			((JComponent) component).setBorder(border);
		}
		component.update(component.getGraphics());
		
		animationRunning[0] = false;
		lock[0].unlock();
	}
	
	/**
	 * Starts the resize animation
	 * 
	 * @param newSize
	 * 		The new size of the component
	 */
	public void onResize(final Dimension newSize) {
		if (animationRunning[1])
			animationRunning[1] = false;
		
		lock[1].lock();
		animationRunning[1] = true;
		final Condition syncCondition = lock[1].newCondition();
		
		final Rectangle bounds = component.getBounds();
		int widthOffset = bounds.width - newSize.width;
		int heightOffset = bounds.height - newSize.height;
		while ((widthOffset != 0 || heightOffset != 0) && animationRunning[1]) {
			if (widthOffset != 0) {
				widthOffset = widthOffset > 0 ? widthOffset - 1 : widthOffset < 0 ? widthOffset + 1 : 0;
			}
			
			if (heightOffset != 0) {
				heightOffset = heightOffset > 0 ? heightOffset - 1 : heightOffset < 0 ? heightOffset + 1 : 0;
			}
			
			component.setSize(newSize.width + widthOffset, newSize.height + heightOffset);
			component.repaint(component.getX(), component.getY(), newSize.width + widthOffset, newSize.height + heightOffset);
			try {
				syncCondition.await(1, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
			}
		}
		
		if (widthOffset != 0 || heightOffset != 0) {
			component.setSize(newSize.width, newSize.height);
			component.repaint(component.getX(), component.getY(), newSize.width, newSize.height);
		}
		
		animationRunning[1] = false;
		lock[1].unlock();
	}
}
