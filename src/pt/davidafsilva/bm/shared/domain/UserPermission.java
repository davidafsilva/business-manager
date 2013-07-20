package pt.davidafsilva.bm.shared.domain;


/**
 * UserPermission.java
 * 
 * The user permissions enumeration
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 3:08:45 PM
 */
public enum UserPermission {
	ADMIN(1), USER(0);
	
	private int code;
	
	/**
	 * User permission enumeration constructor
	 * @param code
	 * 		The permission code
	 */
	UserPermission(int code) {
		this.code = code;
	}
	
	/**
	 * Gets the permission code
	 * @return
	 * 		The permission code
	 */
	public int getCode() {
		return this.code;
	}
	
	/**
	 * Gets the user permission given his code
	 * 
	 * @param code
	 * 		The user permission code
	 * @return
	 * 		The user permission, <code>null</code> if the code is invalid.
	 */
	public static UserPermission fromCode(int code) {
		if (code == 0)
			return USER;
		else if (code == 1)
			return ADMIN;
		
		return null;
	}
}
