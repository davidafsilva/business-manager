package pt.davidafsilva.bm.shared.exception;


/**
 * ApplicationException.java
 * 
 * The application exception class
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 2:49:17 PM
 */
public class ApplicationException extends Exception {
	
	private static final long serialVersionUID = -7675532829815733649L;
	
	/**
	 * The constructor
	 * 
	 * @param message
	 *        The exception message
	 */
	public ApplicationException(String message) {
		super(message);
	}
}
