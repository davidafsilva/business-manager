package pt.davidafsilva.bm.client.ui.grid;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import pt.davidafsilva.bm.client.ui.grid.renderer.DSCheckBoxCell;
import pt.davidafsilva.bm.client.ui.grid.renderer.DSComboBoxCell;
import pt.davidafsilva.bm.client.ui.grid.renderer.DSDateCell;
import pt.davidafsilva.bm.client.ui.grid.renderer.DSHiddenCell;
import pt.davidafsilva.bm.client.ui.grid.renderer.DSImageCell;
import pt.davidafsilva.bm.client.ui.grid.renderer.DSTextCell;


/**
 * DSDataGridColumn.java
 * 
 * The data grid column
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 9:39:59 PM
 * @param <T>
 *        The object type
 * @param <V>
 *        The column value type
 */
public abstract class DSDataGridColumn<T extends Object, V extends Object> extends TableColumn {
	
	private static final long serialVersionUID = -4835784666960163612L;
	
	// editable status
	private boolean editable;
	
	// the default width
	public static final int DEFAULT_WIDTH = 100;
	
	// the column type
	private DSDataGridColumnType type;
	
	// the percentage width
	private int pctWidth;
	
	/**
	 * Constructs a column with the default width (percentage).
	 * The column type will be calculate at the runtime by looking
	 * at the value types.
	 * 
	 * @param name
	 *        The column name
	 * @see #DEFAULT_WIDTH
	 * @see DSDataGrid#getColumnClass(int)
	 */
	public DSDataGridColumn(String name) {
		this(name, null, DEFAULT_WIDTH);
	}
	
	/**
	 * Constructs the grid column with the default width.
	 * The column type will be calculate at the runtime by looking
	 * at the value types.
	 * 
	 * @param name
	 *        The column name
	 * @param width
	 *        The column width in percentage
	 */
	public DSDataGridColumn(String name, int width) {
		this(name, null, width);
	}
	
	/**
	 * Constructs the grid column with the default width (percentage).
	 * 
	 * @param name
	 *        The column name
	 * @param type
	 *        The column type
	 * @see #DEFAULT_WIDTH
	 */
	public DSDataGridColumn(String name, DSDataGridColumnType type) {
		this(name, type, DEFAULT_WIDTH);
	}
	
	/**
	 * Constructs the grid column with the default width.
	 * 
	 * @param name
	 *        The column name
	 * @param type
	 *        The column type
	 * @param width
	 *        The column width in percentage
	 */
	public DSDataGridColumn(String name, DSDataGridColumnType type, int width) {
		super(0, 0, getRendererByType(type), getEditorByType(type));
		this.type = type;
		this.headerValue = name;
		this.pctWidth = width;
		
		if (type != null) {
			switch (type) {
				case HIDDEN:
					this.isResizable = false;
					this.width = 0;
					this.minWidth = 0;
					this.maxWidth = 0;
					break;
				default:
					break;
			}
		}
	}
	
	
	/**
	 * Gets the column type
	 * 
	 * @return the column type
	 */
	public DSDataGridColumnType getType() {
		return this.type;
	}
	
	/**
	 * Sets the model for columns types <code>=</code> {@link DSDataGridColumnType#COMBOBOX}
	 * 
	 * @param cModel
	 *        the cModel to set
	 */
	@SuppressWarnings("unchecked")
	public void setModel(ComboBoxModel<V> cModel) {
		if (type != DSDataGridColumnType.COMBOBOX) {
			throw new UnsupportedOperationException("Cannot set a model for column types other than DSDataGridColumnType.COMBOBOX");
		}
		((JComboBox<Object>) ((DefaultCellEditor) cellEditor).getComponent()).setModel((ComboBoxModel<Object>) cModel);
	}
	
	/**
	 * Sets the maximum width of the column to <code>maxWidth</code> or,
	 * if <code>maxWidth</code> is less than the minimum width,
	 * to the minimum width.
	 * 
	 * <p>
	 * If the value of the <code>width</code> or <code>preferredWidth</code> property is more than the new maximum width, this method sets that property to the new maximum width.
	 * </p>
	 * <p>
	 * if the type of the column is {@link DSDataGridColumnType#HIDDEN} an exception will be thrown, since the maximum width of those columns must be 0.
	 * </p>
	 * 
	 * @param maxWidth
	 *        the new maximum width
	 * @throws UnsupportedOperationException
	 *         If the column type is {@link DSDataGridColumnType#HIDDEN}
	 * @see #getMaxWidth
	 * @see #setPreferredWidth
	 * @see #setMinWidth
	 * @beaninfo
	 *           bound: true
	 *           description: The maximum width of the column.
	 */
	@Override
	public void setMaxWidth(int maxWidth) {
		if (type != null && type == DSDataGridColumnType.HIDDEN) {
			throw new UnsupportedOperationException("The maximum width of HIDDEN columns cannot be changed.");
		}
		super.setMaxWidth(maxWidth);
	}
	
