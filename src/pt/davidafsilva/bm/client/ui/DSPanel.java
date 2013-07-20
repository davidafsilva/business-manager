package pt.davidafsilva.bm.client.ui;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import pt.davidafsilva.bm.client.ui.interfaces.AnimationCompleteCallback;


/**
 * DSTranslucentPanel.java
 * 
 * The translucent panel implementation
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 7:09:22 PM
 */
public class DSPanel extends JPanel {
	
	private static final long serialVersionUID = -3064825178914125179L;
	
	// the opacity property
	private float opacity = 1;
	
	private BufferedImage image = null;
	
	// fade lock
	private final ReentrantLock lock = new ReentrantLock();
	
	// fade bools
	private boolean fadeInRunning;
	private boolean fadeOutRunning;
	
	/**
	 * Creates a new <code>DSPanel</code> with a double buffer
	 * and a flow layout.
	 */
	public DSPanel() {
		super(true);
	}
	
	/**
	 * Create a new buffered JPanel with the specified layout manager
	 * 
	 * @param manager
	 *        the LayoutManager to use
	 */
	public DSPanel(LayoutManager manager) {
		super(manager, true);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		if (image == null || image.getWidth() != getWidth() || image.getHeight() != getHeight()) {
			image = (BufferedImage) createImage(getWidth(), getHeight());
		}
		
		// clean the panel
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		// call the super
		Graphics2D g2 = image.createGraphics();
		g2.setClip(g.getClip());
		super.paint(g2);
		g2.dispose();
		
		// fill the alpha composite
		g2 = (Graphics2D) g;
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		
		// draw the image
		g2.drawImage(image, 0, 0, null);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	/**
	 * Sets the opacity level
	 * 
	 * @param opacity
	 *        The opacity to set
	 */
	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}
	
	/**
	 * Gets the current opacity level
	 * 
	 * @return
	 *         The opacity level
	 */
	public float getOpacity() {
		return opacity;
	}
	
	/**
	 * Fades in the component
	 */
	public void fadeIn() {
		fadeIn(null);
	}
	
	/**
	 * Fades in the component
	 * 
	 * @param callback
	 *        The callback handler
	 */
	public void fadeIn(final AnimationCompleteCallback callback) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				stopFadeIn();
				stopFadeOut();
				lock.lock();
				fadeInRunning = true;
				final Condition syncCondition = lock.newCondition();
				
				for (; opacity < 1f && fadeInRunning; setOpacity(opacity + 0.025f >= 1f ? 1f : opacity + 0.025f)) {
					if (SwingUtilities.isEventDispatchThread()) {
						update(getGraphics());
					} else {
						try {
							SwingUtilities.invokeAndWait(new Runnable() {
								
								@Override
								public void run() {
									update(getGraphics());
								}
							});
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					try {
						syncCondition.await(10, TimeUnit.MILLISECONDS);
					} catch (InterruptedException e) {
					}
				}
				
				if (opacity != 1) {
					opacity = 1;
					if (SwingUtilities.isEventDispatchThread()) {
						update(getGraphics());
					} else {
						try {
							SwingUtilities.invokeAndWait(new Runnable() {
								
								@Override
								public void run() {
									update(getGraphics());
								}
							});
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				
				fadeInRunning = false;
				
				if (callback != null) {
					callback.onAnimationComplete();
				}
				
				lock.unlock();
			}
		}).start();
	}
	
	/**
	 * Stops the current fade in effect
	 */
	public void stopFadeIn() {
		fadeInRunning = false;
	}
	
	/**
	 * Fades out the component
	 */
	public void fadeOut() {
		fadeOut(null);
	}
	
	/**
	 * Fades out the component
	 * 
	 * @param callback
	 *        The callback handler
	 */
	public void fadeOut(final AnimationCompleteCallback callback) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				stopFadeOut();
				stopFadeIn();
				lock.lock();
				fadeOutRunning = true;
				final Condition syncCondition = lock.newCondition();
				
				for (; opacity > 0 && fadeOutRunning; setOpacity(opacity - 0.025f <= 0 ? 0f : opacity - 0.025f)) {
					if (SwingUtilities.isEventDispatchThread()) {
						update(getGraphics());
					} else {
						try {
							SwingUtilities.invokeAndWait(new Runnable() {
								
								@Override
								public void run() {
									update(getGraphics());
								}
							});
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					try {
						syncCondition.await(10, TimeUnit.MILLISECONDS);
					} catch (InterruptedException e) {
					}
				}
				
				if (opacity != 0) {
					opacity = 0;
					if (SwingUtilities.isEventDispatchThread()) {
						update(getGraphics());
					} else {
						try {
							SwingUtilities.invokeAndWait(new Runnable() {
								
								@Override
								public void run() {
									update(getGraphics());
								}
							});
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				
				fadeOutRunning = false;
				
				if (callback != null) {
					callback.onAnimationComplete();
				}
				
				lock.unlock();
			}
		}).start();
	}
	
	/**
	 * Stops the current fade out effect
	 */
	public void stopFadeOut() {
		fadeOutRunning = false;
	}
	
	
}
