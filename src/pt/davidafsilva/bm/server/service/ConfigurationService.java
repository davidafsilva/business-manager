package pt.davidafsilva.bm.server.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import pt.davidafsilva.bm.server.dao.interfaces.IConfigurationDAO;
import pt.davidafsilva.bm.server.service.interfaces.IConfigurationService;
import pt.davidafsilva.bm.shared.domain.Configuration;
import pt.davidafsilva.bm.shared.exception.ApplicationException;


/**
 * ConfigurationService.java
 * 
 * The configuration service implementation
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 6:50:59 PM
 */
public class ConfigurationService implements IConfigurationService {
	
	// logger
	private final Logger log = Logger.getLogger(ConfigurationService.class);
	
	private final IConfigurationDAO configurationDAO = IConfigurationDAO.Util.getInstance();
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.service.interfaces.IConfigurationService#getUserConfiguration(int)
	 */
	@Override
	public Map<String, Configuration> getUserConfiguration(int userId) throws ApplicationException {
		Map<String, Configuration> map = null;
		log.debug("getUserConfiguration(): getting the user configurations.. ");
		try {
			List<Configuration> configs = configurationDAO.getAll(userId);
			map = new HashMap<String, Configuration>(configs.size());
			for (Configuration config : configs) {
				map.put(config.getKey(), config);
			}
			
			log.debug("getUserConfiguration(): getting the user configuration sucessfully");
		} catch (RuntimeException | SQLException e) {
			log.error("getUserConfiguration(): error getting user configuration ", e);
			throw new ApplicationException("Ocorreu um erro a obter as configura\u00E7\u00F5es do utilizador.");
		}
		
		return map;
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.service.interfaces.IConfigurationService#update
	 * (pt.davidafsilva.bm.shared.domain.Configuration)
	 */
	@Override
	public void update(List<Configuration> configs) throws ApplicationException {
		log.debug("update(): updating the configuration.. ");
		try {
			for (Configuration config : configs) {
				configurationDAO.update(config);
			}
			
			log.debug("update(): configuration updated sucessfully");
		} catch (RuntimeException | SQLException e) {
			log.error("update(): error updating configuration", e);
			throw new ApplicationException("Ocorreu um erro a actualizar a configura\u00E7\u00E3o.");
		}
	}
	
	
}
