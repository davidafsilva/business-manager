package pt.davidafsilva.bm.server.service.interfaces;

import java.sql.SQLException;
import java.util.List;
import pt.davidafsilva.bm.server.service.ClientService;
import pt.davidafsilva.bm.shared.domain.Client;
import pt.davidafsilva.bm.shared.exception.ApplicationException;


/**
 * IClientService.java
 * 
 * The client service (business code) interface
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 11:52:37 AM
 */
public interface IClientService {
	
	/**
	 * Gets all the clients
	 * 
	 * @throws SQLException
	 *         If any error occurs at the database level.
	 */
	List<Client> getAll();
	
	/**
	 * Saves the given client
	 * 
	 * @param client
	 *        The client to save
	 * @return <code>true</code> if the operation was successfully
	 *         executed, <code>false</code> otherwise.
	 * @throws ApplicationException
	 *         If there's already a client with the given code
	 */
	void save(Client client) throws ApplicationException;
	
	/**
	 * Updates the given client
	 * 
	 * @param client
	 *        The client to update
	 * @return <code>true</code> if the operation was successfully
	 *         executed, <code>false</code> otherwise.
	 * @throws ApplicationException
	 *         If there's already a client with the given code
	 */
	void update(Client client) throws ApplicationException;
	
	/**
	 * Deletes the given client
	 * 
	 * @param client
	 *        The client to delete
	 * @return <code>true</code> if the operation was successfully
	 *         executed, <code>false</code> otherwise.
	 * @throws ApplicationException
	 *         If the given client is already associated with a sale
	 */
	void delete(Client client) throws ApplicationException;
	
	
	/**
	 * Utility class for simplifying access to the instance of service.
	 */
	public static class Util {
		
		private static IClientService instance;
		
		public static IClientService getInstance() {
			if (instance == null) {
				instance = new ClientService();
			}
			return instance;
		}
	}
}
