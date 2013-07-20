package pt.davidafsilva.bm.shared.crypto;


/**
 * Keys.java
 * 
 * The crypto keys constants
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 9:36:44 PM
 */
public class Keys {
	
	// The application master key
	public static final char[] masterkey = ("8Zn6XbL7" + "kAAeMWxH" + "E3wDcGsb" + "Wk7nFvdX" + "gH2nsTqs").toCharArray();
	
	// the application hash key
	public static final byte[] hashkey = ("lDfRNCKT" + "slj0W093" + "IaJXSTqY" + "oZ4XVAld").getBytes();
	
	// The application MAC key
	public static final byte[] mackey = ("W4cvzRAB" + "GuvAZcFB" + "gHy4JNH3" + "5v8XGAV8").getBytes();
}
