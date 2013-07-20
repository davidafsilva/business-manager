package pt.davidafsilva.bm.shared.domain;

import java.util.Map;


/**
 * User.java
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 10:52:39 PM
 */
public class User extends DomainBase {
	
	// properties
	private int id;
	private String username;
	private String password;
	private String name;
	private String code;
	private int permission;
	
	
	/**
	 * Default empty constructor
	 */
	public User() {
		
	}
	
	
	/**
	 * Constructor
	 * 
	 * @param id
	 *        The user id
	 * @param username
	 *        The user authentication name
	 * @param password
	 *        The user password
	 * @param name
	 *        The user complete name
	 * @param code
	 *        The user code
	 * @param permission
	 *        The user permissions
	 */
	public User(int id, String username, String password, String name, String code, int permission) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.code = code;
		this.permission = permission;
	}
	
	
	/**
	 * Gets the user id
	 * 
	 * @return
	 *         The user id
	 */
	public int getId() {
		return id;
	}
	
	
	/**
	 * Sets the user id
	 * 
	 * @param id
	 *        The id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	
	/**
	 * Gets the user authentication name
	 * 
	 * @return
	 *         The user authentication name
	 */
	public String getUsername() {
		return username;
	}
	
	
	/**
	 * Sets the user authentication name
	 * 
	 * @param username
	 *        The user authentication name to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	/**
	 * Gets the user password
	 * 
	 * @return
	 *         The user password
	 */
	public String getPassword() {
		return password;
	}
	
	
	/**
	 * Sets the user password
	 * 
	 * @param password
	 *        The user password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	/**
	 * Gets the name value
	 * 
	 * @return the name value
	 */
	public String getName() {
		return name;
	}
	
	
	/**
	 * Sets the user name
	 * 
	 * @param name
	 *        The user name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	/**
	 * Gets the user code
	 * 
	 * @return
	 *         The user code
	 */
	public String getCode() {
		return code;
	}
	
	
	/**
	 * Sets the user code
	 * 
	 * @param code
	 *        The code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	
	/**
	 * Gets the user permissions
	 * 
	 * @return
	 *         The users permission
	 */
	public int getPermission() {
		return permission;
	}
	
	
	/**
	 * Sets the user permissions
	 * 
	 * @param permission
	 *        The user permission to set
	 */
	public void setPermission(int permission) {
		this.permission = permission;
	}
	
	
	//	/* (non-Javadoc)
	//	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	//	 */
	//	@Override
	//	public void writeExternal(ObjectOutput out) throws IOException {
	//		NetworkUtil.write(int.class, id, out);
	//		NetworkUtil.write(String.class, username, out);
	//		NetworkUtil.write(String.class, password, out);
	//		NetworkUtil.write(String.class, name, out);
	//		NetworkUtil.write(String.class, code, out);
	//		NetworkUtil.write(int.class, permission, out);
	//	}
	//	
	//	/* (non-Javadoc)
	//	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	//	 */
	//	@Override
	//	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
	//		id = NetworkUtil.read(int.class, in);
	//		username = NetworkUtil.read(String.class, in);
	//		password = NetworkUtil.read(String.class, in);
	//		name = NetworkUtil.read(String.class, in);
	//		code = NetworkUtil.read(String.class, in);
	//		permission = NetworkUtil.read(int.class, in);
	//	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.shared.domain.DomainBase#clone(java.util.Map)
	 */
	@Override
	Object clone(Map<Object, Object> cloned) {
		if (cloned.containsKey(this)) {
			return cloned.get(this);
		}
		
		User clone = new User();
		cloned.put(this, clone);
		
		clone.id = id;
		clone.username = username;
		clone.password = password;
		clone.name = name;
		clone.code = code;
		clone.permission = permission;
		
		return clone;
	}
}
