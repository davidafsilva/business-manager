package pt.davidafsilva.bm.server.db;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Collection;
import java.util.Date;
import org.apache.log4j.Logger;
import org.h2.jdbc.JdbcSQLException;
import org.h2.jdbcx.JdbcDataSource;
import pt.davidafsilva.bm.server.db.exception.DatabaseNotInicializedException;
import pt.davidafsilva.bm.shared.utils.FileUtil;
import pt.davidafsilva.bm.shared.utils.Lock;


/**
 * IDatabaseDAO.java
 * 
 * The database helper
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 11:24:29 PM
 */
public class Database {
	
	// logger
	private final Logger log = Logger.getLogger(Database.class);
	
	// The database connection
	private Connection connection;
	
	// the transaction lock
	private final Lock lock = new Lock();
	
	/**
	 * Default constructor
	 */
	private Database() {
	}
	
	private static Database instance;
	
	/**
	 * Gets the singleton instance of this database
	 * 
	 * @return The database
	 */
	public static Database getInstance() {
		if (instance == null) {
			instance = new Database();
		}
		return instance;
	}
	
	/**
	 * Connects to the database server
	 * 
	 * @throws SQLException
	 *         if a database access error occurs
	 * @throws DatabaseNotInicializedException
	 *         if the database is not initialized, see {@link #initialize()}.
	 */
	public void connect() throws SQLException, DatabaseNotInicializedException {
		try {
			if (!isConnected()) {
				log.debug("connect(): connecting to the database..");
				connect(true);
				log.debug("connect(): connection successfully established.");
			}
		} catch (SQLException e) {
			log.error("connect(): error connecting to the database.", e);
		}
	}
	
	/**
	 * Connects to the database
	 * 
	 * @param existent
	 *        connect only if the database exists
	 * @throws SQLException
	 *         if a database access error occurs
	 * @throws DatabaseNotInicializedException
	 *         if the database is not initialized, see {@link #initialize()}.
	 */
	protected void connect(boolean existent) throws SQLException, DatabaseNotInicializedException {
		try {
			JdbcDataSource ds = new JdbcDataSource();
			String url = "jdbc:h2:file:";
			url += FileUtil.getApplicationDirectory() + File.separator + "bm";
			url += ";CIPHER=AES;FILE_LOCK=SOCKET";
			if (existent) {
				url += ";IFEXISTS=TRUE";
			}
			ds.setURL(url);
			ds.setUser("bm");
			ds.setPassword("RStr-z9vnv!vZz%wK TBq2e!7NY25&Z3$J");
			connection = ds.getConnection();
		} catch (JdbcSQLException e) {
			if (e.getErrorCode() == 90013) {
				throw new DatabaseNotInicializedException();
			}
			throw e;
		}
	}
	
	/**
	 * Disconnects from the database server
	 */
	public void disconnect() {
		if (isConnected()) {
			try {
				log.debug("disconnect(): disconnecting from the database..");
				connection.close();
				log.debug("disconnect(): successfully disconnected from the database.");
			} catch (Exception e) {
				log.error("disconnect(): error connecting to the database.", e);
			}
		}
	}
	
	/**
	 * Checks if there's a connection active to the database
	 * 
	 * @return
	 *         <code>true</code> if there's a connection active, <code>false</code> otherwise
	 */
	public boolean isConnected() {
		try {
			return connection != null && !connection.isClosed();
		} catch (SQLException e) {
			return false;
		}
	}
	
	
	/**
	 * Executes the given query.
	 * 
	 * @param query
	 *        The query to execute
	 * @throws SQLException
	 *         If an error occurs while executing the query
	 */
	public void execute(Query query) throws SQLException {
		execute(query, false, null, false);
	}
	
	/**
	 * Executes one transaction
	 * 
	 * @param queries
	 *        The ordered queries collection to execute
	 * @throws SQLException
	 *         If an error occurs while executing the query
	 */
	public void executeTransaction(Collection<Query> queries) throws SQLException {
		try {
			// acquires the lock
			lock.lock();
			
			connection.setAutoCommit(false);
			for (Query query : queries) {
				execute(query, false, null, true);
			}
			
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw e;
		} finally {
			connection.setAutoCommit(true);
			
			// frees the lock
			lock.unlock();
		}
	}
	
	/**
	 * Executes the given query.
	 * 
	 * @param <T>
	 * 
	 * @param query
	 *        The query to execute
	 * @param handler
	 *        The handler which will handle the result set
	 * @throws SQLException
	 *         If an error occurs while executing the query
	 */
	public <T> void executeQuery(Query query, ResultHandler<T> handler) throws SQLException {
		execute(query, true, handler, false);
	}
	
	/**
	 * Executes the given query.
	 * 
	 * @param <T>
	 * 
	 * @param query
	 *        The query to execute
	 * @param handler
	 *        The handler which will handle the result set
	 * @return
	 *         The <T> typed object with the query result
	 * @throws SQLException
	 *         If an error occurs while executing the query
	 */
	public <T> T executeQueryResult(Query query, ResultHandler<T> handler) throws SQLException {
		return execute(query, true, handler, false);
	}
	
