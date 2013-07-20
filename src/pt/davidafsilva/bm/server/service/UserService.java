package pt.davidafsilva.bm.server.service;

import java.sql.SQLException;
import org.apache.log4j.Logger;
import pt.davidafsilva.bm.server.dao.interfaces.IUserDAO;
import pt.davidafsilva.bm.server.service.interfaces.IUserService;
import pt.davidafsilva.bm.shared.crypto.SHA;
import pt.davidafsilva.bm.shared.domain.User;
import pt.davidafsilva.bm.shared.exception.ApplicationException;
import pt.davidafsilva.bm.shared.exception.InvalidAuthenticationCredentialsException;


/**
 * UserService.java
 * 
 * The user service (business code) implementation
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 9:29:17 PM
 */
public class UserService implements IUserService {
	
	// logger
	private final Logger log = Logger.getLogger(UserService.class);
	
	private final IUserDAO repository = IUserDAO.Util.getInstance();
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.service.interfaces.IUserService#authenticate(java.lang.String, java.lang.String)
	 */
	@Override
	public User authenticate(String username, String password) throws InvalidAuthenticationCredentialsException {
		User user = null;
		
		log.debug("authenticate(): authenticating the user.. ");
		try {
			// hash the  the password
			String hash = SHA.compute(password);
			
			user = repository.authenticate(username, hash);
			if (user == null) {
				throw new InvalidAuthenticationCredentialsException();
			}
			
			log.debug("authenticate(): authenticated user sucessfully");
		} catch (RuntimeException | SQLException e) {
			log.error("authenticate(): error authenticating user", e);
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			}
			
			throw new RuntimeException(e);
		}
		
		return user;
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.service.interfaces.IUserService#changePassword(pt.davidafsilva.bm.shared.domain.User, java.lang.String, java.lang.String)
	 */
	@Override
	public void changePassword(User user, String currentPassword, String newPassword) throws ApplicationException {
		log.debug("changePassword(): changing user password.. ");
		try {
			try {
				authenticate(user.getUsername(), currentPassword);
			} catch (InvalidAuthenticationCredentialsException iace) {
				throw new ApplicationException("A password actual que introduziu estu00E1 incorrecta.");
			}
			
			String pwdHash = SHA.compute(newPassword);
			repository.updatePassword(user.getId(), pwdHash);
			user.setPassword(pwdHash);
			
			log.debug("changePassword(): changed user password sucessfully");
		} catch (RuntimeException | SQLException e) {
			log.error("changePassword(): error changing user password", e);
			throw new ApplicationException("Erro ao alterar a password do utilizador");
		}
	}
}
