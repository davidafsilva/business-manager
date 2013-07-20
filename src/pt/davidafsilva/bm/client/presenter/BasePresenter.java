package pt.davidafsilva.bm.client.presenter;

import pt.davidafsilva.bm.client.view.BaseView;


/**
 * BasePresenter.java
 * 
 * The base presenter implementation
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 9:56:11 PM
 */
@SuppressWarnings("rawtypes")
public abstract class BasePresenter<V extends BaseView> {
	
	// the presenter view
	protected V view;
	
	/**
	 * Instantiates the base presenter
	 * 
	 * @param view
	 *        The presenter's view
	 */
	@SuppressWarnings("unchecked")
	public BasePresenter(V view) {
		this.view = view;
		this.view.setPresenter(this);
	}
	
	/**
	 * Gets the presenter view
	 * 
	 * @return
	 *         The view associated with the presenter
	 */
	public V getView() {
		return this.view;
	}
	
	/**
	 * Issues a show operation
	 */
	public void show() {
		view.setVisible(true);
	}
	
	/**
	 * Issues a hide operation
	 */
	public void hide() {
		view.setVisible(false);
	}
	
	/**
	 * Issues a close operation
	 */
	public void close() {
		view.clean();
		view.close();
		destroy();
	}
	
	/**
	 * Destroys the presenter
	 */
	public void destroy() {
		view.destroy();
		view = null;
	}
	
	/**
	 * This method is called after the view is showed.
	 * Subclasses can {@link Override} this method to perform
	 * the appropriate operations after the view is ready/shown.
	 */
	public void onShow() {
		// empty by default
	}
}
