package pt.davidafsilva.bm.server.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import pt.davidafsilva.bm.server.dao.interfaces.IUserDAO;
import pt.davidafsilva.bm.server.db.Database;
import pt.davidafsilva.bm.server.db.Query;
import pt.davidafsilva.bm.server.db.ResultHandler;
import pt.davidafsilva.bm.shared.domain.User;


/**
 * UserDAO.java
 * 
 * The user data access object implementation.
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 11:16:25 PM
 */
public class UserDAO implements IUserDAO {
	
	// logger
	private final Logger log = Logger.getLogger(UserDAO.class);
	
	private final Database db = Database.getInstance();
	
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.dao.interfaces.IUserDAO#authenticate(java.lang.String, java.lang.String)
	 */
	@Override
	public User authenticate(String username, String password) throws SQLException {
		Query query;
		String sql;
		User user = null;
		
		log.debug("authenticate(): authenticating the user.. ");
		try {
			// build the query
			sql = "SELECT ";
			sql += "  ID, ";
			sql += "  USERNAME, ";
			sql += "  PASSWORD, ";
			sql += "  NAME, ";
			sql += "  CODE, ";
			sql += "  PERMISSION ";
			sql += "FROM ";
			sql += "  USER ";
			sql += "WHERE ";
			sql += "  USERNAME = :username AND ";
			sql += "  PASSWORD = :passwd ";
			query = new Query(sql);
			
			// query parameters
			query.setParameter("username", username);
			query.setParameter("passwd", password);
			
			// execute the query
			user = db.executeQueryResult(query, new ResultHandler<User>() {
				
				@Override
				public User handleResult(ResultSet result, boolean hasResults) throws SQLException {
					User usr = null;
					if (hasResults) {
						usr = new User();
						usr.setId(result.getInt("ID"));
						usr.setUsername(result.getString("USERNAME"));
						usr.setPassword(result.getString("PASSWORD"));
						usr.setName(result.getString("NAME"));
						usr.setCode(result.getString("CODE"));
						usr.setPermission(result.getInt("PERMISSION"));
					}
					
					return usr;
				}
			});
			
			log.debug("authenticate(): authenticated user sucessfully");
		} catch (RuntimeException | SQLException e) {
			log.error("authenticate(): error authenticating user", e);
			throw e;
		}
		
		return user;
	}
	
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.dao.interfaces.IUserDAO#updatePassword(int, java.lang.String)
	 */
	@Override
	public void updatePassword(int userId, String pwdHash) throws SQLException {
		Query query;
		String sql;
		
		log.debug("updatePassword(): updating the user password.. ");
		try {
			// build the query
			sql = "UPDATE USER SET ";
			sql += "  PASSWORD = :pwd ";
			sql += "WHERE ";
			sql += "  ID       = :id";
			query = new Query(sql);
			
			// query parameters
			query.setParameter("pwd", pwdHash);
			query.setParameter("id", userId);
			
			// execute the query
			db.execute(query);
			
			log.debug("updatePassword(): user password updated sucessfully");
		} catch (RuntimeException | SQLException e) {
			log.error("updatePassword(): error updating user password", e);
			throw e;
		}
	}
}