	/**
	 * Executes the given query.
	 * 
	 * @param <T>
	 * 
	 * @param query
	 *        The query to execute
	 * @param hasPendingQueries
	 *        If the query to execute has a pending query
	 * @param handler
	 *        The handler which will handle the result set
	 * @return
	 *         The <T> typed object with the query result
	 * @throws SQLException
	 *         If an error occurs while executing the query
	 */
	public <T> T executeQueryResult(Query query, boolean hasPendingQueries, ResultHandler<T> handler) throws SQLException {
		return execute(query, true, handler, hasPendingQueries);
	}
	
	
	/**
	 * Executes the SQL query/update statement
	 * 
	 * @param query
	 *        The query to execute
	 * @param select
	 *        The flag that indicates that the execution is a select statement
	 * @param handler
	 *        The result handler (optional), if <code>select</code> = <code>true</code>
	 * @return
	 *         The result of the execution
	 * @throws SQLException
	 */
	@SuppressWarnings("resource")
	private <T> T execute(Query query, boolean select, ResultHandler<T> handler, boolean bypassLock) throws SQLException {
		PreparedStatement stmt = null;
		T res = null;
		
		try {
			if (!bypassLock) {
				lock.lock();
			}
			
			// connect if the connection isn't available
			if (!isConnected()) {
				connect();
			}
			
			// create the statement
			stmt = connection.prepareStatement(query.getSql());
			
			// set the parameters
			int idx = 0;
			for (Object value : query.getValues()) {
				int type = getObjectType(value);
				
				switch (type) {
					case Types.NULL:
						stmt.setNull(++idx, type);
						break;
					case Types.INTEGER:
						stmt.setInt(++idx, (Integer) value);
						break;
					case Types.NUMERIC:
						stmt.setBigDecimal(++idx, (BigDecimal) value);
						break;
					case Types.BOOLEAN:
						stmt.setBoolean(++idx, (Boolean) value);
						break;
					case Types.SMALLINT:
						stmt.setShort(++idx, (Short) value);
						break;
					case Types.BIGINT:
						stmt.setLong(++idx, (Long) value);
						break;
					case Types.REAL:
						stmt.setFloat(++idx, (Float) value);
						break;
					case Types.DOUBLE:
						stmt.setDouble(++idx, (Double) value);
						break;
					case Types.BINARY:
						if (value.getClass() == byte.class) {
							stmt.setByte(++idx, (byte) value);
						} else {
							stmt.setByte(++idx, ((Byte) value).byteValue());
						}
						break;
					case Types.VARCHAR:
						stmt.setString(++idx, (String) value);
						break;
					case Types.TIMESTAMP:
						if (value.getClass() == Date.class) {
							stmt.setTimestamp(++idx, new Timestamp(((Date) value).getTime()));
						} else {
							stmt.setTimestamp(++idx, (Timestamp) value);
						}
						break;
					case Types.ARRAY:
						//TODO: implement
						break;
					case Types.OTHER:
						//TODO: implement
						break;
				}
			}
			
			// log
			log.info("execute(): executing the query:\n" + query.getSql());
			
			// execute the select / update statement
			if (select) {
				ResultSet result = stmt.executeQuery();
				if (handler != null) {
					res = handler.handleResult(result, result.first());
				}
			} else {
				stmt.executeUpdate();
			}
			return res;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			
			if (!bypassLock) {
				lock.unlock();
			}
		}
	}
	
	/**
	 * Gets the SQL type mapped for a given object
	 * 
	 * @param obj
	 *        The object to evaluate
	 * @return
	 *         The {@link Types} of the object
	 */
	private int getObjectType(Object obj) {
		if (obj == null) {
			return Types.NULL;
		} else if (obj.getClass() == Integer.class) {
			return Types.INTEGER;
		} else if (obj.getClass() == BigDecimal.class) {
			return Types.NUMERIC;
		} else if (obj.getClass() == Boolean.class) {
			return Types.BOOLEAN;
		} else if (obj.getClass() == Short.class) {
			return Types.SMALLINT;
		} else if (obj.getClass() == Long.class) {
			return Types.BIGINT;
		} else if (obj.getClass() == Float.class) {
			return Types.REAL;
		} else if (obj.getClass() == Double.class) {
			return Types.DOUBLE;
		} else if (obj.getClass() == Byte.class) {
			return Types.BINARY;
		} else if (obj.getClass() == String.class) {
			return Types.VARCHAR;
		} else if (obj.getClass() == Date.class || obj.getClass() == Timestamp.class) {
			return Types.TIMESTAMP;
		} else if (obj.getClass().isArray()) {
			return Types.ARRAY;
		} else if (obj instanceof Iterable) {
			return Types.OTHER;
		}
		
		return Types.NULL;
	}
}
