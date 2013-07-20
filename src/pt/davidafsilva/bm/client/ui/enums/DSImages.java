package pt.davidafsilva.bm.client.ui.enums;


/**
 * DSImages.java
 * @author David Silva <david@davidafsilva.pt>
 * @date 9:11:47 PM
 */
public enum DSImages {
	DROPDOWN_ARROW("iVBORw0KGgoAAAANSUhEUgAAABkAAAAXCAIAAABxgXNEAAAAZklEQVQ4y2M6Qz3AxMDAYEwNADQHZNZ/agAamEUtMJj9OGLNqsMByEkTjY2NRAoSlSYaGhqQlQK5FKVVuHFo5uJ01z+8ABJM/wiBQZ6HRvM2CWb9+vXrBzUA0BymL1++XKIGAJoDADkkeulkMtawAAAAAElFTkSuQmCC");
	
	private String data;
	
	/**
	 * Enumeration constructor
	 * @param data
	 * 		The image data
	 */
	DSImages(String data) {
		this.data = data;
	}
	
	
	/**
	 * Gets the image data
	 * 
	 * @return
	 * 		The image data
	 */
	public String getData() {
		return data;
	}
}
