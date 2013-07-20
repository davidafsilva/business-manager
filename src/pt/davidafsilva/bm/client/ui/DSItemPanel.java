package pt.davidafsilva.bm.client.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import pt.davidafsilva.bm.client.ui.event.ContentChangedEvent;
import pt.davidafsilva.bm.client.ui.event.ContentChangedEvent.ContentChangedEventListener;
import pt.davidafsilva.bm.client.ui.event.ContentRequestEvent;
import pt.davidafsilva.bm.client.ui.event.ContentRequestEvent.ContentRequestEventListener;
import pt.davidafsilva.bm.client.ui.event.SelectionChangeEvent;
import pt.davidafsilva.bm.client.ui.event.SelectionChangeEvent.SelectionChangeEventListener;


/**
 * DSItemPanel.java
 * 
 * The Item container
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 2:43:19 PM
 */
public class DSItemPanel extends DSPanel {
	
	private static final long serialVersionUID = 527876325254965694L;
	
	// the selected item
	private DSItem selectedItem;
	
	// items
	private final Map<Integer, DSItem> items = new HashMap<Integer, DSItem>();
	
	// the items content container
	private DSPanel container;
	
	// the event subscribers
	protected EventListenerList eventSubscribers = new EventListenerList();
	
	// the content change handler
	private final ContentChangedEventListener contentHandler = new ContentChangedEventListener() {
		
		@Override
		public void onContentChanged(ContentChangedEvent evt) {
			if (evt.getSource() == selectedItem) {
				refreshContents();
			}
		}
	};
	
	// the selection change handler
	private final SelectionChangeEventListener selectionHandler = new SelectionChangeEventListener() {
		
		@Override
		public void onSelectionChange(SelectionChangeEvent evt) {
			if (evt.getSource() != selectedItem && ((DSItem) evt.getSource()).isSelected()) {
				if (selectedItem != null) {
					selectedItem.setSelected(false);
				}
				selectedItem = (DSItem) evt.getSource();
				
				if (container != null) {
					if (useFadeAnimation) {
						//TODO:
						//						container.fadeOut(new AnimationCompleteCallback() {
						//							
						//							@Override
						//							public void onAnimationComplete() {
						//								container.removeAll();
						//								container.repaint();
						//								if (selectedItem.getContents() != null) {
						//									container.add(selectedItem.getContents());
						//									container.repaint();
						//									container.revalidate();
						//									container.fadeIn();
						//								}
						//								fireSelectionChangeEvent();
						//							}
						//						});
					} else {
						SwingUtilities.invokeLater(new Runnable() {
							
							/* (non-Javadoc)
							 * @see java.lang.Runnable#run()
							 */
							@Override
							public void run() {
								container.removeAll();
								container.repaint();
								if (selectedItem.getContents() != null) {
									refreshContents();
								} else {
									fireContentRequestEvent();
								}
							}
						});
					}
				} else {
					fireSelectionChangeEvent();
				}
			}
		}
	};
	
	
	// largest item
	private Dimension largestSize;
	
	// fade animation flag
	private boolean useFadeAnimation = false;
	
	/**
	 * The constructor
	 */
	public DSItemPanel() {
		super();
		setDoubleBuffered(true);
	}
	
	/**
	 * Turns on/off the fading animation for the items content
	 * transition.
	 * 
	 * @param flag
	 *        The flag to set
	 */
	public void setFadeAnimationEnabled(boolean flag) {
		useFadeAnimation = flag;
	}
	
	/**
	 * Checks if the fade animation is currently
	 * enabled.
	 * 
	 * @return
	 *         <code>true</code> if the fade animation is
	 *         enabled, <code>false</code> otherwise.
	 */
	public boolean isFadeAnimationEnabled() {
		return useFadeAnimation;
	}
	
	/**
	 * Gets the current selected item
	 * 
	 * @return
	 *         The current selected item
	 */
	public DSItem getSelectedItem() {
		return selectedItem;
	}
	
	/**
	 * Sets the selected item
	 * 
	 * @param item
	 *        The selected item
	 */
	public void setSelectedItem(DSItem item) {
		if (getSelectedItem() != item) {
			selectedItem.setSelected(false);
			item.setSelected(true);
		}
	}
	
	/**
	 * Subscribes the selection change event
	 * 
	 * @param listener
	 *        The listener to register
	 */
	public void addSelectionChangeListener(SelectionChangeEventListener listener) {
		eventSubscribers.add(SelectionChangeEventListener.class, listener);
	}
	
	/**
	 * Unsubscribe the selection change listener
	 * 
	 * @param listener
	 *        The listener to remove
	 */
	public void removeSelectionChangeListener(SelectionChangeEventListener listener) {
		eventSubscribers.remove(SelectionChangeEventListener.class, listener);
	}
	
