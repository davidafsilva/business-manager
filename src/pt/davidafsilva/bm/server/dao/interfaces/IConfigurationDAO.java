package pt.davidafsilva.bm.server.dao.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import pt.davidafsilva.bm.server.dao.ConfigurationDAO;
import pt.davidafsilva.bm.shared.domain.Configuration;


/**
 * IConfigurationDAO.java
 * 
 * The configuration data access object interface
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 11:26:23 AM
 */
public interface IConfigurationDAO {
	
	/**
	 * Gets the current configuration of the given user
	 * 
	 * @param userId
	 *        The user id
	 * @return A {@link Map} with keys mapped to the configuration itself
	 * @throws SQLException
	 *         if any error occurs while querying the database
	 */
	List<Configuration> getAll(int userId) throws SQLException;
	
	/**
	 * Updates the given configuration
	 * 
	 * @param config
	 *        The configuration
	 * @throws SQLException
	 *         if any error occurs while updating the database
	 */
	void update(Configuration config) throws SQLException;
	
	/**
	 * Utility class for simplifying access to the instance of data access object.
	 */
	public static class Util {
		
		private static IConfigurationDAO instance;
		
		public static IConfigurationDAO getInstance() {
			if (instance == null) {
				instance = new ConfigurationDAO();
			}
			return instance;
		}
	}
}
