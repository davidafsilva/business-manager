package pt.davidafsilva.bm.client.ui.grid.renderer;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


/**
 * DSTextCell.java
 * 
 * The text cell
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 3:58:49 PM
 */
public class DSTextCell extends DefaultTableCellRenderer {
	
	private static final long serialVersionUID = -8310081185867192437L;
	
	/**
	 * Instantiates the text cell as a centered label
	 */
	public DSTextCell() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column) {
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		if (isSelected) {
			super.setBackground(table.getSelectionBackground());
			setForeground(table.getSelectionForeground());
		} else {
			setBackground(table.getBackground());
			setForeground(table.getForeground());
		}
		
		return c;
	}
	
	
	/**
	 * A subclass of <code>DSTextCell</code> that
	 * implements <code>UIResource</code>. <code>DSTextCell</code> doesn't implement <code>UIResource</code> directly so that applications can safely override the <code>cellRenderer</code> property with <code>DefaultTableCellRenderer</code> subclasses.
	 * <p>
	 * <strong>Warning:</strong> Serialized objects of this class will not be compatible with future Swing releases. The current serialization support is appropriate for short term storage or RMI between applications running the same version of Swing. As of 1.4, support for long term storage of all JavaBeans<sup><font size="-2">TM</font></sup> has been added to the <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
	 */
	public static class UIResource extends DSTextCell implements javax.swing.plaf.UIResource {
		
		private static final long serialVersionUID = -6251285924659331654L;
	}
}
