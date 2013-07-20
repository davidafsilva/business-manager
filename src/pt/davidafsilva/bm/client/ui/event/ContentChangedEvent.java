package pt.davidafsilva.bm.client.ui.event;

import java.util.EventListener;
import java.util.EventObject;


/**
 * ContentChangedEvent.java
 * 
 * The content changed event
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 2:31:56 PM
 */
public class ContentChangedEvent extends EventObject {
	
	private static final long serialVersionUID = -21435276353978341L;
	
	/**
	 * Creates the content changed event with the given source
	 * 
	 * @param source
	 *        The source object of the event
	 */
	public ContentChangedEvent(Object source) {
		super(source);
	}
	
	/**
	 * 
	 * ContentChangedEventListener
	 * 
	 * The content changed event listener
	 * 
	 * @author David Silva <david@davidafsilva.pt>
	 * @date 2:33:06 PM
	 */
	public interface ContentChangedEventListener extends EventListener {
		
		/**
		 * The content changed handler
		 * 
		 * @param evt
		 *        The content changed event
		 */
		public void onContentChanged(ContentChangedEvent evt);
	}
	
}
