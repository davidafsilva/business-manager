package pt.davidafsilva.bm.shared.domain;

import java.util.Map;
import pt.davidafsilva.bm.shared.enums.ProductUnit;


/**
 * SaleProduct.java
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 10:42:03 AM
 */
public class SaleProduct extends DomainBase {
	
	private long id;
	private Product baseProduct;
	private double amount;
	private Double price;
	private String discount;
	private Double vat;
	private ProductUnit unit;
	
	/**
	 * Default empty constructor
	 */
	public SaleProduct() {
		
	}
	
	/**
	 * Gets the id value
	 * 
	 * @return the id value
	 */
	public long getId() {
		return this.id;
	}
	
	/**
	 * Sets the id value
	 * 
	 * @param id
	 *        the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * Gets the baseProduct value
	 * 
	 * @return the baseProduct value
	 */
	public Product getBaseProduct() {
		return this.baseProduct;
	}
	
	/**
	 * Sets the baseProduct value
	 * 
	 * @param baseProduct
	 *        the baseProduct to set
	 */
	public void setBaseProduct(Product baseProduct) {
		this.baseProduct = baseProduct;
	}
	
	/**
	 * Gets the amount value
	 * 
	 * @return the amount value
	 */
	public double getAmount() {
		return this.amount;
	}
	
	/**
	 * Sets the amount value
	 * 
	 * @param amount
	 *        the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
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
	
	/**
	 * Gets the discount value
	 * 
	 * @return the discount value
	 */
	public String getDiscount() {
		return this.discount;
	}
	
	/**
	 * Sets the discount value
	 * 
	 * @param discount
	 *        the discount to set
	 */
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	
	/**
	 * Gets the vat value
	 * 
	 * @return the vat value
	 */
	public Double getVat() {
		return this.vat;
	}
	
	/**
	 * Sets the vat value
	 * 
	 * @param vat
	 *        the vat to set
	 */
	public void setVat(Double vat) {
		this.vat = vat;
	}
	
	/**
	 * Gets the unit value
	 * 
	 * @return the unit value
	 */
	public ProductUnit getUnit() {
		return this.unit;
	}
	
	/**
	 * Sets the unit value
	 * 
	 * @param unit
	 *        the unit to set
	 */
	public void setUnit(ProductUnit unit) {
		this.unit = unit;
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.shared.domain.DomainBase#clone(java.util.Map)
	 */
	@Override
	Object clone(Map<Object, Object> cloned) {
		if (cloned.containsKey(this)) {
			return cloned.get(this);
		}
		
		SaleProduct clone = new SaleProduct();
		cloned.put(this, clone);
		
		clone.id = id;
		clone.baseProduct = baseProduct == null ? null : (Product) baseProduct.clone(cloned);
		clone.amount = amount;
		clone.price = price;
		clone.discount = discount;
		clone.vat = vat;
		clone.unit = unit;
		
		return clone;
	}
	
}
