package pt.davidafsilva.bm.server.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import pt.davidafsilva.bm.server.dao.interfaces.ISaleDAO;
import pt.davidafsilva.bm.server.db.Database;
import pt.davidafsilva.bm.server.db.Query;
import pt.davidafsilva.bm.server.db.ResultHandler;
import pt.davidafsilva.bm.shared.domain.Client;
import pt.davidafsilva.bm.shared.domain.Product;
import pt.davidafsilva.bm.shared.domain.Sale;
import pt.davidafsilva.bm.shared.domain.SaleProduct;
import pt.davidafsilva.bm.shared.domain.User;
import pt.davidafsilva.bm.shared.enums.ProductUnit;


/**
 * SaleDAO.java
 * 
 * The sale data access object implementation
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 6:32:32 PM
 */
public class SaleDAO implements ISaleDAO {
	
	// logger
	private final Logger log = Logger.getLogger(SaleDAO.class);
	
	private final Database db = Database.getInstance();
	
	
	/**
	 * Gets the current sequence value and sets it as the id of the newly
	 * inserted sale product
	 * 
	 * @throws SQLException
	 */
	private void updateSaleProductId(SaleProduct product) throws SQLException {
		Query query;
		String sql;
		
		log.debug("updateSaleProductId(): updating sale product id.. ");
		try {
			// build the query
			sql = "SELECT ";
			sql += "  CURRENT_VALUE ";
			sql += "FROM ";
			sql += "  INFORMATION_SCHEMA.SEQUENCES ";
			sql += "WHERE ";
			sql += "  SEQUENCE_SCHEMA = 'PUBLIC' ";
			sql += "  AND SEQUENCE_NAME = 'SEQ_SALE_PRODUCT_ID' ";
			query = new Query(sql);
			
			// execute the query
			long id = db.executeQueryResult(query, new ResultHandler<Long>() {
				
				/* (non-Javadoc)
				 * @see pt.davidafsilva.bm.server.db.ResultHandler#handleResult(java.sql.ResultSet, boolean)
				 */
				@Override
				public Long handleResult(ResultSet result, boolean hasResults)
						throws SQLException {
					if (hasResults) {
						return result.getLong("CURRENT_VALUE");
					}
					return null;
				}
				
			});
			product.setId(id);
			
			log.debug("updateSaleProductId(): sale product id sucessfully updated");
		} catch (RuntimeException | SQLException e) {
			log.error("updateSaleProductId(): error updating sale product id", e);
			throw e;
		}
	}
	
	
	/**
	 * Gets the current sequence value and sets it as the id of the newly
	 * inserted sale
	 * 
	 * @throws SQLException
	 */
	private void updateSaleId(Sale sale) throws SQLException {
		Query query;
		String sql;
		
		log.debug("updateSaleId(): updating sale id.. ");
		try {
			// build the query
			sql = "SELECT ";
			sql += "  CURRENT_VALUE ";
			sql += "FROM ";
			sql += "  INFORMATION_SCHEMA.SEQUENCES ";
			sql += "WHERE ";
			sql += "  SEQUENCE_SCHEMA = 'PUBLIC' ";
			sql += "  AND SEQUENCE_NAME = 'SEQ_SALE_ID' ";
			query = new Query(sql);
			
			// execute the query
			long id = db.executeQueryResult(query, new ResultHandler<Long>() {
				
				/* (non-Javadoc)
				 * @see pt.davidafsilva.bm.server.db.ResultHandler#handleResult(java.sql.ResultSet, boolean)
				 */
				@Override
				public Long handleResult(ResultSet result, boolean hasResults)
						throws SQLException {
					if (hasResults) {
						return result.getLong("CURRENT_VALUE");
					}
					return null;
				}
				
			});
			sale.setId(id);
			
			log.debug("updateSaleId(): sale id sucessfully updated");
		} catch (RuntimeException | SQLException e) {
			log.error("updateSaleId(): error updating sale id", e);
			throw e;
		}
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.dao.interfaces.ISaleDAO#searchSales
	 * (pt.davidafsilva.bm.shared.domain.Client, java.util.Date, java.util.Date)
	 */
	@Override
	public List<Sale> searchSales(User seller, Client client, Date initialDate, Date finalDate)
			throws SQLException {
		List<Sale> sales = null;
		Query query;
		String sql;
		
		log.debug("searchSales(): search for sales.. ");
		try {
			// build the query
			sql = "SELECT ";
			sql += "  SALE.ID              AS SALE_ID, ";
			sql += "  SALE.SALE_DATE       AS SALE_DATE, ";
			sql += "  SALE.DISCHARGE_PLACE AS DISCHARGE_PLACE, ";
			sql += "  SALE.OBSERVATIONS    AS OBSERVATIONS, ";
			sql += "  SALE.PAYMENT_CONDITIONS AS PAYMENT_CONDITIONS, ";
			sql += "  SALE.TOTAL_BRUTE     AS TOTAL_BRUTE, ";
			sql += "  SALE.TOTAL_LIQUID    AS TOTAL_LIQUID, ";
			sql += "  SALE.TOTAL_DISCOUNT  AS TOTAL_DISCOUNT, ";
			sql += "  SALE.TOTAL_VAT       AS TOTAL_VAT, ";
			sql += "  SALE.CARDEX          AS CARDEX, ";
			sql += "  SALE.SENT_BY_EMAIL   AS SENT_BY_EMAIL, ";
			sql += "  CLIENT.ID         AS CLI_ID, ";
			sql += "  CLIENT.CODE       AS CLI_CODE, ";
			sql += "  CLIENT.NAME       AS CLI_NAME, ";
			sql += "  CLIENT.ADDRESS    AS CLI_ADDRESS, ";
			sql += "  CLIENT.PHONE      AS CLI_PHONE, ";
			sql += "  CLIENT.FAX        AS CLI_FAX, ";
			sql += "  CLIENT.VAT_NUMBER AS CLI_VAT ";
			sql += "FROM ";
			sql += "  SALES SALE ";
			sql += "INNER JOIN CLIENT CLIENT ";
			sql += "  ON SALE.CLIENT_ID = CLIENT.ID ";
			sql += "WHERE ";
			sql += "  1 = 1 ";
			
			// set where clause
			if (seller != null) {
				sql += "  AND SELLER_ID = :seller ";
			}
			if (client != null) {
				sql += "  AND CLIENT_ID = :client ";
			}
			if (initialDate != null) {
				sql += "  AND SALE_DATE >= :initialDate ";
			}
			if (finalDate != null) {
				sql += "  AND SALE_DATE <= :finalDate ";
			}
			sql += "ORDER BY SALE_ID DESC";
			
			// initialize the query
			query = new Query(sql);
			
			// set parameters
			if (seller != null) {
				query.setParameter("seller", seller.getId());
			}
			if (client != null) {
				query.setParameter("client", client.getId());
			}
			if (initialDate != null) {
				query.setParameter("initialDate", initialDate);
			}
			if (finalDate != null) {
				query.setParameter("finalDate", finalDate);
			}
			
			// execute the query
			sales = db.executeQueryResult(query, new ResultHandler<List<Sale>>() {
				
				/* (non-Javadoc)
				 * @see pt.davidafsilva.bm.server.db.ResultHandler#handleResult(java.sql.ResultSet, boolean)
				 */
				@Override
				public List<Sale> handleResult(ResultSet result, boolean hasResults)
						throws SQLException {
					ArrayList<Sale> lst = new ArrayList<Sale>();
					Sale sale = null;
					while (hasResults) {
						// sale
						sale = buildEntityFromResultSet(result);
						lst.add(sale);
						
						// sale products
						sale.setProducts(getSaleProducts(sale.getId()));
						
						hasResults = result.next();
					}
					
					return lst;
				}
				
			});
			
			log.debug("searchSales(): sales searched sucessfully");
		} catch (RuntimeException | SQLException e) {
			log.error("searchSales(): error searching for sales", e);
			throw e;
		}
		
		return sales;
	}
	
	private List<SaleProduct> getSaleProducts(long saleId) throws SQLException {
		List<SaleProduct> products = null;
		Query query;
		String sql;
		
		try {
			// build the query
			sql = "SELECT ";
			sql += "  SALE_PRODUCT.ID        AS SP_ID, ";
			sql += "  SALE_PRODUCT.AMOUNT    AS SP_AMOUNT, ";
			sql += "  SALE_PRODUCT.UNIT      AS SP_UNIT, ";
			sql += "  SALE_PRODUCT.PRICE     AS SP_PRICE, ";
			sql += "  SALE_PRODUCT.DISCOUNT  AS SP_DISCOUNT, ";
			sql += "  SALE_PRODUCT.VAT       AS SP_VAT, ";
			sql += "  PRODUCT.ID           AS P_ID, ";
			sql += "  PRODUCT.CODE         AS P_CODE, ";
			sql += "  PRODUCT.DESCRIPTION  AS P_DESCRIPTION ";
			sql += "FROM ";
			sql += "  SALE_PRODUCT, SALE_PRODUCTS_SET ";
			sql += "INNER JOIN PRODUCT ";
			sql += "  ON PRODUCT.ID = SALE_PRODUCT.PRODUCT_ID ";
			sql += "WHERE ";
			sql += "  SALE_PRODUCTS_SET.SALE_ID = :saleId ";
			sql += "  AND SALE_PRODUCT.ID = SALE_PRODUCTS_SET.PRODUCT_ID ";
			sql += "ORDER BY SP_ID ASC";
			
			// initialize the query
			query = new Query(sql);
			
			// set parameters
			query.setParameter("saleId", saleId);
			
			// execute the query
			products = db.executeQueryResult(query, true, new ResultHandler<List<SaleProduct>>() {
				
				/* (non-Javadoc)
				 * @see pt.davidafsilva.bm.server.db.ResultHandler#handleResult(java.sql.ResultSet, boolean)
				 */
				@Override
				public List<SaleProduct> handleResult(ResultSet result, boolean hasResults)
						throws SQLException {
					ArrayList<SaleProduct> lst = new ArrayList<SaleProduct>();
					while (hasResults) {
						// sale product
						SaleProduct sp = new SaleProduct();
						sp.setId(result.getLong("SP_ID"));
						sp.setAmount(result.getDouble("SP_AMOUNT"));
						sp.setPrice(result.getDouble("SP_PRICE"));
						if (result.wasNull()) {
							sp.setPrice(null);
						}
						sp.setDiscount(result.getString("SP_DISCOUNT"));
						if (result.wasNull()) {
							sp.setDiscount(null);
						}
						sp.setVat(result.getDouble("SP_VAT"));
						if (result.wasNull()) {
							sp.setVat(null);
						}
						sp.setUnit(ProductUnit.fromCode(result.getInt("SP_UNIT")));
						
						// base product
						Product p = new Product();
						p.setId(result.getInt("P_ID"));
						p.setCode(result.getString("P_CODE"));
						p.setDescription(result.getString("P_DESCRIPTION"));
						sp.setBaseProduct(p);
						
						lst.add(sp);
						hasResults = result.next();
					}
					
					return lst;
				}
				
			});
			
		} catch (RuntimeException | SQLException e) {
			throw e;
		}
		
		return products;
		
	}
	
	/**
	 * Builds a sale from a result set
	 * 
	 * @param rs
	 *        The result set
	 * @return The product object
	 * @throws SQLException
	 */
	private Sale buildEntityFromResultSet(ResultSet rs) throws SQLException {
		// sale
		Sale sale = new Sale();
		sale.setId(rs.getLong("SALE_ID"));
		sale.setSaleDate(rs.getTimestamp("SALE_DATE"));
		sale.setDischargePlace(rs.getString("DISCHARGE_PLACE"));
		sale.setObservations(rs.getString("OBSERVATIONS"));
		sale.setPaymentConditions(rs.getString("PAYMENT_CONDITIONS"));
		sale.setTotalBrute(rs.getDouble("TOTAL_BRUTE"));
		sale.setTotalLiquid(rs.getDouble("TOTAL_LIQUID"));
		sale.setTotalDiscount(rs.getDouble("TOTAL_DISCOUNT"));
		sale.setTotalVat(rs.getDouble("TOTAL_VAT"));
		sale.setHasCardex(rs.getInt("CARDEX") == 1);
		sale.setSentByEmail(rs.getInt("SENT_BY_EMAIL") == 1);
		
		// client
		Client client = new Client();
		client.setId(rs.getInt("CLI_ID"));
		client.setCode(rs.getString("CLI_CODE"));
		client.setName(rs.getString("CLI_NAME"));
		client.setAddress(rs.getString("CLI_ADDRESS"));
		client.setPhone(rs.getString("CLI_PHONE"));
		client.setFax(rs.getString("CLI_FAX"));
		client.setVatNumber(rs.getString("CLI_VAT"));
		sale.setClient(client);
		
		return sale;
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.dao.interfaces.ISaleDAO#save(pt.davidafsilva.bm.shared.domain.Sale)
	 */
	@Override
	public void save(Sale sale) throws SQLException {
		Query query;
		String sql;
		
		log.debug("save(): saving the sale.. ");
		try {
			// build the query
			sql = "INSERT INTO SALES ";
			sql += "  (SALE_DATE, ";
			sql += "  DISCHARGE_PLACE, ";
			sql += "  OBSERVATIONS, ";
			sql += "  PAYMENT_CONDITIONS, ";
			sql += "  TOTAL_BRUTE, ";
			sql += "  TOTAL_LIQUID, ";
			sql += "  TOTAL_DISCOUNT, ";
			sql += "  TOTAL_VAT, ";
			sql += "  CARDEX, ";
			sql += "  SENT_BY_EMAIL, ";
			sql += "  CLIENT_ID, ";
			sql += "  SELLER_ID) ";
			sql += "VALUES ";
			sql += "  (:saleDate, ";
			sql += "   :place,";
			sql += "   :observations,";
			sql += "   :conditions,";
			sql += "   :brute,";
			sql += "   :liquid,";
			sql += "   :discount,";
			sql += "   :vat,";
			sql += "   :cardex,";
			sql += "   :sentByEmail,";
			sql += "   :client,";
			sql += "   :seller)";
			query = new Query(sql);
			
			// query parameters
			query.setParameter("saleDate", sale.getSaleDate());
			query.setParameter("place", sale.getDischargePlace());
			query.setParameter("observations", sale.getObservations());
			query.setParameter("conditions", sale.getPaymentConditions());
			query.setParameter("brute", sale.getTotalBrute());
			query.setParameter("liquid", sale.getTotalLiquid());
			query.setParameter("discount", sale.getTotalDiscount());
			query.setParameter("vat", sale.getTotalVat());
			query.setParameter("cardex", sale.hasCardex() ? 1 : 0);
			query.setParameter("sentByEmail", sale.wasSentByEmail() ? 1 : 0);
			query.setParameter("client", sale.getClient().getId());
			query.setParameter("seller", sale.getSeller().getId());
			
			// execute the query
			db.execute(query);
			
			// updates the given sale id
			updateSaleId(sale);
			
			// save the sale products
			saveSaleProducts(sale, sale.getProducts());
			
			log.debug("save(): sale saved sucessfully");
		} catch (RuntimeException | SQLException e) {
			log.error("save(): error saving sale", e);
			throw e;
		}
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.dao.interfaces.ISaleDAO#setSentByEmail(long)
	 */
	@Override
	public void setSentByEmail(long saleId) throws SQLException {
		Query query;
		String sql;
		
		log.debug("setSentByEmail(): updating the sale.. ");
		try {
			// build the query
			sql = "UPDATE SALES SET SENT_BY_EMAIL = :sentByEmail WHERE ID = :saleId";
			query = new Query(sql);
			
			// query parameters
			query.setParameter("sentByEmail", 1);
			query.setParameter("saleId", saleId);
			
			// execute the query
			db.execute(query);
			
			log.debug("setSentByEmail(): updating saved sucessfully");
		} catch (RuntimeException | SQLException e) {
			log.error("setSentByEmail(): error updating sale", e);
			throw e;
		}
	}
	
	/**
	 * Saves the sale products
	 * 
	 * @param sale
	 *        The saved sale
	 * @param products
	 *        The products to be saved
	 * @throws SQLException
	 *         if any error occurs while saving the sale products
	 */
	private void saveSaleProducts(Sale sale, List<SaleProduct> products) throws SQLException {
		Query query;
		String sql1, sql2;
		
		// build the queries
		sql1 = "INSERT INTO SALE_PRODUCT ";
		sql1 += "  (AMOUNT, ";
		sql1 += "  PRICE, ";
		sql1 += "  UNIT, ";
		sql1 += "  DISCOUNT, ";
		sql1 += "  VAT, ";
		sql1 += "  PRODUCT_ID) ";
		sql1 += "VALUES ";
		sql1 += "  (:amount, ";
		sql1 += "   :price,";
		sql1 += "   :unit,";
		sql1 += "   :discount,";
		sql1 += "   :vat,";
		sql1 += "   :product)";
		
		sql2 = "INSERT INTO SALE_PRODUCTS_SET ";
		sql2 += " (SALE_ID,PRODUCT_ID) ";
		sql2 += "VALUES ";
		sql2 += " (:sale, :product)";
		
		for (SaleProduct product : products) {
			query = new Query(sql1);
			
			// query parameters
			query.setParameter("amount", product.getAmount());
			query.setParameter("price", product.getPrice());
			query.setParameter("unit", product.getUnit() == null ? null : product.getUnit().getCode());
			query.setParameter("discount", product.getDiscount());
			query.setParameter("vat", product.getVat());
			query.setParameter("product", product.getBaseProduct().getId());
			
			// execute the query
			db.execute(query);
			
			// updates the given sale id
			updateSaleProductId(product);
			
			// save the set
			query = new Query(sql2);
			
			// query parameters
			query.setParameter("sale", sale.getId());
			query.setParameter("product", product.getId());
			
			// execute the query
			db.execute(query);
		}
	}
}