	/**
	 * Sets whether this column can be resized.
	 * <p>
	 * if the type of the column is {@link DSDataGridColumnType#HIDDEN} an exception will be thrown, since those columns cannot be resizable columns.
	 * </p>
	 * 
	 * @param isResizable
	 *        if true, resizing is allowed; otherwise false
	 * @see #getResizable
	 * @throws UnsupportedOperationException
	 *         If the column type is {@link DSDataGridColumnType#HIDDEN}
	 * @beaninfo
	 *           bound: true
	 *           description: Whether or not this column can be resized.
	 */
	@Override
	public void setResizable(boolean isResizable) {
		if (type != null && type == DSDataGridColumnType.HIDDEN) {
			throw new UnsupportedOperationException("The resizable property of HIDDEN columns cannot be changed.");
		}
		super.setResizable(isResizable);
	}
	
	/**
	 * Resizes the column to fit the width of its header cell.
	 * 
	 * This method does nothing if the header renderer is <code>null</code> (the default case) or the column type is {@link DSDataGridColumnType#HIDDEN}.
	 * Otherwise, it sets the minimum, maximum and preferred
	 * widths of this column to the widths of the minimum, maximum and preferred
	 * sizes of the Component delivered by the header renderer.
	 * The transient "width" property of this TableColumn is also set to the
	 * preferred width. Note this method is not used internally by the table
	 * package.
	 * 
	 * @see #setPreferredWidth
	 */
	@Override
	public void sizeWidthToFit() {
		if (type != null && type == DSDataGridColumnType.HIDDEN) {
			return;
		}
		super.sizeWidthToFit();
	}
	
	
	/**
	 * Gets the cell renderer based on his type
	 * 
	 * @param type
	 *        The cell type
	 * @return
	 *         The cell renderer
	 */
	protected static TableCellRenderer getRendererByType(DSDataGridColumnType type) {
		TableCellRenderer renderer = null;
		if (type != null) {
			switch (type) {
				case TEXT:
				case NUMERIC:
				case DECIMAL:
					renderer = new DSTextCell();
					break;
				case DATE:
					renderer = new DSDateCell();
					break;
				case CHECKBOX:
					renderer = new DSCheckBoxCell();
					break;
				case COMBOBOX:
					renderer = new DSComboBoxCell();
					break;
				case IMAGE:
					renderer = new DSImageCell();
					break;
				case HIDDEN:
					renderer = new DSHiddenCell();
					break;
			}
		}
		return renderer;
	}
	
	/**
	 * Gets the cell editor based on his type
	 * 
	 * @param type
	 *        The cell type
	 * @return
	 *         The cell editor
	 */
	protected static TableCellEditor getEditorByType(DSDataGridColumnType type) {
		TableCellEditor editor = null;
		if (type != null) {
			switch (type) {
				case COMBOBOX:
					editor = new DefaultCellEditor(new JComboBox<>());
					break;
				case HIDDEN:
					editor = new DefaultCellEditor(new JTextField()); //TODO: fix
					break;
				default:
					break;
			}
		}
		
		return editor;
	}
	
	/**
	 * Gets the column name. It's also used as the header value.
	 * 
	 * @return
	 *         The column name / header value
	 */
	public String getName() {
		return headerValue.toString();
	}
	
	/**
	 * Sets the column name as well as his header value.
	 * 
	 * @param name
	 *        The name to set
	 */
	public void setName(String name) {
		this.headerValue = name;
	}
	
	/**
	 * Sets the column as (non-)editable
	 * 
	 * @param editable
	 *        The editable status to set
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	/**
	 * Gets the percentage set for the column width
	 * 
	 * @return the pctWidth value
	 */
	public int getPctWidth() {
		return this.pctWidth;
	}
	
	
	/**
	 * Sets the percentage for the column width
	 * 
	 * @param pctWidth
	 *        the pctWidth to set
	 */
	public void setPctWidth(int pctWidth) {
		this.pctWidth = pctWidth;
	}
	
	/**
	 * Checks the editable status of the column
	 * 
	 * @return
	 *         <code>true</code> if the column is editable, <code>false</code> otherwise.
	 */
	public boolean isEditable() {
		return editable;
	}
	
	/**
	 * Gets the column value
	 * 
	 * @param object
	 *        The object
	 * @return
	 *         The value of the column
	 */
	public abstract V getValue(T object);
	
	/**
	 * Sets the column value for editable columns
	 * 
	 * @param object
	 *        The object
	 * @param value
	 *        The value of the column
	 */
	public abstract void setValue(T object, V value);
}
