package pt.davidafsilva.bm.shared.domain;

import java.util.Map;
import pt.davidafsilva.bm.shared.enums.ProductUnit;


/**
 * Product.java
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 12:52:22 PM
 */
public class Product extends DomainBase {
	
	private int id;
	private String description;
	private String code;
	private ProductUnit defaultUnit;
	private Double defaultAmount;
	private Double price;
	
	/**
	 * Default empty constructor
	 */
	public Product() {
		
	}
	
	/**
	 * Product Constructor
	 * 
	 * @param id
	 *        The product id
	 * @param description
	 *        The product description
	 * @param code
	 *        The product code
	 * @param price
	 *        The product price
	 * @param defaultUnit
	 *        The default unit
	 * @param defaultAmount
	 *        The default amount
	 */
	public Product(int id, String description, String code, double price, ProductUnit defaultUnit, Double defaultAmount) {
		super();
		this.id = id;
		this.description = description;
		this.code = code;
		this.defaultUnit = defaultUnit;
		this.defaultAmount = defaultAmount;
		this.price = price;
	}
	
	
	/**
	 * Gets the id value
	 * 
	 * @return the id value
	 */
	public int getId() {
		return this.id;
	}
	
	
	/**
	 * Sets the id value
	 * 
	 * @param id
	 *        the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	
	/**
	 * Gets the description value
	 * 
	 * @return the description value
	 */
	public String getDescription() {
		return this.description;
	}
	
	
	/**
	 * Sets the description value
	 * 
	 * @param description
	 *        the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	/**
	 * Gets the code value
	 * 
	 * @return the code value
	 */
	public String getCode() {
		return this.code;
	}
	
	
	/**
	 * Sets the code value
	 * 
	 * @param code
	 *        the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	
	/**
	 * Gets the defaultUnit value
	 * 
	 * @return the defaultUnit value
	 */
	public ProductUnit getDefaultUnit() {
		return this.defaultUnit;
	}
	
	
	/**
	 * Sets the defaultUnit value
	 * 
	 * @param defaultUnit
	 *        the defaultUnit to set
	 */
	public void setDefaultUnit(ProductUnit defaultUnit) {
		this.defaultUnit = defaultUnit;
	}
	
	
	/**
	 * Gets the defaultAmount value
	 * 
	 * @return the defaultAmount value
	 */
	public Double getDefaultAmount() {
		return this.defaultAmount;
	}
	
	
	/**
	 * Sets the defaultAmount value
	 * 
	 * @param defaultAmount
	 *        the defaultAmount to set
	 */
	public void setDefaultAmount(Double defaultAmount) {
		this.defaultAmount = defaultAmount;
	}
	
	
	/**
	 * Gets the price value
	 * 
	 * @return the price value
	 */
	public Double getPrice() {
		return this.price;
	}
	
	
	/**
	 * Sets the price value
	 * 
	 * @param price
	 *        the price to set
	 */
	public void setPrice(Double price) {
		this.price = price;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.id;
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Product other = (Product) obj;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.shared.domain.DomainBase#clone(java.util.Map)
	 */
	@Override
	Object clone(Map<Object, Object> cloned) {
		if (cloned.containsKey(this)) {
			return cloned.get(this);
		}
		
		Product clone = new Product();
		cloned.put(this, clone);
		
		clone.id = id;
		clone.description = description;
		clone.code = code;
		clone.price = price;
		clone.defaultUnit = defaultUnit;
		clone.defaultAmount = defaultAmount == null ? null : new Double(defaultAmount.doubleValue());
		
		return clone;
	}
	
}
