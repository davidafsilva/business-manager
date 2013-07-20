package pt.davidafsilva.bm.client.enums;

import java.net.URL;





/**
 * ImagesEnum.java
 * 
 * The imagens used by the UI
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 7:07:14 PM
 */
public enum ImagesEnum {
	ERROR("assets/error.png"),
	INFORMATION("assets/information.png"),
	WINDOWS_ICON("assets/money.png"),
	VAULT("assets/vault.png"),
	MENU_BILLING("assets/billing.png"),
	MENU_HISTORIC("assets/historic.png"),
	MENU_CLIENTS("assets/clients.png"),
	MENU_PRODUCTS("assets/products.png"),
	MENU_PREFERENCES("assets/preferences.png"),
	BTN_INSERT("assets/add.png"),
	BTN_EDIT("assets/edit.png"),
	BTN_DELETE("assets/delete.png"),
	BTN_DELETE_SMALL("assets/delete_small.png"),
	QUESTION("assets/question.png"),
	BTN_COPY_SMALL("assets/copy_small.png"),
	BTN_PASTE_SMALL("assets/paste_small.png");
	
	private String path;
	
	/**
	 * Enumeration constructor
	 * 
	 * @param path
	 *        The image path
	 */
	ImagesEnum(String path) {
		this.path = path;
	}
	
	
	/**
	 * Gets the image path
	 * 
	 * @return
	 *         The image path
	 */
	public URL getPath() {
		return ImagesEnum.class.getResource("/" + path);
	}
}
