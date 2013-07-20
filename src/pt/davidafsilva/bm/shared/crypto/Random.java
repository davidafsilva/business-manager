package pt.davidafsilva.bm.shared.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.apache.log4j.Logger;


/**
 * Random.java
 * 
 * This class provides functions to generate random
 * secure random data.
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 10:21:19 PM
 */
public class Random {
	
	// logger
	private Logger log = Logger.getLogger(Random.class);
	
	// singleton instance
	private static Random random;
	
	// secure random instance
	private SecureRandom prng;
	
	/**
	 * Private constructor
	 * @throws NoSuchAlgorithmException 
	 */
	private Random() throws NoSuchAlgorithmException {
		try {
			prng = SecureRandom.getInstance("SHA1PRNG");
			prng.setSeed(prng.generateSeed(256));
		} catch (NoSuchAlgorithmException e) {
			log.error("Random(): error while instantiating the secure random generator.", e);
			throw e;
		}
	}
	
	/**
	 * Gets the singleton instance of the random generator
	 * 
	 * @return 
	 * 		The shared instance of the random generator
	 * @throws NoSuchAlgorithmException 
	 * 		If there's no implementation available
	 * 		for the PRNG.
	 */
	public static Random getInstance() throws NoSuchAlgorithmException {
		if (random == null)
			random = new Random();
		
		return random;
	}
	
	
	/**
	 * Generates <b>length</b> amount of random byte data.
	 * @param length
	 * 		The number of bytes to generate
	 * @return
	 * 		A byte array with <b>length</b> random bytes
	 */
	public byte[] getBytes(int length) {
		if (length <= 0)
			throw new NumberFormatException("length must be greater than 1");
		
		byte[] b = new byte[length];
		prng.nextBytes(b);
		return b;
	}
}
