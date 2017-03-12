package pt.davidafsilva.bm.shared.enums;


/**
 * ConfigurationKeys.java
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 11:14:56 AM
 */
public enum ConfigurationKey {
	EMAIL_AUTHOR("email-author"),
	EMAIL_ADDRESS_SOURCE("email-addr-src"),
	EMAIL_ADDRESS_SOURCE_PWD("email-addr-pwd"),
	EMAIL_ADDRESS_TARGET("email-addr"),
	EMAIL_SUBJECT("email-subject"),
	EMAIL_BODY("email-body"),
	SALE_INFO_PROD_CODE("sale-prod-code"),
	SALE_INFO_PROD_DESCRIPTION("sale-prod-desc"),
	SALE_INFO_PROD_QTY("sale-prod-qty"),
	SALE_INFO_PROD_PRICE("sale-prod-price"),
	SALE_INFO_DISCOUNT("sale-discount"),
	SALE_INFO_VAT("sale-vat"),
	SALE_INFO_TOTALS("sale-totals");
	
	private String code;
	
	/**
	 * Constructor
	 * 
	 * @param code
	 *        The key code
	 */
	ConfigurationKey(String code) {
		this.code = code;
	}
	
	/**
	 * Gets the key code associate with the enumeration
	 * 
	 * @return The key code
	 */
	public String getCode() {
		return code;
	}
	
}
