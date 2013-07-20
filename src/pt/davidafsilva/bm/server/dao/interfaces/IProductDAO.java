package pt.davidafsilva.bm.server.dao.interfaces;

import java.sql.SQLException;
import java.util.List;
import pt.davidafsilva.bm.server.dao.ProductDAO;
import pt.davidafsilva.bm.shared.domain.Product;


/**
 * IProductDAO.java
 * 
 * The product data access object interface
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 1:09:10 PM
 */
public interface IProductDAO {
	
	/**
	 * Gets the given product
	 * 
	 * @param id
	 *        The product id
	 * @throws SQLException
	 *         If any error occurs at the database level.
	 */
	Product get(int id) throws SQLException;
	
	/**
	 * Gets the given product
	 * 
	 * @param id
	 *        The id to skip
	 * @param code
	 *        The product code
	 * @throws SQLException
	 *         If any error occurs at the database level.
	 */
	Product get(int id, String code) throws SQLException;
	
	/**
	 * Gets all the products
	 * 
	 * @throws SQLException
	 *         If any error occurs at the database level.
	 */
	List<Product> getAll() throws SQLException;
	
	
	/**
	 * Saves the given product
	 * 
	 * @param product
	 *        The product to save
	 * @throws SQLException
	 *         If any error occurs at the database transaction level.
	 */
	void save(Product product) throws SQLException;
	
	/**
	 * Updates the given product
	 * 
	 * @param product
	 *        The product to update
	 * @throws SQLException
	 *         If any error occurs at the database transaction level.
	 */
	void update(Product product) throws SQLException;
	
	/**
	 * Deletes the given product
	 * 
	 * @param product
	 *        The product to delete
	 * @throws SQLException
	 *         If any error occurs at the database transaction level.
	 */
	void delete(Product product) throws SQLException;
	
	/**
	 * Utility class for simplifying access to the instance of data access object.
	 */
	public static class Util {
		
		private static IProductDAO instance;
		
		public static IProductDAO getInstance() {
			if (instance == null) {
				instance = new ProductDAO();
			}
			return instance;
		}
	}
}
