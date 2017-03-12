package pt.davidafsilva.bm.client.view;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;

import pt.davidafsilva.bm.client.enums.ImagesEnum;
import pt.davidafsilva.bm.client.presenter.BasePresenter;
import pt.davidafsilva.bm.client.ui.DSFrame;


/**
 * BaseView.java
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 9:18:09 PM
 */
public abstract class BaseView<P extends BasePresenter<?>> extends DSFrame {
	
	private static final long serialVersionUID = -5994495779090504496L;
	
	// presenter
	protected P presenter;
	
	/**
	 * Create the base dialog.
	 */
	public BaseView() {
		this(null);
	}
	
	/**
	 * Creates the base dialog
	 * 
	 * @param title
	 *        The view title
	 */
	public BaseView(String title) {
		this(title, null);
	}
	
	/**
	 * Creates the base dialog
	 * 
	 * @param title
	 *        The view title
	 * @param parent
	 *        The view parents view
	 */
	public BaseView(String title, BasePresenter<? extends BaseView<?>> parent) {
		super(title);
		setIconImage(new ImageIcon(ImagesEnum.WINDOWS_ICON.getPath()).getImage());
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setResizable(true);
		initialize();
		if (parent != null) {
			setLocationRelativeTo(parent.getView());
		} else {
			setLocationRelativeTo(null);
		}
		
		// add a windows listener, so we can call the proper methods
		addWindowListener(new WindowListener() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowOpened(WindowEvent e) {
				onShow(); // the view receive the event first, 
							// therefore, the presenter's onShow will prevail
				presenter.onShow();
			}
			
			/* (non-Javadoc)
			 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowIconified(WindowEvent e) {
			}
			
			/* (non-Javadoc)
			 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowDeiconified(WindowEvent e) {
			}
			
			/* (non-Javadoc)
			 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowDeactivated(WindowEvent e) {
			}
			
			/* (non-Javadoc)
			 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				presenter.close();
			}
			
			/* (non-Javadoc)
			 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowClosed(WindowEvent e) {
			}
			
			/* (non-Javadoc)
			 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowActivated(WindowEvent e) {
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JFrame#setIconImage(java.awt.Image)
	 */
	@Override
	public void setIconImage(Image image) {
		super.setIconImage(image);
//		if (Application.get().isOSX()) {
//			com.apple.eawt.Application.getApplication().setDockIconImage(new ImageIcon(ImagesEnum.WINDOWS_ICON.getPath()).getImage());
//		}
	}
	
	/**
	 * Gets the view's presenter
	 * 
	 * @return
	 *         the presenter
	 */
	public P getPresenter() {
		return presenter;
	}
	
	
	/**
	 * Sets the view's presenter
	 * 
	 * @param presenter
	 *        the presenter to set
	 */
	public void setPresenter(P presenter) {
		this.presenter = presenter;
	}
	
	/**
	 * Initializes the view components
	 */
	public abstract void initialize();
	
	/**
	 * Cleans the view
	 */
	public abstract void clean();
	
	/**
	 * Closes the view
	 */
	public void close() {
		setVisible(false);
		dispose();
	}
	
	/**
	 * Destroys the view
	 */
	public void destroy() {
		presenter = null;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Window#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean b) {
		boolean old = isVisible();
		super.setVisible(b);
		if (b && !old) {
			onShow();
		}
	}
	
	/**
	 * This method is called after the view is showed.
	 * Subclasses can {@link Override} this method to perform
	 * the appropriate operations after the view is ready/shown.
	 */
	public void onShow() {
		// empty by default
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		if (enabled) {
			setCursor(Cursor.getDefaultCursor());
		} else {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		}
		super.setEnabled(enabled);
	}
}
