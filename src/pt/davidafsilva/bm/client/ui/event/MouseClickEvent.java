package pt.davidafsilva.bm.client.ui.event;

import java.util.EventListener;
import java.util.EventObject;


/**
 * MouseClickEvent.java
 * @author David Silva <david@davidafsilva.pt>
 * @date 10:21:32 PM
 */
public class MouseClickEvent extends EventObject {
	
	private static final long serialVersionUID = -760431588345614589L;
	
	/**
	 * Creates the mouse click event with the given source
	 * @param source
	 * 		The source object of the event
	 */
	public MouseClickEvent(Object source) {
		super(source);
	}
	
	/**
	 * 
	 * MouseClickEventListener
	 * 
	 * The mouse click event listener
	 * 
	 * @author David Silva <david@davidafsilva.pt>
	 * @date 2:33:06 PM
	 */
	public interface MouseClickEventListener extends EventListener {
		
		/**
		 * The mouse click handler
		 * 
		 * @param evt
		 * 		The mouse click event
		 */
		public void onMouseClick(MouseClickEvent evt);
	}
	
}
