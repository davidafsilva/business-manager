package pt.davidafsilva.bm.server.service.interfaces;

import java.io.File;
import java.util.Date;
import java.util.List;
import pt.davidafsilva.bm.server.service.SaleService;
import pt.davidafsilva.bm.shared.domain.Client;
import pt.davidafsilva.bm.shared.domain.Sale;
import pt.davidafsilva.bm.shared.domain.User;
import pt.davidafsilva.bm.shared.exception.ApplicationException;


/**
 * ISaleService.java
 * 
 * The sale service (business code) interface
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 6:13:52 PM
 */
public interface ISaleService {
	
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
	 */
	List<Sale> searchSales(User seller, Client client, Date initialDate, Date finalDate);
	
	/**
	 * Saves the given sale
	 * 
	 * @param sale
	 *        The sale to save
	 * @throws ApplicationException
	 *         If there's an error while saving the sale
	 */
	void save(Sale sale) throws ApplicationException;
	
	/**
	 * Marks the given sale has sent by e-mail
	 * 
	 * @param sale
	 *        The sale to update
	 * @throws ApplicationException
	 *         If there's an error while updating the sale
	 */
	void markSaleHasSentByEmail(Sale sale) throws ApplicationException;
	
	/**
	 * Generates the sale report of the given sale
	 * 
	 * @param user
	 *        The user
	 * @param report
	 *        The file pointer
	 * @param sale
	 *        The sale to print
	 * @return The report document
	 * @throws ApplicationException
	 *         if any error occurs while generating the report
	 */
	File generateReport(User user, File report, Sale sale) throws ApplicationException;
	
	/**
	 * Sends the given report by e-mail
	 * 
	 * @param sale
	 *        The sale of the report
	 * @param fp
	 *        The report file pointer
	 * @throws ApplicationException
	 *         if any error occurs while sending the report
	 */
	void sendReport(User user, Sale sale, File fp) throws ApplicationException;
	
	/**
	 * Utility class for simplifying access to the instance of service.
	 */
	public static class Util {
		
		private static ISaleService instance;
		
		public static ISaleService getInstance() {
			if (instance == null) {
				instance = new SaleService();
			}
			return instance;
		}
	}
}
