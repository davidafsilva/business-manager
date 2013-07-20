package pt.davidafsilva.bm.client.presenter;

import pt.davidafsilva.bm.client.Application;
import pt.davidafsilva.bm.client.view.LoginView;
import pt.davidafsilva.bm.server.service.interfaces.IUserService;
import pt.davidafsilva.bm.shared.domain.User;
import pt.davidafsilva.bm.shared.exception.InvalidAuthenticationCredentialsException;


/**
 * LoginPresenter.java
 * 
 * @author David Silva <david@davidafsilva.pt>
 * @date 9:14:36 PM
 */
public class LoginPresenter extends BasePresenter<LoginView> {
	
	/**
	 * The user service
	 */
	private IUserService userService = IUserService.Util.getInstance();
	
	/**
	 * The coming from shutdown flag
	 */
	private boolean comingFromShutdown;
	
	/**
	 * Instantiates the login presenter
	 * 
	 * @param view
	 *        The login view
	 */
	public LoginPresenter(LoginView view) {
		super(view);
	}
	
	/**
	 * Authenticates the client given his login credentials
	 * 
	 * @param username
	 *        The client user name
	 * @param password
	 *        The client password
	 */
	public void authenticate(String username, String password) {
		if (username.trim().length() > 0 && password.trim().length() > 0) {
			try {
				User user = userService.authenticate(username.trim(), password.trim());
				Application.get().start(user);
			} catch (InvalidAuthenticationCredentialsException e) {
				getView().invalidLoginCredentials();
			} catch (RuntimeException e) {
				getView().loginError();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.client.presenter.BasePresenter#destroy()
	 */
	@Override
	public void destroy() {
		userService = null;
		super.destroy();
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.client.presenter.BasePresenter#hide()
	 */
	@Override
	public void hide() {
		getView().clean();
		super.hide();
	}
	
	/* (non-Javadoc)
	 * @see pt.davidafsilva.bm.client.presenter.BasePresenter#close()
	 */
	@Override
	public void close() {
		if (!comingFromShutdown) {
			comingFromShutdown = true;
			super.close();
			Application.get().shutdown();
		}
	}
	
}
