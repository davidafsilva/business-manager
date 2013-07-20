package pt.davidafsilva.bm.server.service.interfaces;

import pt.davidafsilva.bm.server.service.UserService;
import pt.davidafsilva.bm.shared.domain.User;
import pt.davidafsilva.bm.shared.exception.ApplicationException;
import pt.davidafsilva.bm.shared.exception.InvalidAuthenticationCredentialsException;


/**
 * IUserService.java
 * 
 * The user service (business code) interface
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 9:27:42 PM
 */
public interface IUserService {
	
	/**
	 * Authenticate the user, given his login credentials.
	 * 
	 * @param username
	 *        The user name
	 * @param password
	 *        The user's password
	 * @return
	 *         The authenticated user
	 * @throws InvalidAuthenticationCredentialsException
	 *         Thrown if the credentials did not match any existent user.
	 */
	public User authenticate(String username, String password) throws InvalidAuthenticationCredentialsException;
	
	/**
	 * Changes the password for the given user
	 * 
	 * @param user
	 *        The user to be changed the password
	 * @param currentPassword
	 *        The current password
	 * @param newPassword
	 *        The new password
	 */
	public void changePassword(User user, String currentPassword, String newPassword)
			throws ApplicationException;
	
	/**
	 * Utility class for simplifying access to the instance of service.
	 */
	public static class Util {
		
		private static IUserService instance;
		
		public static IUserService getInstance() {
			if (instance == null) {
				instance = new UserService();
			}
			return instance;
		}
	}
}
