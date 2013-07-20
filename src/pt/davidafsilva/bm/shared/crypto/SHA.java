package pt.davidafsilva.bm.shared.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.log4j.Logger;


/**
 * SHA.java
 * 
 * SHA hash functions
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 8:06:51 PM
 */
public class SHA {
	
	// logger
	private static Logger log = Logger.getLogger(SHA.class);
	
	/**
	 * Computes the SHA-256 hash of the given data
	 * 
	 * @param data
	 * 		The data to compute the hash
	 * @return
	 * 		The hash of the <code>data</code>
	 */
	public static String compute(String data) {
		try {
			// instantiate the message digest
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(concat(data.getBytes(), Keys.hashkey));
			
			// digest the byte data
			byte byteData[] = md.digest();
			
			//convert the byte to hex format
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			log.error("compute(): error computing the sha-256 hash", e);
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static byte[] concat(byte[] A, byte[] B) {
		byte[] C = new byte[A.length + B.length];
		System.arraycopy(A, 0, C, 0, A.length);
		System.arraycopy(B, 0, C, A.length, B.length);
		return C;
	}
}
