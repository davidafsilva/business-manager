package pt.davidafsilva.bm.client.ui.grid;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;


/**
 * DSDataGridColumnModel.java
 * 
 * The data grid column model
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 11:31:03 AM
 */
public class DSDataGridColumnModel extends DefaultTableColumnModel {
	
	private static final long serialVersionUID = 2610070146260865004L;
	
	/**
	 * Removes all the columns from the model
	 * 
	 * @see #removeColumn(TableColumn)
	 * @see #addColumn(TableColumn)
	 */
	public void removeAllColumns() {
		for (TableColumn col : tableColumns) {
			removeColumn(col);
		}
	}
}
