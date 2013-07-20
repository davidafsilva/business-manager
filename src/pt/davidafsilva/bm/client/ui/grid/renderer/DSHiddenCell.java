package pt.davidafsilva.bm.client.ui.grid.renderer;

/**
 * DSHiddenCell.java
 * @author David Silva <david@davidafsilva.pt>
 * @date 7:14:25 PM
 */
public class DSHiddenCell extends DSTextCell.UIResource {
	
	private static final long serialVersionUID = 1810369082977685181L;
	
	/**
	 * Instantiates the text cell a hidden cell
	 */
	public DSHiddenCell() {
		super();
		setVisible(false);
		setEnabled(false);
	}
}
