package pt.davidafsilva.bm.client.ui.grid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.swing.table.AbstractTableModel;


/**
 * DSDataGridModel.java
 * 
 * The data grid model implementation
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 3:36:35 PM
 */
public class DSDataGridModel extends AbstractTableModel implements Serializable {
	
	private static final long serialVersionUID = 5191080440547152564L;
	
	// the columns
	private DSDataGridColumn<Object, Object>[] columns;
	
	// the data source
	private List<Object> datasource;
	
	/**
	 * Creates the data model
	 */
	public DSDataGridModel() {
	}
	
	/**
	 * Creates the data model base on the grid columns
	 * 
	 * @param columns
	 *        The grid columns
	 */
	@SuppressWarnings({"unchecked"})
	public <T extends Object, V extends Object> DSDataGridModel(List<DSDataGridColumn<T, V>> columns) {
		this.columns = columns.toArray(new DSDataGridColumn[columns.size()]);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return datasource == null ? 0 : datasource.size();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return columns == null ? 0 : columns.length;
	}
	
	/**
	 * Sets the columns of the data model
	 * 
	 * @param columns
	 *        The grid columns
	 */
	@SuppressWarnings({"unchecked"})
	public <T extends Object> void setColumns(List<DSDataGridColumn<T, ?>> columns) {
		this.columns = columns.toArray(new DSDataGridColumn[columns.size()]);
		fireTableStructureChanged();
	}
	
	/**
	 * Adds the given column to the data model
	 * 
	 * @param column
	 *        The grid column
	 */
	@SuppressWarnings({"unchecked"})
	protected <T extends Object, V extends Object> void addColumn(DSDataGridColumn<T, V> column) {
		int index = this.columns == null ? 0 : this.columns.length;
		resizeColumnsArray();
		this.columns[index] = (DSDataGridColumn<Object, Object>) column;
		fireTableStructureChanged();
	}
	
	/**
	 * Resizes the columns array
	 */
	@SuppressWarnings({"unchecked"})
	private void resizeColumnsArray() {
		if (this.columns == null) {
			this.columns = new DSDataGridColumn[1];
		} else {
			DSDataGridColumn<Object, Object>[] tmp = new DSDataGridColumn[columns.length + 1];
			for (int i = 0; i < columns.length; i++) {
				tmp[i] = columns[i];
			}
			this.columns = tmp;
		}
	}
	
	/**
	 * Gets the existent columns at the model
	 * 
	 * @return
	 *         The columns defined in this model
	 */
	public List<DSDataGridColumn<Object, Object>> getColumns() {
		return Arrays.asList(this.columns);
	}
	
	/**
	 * Gets the column at the given index
	 * 
	 * @param index
	 *        The index of the column
	 * @return
	 *         The column at the given index
	 * @throws ArrayIndexOutOfBoundsException
	 *         if the given index is out of the columns array range
	 */
	public DSDataGridColumn<Object, Object> getColumnAt(int index) {
		return this.columns[index];
	}
	
	/**
	 * Sets the grid data source
	 * 
	 * @param datasource
	 *        The data source
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> void setDataSource(List<T> datasource) {
		this.datasource = (List<Object>) datasource;
		fireTableDataChanged();
	}
	
	/**
	 * Gets the grid data source
	 * 
	 * @return
	 *         The data source objects
	 */
	public List<Object> getDataSource() {
		return this.datasource;
	}
	
	/**
	 * Adds the given item to the end of the data source
	 * 
	 * @param item
	 *        The item to add
	 * @return
	 *         <code>true</code> if the data source changed as a result of the call
	 */
	public boolean addItem(Object item) {
		if (datasource == null) {
			datasource = new ArrayList<Object>();
		}
		return addItem(item, datasource.size());
	}
	
