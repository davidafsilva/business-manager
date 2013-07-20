package pt.davidafsilva.bm.server.service.interfaces;

import java.util.List;
import java.util.Map;
import pt.davidafsilva.bm.server.service.ConfigurationService;
import pt.davidafsilva.bm.shared.domain.Configuration;
import pt.davidafsilva.bm.shared.exception.ApplicationException;


/**
 * IConfigurationService.java
 * 
 * The configuration service interface
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 6:52:53 PM
 */
public interface IConfigurationService {
	
	/**
	 * Gets the current configuration of the given user
	 * 
	 * @param userId
	 *        The user id
	 * @return A {@link Map} with keys mapped to the configuration itself
	 * @throws ApplicationException
	 *         If any error occurs while querying the database
	 */
	Map<String, Configuration> getUserConfiguration(int userId) throws ApplicationException;
	
	/**
	 * Updates the given configuration
	 * 
	 * @param configs
	 *        The configurations to update
	 * @throws ApplicationException
	 *         If any error occurs while updating the configuration
	 */
	void update(List<Configuration> configs) throws ApplicationException;
	
	/**
	 * Utility class for simplifying access to the instance of service.
	 */
	public static class Util {
		
		private static IConfigurationService instance;
		
		public static IConfigurationService getInstance() {
			if (instance == null) {
				instance = new ConfigurationService();
			}
			return instance;
		}
	}
}
