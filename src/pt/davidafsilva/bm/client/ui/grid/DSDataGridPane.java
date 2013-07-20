package pt.davidafsilva.bm.client.ui.grid;

import java.awt.Dimension;
import javax.swing.JScrollPane;


/**
 * DSDataGridPane.java
 * 
 * The data grid pane, which is a composite component.
 * It has a grid is the north container, and the interaction buttons
 * at the south.
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 10:09:10 PM
 */
public class DSDataGridPane extends JScrollPane {
	
	private static final long serialVersionUID = 3586565513680924290L;
	
	// the grid
	private final DSDataGrid grid;
	
	// the buttons container
	//private final JPanel leftButtons, rightButtons;
	
	//	// the buttons
	//	private final JButton insertButton, editButton, deleteButton, filterButton,
	//			removeFilterButton, printButton;
	
	/**
	 * Initializes the component
	 */
	public DSDataGridPane() {
		// container
		super(new DSDataGrid());
		
		// grid
		grid = (DSDataGrid) getViewport().getView();
		grid.setFillsViewportHeight(true);
		//		// buttons
		//		insertButton = new JButton("insert");
		//		editButton = new JButton("edit");
		//		deleteButton = new JButton("delete");
		//		filterButton = new JButton("filter");
		//		removeFilterButton = new JButton("remove filter");
		//		printButton = new JButton("print");
		//		
		//		// buttons container
		//		JPanel buttonsContainer = new JPanel(new BorderLayout());
		//		buttonsContainer.setPreferredSize(new Dimension(500, 30));
		//		
		//		leftButtons = new JPanel(new SpringLayout());
		//		buttonsContainer.add(leftButtons, BorderLayout.WEST);
		//		
		//		rightButtons = new JPanel(new SpringLayout());
		//		buttonsContainer.add(rightButtons, BorderLayout.EAST);
		//		
		//		add(buttonsContainer, BorderLayout.SOUTH);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setPreferredSize(java.awt.Dimension)
	 */
	@Override
	public void setPreferredSize(Dimension preferredSize) {
		super.setPreferredSize(new Dimension(preferredSize.width + 30, preferredSize.height));
		//grid.setPreferredSize(preferredSize);
		grid.setPreferredScrollableViewportSize(preferredSize);
	}
	
	/**
	 * Gets the grid component (inner component)
	 * 
	 * @return
	 *         The grid component
	 */
	public DSDataGrid getGrid() {
		return grid;
	}
	
	//	/**
	//	 * Sets the bottom buttons text.
	//	 * 
	//	 * If the text is <code>null</code> the button is removed from the layout.
	//	 * 
	//	 * @param insert
	//	 *        The insert button text
	//	 * @param edit
	//	 *        The edit button text
	//	 * @param delete
	//	 *        The delete button text
	//	 * @param filter
	//	 *        The filter button text
	//	 * @param removeFilter
	//	 *        The remove filter button text
	//	 * @param print
	//	 *        The print button text
	//	 */
	//	public void setButtonsText(String insert, String edit, String delete,
	//			String filter, String removeFilter, String print) {
	//		setButtonLabelOrRemove(leftButtons, insertButton, insert);
	//		setButtonLabelOrRemove(leftButtons, editButton, edit);
	//		setButtonLabelOrRemove(leftButtons, deleteButton, delete);
	//		SpringUtilities.makeCompactGrid(leftButtons, 1,
	//				leftButtons.getComponentCount(), -5, 0, -6, 0);
	//		setButtonLabelOrRemove(rightButtons, filterButton, filter);
	//		setButtonLabelOrRemove(rightButtons, removeFilterButton, removeFilter);
	//		setButtonLabelOrRemove(rightButtons, printButton, print);
	//		SpringUtilities.makeCompactGrid(rightButtons, 1,
	//				rightButtons.getComponentCount(), -5, 0, -6, 0);
	//	}
	//	
	//	/**
	//	 * Sets the button text and includes it in his container if <code>label</code> is not <code>null</code>.
	//	 * 
	//	 * @param container
	//	 *        The button container
	//	 * @param button
	//	 *        The button component
	//	 * @param label
	//	 *        The button text
	//	 */
	//	private void setButtonLabelOrRemove(JPanel container, JButton button, String label) {
	//		if (label != null) {
	//			button.setText(label);
	//			container.add(button);
	//		}
	//	}
}
