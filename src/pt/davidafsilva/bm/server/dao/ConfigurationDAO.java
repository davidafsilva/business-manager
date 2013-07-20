package pt.davidafsilva.bm.server.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import pt.davidafsilva.bm.server.dao.interfaces.IConfigurationDAO;
import pt.davidafsilva.bm.server.db.Database;
import pt.davidafsilva.bm.server.db.Query;
import pt.davidafsilva.bm.server.db.ResultHandler;
import pt.davidafsilva.bm.shared.domain.Configuration;
import pt.davidafsilva.bm.shared.domain.User;


/**
 * ConfigurationDAO.java
 * 
 * The configuration data access object implementation
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 11:37:14 AM
 */
public class ConfigurationDAO implements IConfigurationDAO {
	
	// logger
	private final Logger log = Logger.getLogger(ConfigurationDAO.class);
	
	private final Database db = Database.getInstance();
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.dao.interfaces.IConfigurationDAO#getAll(int)
	 */
	@Override
	public List<Configuration> getAll(int userId) throws SQLException {
		List<Configuration> configs = null;
		Query query;
		String sql;
		
		log.debug("getAll(): getting all configurations of the user.. ");
		try {
			// build the query
			sql = "SELECT ";
			sql += "  * ";
			sql += "FROM ";
			sql += "  CONFIGURATION ";
			sql += "WHERE ";
			sql += "  USER_ID = :userId ";
			query = new Query(sql);
			
			// set the parameters
			query.setParameter("userId", userId);
			
			// execute the query
			configs = db.executeQueryResult(query, new ResultHandler<List<Configuration>>() {
				
				/* (non-Javadoc)
				 * @see pt.davidafsilva.bm.server.db.ResultHandler#handleResult(java.sql.ResultSet, boolean)
				 */
				@Override
				public List<Configuration> handleResult(ResultSet result, boolean hasResults)
						throws SQLException {
					ArrayList<Configuration> lst = new ArrayList<Configuration>();
					while (hasResults) {
						lst.add(buildEntityFromResultSet(result));
						hasResults = result.next();
					}
					
					return lst;
				}
				
			});
			
			log.debug("getAll(): get all configurations sucessfull");
		} catch (RuntimeException | SQLException e) {
			log.error("getAll(): error getting all configurations", e);
			throw e;
		}
		
		return configs;
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.dao.interfaces.IConfigurationDAO#update(pt.davidafsilva.bm.shared.domain.Configuration)
	 */
	@Override
	public void update(Configuration config) throws SQLException {
		Query query;
		String sql;
		
		log.debug("update(): updating the configuration.. ");
		try {
			// build the query
			sql = "UPDATE CONFIGURATION SET ";
			sql += "  VALUE = :value ";
			sql += "WHERE ";
			sql += "  ID    = :id";
			query = new Query(sql);
			
			// query parameters
			query.setParameter("value", config.getValue());
			query.setParameter("id", config.getId());
			
			// execute the query
			db.execute(query);
			
			log.debug("update(): configuration updated sucessfully");
		} catch (RuntimeException | SQLException e) {
			log.error("update(): error updating configuration", e);
			throw e;
		}
	}
	
	/**
	 * Builds a configuration from a result set
	 * 
	 * @param rs
	 *        The result set
	 * @return The product object
	 * @throws SQLException
	 */
	private Configuration buildEntityFromResultSet(ResultSet rs) throws SQLException {
		Configuration config = new Configuration();
		config.setId(rs.getInt("ID"));
		config.setDescription(rs.getString("DESCRIPTION"));
		config.setKey(rs.getString("KEY"));
		config.setValue(rs.getString("VALUE"));
		User user = new User();
		user.setId(rs.getInt("USER_ID"));
		config.setUser(user);
		return config;
	}
}
