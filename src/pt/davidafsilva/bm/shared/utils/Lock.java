package pt.davidafsilva.bm.shared.utils;


/**
 * Lock.java
 * 
 * A simple lock implementation
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 5:06:02 PM
 */
public class Lock {
	
	private boolean locked = false;
	
	public Lock() {
	};
	
	/**
	 * Acquire lock
	 */
	public synchronized void lock() {
		while (locked)
			try {
				wait();
			} catch (InterruptedException e) {
			}
		
		//lock acquired!
		locked = true;
	}
	
	/**
	 * Free the lock
	 */
	public synchronized void unlock() {
		//lock free'd
		locked = false;
		//notify another thread that might be waiting for the lock
		notify();
	}
}
