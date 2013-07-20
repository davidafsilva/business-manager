package pt.davidafsilva.bm.client.ui;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import pt.davidafsilva.bm.client.ui.exception.InvalidAnimationParameterException;


/**
 * DSFrame.java
 * 
 * The frame dialog object
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 11:27:46 AM
 */
public class DSFrame extends JFrame {
	
	private static final long serialVersionUID = -3861613322864193728L;
	
	// the animation
	private DSAnimation animation;
	
	/**
	 * Default empty constructor
	 */
	public DSFrame() {
		this("");
	}
	
	/**
	 * Constructor
	 * 
	 * @param title
	 * 		The frame title
	 */
	public DSFrame(String title) {
		super(title);
		animation = new DSAnimation(this);
	}
	
	/**
	 * Animates the component
	 * 
	 * @param type
	 * 		The animation type to execute
	 */
	public void animate(final AnimationType type, final Object... params) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				switch (type) {
					case ERROR :
						animation.onError();
						break;
					case RESIZE :
						if (params.length != 1 && params[0] instanceof Dimension)
							throw new InvalidAnimationParameterException("Expected parameter 1 (idx 0) to be java.awt.Dimension.");
						animation.onResize((Dimension) params[0]);
						break;
				}
			}
		});
	}
}
