package pt.davidafsilva.bm.client.ui.grid;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIDefaults;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import pt.davidafsilva.bm.client.ui.utils.DSLazyValue;


/**
 * DSDataGrid.java
 * 
 * The data grid component
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 10:34:29 PM
 */
public class DSDataGrid extends JTable {
	
	private static final long serialVersionUID = -5258017338066422575L;
	
	// editable status
	private boolean editable;
	
	// even row color
	private Color evenBackgroundColor = new Color(0xeaf5ff);
	private Color evenForeColor = getForeground();
	
	/**
	 * Component constructor
	 */
	public DSDataGrid() {
		super();
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setAutoResizeMode(AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		setShowGrid(false);
		setSelectionBackground(new Color(170, 187, 204));
		setRowSelectionAllowed(true);
		addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				fixColumnWidth(e.getComponent().getSize().width);
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
	}
	
	/**
	 * Sets the grid columns and initializes the grid model
	 * 
	 * @param columns
	 *        The grid columns
	 * @see #createDefaultDataModel()
	 */
	public <T extends Object> void setColumns(List<DSDataGridColumn<T, ?>> columns) {
		if (getModel() == null) {
			setModel(createDefaultDataModel());
		}
		
		getModel().setColumns(columns);
	}
	
	/**
	 * Removed since DS framework 1.0 <br/>
	 * This method will try to cast the column to {@link DSDataGridColumn} class, if it fails it'll
	 * throw an exception.
	 * Use {@link #addColumn(DSDataGridColumn)} instead.
	 * 
	 * @throws UnsupportedOperationException
	 *         If <code>aColumn</code> isn't a subclass of {@link DSDataGridColumn}
	 * @see {@link #addColumn(DSDataGridColumn)}
	 */
	@Override
	@Deprecated
	public void addColumn(TableColumn aColumn) {
		if (aColumn instanceof DSDataGridColumn) {
			addColumn((DSDataGridColumn<?, ?>) aColumn);
		} else {
			throw new UnsupportedOperationException("Invalid column. The columns class must be or extend DSDataGridColumn.");
		}
	}
	
	/**
	 * Adds the given column to the data model.
	 * If the grid model isn't already initialized, it'll be initialized.
	 * 
	 * @param column
	 *        The grid column
	 * @see #createDefaultDataModel()
	 */
	public <T extends Object, V extends Object> void addColumn(DSDataGridColumn<T, V> column) {
		if (getModel() == null) {
			setModel(createDefaultDataModel());
		}
		//super.addColumn(column);
		getModel().addColumn(column);
	}
	
	/**
	 * Adds the given grid columns to the grid model.
	 * If the grid model isn't already initialized, it'll be initialized.
	 * 
	 * @param columns
	 *        The grid columns
	 * @see #createDefaultDataModel()
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void addColumns(DSDataGridColumn... columns) {
		for (DSDataGridColumn column : columns) {
			addColumn(column);
		}
	}
	
	/**
	 * Sets the grid data source
	 * 
	 * @param datasource
	 *        The data source
	 */
	public <T extends Object> void setDataSource(List<T> datasource) {
		getModel().setDataSource(datasource);
	}
	
	/**
	 * Sets the grid as (non-)editable
	 * 
	 * @param editable
	 *        The editable status to set
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	/**
	 * Gets the even row background color
	 * 
	 * @return
	 *         The even row background color
	 */
	public Color getEvenRowBackgroundColor() {
		return evenBackgroundColor;
	}
	
	/**
	 * Sets the even row background color
	 * 
	 * @param c
	 *        The desired background color
	 */
	public void setEvenRowBackgroundColor(Color c) {
		evenBackgroundColor = c;
	}
	
	/**
	 * Gets the even row foreground color
	 * 
	 * @return
	 *         The even row foreground color
	 */
	public Color getEvenRowForegroundColor() {
		return evenForeColor;
	}
	
	/**
	 * Sets the even row foreground color
	 * 
	 * @param c
	 *        The desired foreground color
	 */
	public void setEvenRowForegroundColor(Color c) {
		evenForeColor = c;
	}
	
	/**
	 * Gets the selected item
	 * 
	 * @return The selected item
	 */
	public Object getSelectedItem() {
		Object selected = null;
		int rowIdx = getSelectedRow();
		if (rowIdx >= 0) {
			selected = getModel().getItemAt(rowIdx);
		}
		return selected;
	}
	
	/**
	 * Sets the data model for the grid to <code>dataModel</code> and registers
	 * with it for listener notifications from the new data model.
	 * 
	 * @param dataModel
	 *        The new mode for this grid
	 * @throws IllegalArgumentException
	 *         if <code>dataModel</code> is <code>null</code>
	 * @see #getModel
	 */
	public void setModel(DSDataGridModel dataModel) {
		super.setModel(dataModel);
	}
	
	/**
	 * Removed since DS framework 1.0 <br/>
	 * This method will try to cast the model to {@link DSDataGridModel} class, if it fails it'll
	 * throw an exception.
	 * Use {@link #setModel(DSDataGridModel)} instead.
	 * 
	 * @throws UnsupportedOperationException
	 *         If <code>dataModel</code> isn't a subclass of {@link DSDataGridModel}
	 * @see {@link #setModel(DSDataGridModel)}
	 */
	@Override
	@Deprecated
	public final void setModel(TableModel dataModel) {
		if (dataModel instanceof DSDataGridModel) {
			setModel((DSDataGridModel) dataModel);
		} else {
			throw new UnsupportedOperationException("Invalid data model for setModel. The data model class must be or extend DSDataGridModel.");
		}
	}
	
	/**
	 * Returns the grid model that provides the data displayed by this <code>grid</code>.
	 * 
	 * @return
	 *         The grid model for this <code>grid</code>
	 * @see #setModel
	 */
	@Override
	public DSDataGridModel getModel() {
		return (DSDataGridModel) super.getModel();
	}
	
	/**
	 * Removed since DS framework 1.0 <br/>
	 * This method will try to cast the model to {@link DSDataGridColumnModel} class, if it fails it'll
	 * throw an exception.
	 * Use {@link #setColumnModel(DSDataGridColumnModel)} instead.
	 * 
	 * @throws UnsupportedOperationException
	 *         If <code>columnModel</code> isn't a subclass of {@link DSDataGridColumnModel}
	 * @see {@link #setColumnModel(DSDataGridColumnModel)}
	 */
	@Override
	@Deprecated
	public void setColumnModel(TableColumnModel columnModel) {
		if (columnModel instanceof DSDataGridColumnModel) {
			setColumnModel((DSDataGridColumnModel) columnModel);
		} else {
			throw new UnsupportedOperationException("Invalid column model for setColumnModel. The column model class must be or extend DSDataGridColumnModel.");
		}
	}
	
	/**
	 * Sets the column model for this grid to <code>columnModel</code> and registers
	 * for listener notifications from the new column model. Also sets
	 * the column model of the <code>grid header</code> to <code>columnModel</code>.
	 * 
	 * @param columnModel
	 *        The new data source for this table
	 * @exception IllegalArgumentException
	 *            If <code>columnModel</code> is <code>null</code>
	 * @see #getColumnModel
	 */
	public void setColumnModel(DSDataGridColumnModel columnModel) {
		super.setColumnModel(columnModel);
	}
	
	/**
	 * Returns the <code>TableColumnModel</code> that contains all column information
	 * of this table.
	 * 
	 * @return the object that provides the column state of the table
	 * @see #setColumnModel
	 */
	@Override
	public DSDataGridColumnModel getColumnModel() {
		return (DSDataGridColumnModel) super.getColumnModel();
	}
	
	/**
	 * Returns <code>true</code> if the cell at <code>row</code> and <code>column</code> is editable and the grid is also editable.
	 * Otherwise, invoking <code>setValueAt</code> on the cell will have no effect.
	 * 
	 * <p>
	 * <b>Note</b>: The column is specified in the table view's display order, and not in the <code>TableModel</code>'s column order. This is an important distinction because as the user rearranges the columns in the table, the column at a given index in the view will change. Meanwhile the user's actions never affect the model's column ordering.
	 * 
	 * 
	 * @param row
	 *        the row whose value is to be queried
	 * @param column
	 *        the column whose value is to be queried
	 * @return <code>true</code> if the cell is editable, <code>false</code> otherwise. {@link #setEditable(boolean)}
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		return editable && super.isCellEditable(row, column);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JTable#prepareRenderer(javax.swing.table.TableCellRenderer, int, int)
	 */
	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		//		Object value = getValueAt(row, column);
		//		
		//		boolean isSelected = false;
		//		boolean hasFocus = false;
		//		
		//		// Only indicate the selection and focused cell if not printing
		//		if (!isPaintingForPrint()) {
		//			isSelected = isCellSelected(row, column);
		//			
		//			boolean rowIsLead =
		//					selectionModel.getLeadSelectionIndex() == row;
		//			boolean colIsLead =
		//					columnModel.getSelectionModel().getLeadSelectionIndex() == column;
		//			
		//			hasFocus = rowIsLead && colIsLead && isFocusOwner();
		//		}
		//		
		//		Component c = renderer.getTableCellRendererComponent(this, value,
		//				isSelected, hasFocus,
		//				row, column);
		Component c = super.prepareRenderer(renderer, row, column);
		
		boolean isSelected = isCellSelected(row, column);
		if (row % 2 != 0 && !isSelected) {
			c.setBackground(evenBackgroundColor);
			c.setForeground(evenForeColor);
		}
		
		if (isSelected) {
			// there's no border over selection
			((JComponent) c).setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		}
		
		
		return c;
	}
	
