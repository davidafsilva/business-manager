package pt.davidafsilva.bm.shared.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Sales.java
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 10:33:08 AM
 */
public class Sale extends DomainBase {
	
	private long id;
	private Date saleDate;
	private String dischargePlace;
	private String observations;
	private String paymentConditions;
	private double totalBrute;
	private double totalLiquid;
	private double totalDiscount;
	private double totalVat;
	private boolean hasCardex;
	private boolean wasSentByEmail;
	private Client client;
	private User seller;
	private List<SaleProduct> products = new ArrayList<SaleProduct>();
	
	/**
	 * Default empty constructor
	 */
	public Sale() {
		
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
	 * Gets the saleDate value
	 * 
	 * @return the saleDate value
	 */
	public Date getSaleDate() {
		return this.saleDate;
	}
	
	
	/**
	 * Sets the saleDate value
	 * 
	 * @param saleDate
	 *        the saleDate to set
	 */
	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}
	
	
	/**
	 * Gets the dischargePlace value
	 * 
	 * @return the dischargePlace value
	 */
	public String getDischargePlace() {
		return this.dischargePlace;
	}
	
	
	/**
	 * Sets the dischargePlace value
	 * 
	 * @param dischargePlace
	 *        the dischargePlace to set
	 */
	public void setDischargePlace(String dischargePlace) {
		this.dischargePlace = dischargePlace;
	}
	
	/**
	 * Gets the observations value
	 * 
	 * @return the observations value
	 */
	public String getObservations() {
		return this.observations;
	}
	
	/**
	 * Sets the observations value
	 * 
	 * @param observations
	 *        the observations to set
	 */
	public void setObservations(String observations) {
		this.observations = observations;
	}
	
	
	/**
	 * Gets the paymentConditions value
	 * 
	 * @return the paymentConditions value
	 */
	public String getPaymentConditions() {
		return this.paymentConditions;
	}
	
	/**
	 * Sets the paymentConditions value
	 * 
	 * @param paymentConditions
	 *        the paymentConditions to set
	 */
	public void setPaymentConditions(String paymentConditions) {
		this.paymentConditions = paymentConditions;
	}
	
	
	/**
	 * Gets the totalBrute value
	 * 
	 * @return the totalBrute value
	 */
	public double getTotalBrute() {
		return this.totalBrute;
	}
	
	
	/**
	 * Sets the totalBrute value
	 * 
	 * @param totalBrute
	 *        the totalBrute to set
	 */
	public void setTotalBrute(double totalBrute) {
		this.totalBrute = totalBrute;
	}
	
	/**
	 * Gets the totalLiquid value
	 * 
	 * @return the totalLiquid value
	 */
	public double getTotalLiquid() {
		return this.totalLiquid;
	}
	
	/**
	 * Sets the totalLiquid value
	 * 
	 * @param totalLiquid
	 *        the totalLiquid to set
	 */
	public void setTotalLiquid(double totalLiquid) {
		this.totalLiquid = totalLiquid;
	}
	
	
	/**
	 * Gets the totalDiscount value
	 * 
	 * @return the totalDiscount value
	 */
	public double getTotalDiscount() {
		return this.totalDiscount;
	}
	
	
	/**
	 * Sets the totalDiscount value
	 * 
	 * @param totalDiscount
	 *        the totalDiscount to set
	 */
	public void setTotalDiscount(double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}
	
	
	/**
	 * Gets the totalVat value
	 * 
	 * @return the totalVat value
	 */
	public double getTotalVat() {
		return this.totalVat;
	}
	
	
	/**
	 * Sets the totalVat value
	 * 
	 * @param totalVat
	 *        the totalVat to set
	 */
	public void setTotalVat(double totalVat) {
		this.totalVat = totalVat;
	}
	
	
	/**
	 * Gets the seller value
	 * 
	 * @return the seller value
	 */
	public User getSeller() {
		return this.seller;
	}
	
	
	/**
	 * Sets the seller value
	 * 
	 * @param seller
	 *        the seller to set
	 */
	public void setSeller(User seller) {
		this.seller = seller;
	}
	
	
	/**
	 * Gets the client value
	 * 
	 * @return the client value
	 */
	public Client getClient() {
		return this.client;
	}
	
	
	/**
	 * Sets the client value
	 * 
	 * @param client
	 *        the client to set
	 */
	public void setClient(Client client) {
		this.client = client;
	}
	
	
	/**
	 * Sets the hasCardex value
	 * 
	 * @param hasCardex
	 *        the hasCardex to set
	 */
	public void setHasCardex(boolean hasCardex) {
		this.hasCardex = hasCardex;
	}
	
	
	/**
	 * Gets the hasCardex value
	 * 
	 * @return the hasCardex value
	 */
	public boolean hasCardex() {
		return this.hasCardex;
	}
	
	
	/**
	 * Sets the wasSentByEmail value
	 * 
	 * @param wasSentByEmail
	 *        the wasSentByEmail to set
	 */
	public void setSentByEmail(boolean wasSentByEmail) {
		this.wasSentByEmail = wasSentByEmail;
	}
	
	
	/**
	 * Gets the wasSentByEmail value
	 * 
	 * @return the wasSentByEmail value
	 */
	public boolean wasSentByEmail() {
		return this.wasSentByEmail;
	}
	
	
	/**
	 * Sets the products value
	 * 
	 * @param products
	 *        the products to set
	 */
	public void setProducts(List<SaleProduct> products) {
		this.products = products;
	}
	
	
	/**
	 * Gets the products value
	 * 
	 * @return the products value
	 */
	public List<SaleProduct> getProducts() {
		return this.products;
	}
	
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.shared.domain.DomainBase#clone(java.util.Map)
	 */
	@Override
	Object clone(Map<Object, Object> cloned) {
		if (cloned.containsKey(this)) {
			return cloned.get(this);
		}
		
		Sale clone = new Sale();
		cloned.put(this, clone);
		
		clone.id = id;
		clone.saleDate = saleDate == null ? null : new Date(saleDate.getTime());
		clone.dischargePlace = dischargePlace;
		clone.observations = observations;
		clone.totalBrute = totalBrute;
		clone.totalLiquid = totalLiquid;
		clone.totalDiscount = totalDiscount;
		clone.totalVat = totalVat;
		clone.hasCardex = hasCardex;
		clone.client = client == null ? null : (Client) client.clone(cloned);
		clone.seller = seller == null ? null : (User) seller.clone(cloned);
		
		return clone;
	}
	
}
