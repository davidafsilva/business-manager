package pt.davidafsilva.bm.client.ui.grid.renderer;

import java.awt.Component;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import org.apache.commons.codec.binary.Base64;



/**
 * DSImageCell.java
 * 
 * The image cell
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 4:24:44 PM
 */
public class DSImageCell extends DSTextCell.UIResource {
	
	private static final long serialVersionUID = 580969127301589615L;
	
	/**
	 * Instantiates the image cell as a centered image label.
	 */
	public DSImageCell() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.client.ui.grid.renderer.DSTextCell#getTableCellRendererComponent
	 * (javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, null, isSelected, hasFocus, row, column);
		
		// set the image based on the value type
		if (value == null) {
			setIcon(null);
		} else {
			if (value instanceof Image) {
				// image
				setIcon(new ImageIcon((Image) value));
			} else if (value instanceof Icon) {
				// icon
				setIcon((Icon) value);
			} else if (value instanceof URL) {
				// URL
				setIcon(new ImageIcon((URL) value));
			} else if (value instanceof String) {
				if (value.toString().startsWith("file://")) {
					// file path
					setIcon(new ImageIcon(value.toString()));
				} else {
					// base 64 image
					byte[] byteImage = Base64.decodeBase64(value.toString());
					try {
						Image image = ImageIO.read(new ByteArrayInputStream(byteImage));
						setIcon(new ImageIcon(image));
					} catch (IOException e) {
						System.err.println("Unable to load column image.");
					}
				}
			}
		}
		return this;
	}
}
