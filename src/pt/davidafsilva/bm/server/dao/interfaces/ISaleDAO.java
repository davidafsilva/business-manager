package pt.davidafsilva.bm.server.dao.interfaces;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import pt.davidafsilva.bm.server.dao.SaleDAO;
import pt.davidafsilva.bm.shared.domain.Client;
import pt.davidafsilva.bm.shared.domain.Sale;
import pt.davidafsilva.bm.shared.domain.User;


/**
 * ISaleDAO.java
 * 
 * The sale data access object interface
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 6:27:24 PM
 */
public interface ISaleDAO {
	
	/**
	 * Search for sales with the given filters
	 * 
	 * @param seller
	 *        The seller
	 * @param client
	 *        The client
	 * @param initialDate
	 *        The initial date (interval)
	 * @param finalDate
	 *        The final date (interval)
	 * @return The list with tall the sales that matches the filter criteria
	 * @throws SQLException
	 *         if any error occurs while querying the database
	 */
	List<Sale> searchSales(User seller, Client client, Date initialDate, Date finalDate) throws SQLException;
	
	/**
	 * Saves the given sale
	 * 
	 * @param sale
	 *        The sale to save
	 * @return <code>true</code> if the operation was successfully
	 *         executed, <code>false</code> otherwise.
	 * @throws SQLException
	 *         If there's an error while saving the sale to the database
	 */
	void save(Sale sale) throws SQLException;
	
	
	/**
	 * Marks a given sale as being sent by e-mail
	 * 
	 * @param saleId
	 *        The sale id to update
	 * @throws SQLException
	 *         If there's an error while updating the sale
	 */
	void setSentByEmail(long saleId) throws SQLException;
	
	/**
	 * Utility class for simplifying access to the instance of data access object.
	 */
	public static class Util {
		
		private static ISaleDAO instance;
		
		public static ISaleDAO getInstance() {
			if (instance == null) {
				instance = new SaleDAO();
			}
			return instance;
		}
	}
}
