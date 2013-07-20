package pt.davidafsilva.bm.client.ui.event;

import java.util.EventListener;
import java.util.EventObject;


/**
 * DataRequestEvent.java
 * 
 * The data request event
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 10:43:17 PM
 */
public class DataRequestEvent extends EventObject {
	
	private static final long serialVersionUID = 6518400612259771286L;
	
	/**
	 * Creates the data request event with the given source
	 * @param source
	 * 		The source object of the event
	 */
	public DataRequestEvent(Object source) {
		super(source);
	}
	
	/**
	 * 
	 * DataRequestEventListener
	 * 
	 * The data request event listener
	 * 
	 * @author David Silva <david@davidafsilva.pt>
	 * @date 10:43:17 PM
	 */
	public interface DataRequestEventListener extends EventListener {
		
		/**
		 * The data request handler
		 * 
		 * @param evt
		 * 		The data request event
		 */
		public void onDataRequest(DataRequestEvent evt);
	}
}
