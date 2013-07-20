package pt.davidafsilva.bm.client.ui.event;

import java.util.EventListener;
import java.util.EventObject;


/**
 * ContentRequestEvent.java
 * 
 * The content request event
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 2:31:56 PM
 */
public class ContentRequestEvent extends EventObject {
	
	private static final long serialVersionUID = -21435276353978341L;
	
	/**
	 * Creates the content request event with the given source
	 * 
	 * @param source
	 *        The source object of the event
	 */
	public ContentRequestEvent(Object source) {
		super(source);
	}
	
	/**
	 * 
	 * ContentRequestEventListener
	 * 
	 * The content request event listener
	 * 
	 * @author David Silva <david@davidafsilva.pt>
	 * @date 2:33:06 PM
	 */
	public interface ContentRequestEventListener extends EventListener {
		
		/**
		 * The content request handler
		 * 
		 * @param evt
		 *        The content request event
		 */
		public void onContentRequest(ContentRequestEvent evt);
	}
	
}
