
package pt.davidafsilva.bm.shared.exception;


/**
 * InvalidAuthenticationCredentialsException.java
 * 
 * Exception thrown whenever a user tries to log in providing invalid
 * credentials (username and/or password)
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 11:10:27 PM
 */
public class InvalidAuthenticationCredentialsException extends Exception {
	
	
	private static final long serialVersionUID = 8384007329305202278L;
	
	
	/**
	 * Instantiates the exception
	 */
	public InvalidAuthenticationCredentialsException() {
		super();
	}
	
}
