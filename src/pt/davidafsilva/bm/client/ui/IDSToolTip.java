package pt.davidafsilva.bm.client.ui;


/**
 * IDSToolTip.java
 * 
 * The tool tip interface
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 3:11:19 PM
 */
public interface IDSToolTip {
	
	/**
	 * Registers the text to display in a error tool tip. 
	 * 
	 * @param text
	 * 		The error tool tip text
	 */
	public void setErrorToolTipText(String text);
	
	/**
	 * Gets the error tool tip text
	 * 
	 * @return
	 * 		The error tool tip text
	 */
	public String getErrorToolTipText();
	
	/**
	 * Checks if the next tool tip to display, 
	 * is an error tool tip.
	 * 
	 * @return
	 * 		<code>true</code> if it's an error tool tip, <code>false</code> otherwise.
	 */
	public boolean isErrorToolTip();
	
	/**
	 * Sets the next tool tip to be an error tool tip
	 * if <code>status</code> = <code>true</code>.
	 * 
	 * @param status
	 * 		The status to set
	 */
	public void setIsErrorToolTip(boolean status);
	
	/**
	 * Registers the text to display in a tool tip. 
	 * 
	 * @param text
	 * 		The tool tip text
	 */
	public void setToolTipText(String text);
	
	/**
	 * Gets the error tool tip text
	 * 
	 * @return
	 * 		The error tool tip text
	 */
	public String getToolTipText();
	
}
