package pt.davidafsilva.bm.shared.domain;

import java.util.Map;


/**
 * Client.java
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 11:43:28 AM
 */
public class Client extends DomainBase {
	
	// properties
	private int id;
	private String name;
	private String code;
	private String address;
	private String phone;
	private String fax;
	private String vatNumber;
	private String defaultDischargePlace;
	
	/**
	 * Default empty constructor
	 */
	public Client() {
		
	}
	
	
	/**
	 * Constructor
	 * 
	 * @param id
	 *        The client id
	 * @param name
	 *        The client name
	 * @param code
	 *        The client code
	 * @param address
	 *        The client address
	 * @param phone
	 *        The client phone
	 * @param fax
	 *        The client fax
	 */
	public Client(int id, String name, String code, String address, String phone, String fax) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.address = address;
		this.phone = phone;
		this.fax = fax;
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
	 * Gets the name value
	 * 
	 * @return the name value
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Sets the name value
	 * 
	 * @param name
	 *        the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * Gets the address value
	 * 
	 * @return the address value
	 */
	public String getAddress() {
		return this.address;
	}
	
	/**
	 * Sets the address value
	 * 
	 * @param address
	 *        the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	/**
	 * Gets the phone value
	 * 
	 * @return the phone value
	 */
	public String getPhone() {
		return this.phone;
	}
	
	/**
	 * Sets the phone value
	 * 
	 * @param phone
	 *        the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	/**
	 * Gets the fax value
	 * 
	 * @return the fax value
	 */
	public String getFax() {
		return this.fax;
	}
	
	/**
	 * Sets the fax value
	 * 
	 * @param fax
	 *        the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}
	
	
	/**
	 * Gets the vatNumber value
	 * 
	 * @return the vatNumber value
	 */
	public String getVatNumber() {
		return this.vatNumber;
	}
	
	
	/**
	 * Sets the vatNumber value
	 * 
	 * @param vatNumber
	 *        the vatNumber to set
	 */
	public void setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;
	}
	
	
	/**
	 * Sets the defaultDischargePlace value
	 * 
	 * @param defaultDischargePlace
	 *        the defaultDischargePlace to set
	 */
	public void setDefaultDischargePlace(String defaultDischargePlace) {
		this.defaultDischargePlace = defaultDischargePlace;
	}
	
	
	/**
	 * Gets the defaultDischargePlace value
	 * 
	 * @return the defaultDischargePlace value
	 */
	public String getDefaultDischargePlace() {
		return this.defaultDischargePlace;
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
		Client other = (Client) obj;
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
		
		Client clone = new Client();
		cloned.put(this, clone);
		
		clone.id = id;
		clone.name = name;
		clone.code = code;
		clone.address = address;
		clone.phone = phone;
		clone.fax = fax;
		clone.vatNumber = vatNumber;
		clone.defaultDischargePlace = defaultDischargePlace;
		
		return clone;
	}
	
}
