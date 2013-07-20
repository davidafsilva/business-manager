package pt.davidafsilva.bm.server.service;

import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import pt.davidafsilva.bm.server.dao.interfaces.IProductDAO;
import pt.davidafsilva.bm.server.service.interfaces.IProductService;
import pt.davidafsilva.bm.shared.domain.Product;
import pt.davidafsilva.bm.shared.exception.ApplicationException;


/**
 * ProductService.java
 * 
 * The Product service (business code) implementation
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 1:06:33 PM
 */
public class ProductService implements IProductService {
	
	// logger
	private final Logger log = Logger.getLogger(ProductService.class);
	
	private final IProductDAO productDAO = IProductDAO.Util.getInstance();
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.service.interfaces.IProductService#save(pt.davidafsilva.bm.shared.domain.Product)
	 */
	@Override
	public void save(Product product) throws ApplicationException {
		log.debug("save(): saving the product.. ");
		try {
			// checks if there's already a product with the given code
			if (productDAO.get(0, product.getCode()) != null) {
				throw new ApplicationException("J\u00E1 existe um producto com o c\u00F3digo '" + product.getCode() + "'.");
			}
			productDAO.save(product);
			
			log.debug("save(): product saved sucessfully");
		} catch (RuntimeException | SQLException e) {
			log.error("save(): error saving product", e);
			throw new ApplicationException("Ocorreu um erro a gravar o producto.");
		}
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.service.interfaces.IProductService#update(pt.davidafsilva.bm.shared.domain.Product)
	 */
	@Override
	public void update(Product product) throws ApplicationException {
		log.debug("update(): updating the product.. ");
		try {
			// checks if there's already a product with the given code
			if (productDAO.get(product.getId(), product.getCode()) != null) {
				throw new ApplicationException("J\u00E1 existe um producto com o c\u00F3digo '" + product.getCode() + "'.");
			}
			productDAO.update(product);
			
			log.debug("update(): product updated sucessfully");
		} catch (RuntimeException | SQLException e) {
			log.error("update(): error updating product", e);
			throw new ApplicationException("Ocorreu um erro a actualizar o producto.");
		}
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.service.interfaces.IProductService#delete(pt.davidafsilva.bm.shared.domain.Product)
	 */
	@Override
	public void delete(Product product) throws ApplicationException {
		log.debug("delete(): deleting the product.. ");
		try {
			productDAO.delete(product);
			
			log.debug("delete(): product deleted sucessfully");
		} catch (RuntimeException | SQLException e) {
			log.error("delete(): error deleting product", e);
			e.printStackTrace();
			throw new ApplicationException("Ocorreu um erro a remover o producto.");
		}
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.server.service.interfaces.IProductService#getAll()
	 */
	@Override
	public List<Product> getAll() {
		List<Product> products = null;
		log.debug("getAll(): getting all the products.. ");
		try {
			products = productDAO.getAll();
			
			log.debug("getAll(): products obtained sucessfully");
		} catch (RuntimeException | SQLException e) {
			log.error("getAll(): error getting all products", e);
		}
		
		return products;
	}
	
}
