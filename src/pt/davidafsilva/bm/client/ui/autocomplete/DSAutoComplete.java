package pt.davidafsilva.bm.client.ui.autocomplete;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolTip;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import org.apache.commons.codec.binary.Base64;
import pt.davidafsilva.bm.client.ui.AnimationType;
import pt.davidafsilva.bm.client.ui.DSAnimation;
import pt.davidafsilva.bm.client.ui.DSDialog;
import pt.davidafsilva.bm.client.ui.DSErrorToolTip;
import pt.davidafsilva.bm.client.ui.DSTextField;
import pt.davidafsilva.bm.client.ui.DSToolTip;
import pt.davidafsilva.bm.client.ui.IDSToolTip;
import pt.davidafsilva.bm.client.ui.enums.DSImages;
import pt.davidafsilva.bm.client.ui.event.DataRequestEvent;
import pt.davidafsilva.bm.client.ui.event.DataRequestEvent.DataRequestEventListener;
import pt.davidafsilva.bm.client.ui.event.SelectionChangeEvent;
import pt.davidafsilva.bm.client.ui.event.SelectionChangeEvent.SelectionChangeEventListener;


/**
 * DSAutoComplete.java
 * 
 * The auto complete component
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 8:47:38 PM
 * @param <T>
 *        The type of the auto complete
 */
public abstract class DSAutoComplete<T> extends JPanel implements IDSToolTip {
	
	private static final long serialVersionUID = -1783228943759737132L;
	
	// CONSTANTS
	private final static int VISIBLE_ITEMS = 7;
	private final static int LOOKUP_ITEMS = 20;
	
	
	// the tooltip
	private DSToolTip tooltip;
	
	// the components
	private final JLayeredPane contents;
	private final DSTextField inputText;
	protected Border inputBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
	private final JLabel dropdownButton;
	private BufferedImage image;
	private JScrollPane listContainer;
	private JList<T> listView;
	
	// the data source
	private Collection<T> dataSource = null;
	
	// the selected items
	private final Map<Object, T> selectedItems = new HashMap<Object, T>();
	
	// the latest selected item - ordered
	private final List<Object> selectedItemsKeys = new ArrayList<Object>();
	
	// the selected items visual objects
	private final Map<Object, DSAutoCompleteSelectedItem> selectedItemsObjects = new HashMap<Object, DSAutoCompleteSelectedItem>();
	
	// TODO: allow multiple selection
	//private boolean allowMultipleSelection;
	
	// error tool tip
	private boolean isErrorTooltip = false;
	private DSErrorToolTip errorTooltip;
	private String errorTip = null;
	
	// the animation
	private DSAnimation animation;
	
	// the event subscribers
	// - selection change event 
	// - data request event  
	private final EventListenerList eventSubscribers = new EventListenerList();
	
	// control vars
	private boolean dataRequestHandlerSet = false;
	private boolean dataRequested = false;
	private String lastSearchedText = null;
	private ArrayList<T> matches;
	private long listViewCloseTimestamp;
	private int inputOffset = 5;
	private int highlightedItemIdx = -1;
	
