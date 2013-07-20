package pt.davidafsilva.bm.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * ResultHandler.java
 * 
 * The result handler interface
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 12:28:43 PM
 */
public interface ResultHandler<T> {
	
	/**
	 * Handles the query result.
	 * 
	 * @param result
	 * 		The resultant result set from executing the query
	 * @param hasResults
	 * 		The flag which indicates whether or not there are
	 * 		any results to fetch.
	 * @throws SQLException
	 * 		If an error occurs while fetching the results
	 */
	T handleResult(ResultSet result, boolean hasResults) throws SQLException;
}
