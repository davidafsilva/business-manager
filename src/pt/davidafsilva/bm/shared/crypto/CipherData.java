package pt.davidafsilva.bm.shared.crypto;


/**
 * CipherData.java
 * 
 * This object stored the ciphered data and the
 * respective IV.
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 10:42:43 PM
 */
public class CipherData {
	
	// the IV
	private byte[] iv;
	// the ciphered data
	private byte[] data;
	// the salt
	private byte[] salt;
	
	/**
	 * Initializes the ciphered data with the iv and
	 * the data ciphered
	 * 
	 * @param salt
	 * 		The salt generated for the cipher
	 * @param iv	
	 * 		The initialization vector generated in the cipher
	 * @param data
	 * 		The ciphered data
	 */
	public CipherData(byte[] salt, byte[] iv, byte[] data) {
		this.salt = salt;
		this.iv = iv;
		this.data = data;
	}
	
	
	/**
	 * Gets the iv value
	 *
	 * @return the iv value
	 */
	public byte[] getIV() {
		return iv;
	}
	
	
	/**
	 * Gets the data value
	 *
	 * @return the data value
	 */
	public byte[] getData() {
		return data;
	}
	
	
	
	/**
	 * Gets the salt value
	 *
	 * @return the salt value
	 */
	public byte[] getSalt() {
		return salt;
	}
}
