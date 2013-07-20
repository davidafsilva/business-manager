package pt.davidafsilva.bm.server.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import pt.davidafsilva.bm.server.dao.interfaces.IProductDAO;
import pt.davidafsilva.bm.server.db.Database;
import pt.davidafsilva.bm.server.db.Query;
import pt.davidafsilva.bm.server.db.ResultHandler;
import pt.davidafsilva.bm.shared.domain.Product;
import pt.davidafsilva.bm.shared.enums.ProductUnit;


/**
 * ProductDAO.java
 * 
 * The product data access object implementation
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 1:10:12 PM
 */
public class ProductDAO implements IProductDAO {
	
	// logger
	private final Logger log = Logger.getLogger(ProductDAO.class);
	
	private final Database db = Database.getInstance();
	
	/**
	 * Gets the current sequence value and sets it as the id of the newly
	 * inserted product
	 * 
	 * @throws SQLException
	 */
	private void updateProductId(Product product) throws SQLException {
		Query query;
		String sql;
		
		log.debug("updateProductId(): updating product id.. ");
		try {
			// build the query
			sql = "SELECT ";
			sql += "  CURRENT_VALUE ";
			sql += "FROM ";
			sql += "  INFORMATION_SCHEMA.SEQUENCES ";
			sql += "WHERE ";
			sql += "  SEQUENCE_SCHEMA = 'PUBLIC' ";
			sql += "  AND SEQUENCE_NAME = 'SEQ_PRODUCT_ID' ";
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
			product.setId(id);
			
			log.debug("updateProductId(): product id sucessfully updated");
		} catch (RuntimeException | SQLException e) {
			log.error("updateProductId(): error updating product id", e);
			throw e;
		}
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.dao.interfaces.IProductDAO#save(pt.davidafsilva.bm.shared.domain.Product)
	 */
	@Override
	public void save(Product product) throws SQLException {
		Query query;
		String sql;
		
		log.debug("save(): saving the product.. ");
		try {
			// build the query
			sql = "INSERT INTO PRODUCT ";
			sql += "  (DESCRIPTION, ";
			sql += "  CODE, ";
			sql += "  DEFAULT_UNIT, ";
			sql += "  DEFAULT_AMOUNT, ";
			sql += "  PRICE) ";
			sql += "VALUES ";
			sql += "  (:description, ";
			sql += "   :code,";
			sql += "   :defaultUnit,";
			sql += "   :defaultAmount,";
			sql += "   :price)";
			query = new Query(sql);
			
			// query parameters
			query.setParameter("description", product.getDescription());
			query.setParameter("code", product.getCode());
			query.setParameter("defaultUnit", product.getDefaultUnit() == null ? null : product.getDefaultUnit().getCode());
			query.setParameter("defaultAmount", product.getDefaultAmount());
			query.setParameter("price", product.getPrice());
			
			// execute the query
			db.execute(query);
			
			// updates the given product id
			updateProductId(product);
			
			log.debug("save(): product saved sucessfully");
		} catch (RuntimeException | SQLException e) {
			log.error("save(): error saving product", e);
			throw e;
		}
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.dao.interfaces.IProductDAO#update(pt.davidafsilva.bm.shared.domain.Product)
	 */
	@Override
	public void update(Product product) throws SQLException {
		Query query;
		String sql;
		
		log.debug("update(): updating the product.. ");
		try {
			// build the query
			sql = "UPDATE PRODUCT SET ";
			sql += "  DESCRIPTION    = :description, ";
			sql += "  CODE           = :code, ";
			sql += "  DEFAULT_UNIT   = :defaultUnit, ";
			sql += "  DEFAULT_AMOUNT = :defaultAmount, ";
			sql += "  PRICE          = :price ";
			sql += "WHERE ";
			sql += "  ID      = :id";
			query = new Query(sql);
			
			// query parameters
			query.setParameter("description", product.getDescription());
			query.setParameter("code", product.getCode());
			query.setParameter("defaultUnit", product.getDefaultUnit() == null ? null : product.getDefaultUnit().getCode());
			query.setParameter("defaultAmount", product.getDefaultAmount());
			query.setParameter("price", product.getPrice());
			query.setParameter("id", product.getId());
			
			// execute the query
			db.execute(query);
			
			log.debug("update(): product updated sucessfully");
		} catch (RuntimeException | SQLException e) {
			log.error("update(): error updating product", e);
			throw e;
		}
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.dao.interfaces.IProductDAO#delete(pt.davidafsilva.bm.shared.domain.Product)
	 */
	@Override
	public void delete(Product product) throws SQLException {
		Query query;
		String sql;
		
		log.debug("delete(): deleting the product.. ");
		try {
			// build the query
			sql = "DELETE FROM PRODUCT ";
			sql += "WHERE ID = :id";
			query = new Query(sql);
			
			// query parameters
			query.setParameter("id", product.getId());
			
			// execute the query
			db.execute(query);
			
			log.debug("delete(): product deleted sucessfully");
		} catch (RuntimeException | SQLException e) {
			log.error("delete(): error deleting product", e);
			throw e;
		}
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.dao.interfaces.IProductDAO#get(int)
	 */
	@Override
	public Product get(int id) throws SQLException {
		Product product = null;
		Query query;
		String sql;
		
		log.debug("get(): getting product by id.. ");
		try {
			// build the query
			sql = "SELECT ";
			sql += "  * ";
			sql += "FROM ";
			sql += "  PRODUCT ";
			sql += "WHERE ";
			sql += "  ID = :id ";
			query = new Query(sql);
			
			// set the parameter
			query.setParameter("id", id);
			
			// execute the query
			product = db.executeQueryResult(query, new ResultHandler<Product>() {
				
				/* (non-Javadoc)
				 * @see pt.davidafsilva.bm.server.db.ResultHandler#handleResult(java.sql.ResultSet, boolean)
				 */
				@Override
				public Product handleResult(ResultSet result, boolean hasResults)
						throws SQLException {
					if (hasResults) {
						return buildEntityFromResultSet(result);
					}
					return null;
				}
				
			});
			
			log.debug("get(): get product by id sucessfull");
		} catch (RuntimeException | SQLException e) {
			log.error("get(): error getting product by id", e);
			throw e;
		}
		
		return product;
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.dao.interfaces.IProductDAO#get(int, java.lang.String)
	 */
	@Override
	public Product get(int id, String code) throws SQLException {
		Product product = null;
		Query query;
		String sql;
		
		log.debug("get(): getting product by code.. ");
		try {
			// build the query
			sql = "SELECT ";
			sql += "  * ";
			sql += "FROM ";
			sql += "  PRODUCT ";
			sql += "WHERE ";
			sql += "  ID  <> :id ";
			sql += "  AND CODE = :code ";
			query = new Query(sql);
			
			// set the parameter
			query.setParameter("id", id);
			query.setParameter("code", code);
			
			// execute the query
			product = db.executeQueryResult(query, new ResultHandler<Product>() {
				
				/* (non-Javadoc)
				 * @see pt.davidafsilva.bm.server.db.ResultHandler#handleResult(java.sql.ResultSet, boolean)
				 */
				@Override
				public Product handleResult(ResultSet result, boolean hasResults)
						throws SQLException {
					if (hasResults) {
						return buildEntityFromResultSet(result);
					}
					return null;
				}
				
			});
			
			log.debug("get(): get product by code sucessfull");
		} catch (RuntimeException | SQLException e) {
			log.error("get(): error getting product by code", e);
			throw e;
		}
		
		return product;
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.dao.interfaces.IProductDAO#getAll()
	 */
	@Override
	public List<Product> getAll() throws SQLException {
		List<Product> products = null;
		Query query;
		String sql;
		
		log.debug("getAll(): getting all products.. ");
		try {
			// build the query
			sql = "SELECT ";
			sql += "  * ";
			sql += "FROM ";
			sql += "  PRODUCT ";
			query = new Query(sql);
			
			// execute the query
			products = db.executeQueryResult(query, new ResultHandler<List<Product>>() {
				
				/* (non-Javadoc)
				 * @see pt.davidafsilva.bm.server.db.ResultHandler#handleResult(java.sql.ResultSet, boolean)
				 */
				@Override
				public List<Product> handleResult(ResultSet result, boolean hasResults)
						throws SQLException {
					ArrayList<Product> lst = new ArrayList<Product>();
					while (hasResults) {
						lst.add(buildEntityFromResultSet(result));
						hasResults = result.next();
					}
					
					return lst;
				}
				
			});
			
			log.debug("getAll(): get all products sucessfull");
		} catch (RuntimeException | SQLException e) {
			log.error("getAll(): error getting all products", e);
			throw e;
		}
		
		return products;
	}
	
	/**
	 * Builds a product from a result set
	 * 
	 * @param rs
	 *        The result set
	 * @return The product object
	 * @throws SQLException
	 */
	private Product buildEntityFromResultSet(ResultSet rs) throws SQLException {
		Product product = new Product();
		product.setId(rs.getInt("ID"));
		product.setCode(rs.getString("CODE"));
		product.setDescription(rs.getString("DESCRIPTION"));
		product.setPrice(rs.getDouble("PRICE"));
		if (rs.wasNull()) {
			product.setPrice(null);
		}
		product.setDefaultAmount(rs.getDouble("DEFAULT_AMOUNT"));
		if (rs.wasNull()) {
			product.setDefaultAmount(null);
		}
		product.setDefaultUnit(ProductUnit.fromCode(rs.getInt("DEFAULT_UNIT")));
		return product;
	}
}
