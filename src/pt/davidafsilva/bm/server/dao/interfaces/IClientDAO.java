package pt.davidafsilva.bm.server.dao.interfaces;

import java.sql.SQLException;
import java.util.List;
import pt.davidafsilva.bm.server.dao.ClientDAO;
import pt.davidafsilva.bm.shared.domain.Client;


/**
 * IClientDAO.java
 * 
 * The client data access object interface
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 11:56:47 AM
 */
public interface IClientDAO {
	
	/**
	 * Gets the given client
	 * 
	 * @param id
	 *        The client id
	 * @throws SQLException
	 *         If any error occurs at the database level.
	 */
	Client get(int id) throws SQLException;
	
	/**
	 * Gets the given client
	 * 
	 * @param id
	 *        The id to skip
	 * @param code
	 *        The client code
	 * @throws SQLException
	 *         If any error occurs at the database level.
	 */
	Client get(int id, String code) throws SQLException;
	
	/**
	 * Gets all the clients
	 * 
	 * @throws SQLException
	 *         If any error occurs at the database level.
	 */
	List<Client> getAll() throws SQLException;
	
	
	/**
	 * Saves the given client
	 * 
	 * @param client
	 *        The client to save
	 * @throws SQLException
	 *         If any error occurs at the database transaction level.
	 */
	void save(Client client) throws SQLException;
	
	/**
	 * Updates the given client
	 * 
	 * @param client
	 *        The client to update
	 * @throws SQLException
	 *         If any error occurs at the database transaction level.
	 */
	void update(Client client) throws SQLException;
	
	/**
	 * Deletes the given client
	 * 
	 * @param client
	 *        The client to delete
	 * @throws SQLException
	 *         If any error occurs at the database transaction level.
	 */
	void delete(Client client) throws SQLException;
	
	/**
	 * Utility class for simplifying access to the instance of data access object.
	 */
	public static class Util {
		
		private static IClientDAO instance;
		
		public static IClientDAO getInstance() {
			if (instance == null) {
				instance = new ClientDAO();
			}
			return instance;
		}
	}
}
