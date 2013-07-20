package pt.davidafsilva.bm.client.ui.grid.renderer;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;
import javax.swing.table.TableCellRenderer;
import pt.davidafsilva.bm.client.ui.DSCheckBox;


/**
 * DSCheckBoxCell.java
 * 
 * The check box cell
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 4:32:01 PM
 */
public class DSCheckBoxCell extends DSCheckBox implements TableCellRenderer, UIResource {
	
	private static final long serialVersionUID = 2051578047356115842L;
	
	/**
	 * Instantiates the check box cell as a centered check box
	 */
	public DSCheckBoxCell() {
		super();
		setVerticalAlignment(CENTER);
		setHorizontalAlignment(CENTER);
		setOpaque(true);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent
	 * (javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column) {
		if (isSelected) {
			super.setBackground(table.getSelectionBackground());
			setForeground(table.getSelectionForeground());
		} else {
			setBackground(table.getBackground());
			setForeground(table.getForeground());
		}
		
		if (hasFocus) {
			setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
		} else {
			setBorder(null);
		}
		
		setSelected(valueToBoolean(value));
		return this;
	}
	
	/**
	 * Infers the boolean value from the given value-object
	 * <br/>
	 * The supported affirmative boolean values are:
	 * 	<ul>
	 * 		<li>"1", "true", "yes" - as {@link String}</li>
	 * 		<li>1 - as {@link Integer}/{@link Double}</li>
	 * 	</ul>
	 * If none of the listed affirmative values are found, <code>false</code> is returned.
	 * 
	 * @param value
	 * 		The value object
	 * @return
	 * 		<code>true</code> or <code>false</code> base on
	 * 		the value-object value.
	 */
	protected boolean valueToBoolean(Object value) {
		if (value != null) {
			if (value.toString().equals("1") ||
					value.toString().equalsIgnoreCase("true") ||
					value.toString().equalsIgnoreCase("yes"))
				return true;
		}
		
		return false;
	}
}
