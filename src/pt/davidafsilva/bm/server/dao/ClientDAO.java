package pt.davidafsilva.bm.server.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import pt.davidafsilva.bm.server.dao.interfaces.IClientDAO;
import pt.davidafsilva.bm.server.db.Database;
import pt.davidafsilva.bm.server.db.Query;
import pt.davidafsilva.bm.server.db.ResultHandler;
import pt.davidafsilva.bm.shared.domain.Client;


/**
 * ClientDAO.java
 * 
 * The client data access object implementation
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 11:58:58 AM
 */
public class ClientDAO implements IClientDAO {
	
	// logger
	private final Logger log = Logger.getLogger(ClientDAO.class);
	
	private final Database db = Database.getInstance();
	
	/**
	 * Gets the current sequence value and sets it as the id of the newly
	 * inserted client
	 * 
	 * @throws SQLException
	 */
	private void updateClientId(Client client) throws SQLException {
		Query query;
		String sql;
		
		log.debug("updateClientId(): updating client id.. ");
		try {
			// build the query
			sql = "SELECT ";
			sql += "  CURRENT_VALUE ";
			sql += "FROM ";
			sql += "  INFORMATION_SCHEMA.SEQUENCES ";
			sql += "WHERE ";
			sql += "  SEQUENCE_SCHEMA = 'PUBLIC' ";
			sql += "  AND SEQUENCE_NAME = 'SEQ_CLIENT_ID' ";
			query = new Query(sql);
			
			// execute the query
			int id = db.executeQueryResult(query, new ResultHandler<Integer>() {
				
				/* (non-Javadoc)
				 * @see pt.davidafsilva.bm.server.db.ResultHandler#handleResult(java.sql.ResultSet, boolean)
				 */
				@Override
				public Integer handleResult(ResultSet result, boolean hasResults)
						throws SQLException {
					if (hasResults) {
						return result.getInt("CURRENT_VALUE");
					}
					return null;
				}
				
			});
			client.setId(id);
			
			log.debug("updateClientId(): client id sucessfully updated");
		} catch (RuntimeException | SQLException e) {
			log.error("updateClientId(): error updating client id", e);
			throw e;
		}
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.dao.interfaces.IClientDAO#save(pt.davidafsilva.bm.shared.domain.Client)
	 */
	@Override
	public void save(Client client) throws SQLException {
		Query query;
		String sql;
		
		log.debug("save(): saving the client.. ");
		try {
			// build the query
			sql = "INSERT INTO CLIENT ";
			sql += "  (NAME, ";
			sql += "  CODE, ";
			sql += "  ADDRESS, ";
			sql += "  PHONE, ";
			sql += "  FAX, ";
			sql += "  VAT_NUMBER, ";
			sql += "  DEFAULT_DISCHARGE_PLACE) ";
			sql += "VALUES ";
			sql += "  (:name, ";
			sql += "   :code,";
			sql += "   :address,";
			sql += "   :phone,";
			sql += "   :fax,";
			sql += "   :vatNumber,";
			sql += "   :defaultDischargePlace)";
			query = new Query(sql);
			
			// query parameters
			query.setParameter("name", client.getName());
			query.setParameter("code", client.getCode());
			query.setParameter("address", client.getAddress());
			query.setParameter("phone", client.getPhone());
			query.setParameter("fax", client.getFax());
			query.setParameter("vatNumber", client.getVatNumber());
			query.setParameter("defaultDischargePlace", client.getDefaultDischargePlace());
			
			// execute the query
			db.execute(query);
			
			// updates the given client id
			updateClientId(client);
			
			log.debug("save(): client saved sucessfully");
		} catch (RuntimeException | SQLException e) {
			log.error("save(): error saving client", e);
			throw e;
		}
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.dao.interfaces.IClientDAO#update(pt.davidafsilva.bm.shared.domain.Client)
	 */
	@Override
	public void update(Client client) throws SQLException {
		Query query;
		String sql;
		
		log.debug("update(): updating the client.. ");
		try {
			// build the query
			sql = "UPDATE CLIENT SET ";
			sql += "  NAME    = :name, ";
			sql += "  CODE    = :code, ";
			sql += "  ADDRESS = :address, ";
			sql += "  PHONE   = :phone, ";
			sql += "  FAX     = :fax, ";
			sql += "  VAT_NUMBER = :vatNumber, ";
			sql += "  DEFAULT_DISCHARGE_PLACE = :defaultDischargePlace ";
			sql += "WHERE ";
			sql += "  ID      = :id";
			query = new Query(sql);
			
			// query parameters
			query.setParameter("name", client.getName());
			query.setParameter("code", client.getCode());
			query.setParameter("address", client.getAddress());
			query.setParameter("phone", client.getPhone());
			query.setParameter("fax", client.getFax());
			query.setParameter("vatNumber", client.getVatNumber());
			query.setParameter("defaultDischargePlace", client.getDefaultDischargePlace());
			query.setParameter("id", client.getId());
			
			// execute the query
			db.execute(query);
			
			log.debug("update(): client updated sucessfully");
		} catch (RuntimeException | SQLException e) {
			log.error("update(): error updating client", e);
			throw e;
		}
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.dao.interfaces.IClientDAO#delete(pt.davidafsilva.bm.shared.domain.Client)
	 */
	@Override
	public void delete(Client client) throws SQLException {
		Query query;
		String sql;
		
		log.debug("delete(): deleting the client.. ");
		try {
			// build the query
			sql = "DELETE FROM CLIENT ";
			sql += "WHERE ID = :id";
			query = new Query(sql);
			
			// query parameters
			query.setParameter("id", client.getId());
			
			// execute the query
			db.execute(query);
			
			log.debug("delete(): client deleted sucessfully");
		} catch (RuntimeException | SQLException e) {
			log.error("delete(): error deleting client", e);
			throw e;
		}
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.dao.interfaces.IClientDAO#get(int)
	 */
	@Override
	public Client get(int id) throws SQLException {
		Client client = null;
		Query query;
		String sql;
		
		log.debug("get(): getting client by id.. ");
		try {
			// build the query
			sql = "SELECT ";
			sql += "  * ";
			sql += "FROM ";
			sql += "  CLIENT ";
			sql += "WHERE ";
			sql += "  ID = :id ";
			query = new Query(sql);
			
			// set the parameter
			query.setParameter("id", id);
			
			// execute the query
			client = db.executeQueryResult(query, new ResultHandler<Client>() {
				
				/* (non-Javadoc)
				 * @see pt.davidafsilva.bm.server.db.ResultHandler#handleResult(java.sql.ResultSet, boolean)
				 */
				@Override
				public Client handleResult(ResultSet result, boolean hasResults)
						throws SQLException {
					if (hasResults) {
						return buildEntityFromResultSet(result);
					}
					return null;
				}
				
			});
			
			log.debug("get(): get client by id sucessfull");
		} catch (RuntimeException | SQLException e) {
			log.error("get(): error getting client by id", e);
			throw e;
		}
		
		return client;
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.dao.interfaces.IClientDAO#get(int, java.lang.String)
	 */
	@Override
	public Client get(int id, String code) throws SQLException {
		Client client = null;
		Query query;
		String sql;
		
		log.debug("get(): getting client by code.. ");
		try {
			// build the query
			sql = "SELECT ";
			sql += "  * ";
			sql += "FROM ";
			sql += "  CLIENT ";
			sql += "WHERE ";
			sql += "  ID  <> :id ";
			sql += "  AND CODE = :code ";
			query = new Query(sql);
			
			// set the parameter
			query.setParameter("id", id);
			query.setParameter("code", code);
			
			// execute the query
			client = db.executeQueryResult(query, new ResultHandler<Client>() {
				
				/* (non-Javadoc)
				 * @see pt.davidafsilva.bm.server.db.ResultHandler#handleResult(java.sql.ResultSet, boolean)
				 */
				@Override
				public Client handleResult(ResultSet result, boolean hasResults)
						throws SQLException {
					if (hasResults) {
						return buildEntityFromResultSet(result);
					}
					return null;
				}
				
			});
			
			log.debug("get(): get client by code sucessfull");
		} catch (RuntimeException | SQLException e) {
			log.error("get(): error getting client by code", e);
			throw e;
		}
		
		return client;
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.dao.interfaces.IClientDAO#getAll()
	 */
	@Override
	public List<Client> getAll() throws SQLException {
		List<Client> clients = null;
		Query query;
		String sql;
		
		log.debug("getAll(): getting all clients.. ");
		try {
			// build the query
			sql = "SELECT ";
			sql += "  * ";
			sql += "FROM ";
			sql += "  CLIENT ";
			query = new Query(sql);
			
			// execute the query
			clients = db.executeQueryResult(query, new ResultHandler<List<Client>>() {
				
				/* (non-Javadoc)
				 * @see pt.davidafsilva.bm.server.db.ResultHandler#handleResult(java.sql.ResultSet, boolean)
				 */
				@Override
				public List<Client> handleResult(ResultSet result, boolean hasResults)
						throws SQLException {
					ArrayList<Client> lst = new ArrayList<Client>();
					while (hasResults) {
						lst.add(buildEntityFromResultSet(result));
						hasResults = result.next();
					}
					
					return lst;
				}
				
			});
			
			log.debug("getAll(): get all clients sucessfull");
		} catch (RuntimeException | SQLException e) {
			log.error("getAll(): error getting all clients", e);
			throw e;
		}
		
		return clients;
	}
	
	/**
	 * Builds a client from a result set
	 * 
	 * @param rs
	 *        The result set
	 * @return The client object
	 * @throws SQLException
	 */
	private Client buildEntityFromResultSet(ResultSet rs) throws SQLException {
		Client client = new Client();
		client.setId(rs.getInt("ID"));
		client.setCode(rs.getString("CODE"));
		client.setName(rs.getString("NAME"));
		client.setAddress(rs.getString("ADDRESS"));
		client.setPhone(rs.getString("PHONE"));
		client.setFax(rs.getString("FAX"));
		client.setVatNumber(rs.getString("VAT_NUMBER"));
		client.setDefaultDischargePlace(rs.getString("DEFAULT_DISCHARGE_PLACE"));
		return client;
	}
	
}
