package pt.davidafsilva.bm.client;

import java.sql.SQLException;

import javax.swing.*;

import pt.davidafsilva.bm.client.presenter.BusinessManagerPresenter;
import pt.davidafsilva.bm.client.presenter.LoginPresenter;
import pt.davidafsilva.bm.client.view.BusinessManagerView;
import pt.davidafsilva.bm.client.view.LoginView;
import pt.davidafsilva.bm.server.db.DatabaseManager;
import pt.davidafsilva.bm.shared.domain.User;


/**
 * Application.java
 *
 * The application class
 *
 * @author David Silva <david@davidafsilva.pt>
 * @date 11:18:00 PM
 */
public class Application {

	// application singleton instance
	private static Application instance;

	// authed user
	private User user;

	// Authentication screen
	private LoginPresenter loginPresenter;

	// BM presenter (main)
	private BusinessManagerPresenter bmPresenter;

	/**
	 * Gets the application single-ton instance
	 *
	 * @return
	 *         The application instance
	 */
	public static Application get() {
		if (instance == null) {
			instance = new Application();
			instance.initialize();
		}
		return instance;
	}

	/**
	 * Initializes the application
	 */
	private void initialize() {
		// set windows look and feel
		if (isWindows()) {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} catch (Exception e) {
			}
		}

		// set the default tooltip delay
		ToolTipManager.sharedInstance().setDismissDelay(10000);
		ToolTipManager.sharedInstance().setInitialDelay(500);

		// initialize the database
		try {
			if (!DatabaseManager.isInitialized()) {
				// creates database
				DatabaseManager.create();
			}
			DatabaseManager.setupConfiguration();
		} catch (SQLException e) {
			throw new RuntimeException("Ocorreu um erro ao inicializar a aplica\u00E7\u00E3o.\n" + e.getMessage());
		}
	}

	private static void setUIFont(javax.swing.plaf.FontUIResource f){
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get (key);
			if (value != null && value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put (key, f);
		}
	}

	/**
	 * Initializes and starts the application
	 *
	 * @throws UnsupportedLookAndFeelException
	 */
	public void start() {
		// starts the application
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					loginPresenter = new LoginPresenter(new LoginView());
					//loginPresenter.authenticate("admin", "admin");
					loginPresenter.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Starts the application with the given user (post-authentication).
	 *
	 * @param authUser
	 *        The authenticated user
	 */
	public void start(User authUser) {
		if (authUser == null) {
			throw new RuntimeException("Invalid user received");
		}
		this.user = authUser;
		loginPresenter.hide();

		// starts the main application
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					getMainPresenter().show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Gets the main application presenter
	 *
	 * @return The main application presenter
	 */
	private BusinessManagerPresenter getMainPresenter() {
		if (bmPresenter == null) {
			bmPresenter = new BusinessManagerPresenter(new BusinessManagerView());
		}

		return bmPresenter;
	}

	/**
	 * Shuts down the application
	 */
	public void shutdown() {
		user = null;
		loginPresenter.close();
		loginPresenter = null;
		if (bmPresenter != null) {
			bmPresenter.close();
		}
		bmPresenter = null;
		instance = null;
		System.exit(0);
	}

	/**
	 * Logs out from the application
	 */
	public void logout() {
		user = null;
		if (bmPresenter != null) {
			bmPresenter.close();
		}
		bmPresenter = null;
		loginPresenter.show();
	}

	/**
	 * Gets the authenticated user
	 *
	 * @return
	 *         The authenticated user
	 */
	public User getAuthenticatedUser() {
		return user;
	}


	/**
	 * The entry point.
	 * Launches the application.
	 *
	 * @param args
	 *        Command line arguments
	 */
	public static void main(String[] args) {
		Application.get().start();
	}

	/**
	 * Checks if is running under OS X
	 *
	 * @return <code>true</code> if the application is running under OS X, <code>false</code> otherwise.
	 */
	public boolean isOSX() {
		return System.getProperty("os.name", "n/a").equalsIgnoreCase("mac os x");
	}

	/**
	 * Checks if is running under a Windows platform
	 *
	 * @return <code>true</code> if the application is running under windows, <code>false</code> otherwise.
	 */
	public boolean isWindows() {
		return System.getProperty("os.name", "n/a").contains("Windows");
	}

}