	/*
	 * The input text key listener
	 */
	private final KeyListener inputKeyListener = new KeyListener() {
		
		/* (non-Javadoc)
		 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
		 */
		@Override
		public void keyTyped(KeyEvent e) {
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
		 */
		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				e.consume();
			} else if (e.getKeyCode() == KeyEvent.VK_UP) {
				e.consume();
			} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				e.consume();
			} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				e.consume();
			} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				e.consume();
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				e.consume();
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				e.consume();
			}
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
		 */
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				moveDownSelection();
				e.consume();
			} else if (e.getKeyCode() == KeyEvent.VK_UP) {
				moveUpSelection();
				e.consume();
			} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				setListViewVisible(false);
				e.consume();
			} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				if (getSelectedItems().size() > 0) {
					if (highlightedItemIdx >= 0) {
						removeItemFromSelection(selectedItems.get(selectedItemsKeys.get(highlightedItemIdx)), true);
						e.consume();
					} else if (inputText.getText().length() == 0) {
						removeLatestSelectedItem();
						e.consume();
					}
				}
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				if (getSelectedItems().size() > 0 && (inputText.getText().length() == 0 || inputText.getCaretPosition() == 0)) {
					if (highlightedItemIdx >= 0) {
						// deselect
						selectedItemsObjects.get(selectedItemsKeys.get(highlightedItemIdx)).setHighlighted(false);
						highlightedItemIdx--;
						// select
						if (highlightedItemIdx >= 0) {
							selectedItemsObjects.get(selectedItemsKeys.get(highlightedItemIdx)).setHighlighted(true);
						}
					} else {
						// first item
						highlightedItemIdx = selectedItemsKeys.size() - 1;
						selectedItemsObjects.get(selectedItemsKeys.get(highlightedItemIdx)).setHighlighted(true);
					}
					e.consume();
				}
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				if (getSelectedItems().size() > 0 && inputText.getText().length() == 0) {
					if (highlightedItemIdx >= 0) {
						// deselect
						selectedItemsObjects.get(selectedItemsKeys.get(highlightedItemIdx)).setHighlighted(false);
						highlightedItemIdx++;
						// select
						if (highlightedItemIdx < selectedItemsKeys.size()) {
							selectedItemsObjects.get(selectedItemsKeys.get(highlightedItemIdx)).setHighlighted(true);
						} else {
							highlightedItemIdx = -1;
						}
					} else {
						// first item
						highlightedItemIdx = 0;
						selectedItemsObjects.get(selectedItemsKeys.get(highlightedItemIdx)).setHighlighted(true);
					}
					e.consume();
				}
			} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (listView.getSelectedIndex() >= 0) {
					addItemToSelection(listView.getSelectedValue());
					setListViewVisible(false);
				} else if (isListViewVisible() && listView.getModel().getSize() == 1) {
					listView.setSelectedIndex(0);
					addItemToSelection(listView.getSelectedValue());
					setListViewVisible(false);
				}
				e.consume();
			}
		}
	};
	
	/**
	 * Constructs a new auto complete.
	 */
	public DSAutoComplete() {
		//super(new BorderLayout());
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		super.setPreferredSize(new Dimension(175, 23));
		
		contents = new JLayeredPane();
		
		inputText = new DSTextField();
		inputText.setPreferredSize(new Dimension(150, 23));
		//inputText.setLocation(0, 0);
		inputText.setBounds(0, 0, 150, 23);
		inputText.addKeyListener(inputKeyListener);
		inputText.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				onInputTextChange();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				onInputTextChange();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				onInputTextChange();
			}
		});
		
		CompoundBorder border = new CompoundBorder(inputBorder, BorderFactory.createEmptyBorder(0, inputOffset, 0, 0));
		inputText.setBorder(border);
		
		dropdownButton = new JLabel();
		dropdownButton.setPreferredSize(new Dimension(25, 23));
		//dropdownButton.setLocation(147, 0);
		dropdownButton.setBounds(150, 0, 25, 23);
		dropdownButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		dropdownButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (System.currentTimeMillis() - listViewCloseTimestamp > 250) {
						lastSearchedText = null;
						onInputTextChange();
					}
				}
			}
		});
		// load the image
		byte[] dropdrownImage = Base64.decodeBase64(DSImages.DROPDOWN_ARROW.getData());
		try {
			image = ImageIO.read(new ByteArrayInputStream(dropdrownImage));
			dropdownButton.setIcon(new ImageIcon(image));
		} catch (IOException e) {
			System.err.println("Unable to load drop down image.");
			dropdownButton.setText("v");
		}
		
		// creates the popup menu
		createListView();
		JPopupMenu dropDownMenu = new JPopupMenu();
		dropDownMenu.setFocusable(false);
		dropDownMenu.add(listContainer);
		dropDownMenu.addPopupMenuListener(new PopupMenuListener() {
			
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			}
			
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			}
			
			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
				listViewCloseTimestamp = System.currentTimeMillis();
			}
		});
		inputText.setComponentPopupMenu(dropDownMenu);
		
		contents.add(inputText);
		contents.add(dropdownButton);
		add(contents, BorderLayout.WEST);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setPreferredSize(java.awt.Dimension)
	 */
	@Override
	public void setPreferredSize(Dimension preferredSize) {
		super.setPreferredSize(preferredSize);
		inputText.setPreferredSize(new Dimension(preferredSize.width - 25, preferredSize.height));
		inputText.setBounds(0, 0, preferredSize.width - 25, preferredSize.height);
		
		dropdownButton.setPreferredSize(new Dimension(25, preferredSize.height));
		dropdownButton.setBounds(preferredSize.width - 25, 0, 25, preferredSize.height);
		dropdownButton.setIcon(new ImageIcon(image.getScaledInstance(25, preferredSize.height, Image.SCALE_SMOOTH)));
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
	 * Sets the data request handler
	 * 
	 * @param handler
	 *        The handler to register
	 */
	public synchronized void setDataRequestHandler(DataRequestEventListener handler) {
		if (dataRequestHandlerSet) {
			throw new UnsupportedOperationException("only one data request handler per instance is allowed.");
		}
		dataRequestHandlerSet = true;
		eventSubscribers.add(DataRequestEventListener.class, handler);
	}
	
	/**
	 * Removes the data request handler
	 * 
	 * @param handler
	 *        The handler to remove
	 */
	public void removeDataRequestHandler(DataRequestEventListener handler) {
		eventSubscribers.remove(DataRequestEventListener.class, handler);
	}
	
	/**
	 * Fires the data request event
	 */
	private synchronized void fireDataRequestEvent() {
		dataRequested = true;
		setEnabled(false);
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		DataRequestEvent evt = new DataRequestEvent(this);
		for (DataRequestEventListener listener : eventSubscribers.getListeners(DataRequestEventListener.class)) {
			listener.onDataRequest(evt);
			break;
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		inputText.setEnabled(enabled);
		dropdownButton.setEnabled(enabled);
	}
	
	/**
	 * Initializes the list view
	 */
	private void createListView() {
		listView = new JList<T>(new DefaultListModel<T>());
		listView.setCellRenderer(new DSAutoCompleteListCellRenderer());
		listView.setSelectionBackground(new Color(0xb9dbf2));
		listView.setSelectionForeground(new Color(0x444444));
		listView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listView.setSelectedIndex(0);
		listView.addMouseMotionListener(new MouseAdapter() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				int idx = listView.locationToIndex(e.getPoint());
				if (idx >= 0) {
					//TODO: highlight the item
				}
			}
		});
		listView.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (listView.getSelectedValue() != null) {
						addItemToSelection(listView.getSelectedValue());
						setListViewVisible(false);
					}
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		listContainer = new JScrollPane(listView);
		listContainer.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		listContainer.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		listView.setVisibleRowCount(VISIBLE_ITEMS);
	}
	
	/**
	 * Hides the list view
	 */
	private void setListViewVisible(boolean visible) {
		if (visible) {
			listContainer.setPreferredSize(new Dimension(inputText.getPreferredSize().width - 5,
					listView.getPreferredScrollableViewportSize().height));
			
			inputText.getComponentPopupMenu().show(DSAutoComplete.this, 3, DSAutoComplete.this.getHeight());
		} else {
			inputText.getComponentPopupMenu().setVisible(visible);
		}
	}
	
	/**
	 * Checks if the list view is currently visible
	 * 
	 * @return
	 *         <code>true</code> if the list view is visible, <code>false</code> otherwise.
	 */
	private boolean isListViewVisible() {
		return inputText.getComponentPopupMenu().isVisible();
	}
	
	/**
	 * Moves down the selection at the list view
	 */
	protected void moveDownSelection() {
		if (isListViewVisible()) {
			int idx = listView.getSelectedIndex() + 1;
			if (idx < listView.getModel().getSize()) {
				listView.setSelectedIndex(idx);
				listView.ensureIndexIsVisible(idx);
			}
		} else {
			lastSearchedText = null;
			onInputTextChange();
			
			if (listView.getModel().getSize() > 0) {
				listView.setSelectedIndex(0);
				listView.ensureIndexIsVisible(0);
			}
		}
	}
	
	/**
	 * Moves up the selection at the list view
	 */
	protected void moveUpSelection() {
		if (isListViewVisible()) {
			int idx = listView.getSelectedIndex() - 1;
			if (idx >= 0) {
				listView.setSelectedIndex(idx);
				listView.ensureIndexIsVisible(idx);
			} else {
				setListViewVisible(false);
			}
		}
	}
	
	/**
	 * Handles the text change event
	 */
	protected void onInputTextChange() {
		if (getDataSource() == null) {
			if (!dataRequested) {
				// needs data source
				fireDataRequestEvent();
			}
		} else if (getDataSource().size() > 0) {
			String searchText = inputText.getText();
			
			if (lastSearchedText != null && lastSearchedText.equals(searchText) && isListViewVisible()) {
				return;
			}
			
			if (lastSearchedText == null || !lastSearchedText.equals(searchText)) {
				lastSearchedText = searchText;
				String regex = "^(" + Pattern.quote(searchText) + ")";
				// creates the pattern
				int flags = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS | Pattern.MULTILINE;
				Pattern pattern = Pattern.compile(regex, flags);
				Matcher matcher = null;
				
				matches = new ArrayList<T>();
				for (T item : getDataSource()) {
					if (matches.size() == LOOKUP_ITEMS) {
						break;
					}
					
					if (isItemSelected(item)) {
						continue;
					}
					
					// test the code
					if (getCode(item) != null) {
						matcher = pattern.matcher(getCode(item));
						if (matcher.find()) {
							matches.add(item);
							continue;
						}
					}
					
					// test the description
					matcher = pattern.matcher(getDescription(item));
					if (matcher.find()) {
						matches.add(item);
						continue;
					}
				}
			}
			handleSearchResult(matches);
		}
	}
	
	/**
	 * Displays the list view with the given results
	 * 
	 * @param result
	 *        The search result
	 */
	private void handleSearchResult(ArrayList<T> result) {
		DefaultListModel<T> model = (DefaultListModel<T>) listView.getModel();
		model.clear();
		Collections.sort(result, new Comparator<T>() {
			
			@Override
			public int compare(T o1, T o2) {
				return getCode(o1).compareTo(getCode(o2));
			}
			
		});
		for (T item : result) {
			model.addElement(item);
		}
		
		if (result.size() > 0) {
			if (!isListViewVisible()) {
				setListViewVisible(true);
			}
		} else {
			setListViewVisible(false);
		}
	}
	
	/**
	 * Sets the component data source
	 * 
	 * @param dataSource
	 *        The component data source
	 */
	public void setDataSource(Collection<T> dataSource) {
		this.dataSource = dataSource == null ? new ArrayList<T>() : dataSource;
		lastSearchedText = null;
		
		if (dataRequested) {
			setEnabled(true);
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			inputText.requestFocus();
			
			if (this.dataSource.isEmpty()) {
				DSDialog.error(null, "N\u00E3o existem elementos para ser listados.", "Erro");
			} else {
				onInputTextChange();
			}
		}
		dataRequested = false;
	}
	
	/**
	 * Clears the data source - resets it to it's initial state.
	 */
	public void clearDataSource() {
		this.dataSource = null;
		lastSearchedText = null;
	}
	
	/**
	 * Gets the current data source
	 * 
	 * @return
	 *         The current data source
	 */
	public Collection<T> getDataSource() {
		return dataSource;
	}
	
	/**
	 * Checks if a given item is selected
	 * 
	 * @param item
	 *        The item to check
	 * @return
	 *         <code>true</code> if the item is selected, <code>false</code> otherwise.
	 */
	private boolean isItemSelected(T item) {
		return selectedItems.containsKey(getId(item));
	}
	
	/**
	 * Adds the given item to the selection
	 * 
	 * @param item
	 *        The item to add to the selection
	 */
	private void addItemToSelection(T item) {
		clearSelectedItems();
		
		DSAutoCompleteSelectedItem obj = new DSAutoCompleteSelectedItem(item);
		obj.setBounds(5, 2, 0, 0);
		selectedItemsObjects.put(getId(item), obj);
		contents.add(obj, 0);
		selectedItems.put(getId(item), item);
		selectedItemsKeys.add(getId(item));
		
		inputText.setText(null);
		
		// correct the input text positioning
		inputOffset = inputOffset + obj.getPreferredSize().width + 3;
		fixInputBorder();
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				DSAutoComplete.this.repaint();
			}
		});
		
		// fire the selection event
		fireSelectionChangeEvent();
	}
	
	/**
	 * Removes the given item from the selection
	 * 
	 * @param item
	 *        The item to be removed from the selection
	 * @param fireEvent
	 *        To fire or suppress the selection change event
	 */
	private void removeItemFromSelection(T item, boolean fireEvent) {
		if (isItemSelected(item)) {
			Object id = getId(item);
			selectedItems.remove(id);
			
			if (selectedItemsKeys.indexOf(id) == highlightedItemIdx) {
				highlightedItemIdx = -1;
			}
			selectedItemsKeys.remove(id);
			
			DSAutoCompleteSelectedItem obj = selectedItemsObjects.get(id);
			// correct the input text positioning
			inputOffset = inputOffset - obj.getPreferredSize().width - 3;
			fixInputBorder();
			
			contents.remove(obj);
			selectedItemsObjects.remove(id);
			if (SwingUtilities.isEventDispatchThread()) {
				contents.repaint();
			} else {
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						contents.repaint();
					}
				});
			}
			inputText.requestFocus();
			
			// fire the selection event
			if (fireEvent) {
				fireSelectionChangeEvent();
			}
		}
	}
	
	/**
	 * Fix the inner empty border left margin
	 */
	private void fixInputBorder() {
		CompoundBorder border = new CompoundBorder(inputBorder, BorderFactory.createEmptyBorder(0, inputOffset, 0, 0));
		inputText.setBorder(border);
		if (SwingUtilities.isEventDispatchThread()) {
			inputText.repaint();
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					inputText.repaint();
				}
			});
		}
	}
	
	/**
	 * Removes the last selected item
	 */
	private void removeLatestSelectedItem() {
		if (selectedItemsKeys.size() > 0) {
			Object key = selectedItemsKeys.get(selectedItemsKeys.size() - 1);
			removeItemFromSelection(selectedItems.get(key), true);
		}
	}
	
	/**
	 * Clears the current selection
	 */
	public void clearSelectedItems() {
		clearSelectedItems(true);
	}
	
	/**
	 * Clears the current selection
	 * 
	 * @param fireEvent
	 *        To fire the selection change event
	 */
	public void clearSelectedItems(boolean fireEvent) {
		fireEvent = fireEvent && selectedItems.size() > 0;
		for (T item : selectedItems.values()) {
			removeItemFromSelection(item, false);
		}
		
		// fire the selection event
		if (fireEvent) {
			fireSelectionChangeEvent();
		}
	}
	
	/**
	 * Gets the selected items
	 * 
	 * @return
	 *         The selected items
	 */
	public T getSelectedItem() {
		return selectedItems.size() > 0 ? selectedItems.values().iterator().next() : null;
	}
	
	/**
	 * Gets the selected items
	 * 
	 * @return
	 *         The selected items
	 */
	public Collection<T> getSelectedItems() {
		return selectedItems.values();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#createToolTip()
	 */
	@Override
	public JToolTip createToolTip() {
		if (isErrorTooltip) {
			return errorTooltip;
		}
		return tooltip;
	}
	
	/**
	 * Registers the text to display in a error tool tip.
	 * 
	 * @param text
	 *        The error tool tip text
	 */
	@Override
	public void setErrorToolTipText(String text) {
		errorTip = text;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#getToolTipLocation(java.awt.event.MouseEvent)
	 */
	@Override
	public Point getToolTipLocation(MouseEvent event) {
		if (isErrorToolTip()) {
			return new Point(0, 0 - getHeight());
		}
		return new Point(getWidth(), 0);
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.client.ui.IDSToolTip#getErrorToolTipText()
	 */
	@Override
	public String getErrorToolTipText() {
		return errorTip;
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.client.ui.IDSToolTip#isErrorToolTip()
	 */
	@Override
	public boolean isErrorToolTip() {
		return isErrorTooltip;
	}
	
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.client.ui.IDSToolTip#setIsErrorToolTip(boolean)
	 */
	@Override
	public void setIsErrorToolTip(boolean status) {
		isErrorTooltip = status;
	}
	
	/**
	 * Animates the component
	 * 
	 * @param type
	 *        The animation type to execute
	 */
	public void animate(final AnimationType type) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				switch (type) {
					case ERROR:
						animation.onError();
						if (errorTip != null) {
							setIsErrorToolTip(true);
							errorTooltip.showTooltip();
						}
						requestFocus();
						break;
					case RESIZE:
						break;
				}
			}
		});
	}
	
	/**
	 * Gets the typed object id
	 * 
	 * @param object
	 *        The object
	 * @return
	 *         The object id
	 */
	public abstract Object getId(T object);
	
	/**
	 * Gets the typed object code
	 * 
	 * @param object
	 *        The object
	 * @return
	 *         The object code
	 */
	public abstract String getCode(T object);
	
	/**
	 * Gets the typed object description
	 * 
	 * @param object
	 *        The object
	 * @return
	 *         The object description
	 */
	public abstract String getDescription(T object);
	
	
	/**
	 * The cell rendered
	 * 
	 * @author David Silva <david@davidafsilva.pt>
	 * @date 10:14:17 PM
	 */
	class DSAutoCompleteListCellRenderer extends JLabel implements ListCellRenderer<T> {
		
		private static final long serialVersionUID = 6422559331139521950L;
		
		/**
		 * Constructor
		 */
		public DSAutoCompleteListCellRenderer() {
			setOpaque(true);
		}
		
		/**
		 * Renders the given cell
		 * 
		 * @param list
		 *        The list view
		 * @param value
		 *        The current cell item
		 * @param index
		 *        The cell index
		 * @param isSelected
		 *        The selection flag
		 * @param cellHasFocus
		 *        The cell focus flag
		 * @return
		 *         The component to render at the cell
		 */
		@Override
		public Component getListCellRendererComponent(JList<? extends T> list, T value, int index, boolean isSelected, boolean cellHasFocus) {
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			
			setText(getHightlightedDescription(getDescription(value), getCode(value), lastSearchedText));
			return this;
		}
		
		/**
		 * Gets the highlighted description
		 * 
		 * @param description
		 *        The description to highlight
		 * @param code
		 *        The code to display
		 * @param searchText
		 *        The search text
		 * @return
		 *         A string formated to highlight the search query
		 */
		private String getHightlightedDescription(String description, String code, String searchText) {
			String result = null;
			String regex = "^(" + Pattern.quote(searchText) + ")";
			// creates the pattern
			int flags = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS | Pattern.MULTILINE;
			Pattern pattern = Pattern.compile(regex, flags);
			Matcher matcher;
			
			// test the description
			matcher = pattern.matcher(description);
			if (!matcher.find()) {
				result = description;
			} else {
				result = matcher.replaceAll("<b><u>$1</u></b>");
			}
			
			// test the code
			if (code != null) {
				matcher = pattern.matcher(code);
				if (matcher.find()) {
					result += " (" + matcher.replaceAll("<b><u>$1</u></b>") + ")";
				} else {
					result += " (" + code + ")";
				}
			}
			
			return "<html>" + result + "</html>";
		}
	}
	
	/**
	 * The auto complete selected item box
	 * 
	 * @author David Silva <david@davidafsilva.pt>
	 * @date 12:32:18 PM
	 */
	public class DSAutoCompleteSelectedItem extends JPanel {
		
		private static final long serialVersionUID = 1L;
		private final String text;
		private final T item;
		private final JLabel removeButton;
		private final int maxWidth = inputText.getPreferredSize().width;
		private final Color borderColor = new Color(0x435a71);
		private final Color highLightedColor = new Color(0x5b7895);
		private boolean isHighlighted = false;
		
		public DSAutoCompleteSelectedItem(T item) {
			super(new BorderLayout(0, 15));
			this.item = item;
			this.text = getDescription(item);
			setOpaque(false);
			setBackground(new Color(0x9FB6CD));
			setForeground(Color.WHITE);
			addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					setHighlighted(false);
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					setHighlighted(true);
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
				}
			});
			
			removeButton = new JLabel("x") {
				
				private static final long serialVersionUID = 1L;
				
				/* (non-Javadoc)
				 * @see javax.swing.JComponent#addNotify()
				 */
				@Override
				public void addNotify() {
					super.addNotify();
				}
				
				@Override
				protected void paintComponent(Graphics g) {
					// create a round rectangle
					Shape round = new RoundRectangle2D.Float(0, getHeight() / 2 - 3.5F, 8, 11, 10, 10);
					
					// draw the background
					Graphics2D g2 = (Graphics2D) g.create();
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setColor(Color.GRAY);
					g2.fill(round);
					
					// draw the text
					FontMetrics fm = g2.getFontMetrics();
					g2.setFont(fm.getFont().deriveFont(10F));
					g2.setColor(getForeground());
					g2.drawString(getText(), 1, (getHeight() + fm.getAscent()) / 2 - 1);
					
					super.paintComponents(g);
					
					g2.dispose();
					g.dispose();
				}
			};
			removeButton.setBackground(getBackground());
			removeButton.setForeground(getForeground());
			removeButton.setPreferredSize(new Dimension(10, 14));
			removeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			removeButton.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON1) {
						DSAutoComplete.this.removeItemFromSelection(getItem(), true);
					}
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
				}
			});
			add(removeButton, BorderLayout.EAST);
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.JComponent#addNotify()
		 */
		@Override
		public void addNotify() {
			super.addNotify();
			int w = getGraphics().getFontMetrics().stringWidth(text) + 17;
			if (w > maxWidth - 10) {
				w = maxWidth - 10;
			}
			setPreferredSize(new Dimension(w, 18));
			setBounds(getX(), getY(), w, getPreferredSize().height);
		}
		
		/**
		 * Gets the associated item
		 * 
		 * @return
		 *         The associated item
		 */
		private T getItem() {
			return item;
		}
		
		/**
		 * Highlights the item
		 * 
		 * @param highlight
		 *        <code>true</code> will highlight the item, false
		 *        will remove the highlighted item (if applicable)
		 */
		public void setHighlighted(boolean highlight) {
			boolean requiresRepaint = highlight != isHighlighted;
			isHighlighted = highlight;
			
			if (requiresRepaint) {
				if (SwingUtilities.isEventDispatchThread()) {
					repaint();
				} else {
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							DSAutoCompleteSelectedItem.this.repaint();
						}
					});
				}
			}
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		@Override
		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g.create();
			
			// clears the painted area 
			g2.clearRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
			
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			if (isHighlighted) {
				g2.setColor(highLightedColor);
			} else {
				g2.setColor(getBackground());
			}
			
			// create a round rectangle - background
			FontMetrics fm = g2.getFontMetrics();
			Shape round = new RoundRectangle2D.Float(1, 1, getPreferredSize().width - 2, this.getHeight() - 2, 10, 10);
			g2.fill(round);
			
			// draw the border
			g2.setColor(borderColor);
			g2.drawRoundRect(0, 1, getPreferredSize().width - 1, this.getHeight() - 2, 10, 10);
			
			// draw the text
			g2.setColor(getForeground());
			g2.drawString(text, 4, (getHeight() + fm.getAscent()) / 2 - 2);
			
			super.paintComponents(g);
			
			g2.dispose();
			g.dispose();
		}
	}
}
