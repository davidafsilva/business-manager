package pt.davidafsilva.bm.shared.crypto;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.log4j.Logger;


/**
 * Cipher.java
 * 
 * Class that provides cryptography cipher/decipher implementations
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 10:17:54 PM
 */
public class Cipher {
	
	// logger
	private static Logger log = Logger.getLogger(Cipher.class);
	
	/**
	 * Ciphers the given data
	 * 
	 * @param data
	 * 		The plain text data to be ciphered
	 * @return
	 * 		The {@link CipherData} object with the resultant cipher.
	 */
	public static CipherData cipher(String data) {
		try {
			return cipher(data.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			log.error("cipher(): error converting data into UTF-8 encoding.", e);
			return null;
		}
	}
	
	/**
	 * Ciphers the given data
	 * 
	 * @param data
	 * 		The data to be ciphered
	 * @return
	 * 		The {@link CipherData} object with the resultant cipher.
	 * 		Null is returned if an error occurs in the cipher process.
	 */
	public static CipherData cipher(byte[] data) {
		try {
			// get the secret factory instance
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			
			// generate the salt
			byte[] salt = Random.getInstance().getBytes(32);
			
			// instantiate the key specification
			KeySpec spec = new PBEKeySpec(Keys.masterkey, salt, 65536, 128);
			
			// generate the secret key
			SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
			
			// encrypt the data
			javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding");
			
			// initialize the cipher to encrypt
			cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, secret);
			
			// gets the cipher parameters
			AlgorithmParameters params = cipher.getParameters();
			
			// get the IV 
			byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
			
			// get the ciphered data
			byte[] cipherData = cipher.doFinal(data);
			
			return new CipherData(salt, iv, cipherData);
		} catch (NoSuchAlgorithmException e) {
			log.error("cipher(): unable to cipher the data.", e);
		} catch (InvalidKeySpecException e) {
			log.error("cipher(): unable to cipher the data.", e);
		} catch (NoSuchPaddingException e) {
			log.error("cipher(): unable to cipher the data.", e);
		} catch (InvalidKeyException e) {
			log.error("cipher(): unable to cipher the data.", e);
		} catch (InvalidParameterSpecException e) {
			log.error("cipher(): unable to cipher the data.", e);
		} catch (IllegalBlockSizeException e) {
			log.error("cipher(): unable to cipher the data.", e);
		} catch (BadPaddingException e) {
			log.error("cipher(): unable to cipher the data.", e);
		}
		
		return null;
	}
	
	/**
	 * Deciphers the given ciphered data
	 * 
	 * @param data
	 * 		The data to be deciphered
	 * @param iv
	 * 		The initialization vector generated in the cipher
	 * @param salt
	 * 		The salt bytes used in the cipher
	 * @return
	 * 		The {@link Byte[]} with the deciphered data
	 */
	public static byte[] decipher(byte[] data, byte[] iv, byte[] salt) {
		try {
			// get the secret factory instance
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			
			// instantiate the key specification
			KeySpec spec = new PBEKeySpec(Keys.masterkey, salt, 65536, 128);
			
			// generate the secret key
			SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
			
			// decrypt the data
			javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding");
			
			// initialize the cipher to decrypt
			cipher.init(javax.crypto.Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
			
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException e) {
			log.error("decipher(): unable to decipher the data.", e);
		} catch (InvalidKeySpecException e) {
			log.error("decipher(): unable to decipher the data.", e);
		} catch (NoSuchPaddingException e) {
			log.error("decipher(): unable to decipher the data.", e);
		} catch (InvalidKeyException e) {
			log.error("decipher(): unable to decipher the data.", e);
		} catch (IllegalBlockSizeException e) {
			log.error("decipher(): unable to decipher the data.", e);
		} catch (BadPaddingException e) {
			log.error("decipher(): unable to decipher the data.", e);
		} catch (InvalidAlgorithmParameterException e) {
			log.error("decipher(): unable to decipher the data.", e);
		}
		
		return null;
	}
}
