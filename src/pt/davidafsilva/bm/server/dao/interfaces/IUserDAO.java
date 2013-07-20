package pt.davidafsilva.bm.server.dao.interfaces;

import java.sql.SQLException;
import pt.davidafsilva.bm.server.dao.UserDAO;
import pt.davidafsilva.bm.shared.domain.User;
import pt.davidafsilva.bm.shared.exception.InvalidAuthenticationCredentialsException;


/**
 * IUserDAO.java
 * 
 * The user data access object interface.
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 11:04:03 PM
 */
public interface IUserDAO {
	
	
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
	 * @throws SQLException
	 *         If an error occurs while executing the query
	 */
	User authenticate(String username, String password) throws SQLException;
	
	/**
	 * Updates the user password
	 * 
	 * @param userID
	 *        the user identifier
	 * @param pwdHash
	 *        The new password hash
	 * @throws SQLException
	 *         If an error occurs while executing the query
	 */
	void updatePassword(int userId, String pwdHash) throws SQLException;
	
	/**
	 * Utility class for simplifying access to the instance of data access object.
	 */
	public static class Util {
		
		private static IUserDAO instance;
		
		public static IUserDAO getInstance() {
			if (instance == null) {
				instance = new UserDAO();
			}
			return instance;
		}
	}
}
