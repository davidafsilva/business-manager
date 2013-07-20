package pt.davidafsilva.bm.server.service.interfaces;

import java.sql.SQLException;
import java.util.List;
import pt.davidafsilva.bm.server.service.ProductService;
import pt.davidafsilva.bm.shared.domain.Product;
import pt.davidafsilva.bm.shared.exception.ApplicationException;


/**
 * IProductService.java
 * 
 * The product service (business code) interface
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 1:05:19 PM
 */
public interface IProductService {
	
	/**
	 * Gets all the products
	 * 
	 * @throws SQLException
	 *         If any error occurs at the database level.
	 */
	List<Product> getAll();
	
	/**
	 * Saves the given product
	 * 
	 * @param product
	 *        The product to save
	 * @return <code>true</code> if the operation was successfully
	 *         executed, <code>false</code> otherwise.
	 * @throws ApplicationException
	 *         If there's already a product with the given code
	 */
	void save(Product product) throws ApplicationException;
	
	/**
	 * Updates the given product
	 * 
	 * @param product
	 *        The product to update
	 * @return <code>true</code> if the operation was successfully
	 *         executed, <code>false</code> otherwise.
	 * @throws ApplicationException
	 *         If there's already a product with the given code
	 */
	void update(Product product) throws ApplicationException;
	
	/**
	 * Deletes the given product
	 * 
	 * @param product
	 *        The product to delete
	 * @return <code>true</code> if the operation was successfully
	 *         executed, <code>false</code> otherwise.
	 * @throws ApplicationException
	 *         If the given product is already associated with a sale
	 */
	void delete(Product product) throws ApplicationException;
	
	
	/**
	 * Utility class for simplifying access to the instance of service.
	 */
	public static class Util {
		
		private static IProductService instance;
		
		public static IProductService getInstance() {
			if (instance == null) {
				instance = new ProductService();
			}
			return instance;
		}
	}
}
