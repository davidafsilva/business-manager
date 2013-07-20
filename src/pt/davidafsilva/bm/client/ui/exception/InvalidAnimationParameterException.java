package pt.davidafsilva.bm.client.ui.exception;


/**
 * InvalidAnimationParameterException.java
 * 
 * The invalid animation parameters exception.
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 1:22:18 PM
 */
public class InvalidAnimationParameterException extends RuntimeException {
	
	private static final long serialVersionUID = 4039525350148612465L;
	
	/**
	 * Creates the exception.
	 * 
	 * @param message
	 * 		The error message
	 */
	public InvalidAnimationParameterException(String message) {
		super(message);
	}
	
}
