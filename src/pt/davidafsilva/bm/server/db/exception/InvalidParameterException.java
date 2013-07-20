package pt.davidafsilva.bm.server.db.exception;



/**
 * InvalidParameterException.java
 * 
 * This exception is thrown when a users tries to set a parameter
 * with an invalid key.
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 9:42:41 PM
 */
public class InvalidParameterException extends RuntimeException {
	
	private static final long serialVersionUID = 7060283121233963799L;
	
	/**
	 * Instantiate the exception with the parameter <b>parameter</b>
	 * @param parameter
	 * 		The query parameter that caused the exception
	 */
	public InvalidParameterException(String parameter) {
		super(parameter);
	}
}
