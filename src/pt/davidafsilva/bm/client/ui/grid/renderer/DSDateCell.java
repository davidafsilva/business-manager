package pt.davidafsilva.bm.client.ui.grid.renderer;

import java.awt.Component;
import java.text.DateFormat;
import javax.swing.JTable;


/**
 * DSDateCell.java
 * 
 * The date cell
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 4:08:32 PM
 */
public class DSDateCell extends DSTextCell.UIResource {
	
	private static final long serialVersionUID = -979269904964012286L;
	
	// the formatter
	private DateFormat formatter;
	
	/**
	 * Instantiates the date cell as a centered label with
	 * the locale date formatter.
	 */
	public DSDateCell() {
		this(DateFormat.getDateInstance());
	}
	
	/**
	 * Instantiates the date cell as a centered label
	 * @throws IllegalArgumentException
	 * 		if <code>formatter</code> is <code>null</code>.
	 */
	public DSDateCell(DateFormat formatter) {
		super();
		if (formatter == null)
			throw new IllegalArgumentException("You must provide a valid date format instance.");
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
