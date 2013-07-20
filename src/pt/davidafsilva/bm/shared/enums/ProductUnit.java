package pt.davidafsilva.bm.shared.enums;


/**
 * ProductUnit.java
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 12:55:43 PM
 */
public enum ProductUnit {
	G(1),
	KG(2);
	
	private final int code;
	
	/**
	 * The Unit constructor
	 * 
	 * @param code
	 *        The unit code
	 */
	private ProductUnit(int code) {
		this.code = code;
	}
	
	
	/**
	 * Gets the code value
	 * 
	 * @return the code value
	 */
	public int getCode() {
		return this.code;
	}
	
	public static ProductUnit fromCode(int code) {
		switch (code) {
			case 1:
				return G;
			case 2:
				return KG;
			default:
				return null;
		}
	}
	
	
	/**
	 * Gets the literal of this unit
	 * 
	 * @return The literal of the unit
	 */
	public String getLiteral() {
		switch (this.code) {
			case 1:
				return "g";
			case 2:
				return "kg";
			default:
				return null;
		}
	}
}