	/**
	 * Returns the default column model object, which is <code>DSDataGridColumnModel</code>. A subclass can override this
	 * method to return a different column model object.
	 * 
	 * @return the default column model object
	 * @see DSDataGridColumnModel
	 */
	@Override
	protected DSDataGridColumnModel createDefaultColumnModel() {
		return new DSDataGridColumnModel();
	}
	
	/**
	 * Returns the default table model object, which is
	 * a <code>DSDataGridModel</code>. A subclass can override this
	 * method to return a different table model object.
	 * 
	 * @return the default table model object
	 * @see DSDataGridModel
	 */
	@Override
	protected DSDataGridModel createDefaultDataModel() {
		return new DSDataGridModel();
	}
	
	/**
	 * Creates default columns for the table from
	 * the data model using the <code>getColumnCount</code> method
	 * defined in the <code>TableModel</code> interface. <br/>
	 * 
	 * Clears any existing columns before creating the
	 * new columns based on information from the model.
	 * 
	 * @see #getAutoCreateColumnsFromModel
	 */
	@Override
	public void createDefaultColumnsFromModel() {
		DSDataGridModel m = getModel();
		if (m != null) {
			// Remove any current columns
			TableColumnModel cm = getColumnModel();
			while (cm.getColumnCount() > 0) {
				cm.removeColumn(cm.getColumn(0));
			}
			
			// Create new columns from the data model info
			for (int i = 0; i < m.getColumnCount(); i++) {
				DSDataGridColumn<?, ?> col = m.getColumnAt(i);
				col.setModelIndex(i);
				super.addColumn(col);
			}
		}
	}
	
