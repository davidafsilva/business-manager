package pt.davidafsilva.bm.client.ui.grid.renderer;

import java.awt.Component;
import java.text.NumberFormat;
import javax.swing.JTable;


/**
 * DSNumberCell.java
 * 
 * The number cell
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 12:29:00 PM
 */
public class DSNumberCell extends DSTextCell.UIResource {
	
	private static final long serialVersionUID = 817804496948592310L;
	
	// the formatter
	private NumberFormat formatter;
	
	/**
	 * Instantiates the number cell as a centered label with
	 * the locale number formatter.
	 */
	public DSNumberCell() {
		this(NumberFormat.getNumberInstance());
	}
	
	/**
	 * Instantiates the number cell as a centered label
	 * @throws IllegalArgumentException
	 * 		if <code>formatter</code> is <code>null</code>.
	 */
	public DSNumberCell(NumberFormat formatter) {
		super();
		if (formatter == null)
			throw new IllegalArgumentException("You must provide a valid number format instance.");
		this.formatter = formatter;
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.client.ui.grid.renderer.DSTextCell#getTableCellRendererComponent
	 * (javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column) {
		return super.getTableCellRendererComponent(table, value == null ? "" : formatter.format(value), isSelected, hasFocus, row, column);
	}
	
}