	/**
	 * Adds the given item to the data source at the given position
	 * 
	 * @param item
	 *        The item to add
	 * @param position
	 *        The item position (0 based index)
	 * @return
	 *         <code>true</code> if the data source changed as a result of the call
	 */
	public boolean addItem(Object item, int position) {
		if (datasource == null) {
			datasource = new ArrayList<Object>();
		}
		int prevSize = datasource.size();
		datasource.add(position, item);
		if (datasource.size() > prevSize) {
			fireTableRowsInserted(position, position);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Deletes the given item from the data source
	 * 
	 * @param item
	 *        The item to delete
	 * @return
	 *         <code>true</code> if the item was removed as a result of the call
	 */
	public boolean deleteItem(Object item) {
		int position = datasource.indexOf(item);
		boolean result = false;
		if (position >= 0) {
			result = datasource.remove(item);
			fireTableRowsDeleted(position, position);
		}
		
		return result;
	}
	
	/**
	 * Deletes the item at the given position at the data source
	 * 
	 * @param position
	 *        The position of the item to remove (0 based index)
	 * @return
	 *         <code>true</code> if the item was removed as a result of the call
	 */
	public boolean deleteItem(int position) {
		int prevSize = datasource.size();
		datasource.remove(position);
		if (datasource.size() < prevSize) {
			fireTableRowsDeleted(position, position);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Updates the given item at the data source
	 * 
	 * @param item
	 *        The item to update
	 * @return
	 *         <code>true</code> if the item was updated as a result of the call
	 */
	public boolean updateItem(Object item) {
		return updateOrReplaceItem(item, item);
		
	}
	
	/**
	 * Replaces <code>oldItem</code> at the data source for <code>newItem</code>.
	 * 
	 * @param oldItem
	 *        The existent item at the data source
	 * @param newItem
	 *        The item to be place in the <code>oldItem</code> position
	 * @return
	 *         <code>true</code> if the item was replaced as a result of the call
	 */
	public boolean replaceItem(Object oldItem, Object newItem) {
		return updateOrReplaceItem(oldItem, newItem);
	}
	
	/*
	 * Auxiliary method for replace/update items
	 */
	private boolean updateOrReplaceItem(Object item, Object newItem) {
		int idx = 0;
		boolean found = false;
		
		if (newItem != null) {
			Iterator<Object> iter = datasource.iterator();
			Object obj = null;
			while (iter.hasNext()) {
				obj = iter.next();
				if (obj == item || obj.equals(item)) {
					datasource.set(idx, newItem);
					found = true;
					break;
				}
				idx++;
			}
		} else {
			idx = datasource.indexOf(item);
			found = idx >= 0;
		}
		
		if (found) {
			fireTableRowsUpdated(idx, idx);
		}
		
		return found;
	}
	
	/**
	 * Returns the cell editable status
	 * 
	 * @param rowIndex
	 *        The row being queried
	 * @param columnIndex
	 *        The column being queried
	 * @return
	 *         <code>true</code> if the cell is editable <code>false</code> otherwise
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columns[columnIndex].isEditable();
	}
	
	/**
	 * Sets the value in the cell at <code>columnIndex</code> and <code>rowIndex</code> to <code>aValue</code>.
	 * 
	 * @param aValue
	 *        the new value
	 * @param rowIndex
	 *        the row whose value is to be changed
	 * @param columnIndex
	 *        the column whose value is to be changed
	 * @see #getValueAt
	 * @throws ArrayIndexOutOfBoundsException
	 *         if one of given indexes are out of bounds
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) throws ArrayIndexOutOfBoundsException {
		return columns[columnIndex].getValue(datasource.get(rowIndex));
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
	 */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (isCellEditable(rowIndex, columnIndex)) {
			if (aValue != null && columns[columnIndex].getType() != null) {
				switch (columns[columnIndex].getType()) {
					case NUMERIC:
						String tmp = aValue.toString().trim();
						try {
							aValue = Integer.parseInt(tmp);
						} catch (Exception e) {
							aValue = null;
						}
						break;
					case DECIMAL:
						String dbl = aValue.toString().replaceFirst(",", ".");
						try {
							aValue = Double.parseDouble(dbl);
						} catch (Exception e) {
							aValue = null;
						}
						break;
					default:
						break;
				}
			}
			columns[columnIndex].setValue(datasource.get(rowIndex), aValue);
			fireTableRowsUpdated(rowIndex, rowIndex);
			//fireTableCellUpdated(rowIndex, columnIndex);
		}
	}
	
	/**
	 * Returns the name for the column at the given index.
	 * If <code>column</code> cannot be found, returns an empty string.
	 * 
	 * @param column
	 *        the column being queried
	 * @return a string containing the name of <code>column</code>
	 */
	@Override
	public String getColumnName(int column) {
		return columns[column].getName();
	}
	
	
	/**
	 * Returns the class of the column at the given index
	 * 
	 * @param columnIndex
	 *        The column being queried
	 * @return
	 *         The class of the column
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		Object value = null;
		for (int row = 0; row < getRowCount(); row++) {
			value = getValueAt(row, columnIndex);
			if (value != null) {
				break;
			}
		}
		
		return value == null ? Object.class : value.getClass();
	}
	
	/**
	 * Gets the item at <code>rowIndex</code> position.
	 * 
	 * @param rowIndex
	 *        The row index
	 * @return The object at <code>rowIndex</code>
	 */
	public Object getItemAt(int rowIndex) {
		return datasource != null && datasource.size() > rowIndex ? datasource.get(rowIndex) : null;
	}
	
	/**
	 * Refreshes the currently listed items
	 */
	public void refreshListedItems() {
		fireTableRowsUpdated(0, datasource == null ? 0 : datasource.size() - 1);
	}
	
	/**
	 * Clears the current data source and the view
	 */
	public void clear() {
		int idx = datasource == null ? 0 : datasource.size() - 1;
		datasource = null;
		fireTableRowsUpdated(0, idx);
	}
	
}
