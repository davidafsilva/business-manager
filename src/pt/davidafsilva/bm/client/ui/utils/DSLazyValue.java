package pt.davidafsilva.bm.client.ui.utils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.swing.UIDefaults;
import sun.security.util.SecurityConstants;


/**
 * DSLazyValue.java
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 2:21:44 PM
 */
public class DSLazyValue implements UIDefaults.LazyValue {
	
	private final String className;
	private final String methodName;
	private Object[] arguments;
	
	public DSLazyValue(String c) {
		this(c, (String) null);
	}
	
	public DSLazyValue(String c, String m) {
		this(c, m, null);
	}
	
	public DSLazyValue(String c, Object[] o) {
		this(c, null, o);
	}
	
	public DSLazyValue(String c, String m, Object[] o) {
		className = c;
		methodName = m;
		if (o != null) {
			arguments = o.clone();
		}
	}
	
	@Override
	public Object createValue(final UIDefaults table) {
		try {
			Class<?> c;
			
			SecurityManager sm = System.getSecurityManager();
			if (sm != null) {
				ClassLoader ccl = new DSSecurityManager().getCallerClass(3).getClassLoader();
				if (ccl != null) {
					sm.checkPermission(SecurityConstants.GET_CLASSLOADER_PERMISSION);
				}
			}
			
			c = Class.forName(className);
			if (methodName != null) {
				Class<?>[] types = getClassArray(arguments);
				Method m = c.getMethod(methodName, types);
				makeAccessible(m);
				return m.invoke(c, arguments);
			}
			Class<?>[] types = getClassArray(arguments);
			Constructor<?> constructor = c.getConstructor(types);
			makeAccessible(constructor);
			return constructor.newInstance(arguments);
		} catch (Exception e) {
			// Ideally we would throw an exception, unfortunately
			// often times there are errors as an initial look and
			// feel is loaded before one can be switched. Perhaps a
			// flag should be added for debugging, so that if true
			// the exception would be thrown.
		}
		return null;
	}
	
	private void makeAccessible(final AccessibleObject object) {
		AccessController.doPrivileged(new PrivilegedAction<Void>() {
			
			@Override
			public Void run() {
				object.setAccessible(true);
				return null;
			}
		});
	}
	
	private Class<?>[] getClassArray(Object[] args) {
		Class<?>[] types = null;
		if (args != null) {
			types = new Class[args.length];
			for (int i = 0; i < args.length; i++) {
				/* PENDING(ges): At present only the primitive types 
				   used are handled correctly; this should eventually
				   handle all primitive types */
				if (args[i] instanceof java.lang.Integer) {
					types[i] = Integer.TYPE;
				} else if (args[i] instanceof java.lang.Boolean) {
					types[i] = Boolean.TYPE;
				} else if (args[i] instanceof javax.swing.plaf.ColorUIResource) {
					/* PENDING(ges) Currently the Reflection APIs do not 
					   search superclasses of parameters supplied for
					   constructor/method lookup.  Since we only have
					   one case where this is needed, we substitute
					   directly instead of adding a massive amount
					   of mechanism for this.  Eventually this will
					   probably need to handle the general case as well.
					*/
					types[i] = java.awt.Color.class;
				} else {
					types[i] = args[i].getClass();
				}
			}
		}
		return types;
	}
	
}
