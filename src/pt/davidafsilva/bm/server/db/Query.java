package pt.davidafsilva.bm.server.db;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import pt.davidafsilva.bm.server.db.exception.InvalidParameterException;


/**
 * Query.java
 * 
 * The database query object
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 9:25:09 PM
 */
public class Query {
	
	// the sql query
	private String sql;
	
	// the query parameters
	private SortedMap<Integer, Object> parameters = new TreeMap<Integer, Object>();
	
	/**
	 * Create the query object with the given <b>sql</b> query.
	 * <br><br>
	 * Query parameters must be prefixed with <b>:</b>, such as the sufix 
	 * identifies the parameter in question. Therefore duplicated parameters will be replaced
	 * with value object set by {@link #setParameter(String, Object)}.
	 * 
	 * <br/><br/>
	 * 
	 * Example:
	 * <pre>
	 * 	SELECT * FROM TABLE1 T1, TABLE2 WHERE T1.RANDOM = :random AND T2.RANDOM = :random
	 * </pre>
	 * 
	 * @param sql
	 * 		The SQL query
	 */
	public Query(String sql) {
		this.sql = sql;
	}
	
	/**
	 * Gets the SQL value
	 *
	 * @return the SQL value
	 */
	public String getSql() {
		return sql;
	}
	
	/**
	 * Gets the ordered parameters values
	 * 
	 * @return
	 * 		The collection with the parameters values
	 */
	public Collection<Object> getValues() {
		return parameters.values();
	}
	
	/**
	 * Sets (one of) the <b>query</b> parameter(s).
	 * <br/>
	 * Duplicated parameter key will also be replaced by <b>value</b>.
	 * 
	 * @param key 
	 * 		The parameter identifier
	 * @param value	
	 * 		The value object
	 * @throws InvalidParameterException 
	 * 		If <b>key</b> is an invalid parameter identifier
	 */
	public void setParameter(String key, Object value) throws InvalidParameterException {
		setParameter(key, value, 0);
	}
	
	/**
	 * Replaces the query parameters with id <b>key</b> to the value of <b>value</b>.
	 * 
	 * @param key 
	 * 		The parameter identifier
	 * @param value	
	 * 		The value object
	 * @param nCall
	 * 		The method call number
	 * @throws InvalidParameterException 
	 * 		If <b>key</b> is an invalid parameter identifier
	 */
	private void setParameter(String key, Object value, int nCall) {
		try {
			parameters.put(getParameterIndex(key), value);
			setParameter(key, value, ++nCall);
		} catch (InvalidParameterException e) {
			if (nCall == 0)
				throw e;
		}
	}
	
	/**
	 * Gets the parameter identified by <b>key</b> index
	 * 
	 * <br/><br/>
	 * <b><code>TODO:</code></b> improve performance, allow a start index to be provided
	 * to improve the duplicated keys replacement.
	 * 
	 * @param key
	 * 		The parameter id
	 * @return	
	 * 		The parameter index (0-based)
	 * @throws InvalidParameterException
	 * 		Thrown if the parameter <b>key</b> is invalid
	 */
	private int getParameterIndex(String key) throws InvalidParameterException {
		String[] words = sql.split("\\s");
		int idx = -1;
		int wordIdx = 0;
		for (String word : words) {
			if (word.startsWith("(") || word.startsWith("=")) {
				// remove the first character
				if (word.length() > 0) {
					word = word.substring(1);
					wordIdx++;
				}
			}
			
			if (word.startsWith("?"))
				idx++;
			
			if (word.startsWith(":")) {
				if (word.length() > 1) {
					// remove the ':' marker
					String wordAux = word.substring(1);
					
					if (wordAux.contains(",") || wordAux.contains(")")) {
						wordAux = wordAux.replaceAll("\\,", " ").replaceAll("\\)", " ");
					}
					
					if (wordAux.contains(" ")) {
						wordAux = wordAux.split("\\s")[0];
					}
					
					if (wordAux.equals(key)) {
						// replace the parameter with '?'
						String aux = sql.substring(0, wordIdx);
						sql = aux + "?" + sql.substring(wordIdx + wordAux.length() + 1);
						return ++idx;
					}
					
					idx++;
				}
			}
			
			wordIdx += word.length() + 1;
		}
		
		throw new InvalidParameterException(key);
	}
}