	/**
	 * Fires the selection change event
	 */
	private void fireSelectionChangeEvent() {
		SelectionChangeEvent evt = new SelectionChangeEvent(this);
		for (SelectionChangeEventListener listener : eventSubscribers.getListeners(SelectionChangeEventListener.class)) {
			listener.onSelectionChange(evt);
		}
	}
	
	/**
	 * Sets the items container
	 * 
	 * @param container
	 *        The items container
	 */
	public void setItemsContainer(DSPanel container) {
		this.container = container;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Container#add(java.awt.Component)
	 */
	@Override
	public Component add(Component comp) {
		if (comp instanceof DSItem) {
			((DSItem) comp).addSelectionChangeListener(selectionHandler);
			((DSItem) comp).addContentChangedListener(contentHandler);
			items.put(getComponentCount(), (DSItem) comp);
			setProperSize((DSItem) comp);
		}
		
		return super.add(comp);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Container#add(java.awt.Component, int)
	 */
	@Override
	public Component add(Component comp, int index) {
		if (comp instanceof DSItem) {
			((DSItem) comp).addSelectionChangeListener(selectionHandler);
			((DSItem) comp).addContentChangedListener(contentHandler);
			items.put(index, (DSItem) comp);
			setProperSize((DSItem) comp);
		}
		
		return super.add(comp, index);
	}
	
	/**
	 * Sets the size of the child items according to the largest
	 * size among those found.
	 */
	private void setProperSize(DSItem item) {
		if (largestSize == null) {
			largestSize = new Dimension(item.getWidth(), item.getHeight());
		} else {
			boolean changed = false;
			if (item.getHeight() > largestSize.getHeight()) {
				largestSize.setSize(largestSize.getWidth(), item.getHeight());
				changed = true;
			}
			
			if (item.getWidth() > largestSize.getWidth()) {
				largestSize.setSize(item.getWidth(), largestSize.getHeight());
				changed = true;
			}
			
			if (changed) {
				for (DSItem i : items.values()) {
					i.setSize(largestSize);
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Container#remove(java.awt.Component)
	 */
	@Override
	public void remove(Component comp) {
		if (comp instanceof DSItem) {
			((DSItem) comp).removeSelectionChangeListener(selectionHandler);
			((DSItem) comp).removeContentChangedListener(contentHandler);
			for (int idx = 0; idx < getComponentCount(); idx++) {
				if (getComponent(idx) == comp) {
					items.remove(idx);
					break;
				}
			}
		}
		
		super.remove(comp);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Container#remove(int)
	 */
	@Override
	public void remove(int index) {
		Component comp = getComponent(index);
		if (comp instanceof DSItem) {
			((DSItem) comp).removeSelectionChangeListener(selectionHandler);
			((DSItem) comp).removeContentChangedListener(contentHandler);
			items.remove(index);
		}
		
		super.remove(index);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Container#removeAll()
	 */
	@Override
	public void removeAll() {
		for (Component comp : getComponents()) {
			if (comp instanceof DSItem) {
				((DSItem) comp).removeSelectionChangeListener(selectionHandler);
				((DSItem) comp).removeContentChangedListener(contentHandler);
			}
			items.clear();
		}
		
		super.removeAll();
	}
	
	
	/**
	 * Subscribes the contents request event
	 * 
	 * @param listener
	 *        The listener to register
	 */
	public void addContentRequestListener(ContentRequestEventListener listener) {
		eventSubscribers.add(ContentRequestEventListener.class, listener);
	}
	
	/**
	 * Unsubscribe the contents request listener
	 * 
	 * @param listener
	 *        The listener to remove
	 */
	public void removeContentRequestListener(ContentRequestEventListener listener) {
		eventSubscribers.remove(ContentRequestEventListener.class, listener);
	}
	
	/**
	 * Fires the selection change event
	 */
	private void fireContentRequestEvent() {
		ContentRequestEvent evt = new ContentRequestEvent(selectedItem);
		for (ContentRequestEventListener listener : eventSubscribers.getListeners(ContentRequestEventListener.class)) {
			listener.onContentRequest(evt);
		}
	}
	
	/**
	 * Refreshes the current contents
	 */
	private void refreshContents() {
		if (SwingUtilities.isEventDispatchThread()) {
			if (selectedItem.getConstraints() != null) {
				container.add(selectedItem.getContents(), selectedItem.getConstraints());
			} else {
				container.add(selectedItem.getContents());
			}
			container.repaint();
			container.revalidate();
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				
				/* (non-Javadoc)
				 * @see java.lang.Runnable#run()
				 */
				@Override
				public void run() {
					refreshContents();
				}
			});
		}
	}
}
