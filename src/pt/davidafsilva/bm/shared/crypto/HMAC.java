package pt.davidafsilva.bm.shared.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.log4j.Logger;


/**
 * HMAC.java
 * 
 * The HMAC hash function
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 11:48:41 PM
 */
public class HMAC {
	
	// logger
	private static Logger log = Logger.getLogger(HMAC.class);
	
	/**
	 * Calculates the HMAC for the given <b>data</b>
	 * 
	 * @param data
	 * 		The data to be used in the HMAC function
	 * @return
	 * 		The HMAC for <b>data</b>
	 */
	public static byte[] calculate(byte[] data) {
		try {
			// instantiate the key specification
			SecretKeySpec keySpec = new SecretKeySpec(Keys.mackey, "HmacSHA1");
			
			// instantiate the MAC algorithm implementation
			Mac mac = Mac.getInstance("HmacSHA1");
			
			// initialize the MAC
			mac.init(keySpec);
			
			return mac.doFinal(data);
		} catch (NoSuchAlgorithmException e) {
			log.error("calculate(): unable to calculate HMAC for the given data.", e);
		} catch (InvalidKeyException e) {
			log.error("calculate(): unable to calculate HMAC for the given data.", e);
		}
		
		return null;
	}
	
	
	/**
	 * Compares byte-a-byte the two give HMACs.
	 * 
	 * @param hmac1
	 * 		The HMAC 1
	 * @param hmac2
	 * 		The HMAC 2
	 * @return
	 * 		<code>true</code> if both HMAC are exactly the same (byte-comparison),
	 * 		<code>false</code> otherwise.
	 */
	public static boolean compare(byte[] hmac1, byte[] hmac2) {
		return Arrays.equals(hmac1, hmac2);
	}
}
