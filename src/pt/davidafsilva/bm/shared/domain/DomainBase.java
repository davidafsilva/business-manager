package pt.davidafsilva.bm.shared.domain;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Map;


/**
 * DomainBase.java
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 1:59:00 PM
 */
public abstract class DomainBase extends Object implements Cloneable, Externalizable {
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		return clone(new HashMap<Object, Object>());
	}
	
	/**
	 * Clones the current object
	 * 
	 * @param cloned
	 *        The cloned objects
	 * @return
	 *         The cloned object
	 */
	abstract Object clone(Map<Object, Object> cloned);
	
	/* (non-Javadoc)
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		throw new UnsupportedOperationException("Serialization not supported.");
	}
	
	/* (non-Javadoc)
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		throw new UnsupportedOperationException("Serialization not supported.");
	}
	
}
