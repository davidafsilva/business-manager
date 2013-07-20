package pt.davidafsilva.bm.client.ui.event;

import java.util.EventListener;
import java.util.EventObject;


/**
 * SelectionChangeEvent.java
 * 
 * The selection change event
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 2:31:56 PM
 */
public class SelectionChangeEvent extends EventObject {
	
	private static final long serialVersionUID = -760431588345614589L;
	
	/**
	 * Creates the selection change event with the given source
	 * @param source
	 * 		The source object of the event
	 */
	public SelectionChangeEvent(Object source) {
		super(source);
	}
	
	/**
	 * 
	 * SelectionChangeEventListener
	 * 
	 * The selection change event listener
	 * 
	 * @author David Silva <david@davidafsilva.pt>
	 * @date 2:33:06 PM
	 */
	public interface SelectionChangeEventListener extends EventListener {
		
		/**
		 * The selection change handler
		 * 
		 * @param evt
		 * 		The selection change event
		 */
		public void onSelectionChange(SelectionChangeEvent evt);
	}
	
}