	/**
	 * Creates default cell renderers for objects, numbers, doubles, dates,
	 * booleans, and icons.
	 */
	@Override
	protected void createDefaultRenderers() {
		defaultRenderersByColumnClass = new UIDefaults(10, 0.75f);
		
		// Objects
		setLazyValue(Object.class, "pt.davidafsilva.bm.client.ui.grid.renderer.DSTextCell$UIResource");
		
		// String
		setLazyValue(String.class, "pt.davidafsilva.bm.client.ui.grid.renderer.DSTextCell$UIResource");
		
		// Integer, Numbers, Doubles and Floats
		setLazyValue(Integer.class, "pt.davidafsilva.bm.client.ui.grid.renderer.DSNumberCell");
		setLazyValue(Number.class, "pt.davidafsilva.bm.client.ui.grid.renderer.DSNumberCell");
		setLazyValue(Float.class, "pt.davidafsilva.bm.client.ui.grid.renderer.DSNumberCell");
		setLazyValue(Double.class, "pt.davidafsilva.bm.client.ui.grid.renderer.DSNumberCell");
		
		// Dates
		setLazyValue(Date.class, "pt.davidafsilva.bm.client.ui.grid.renderer.DSDateCell");
		
		// Icons and ImageIcons
		setLazyValue(Icon.class, "pt.davidafsilva.bm.client.ui.grid.renderer.DSImageCell");
		setLazyValue(ImageIcon.class, "pt.davidafsilva.bm.client.ui.grid.renderer.DSImageCell");
		
		// Booleans
		setLazyValue(Boolean.class, "pt.davidafsilva.bm.client.ui.grid.renderer.DSCheckBoxCell");
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JTable#createDefaultEditors()
	 */
	@Override
	protected void createDefaultEditors() {
		// TODO Auto-generated method stub
		super.createDefaultEditors();
	}
	
	@SuppressWarnings({"unchecked"})
	private void setLazyValue(Class<?> c, String s) {
		defaultRenderersByColumnClass.put(c, new DSLazyValue(s));
	}
	
	/**
	 * Fix the columns size - since they are defined in percentage
	 * 
	 * @param tWidth
	 *        The component width (100%)
	 */
	private synchronized void fixColumnWidth(int tWidth) {
		int rMode = getAutoResizeMode();
		try {
			setAutoResizeMode(AUTO_RESIZE_OFF);
			// get the total width in percentage
			int total = 0;
			for (int i = 0; i < getColumnModel().getColumnCount(); i++) {
				total += ((DSDataGridColumn<?, ?>) getColumnModel().getColumn(i)).getPctWidth();
			}
			// fix for the percentage value
			if (total < 100) {
				total = 100;
			}
			int consumed = 0;
			
			DSDataGridColumn<?, ?> column;
			int cWidth = 0;
			int finalWidth = 0;
			for (int i = 0; i < getColumnModel().getColumnCount(); i++) {
				column = (DSDataGridColumn<?, ?>) getColumnModel().getColumn(i);
				cWidth = column.getPctWidth();
				consumed += column.getPctWidth();
				if ((i + 1) == getColumnCount() && consumed < total) {
					cWidth = total - consumed + column.getWidth();
				}
				finalWidth = cWidth * tWidth / 100;
				
				column.setWidth(finalWidth);
				column.setPreferredWidth(finalWidth);
			}
		} finally {
			setAutoResizeMode(rMode);
		}
	}
}
