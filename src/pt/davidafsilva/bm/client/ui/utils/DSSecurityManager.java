package pt.davidafsilva.bm.client.ui.utils;


/** 
 * DSSecurityManager.java
 * 
 * The security manager 
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 2:30:40 PM
 */
public final class DSSecurityManager extends SecurityManager {
	
	/**
	 * Gets the caller class base of the current context
	 * 
	 * @param callStackDepth
	 * 		The depth of the class to look for
	 * @return
	 * 		The class in the specified depth
	 * @throws ArrayIndexOutOfBoundsException
	 * 		if the given depth isn't reachable
	 */
	public Class<?> getCallerClass(int callStackDepth) {
		return getClassContext()[callStackDepth];
	}
}
