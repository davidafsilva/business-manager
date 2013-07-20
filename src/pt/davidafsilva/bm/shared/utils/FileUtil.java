package pt.davidafsilva.bm.shared.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64;
import pt.davidafsilva.bm.shared.crypto.Cipher;
import pt.davidafsilva.bm.shared.crypto.CipherData;
import pt.davidafsilva.bm.shared.crypto.HMAC;
import pt.davidafsilva.bm.shared.exception.CorruptedFileException;


/**
 * FileUtil.java
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 12:56:21 PM
 */
public class FileUtil {
	
	/**
	 * Gets the application directory absolute path.
	 * The directory is located at the users home directory.
	 * 
	 * @return
	 *         The absolute path of the application directory
	 */
	public static String getApplicationDirectory() {
		return System.getProperty("user.home") + File.separator + "Business Manager";
	}
	
	/**
	 * Creates the application directory if it doesn't exists
	 */
	public static void createApplicationDirectory() {
		File dir = new File(getApplicationDirectory());
		if (!dir.exists() && !dir.isDirectory()) {
			// create the directory
			dir.mkdir();
			
			// reset permissions
			dir.setReadable(false, false);
			dir.setWritable(false, false);
			dir.setExecutable(false, false);
			
			// set permissions
			dir.setExecutable(true, true);
			dir.setReadable(true, true);
			dir.setWritable(true, true);
		}
	}
	
	/**
	 * Writes the <b>content</b> at the given <b>file</b>
	 * 
	 * @param fd
	 *        The file descriptor object
	 * @param content
	 *        The file content
	 * @throws IOException
	 *         if the file cannot be created/opened
	 */
	public static void writeFile(File fd, byte[] content) throws IOException {
		// ciphers the content
		CipherData cd = Cipher.cipher(content);
		
		if (cd == null) {
			throw new RuntimeException("Unable to cipher the contents.");
		}
		
		// calculates the HMAC
		byte[] hmac = HMAC.calculate(concat(cd.getSalt(), cd.getIV(), cd.getData()));
		
		try (
				// instantiates the writer
				BufferedWriter writer = new BufferedWriter(new FileWriter(fd));		
		) {
			// writes the salt + IV + contents
			writer.write(Base64.encodeBase64String(cd.getSalt()));
			writer.write('\n');
			writer.write(Base64.encodeBase64String(cd.getIV()));
			writer.write('\n');
			writer.write(Base64.encodeBase64String(cd.getData()));
			writer.write('\n');
			
			// writes the HMAC
			writer.write(Base64.encodeBase64String(hmac));
			writer.write('\n');
			
			writer.flush();
		}
	}
	
	/**
	 * Reads the file contents
	 * 
	 * @param fd
	 *        The file descriptor object
	 * @throws IOException
	 *         if the file cannot be opened for read
	 * @throws CorruptedFileException
	 *         If the file is corrupted
	 */
	public static byte[] readFile(File fd) throws IOException, CorruptedFileException {
		try (
				// instantiates the writer
				BufferedReader reader = new BufferedReader(new FileReader(fd));
		) {
			// reads the salt + IV + contents b64
			String b64Salt = reader.readLine();
			if (b64Salt == null || b64Salt.length() == 0) {
				throw new CorruptedFileException();
			}
			String b64IV = reader.readLine();
			if (b64IV == null || b64IV.length() == 0) {
				throw new CorruptedFileException();
			}
			String b64Content = reader.readLine();
			if (b64Content == null || b64Content.length() == 0) {
				throw new CorruptedFileException();
			}
			
			// reads the HMAC
			String b64HMAC = reader.readLine();
			if (b64HMAC == null || b64HMAC.length() == 0) {
				throw new CorruptedFileException();
			}
			
			// base64 decode the file data
			byte[] cSalt = Base64.decodeBase64(b64Salt);
			if (cSalt == null || cSalt.length == 0) {
				throw new CorruptedFileException();
			}
			byte[] cIV = Base64.decodeBase64(b64IV);
			if (cIV == null || cIV.length == 0) {
				throw new CorruptedFileException();
			}
			byte[] cData = Base64.decodeBase64(b64Content);
			if (cData == null || cData.length == 0) {
				throw new CorruptedFileException();
			}
			byte[] rHMAC = Base64.decodeBase64(b64HMAC);
			if (rHMAC == null || rHMAC.length == 0) {
				throw new CorruptedFileException();
			}
			
			// calculates the HMAC
			byte[] cHMAC = HMAC.calculate(concat(cSalt, cIV, cData));
			
			// compare the HMACs
			if (!HMAC.compare(rHMAC, cHMAC)) {
				throw new CorruptedFileException();
			}
			
			// decipher the file contents
			return Cipher.decipher(cData, cIV, cSalt);
		}
	}
	
	/**
	 * Concatenates the three byte arrays
	 * 
	 * @param A
	 *        The byte array A
	 * @param B
	 *        The byte array B
	 * @param C
	 *        The byte array C
	 * @return
	 *         A resultant byte array
	 */
	private static byte[] concat(byte[] A, byte[] B, byte[] C) {
		byte[] Z = new byte[A.length + B.length + C.length];
		System.arraycopy(A, 0, Z, 0, A.length);
		System.arraycopy(B, 0, Z, A.length, B.length);
		System.arraycopy(C, 0, Z, B.length, C.length);
		return Z;
	}
}
