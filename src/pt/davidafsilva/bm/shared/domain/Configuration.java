package pt.davidafsilva.bm.shared.domain;

import java.util.Map;


/**
 * Configuration.java
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 11:09:36 AM
 */
public class Configuration extends DomainBase {
	
	// -----------
	// Properties
	// -----------
	private int id;
	private String description;
	private String key;
	private String value;
	private User user;
	
	
	/**
	 * Default constructor
	 */
	public Configuration() {
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
	 * Gets the key value
	 * 
	 * @return the key value
	 */
	public String getKey() {
		return this.key;
	}
	
	
	/**
	 * Sets the key value
	 * 
	 * @param key
	 *        the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
	
	/**
	 * Gets the value value
	 * 
	 * @return the value value
	 */
	public String getValue() {
		return this.value;
	}
	
	
	/**
	 * Sets the value value
	 * 
	 * @param value
	 *        the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	
	/**
	 * Gets the user value
	 * 
	 * @return the user value
	 */
	public User getUser() {
		return this.user;
	}
	
	
	/**
	 * Sets the user value
	 * 
	 * @param user
	 *        the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.shared.domain.DomainBase#clone(java.util.Map)
	 */
	@Override
	Object clone(Map<Object, Object> cloned) {
		if (cloned.containsKey(this)) {
			return cloned.get(this);
		}
		
		Configuration clone = new Configuration();
		cloned.put(this, clone);
		
		clone.id = id;
		clone.description = description;
		clone.key = key;
		clone.value = value;
		clone.user = user == null ? null : (User) user.clone(cloned);
		
		return clone;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[" + key + ": " + value + "]";
	}
	
}
